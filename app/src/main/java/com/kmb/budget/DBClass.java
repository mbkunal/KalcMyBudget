package com.kmb.budget;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

class DBClass extends AsyncTask<Void,Void,List<?>> {

    /**
     * DO NOT TOUCH
     * UNLESS YOU ARE AWARE WHAT YOU ARE DOING.
     * THIS IS VERY MESSED UP CLASS AND TIGHTLY COUPLED WITH CLASSES FROM WHICH IT IS CALLED.
     * USING VERY BD METHOD AND STATIC STINGS.
     * ANY CHANGE IN STATIC STRINGS MIGHT CAUSE THE APP TO
     * BECOME UNSTABLE AND/OR UNUSABLE.
     *
     *
     */

    private MainDatabase db ;
    private Activity mActivity;
    private CategoryDAO categoryDAO;
    private TransactionDAO transactionDAO;
    private BudgetTransactionDAO budgetTransactionDAO;
    private PropertyDAO propertyDAO;
    private String nm;
    private String tp;
    private long propValue;
    private String propName;
    private String propType;
    private Long categoryId;
    private Long propertyId;
    private String operation;
    private String to;
    private String from;
    private String comment;
    private int amount;
    private Boolean isBudget;
    private Boolean error;
    private Date createDate;
    private Date transactionDate;
    private List mList = null;
    private Long filterId;
    private String caller = "";
    private Long toDate;
    private Long fromDate;
    private String categoryName;
    private long budgetLeft;
    private long monthlyBudget;
    private boolean showMonthly;
    private Context context;

    //General Constructor
    public DBClass(Context context,Activity activity,String operation){
        this.db = MainDatabase.getMainDatabase(context);
        this.categoryDAO = db.categoryDAO();
        this.transactionDAO = db.transactionDAO();
        this.propertyDAO = db.propertyDAO();
        this.budgetTransactionDAO = db.budgetTransactionDAO();
        this.mActivity = activity;
        this.operation = operation;
        this.context =  context;
    }
    //Add Income Expense
    public DBClass(Context context,AddIncomeExpense activity,String operation, String propType, String propName, long propValue, long id){
        this.db = MainDatabase.getMainDatabase(context);
        this.propertyDAO = db.propertyDAO();
        this.propName = propName;
        this.propType = propType;
        this.propValue = propValue;
        this.operation = operation;
        this.mActivity = activity;
        this.propertyId = id;
        this.context =  context;
    }
    // filter by category id
    public DBClass(Context context,Activity activity,String operation,Long filterId){
        this.db = MainDatabase.getMainDatabase(context);
        this.categoryDAO = db.categoryDAO();
        this.transactionDAO = db.transactionDAO();
        this.mActivity = activity;
        this.operation = operation;
        this.filterId = filterId;
        this.context =  context;
    }
    //Add Category constructor
    public DBClass(Context context, String nm, String tp,Long id){
        this.db = MainDatabase.getMainDatabase(context);
        this.categoryDAO = db.categoryDAO();
        this.nm = nm;
        this.tp = tp;
        this.categoryId = id;
        this.operation = "ADD_CATEGORY";
        this.context =  context;
    }
    //Add Transaction constructor
    public DBClass(Context context, String to, String from, String comment, int amount, Date createDate, Date transactionDate, Boolean isBudget) {
        this.db = MainDatabase.getMainDatabase(context);
        this.transactionDAO = db.transactionDAO();
        this.categoryDAO = db.categoryDAO();
        this.budgetTransactionDAO = db.budgetTransactionDAO();
        this.to = to;
        this.from = from;
        this.isBudget = isBudget;
        this.operation = "ADD_TRANSACTION";
        this.comment = comment;
        this.amount = amount;
        this.createDate = createDate;
        this.transactionDate = transactionDate;
        this.context =  context;
    }
    //GET Transaction Constructor (filtered and all)
    public DBClass(Context context, TransactionsActivity transactionsActivity, String getTransactions, Long fromDate, Long toDate, String category) {
        this.db = MainDatabase.getMainDatabase(context);
        this.transactionDAO = db.transactionDAO();
        this.categoryDAO = db.categoryDAO();
        this.toDate = toDate;
        this.fromDate = fromDate;
        this.operation = getTransactions;
        this.mActivity = transactionsActivity;
        this.categoryName = category;
        this.context =  context;
    }

    @Override
    protected List<?> doInBackground(Void... voids) {
        this.error = false;
        switch(operation){
            case("DB_CLEAN"):
                {List<TransactionModal> tmlist = transactionDAO.getAllTransactions();
                for(TransactionModal tm : tmlist){
                    if( tm.getFromId() == tm.getToId()){
                        int j = budgetTransactionDAO.deleteTransactionById(tm.get_id());
                        int i = transactionDAO.delete(tm);
                        //Log.e( j+ "deleted",String.valueOf(i));
                    }
                }
                break;}
            case("ADD_PROPERTY"):
                {if(propertyId ==-1) {
                    try {
                        PropertyModal propertyModal = new PropertyModal();
                        propertyModal.setPropName(propName);
                        propertyModal.setPropValue(propValue);
                        propertyModal.setType(propType);
                        propertyDAO.insertProperty(propertyModal);
                        Log.e("added property", propName + " " + propValue);
                    } catch (Exception e) {
                        this.error = true;
                    }
                }else{
                    PropertyModal propertyModal = propertyDAO.getPropertyById(propertyId);
                    propertyModal.setPropName(propName);
                    propertyModal.setPropValue(propValue);
                    propertyModal.setType(propType);
                    propertyDAO.updateProperty(propertyModal);
                }
                break;}
            case("GET_TRANSACTIONSFiltered"):
                {List<TransactionModal> tmlist;
                if(categoryName.equals("ALL")){
                    tmlist = transactionDAO.getTransactions(fromDate,toDate);
                }else{
                    categoryId = categoryDAO.getCategoryId(categoryName);
                    tmlist = transactionDAO.getTransactions(fromDate,toDate,categoryId);
                }
                List<Transaction> list = new ArrayList<>();
                int i=1;
                for(TransactionModal tm : tmlist){
                    Long fromId = tm.getFromId();
                    Long toId = tm.getToId();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                    Transaction t = new Transaction(tm.get_id(),Integer.toString(i),categoryDAO.getCategoryName(fromId),categoryDAO.getCategoryName(toId),tm.getComment(),sdf.format(tm.getTransactionDate()),Integer.toString(tm.getAmount()));
                    list.add(t);
                    i++;
                }
                mList = list;
                break;}
            case("ADD_CATEGORY"):
                {if(categoryId==-1){
                    CategoryModal category = new CategoryModal();
                    category.setCategoryName(nm);
                    category.setType(tp);
                    categoryDAO.insertCategory(category);
                }
                else{
                    CategoryModal category = categoryDAO.getCategoryById(categoryId);
                    category.setType(tp);
                    category.setCategoryName(nm);
                    category.setId(categoryId);
                    categoryDAO.updateCategory(category);
                    Log.e("updated",nm);
                }
                break;}
            case("ADD_TRANSACTION"):
                {TransactionModal transaction = new TransactionModal();
                transaction.setToId(categoryDAO.getCategoryId(to));
                transaction.setFromId(categoryDAO.getCategoryId(from));
                transaction.setComment(comment.split(";")[0]);
                transaction.setAmount(amount);
                transaction.setTransactionDate(transactionDate);
                transaction.setCreateDate(createDate);
                long id = transactionDAO.insert(transaction);
                Log.e("TransactionId : ", String.valueOf(id)) ;
                if(isBudget){
                    transaction.setComment(comment.split(";")[1]);
                    BudgetTransactionModal budgetTransactionModal = new BudgetTransactionModal(transaction);
                    budgetTransactionModal.set_id(id);
                    id = budgetTransactionDAO.insert(budgetTransactionModal);
                    Log.e("BudgetTransactionId : " ,String.valueOf(id) );
                }
                id = 0;
                break;}
            case("GET_CATEGORIES"):
                {try {
                    mList = categoryDAO.getAllCategoryNames();
                }catch (Exception e){
                    e.printStackTrace();
                    mList = null;
                }
                break;}
            case("GET_TRANSACTIONS"):
                {
                    SharedPreferences sharedPref = context.getSharedPreferences("com.kmb.budget.preferenceFile", Context.MODE_PRIVATE);
                    this.showMonthly = sharedPref.getBoolean("showMonthly", true);
                    List<TransactionModal> tmlist;
                    if (!showMonthly){
                        tmlist = filterId == -1?transactionDAO.getAllTransactions():transactionDAO.getAllTransactionsByCategory(filterId);
                    }else{
                        final Calendar c = Calendar.getInstance();
                        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH) );
                        c.set(Calendar.HOUR_OF_DAY, c.getActualMaximum(Calendar.HOUR_OF_DAY));
                        c.set(Calendar.MILLISECOND,c.getActualMaximum(Calendar.MILLISECOND));
                        c.set(Calendar.MINUTE,c.getActualMaximum(Calendar.MINUTE));
                        long lto = Converters.dateToTimestamp(c.getTime());
                        c.add(Calendar.MONTH, -1);
                        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                        c.set(Calendar.HOUR_OF_DAY, 0);
                        c.set(Calendar.MILLISECOND,0);
                        c.set(Calendar.MINUTE,0);
                        long lfr = Converters.dateToTimestamp(c.getTime());
                        tmlist = filterId == -1?transactionDAO.getTransactions(lfr,lto):transactionDAO.getTransactions(lfr,lto,filterId);
                    }
                List<Transaction> list = new ArrayList<>();
                int i = 1;
                for(TransactionModal tm : tmlist){
                    Long fromId = tm.getFromId();
                    Long toId = tm.getToId();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                    Transaction t = new Transaction(tm.get_id(),Integer.toString(i),categoryDAO.getCategoryName(fromId),categoryDAO.getCategoryName(toId),tm.getComment(),sdf.format(tm.getTransactionDate()),Integer.toString(tm.getAmount()));
                    list.add(t);
                    i++;
                }
                mList = list;
                break;}
            case("GET_ANALYSIS_FOR_CHART"):
                {List<PropertyModal> propertyModalList = propertyDAO.getAllProperties();
                long income = 0;
                long expense = 0;
                for(PropertyModal pm : propertyModalList) {
                    switch( pm.getType().toUpperCase()){
                        case("EXPENSE"):
                            expense += pm.getPropValue();
                            break;
                        case("INCOME"):
                            income += pm.getPropValue();
                            break;
                    }
                }
                monthlyBudget = income-expense;

                Long longFromDate = Converters.dateToTimestamp(Converters.getFirstDate());
                Long longToDate = Converters.dateToTimestamp(Converters.getLastDate());
                List<BudgetTransactionModal> budgetTransactionModalList;
                budgetTransactionModalList = budgetTransactionDAO.getTransactions(longFromDate,longToDate);

                long in =0;
                long ex =0;
                for (BudgetTransactionModal btm : budgetTransactionModalList){
                    if(btm.getComment().toUpperCase().equals("INCOME")){
                        in += btm.getAmount();
                    }
                    else if (btm.getComment().toUpperCase().equals("EXPENSE")){
                        ex += btm.getAmount();
                    }
                }
                budgetLeft = in - ex;}
                //I had intentionally left break statement as below part is needed to be run after above part.
                //DO NOT CHANGE THE ORDER. ADD NEW BOCKS EITHER ABOVE OR BELOW.
            case("GET_ANALYSIS"):
                {List<CategorySum> csl= new ArrayList<>();
                List<CategoryModal> cml = categoryDAO.getAllCategories();
                for(CategoryModal cmt : cml){
                    String cname = cmt.getCategoryName();
                    List<TransactionModal> tml = transactionDAO.getCreditTransaction(cmt.getId());
                    long positive = getTransactionSum(tml);
                    tml = transactionDAO.getDebitTransaction(cmt.getId());
                    long negative = getTransactionSum(tml);
                    long balance = positive - negative;
                    CategorySum cs = new CategorySum(cmt.getId(),cname,Long.toString(balance),cmt.getType());
                    csl.add(cs);
                }
                mList = csl;
                break;}
            case("GET_CATEGORY_LIST"):
                {List<CategoryModal> cml0 = categoryDAO.getAllCategories();
                mList = cml0;
                break;}
            case("GET_PROPERTY_LIST"):
                {List<PropertyModal> propertyModals = propertyDAO.getAllProperties();
                mList = propertyModals;
                break;}
            case("DELETE_CATEGORY"):
                {ListCategory listCategory = (ListCategory) mActivity;
                CategoryModal categoryModal = listCategory.categoryModal;
                Long sink;
                try {
                    sink = categoryDAO.getCategoryId("Sink");
                    List<TransactionModal> tml = transactionDAO.getCreditTransaction(categoryModal.getId());
                    for(TransactionModal tm : tml){
                        tm.setToId(sink);
                        transactionDAO.update(tm);
                    }
                    tml = transactionDAO.getDebitTransaction(categoryModal.getId());
                    for(TransactionModal tm : tml){
                        tm.setFromId(sink);
                        transactionDAO.update(tm);
                    }
                }catch (Exception e){
                    categoryDAO.deleteCategory(categoryModal);
                    break;
                }
                categoryDAO.deleteCategory(categoryModal);
                break;}
            case("DELETE_TRANSACTION"):
                {TransactionsActivity transactionsActivity = (TransactionsActivity)mActivity;
                transactionDAO.deleteTransactionById(transactionsActivity.temp.getId());
                budgetTransactionDAO.deleteTransactionById(transactionsActivity.temp.getId());
                break;}
            case("DELETE_PROPERTY"):
                {ListInEx listInEx = (ListInEx) mActivity;
                propertyDAO.deleteProperty(listInEx.propertyModal);
                break;}
        }
        return mList;
    }
    @Override
    protected void onPostExecute(List<?> list) {
        switch(operation) {
            case("GET_CATEGORIES"):
                List<String> cList = (List<String>)mList;
                if(caller.equals("EXPORT_TRANSACTIONS")){
                    ((ExportTransactions) mActivity).setList(cList);
                }else {
                    ((MainActivity) mActivity).setList(cList);
                }
                break;
            case("GET_TRANSACTIONSFiltered"):
            case("GET_TRANSACTIONS"):
                List<Transaction> tList = (List<Transaction>)mList;
                ((TransactionsActivity)mActivity).createTransactionList(tList);
                break;
            case("GET_ANALYSIS"):
                List<CategorySum> csl = (List<CategorySum>)mList;
                ((AnalysisActivity)mActivity).createAnalysisList(csl);
                break;
            case("GET_ANALYSIS_FOR_CHART"):
                List<CategorySum> csl2 = (List<CategorySum>)mList;
                ((ShowChart)mActivity).showChart(csl2, monthlyBudget, budgetLeft);
                break;
            case("GET_CATEGORY_LIST"):
                List<CategoryModal> allCategoryList = (List<CategoryModal>)mList;
                ((ListCategory)mActivity).setList(allCategoryList);
                break;
            case("GET_PROPERTY_LIST"):
                List<PropertyModal> allProperyList = (List<PropertyModal>)mList;
                ((ListInEx)mActivity).setList(allProperyList);
                break;
            case("ADD_PROPERTY"):
                AddIncomeExpense addIncomeExpense = (AddIncomeExpense)mActivity;
                addIncomeExpense.status(error);
                break;
        }

    }
    public void setCaller(String caller){
        this.caller = caller;
    }
    private long getTransactionSum(List<TransactionModal> tml){
        long sum = 0;
        for(TransactionModal tm : tml){
            sum += tm.getAmount();
        }
        return sum;
    }


}