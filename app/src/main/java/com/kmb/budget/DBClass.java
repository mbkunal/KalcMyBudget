package com.kmb.budget;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class DBClass extends AsyncTask<Void,Void,List<?>> {

    private MainDatabase db ;
    private Activity mActivity;
    private CategoryDAO categoryDAO;
    private TransactionDAO transactionDAO;
    private String nm;
    private String tp;
    private Long categoryId;
    private String operation;
    private String to;
    private String from;
    private String comment;
    private int amount;
    private Date createDate;
    private Date transactionDate;
    private List mList = null;
    private Long filterId;
    private String caller = "";
    private Long toDate;
    private Long fromDate;
    private String categoryName;

    public DBClass(Context context,Activity activity,String operation){
        this.db = MainDatabase.getMainDatabase(context);
        this.categoryDAO = db.categoryDAO();
        this.transactionDAO = db.transactionDAO();
        this.mActivity = activity;
        this.operation = operation;
    }
    public DBClass(Context context,Activity activity,String operation,Long filterId){// filter by category id
        this.db = MainDatabase.getMainDatabase(context);
        this.categoryDAO = db.categoryDAO();
        this.transactionDAO = db.transactionDAO();
        this.mActivity = activity;
        this.operation = operation;
        this.filterId = filterId;
    }
    public DBClass(Context context, String nm, String tp,Long id){
        this.db = MainDatabase.getMainDatabase(context);
        this.categoryDAO = db.categoryDAO();
        this.nm = nm;
        this.tp = tp;
        this.categoryId = id;
        this.operation = "ADD_CATEGORY";
    }
    public DBClass(Context context, String to, String from, String comment, int amount, Date createDate, Date transactionDate) {
        this.db = MainDatabase.getMainDatabase(context);
        this.transactionDAO = db.transactionDAO();
        this.categoryDAO = db.categoryDAO();
        this.to = to;
        this.from = from;
        this.operation = "ADD_TRANSACTION";
        this.comment = comment;
        this.amount = amount;
        this.createDate = createDate;
        this.transactionDate = transactionDate;
    }

    public DBClass(Context context, TransactionsActivity transactionsActivity, String getTransactions, Long fromDate, Long toDate, String category) {
        this.db = MainDatabase.getMainDatabase(context);
        this.transactionDAO = db.transactionDAO();
        this.categoryDAO = db.categoryDAO();
        this.toDate = toDate;
        this.fromDate = fromDate;
        this.operation = getTransactions;
        this.mActivity = transactionsActivity;
        this.categoryName = category;
    }

    @Override
    protected List<?> doInBackground(Void... voids) {
        int st = 0;
        switch(operation){
            case("GET_TRANSACTIONSFiltered"):
                List<TransactionModal> tmlist;
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
                break;
            case("ADD_CATEGORY"):
                if(categoryId==-1){
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
                break;
            case("ADD_TRANSACTION"):
                TransactionModal transaction = new TransactionModal();
                transaction.setToId(categoryDAO.getCategoryId(to));
                transaction.setFromId(categoryDAO.getCategoryId(from));
                transaction.setComment(comment);
                transaction.setAmount(amount);
                transaction.setTransactionDate(transactionDate);
                transaction.setCreateDate(createDate);
                transactionDAO.insert(transaction);
                Log.e("transaction",transaction.getComment());
                break;
            case("GET_CATEGORIES"):
                try {
                    mList = categoryDAO.getAllCategoryNames();
                }catch (Exception e){
                    e.printStackTrace();
                    mList = null;
                }
                break;
            case("GET_TRANSACTIONS"):
                tmlist = filterId == -1?transactionDAO.getAllTransactions():transactionDAO.getAllTransactionsByCategory(filterId);
                list = new ArrayList<>();
                i = 1;
                for(TransactionModal tm : tmlist){
                    Long fromId = tm.getFromId();
                    Long toId = tm.getToId();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                    Transaction t = new Transaction(tm.get_id(),Integer.toString(i),categoryDAO.getCategoryName(fromId),categoryDAO.getCategoryName(toId),tm.getComment(),sdf.format(tm.getTransactionDate()),Integer.toString(tm.getAmount()));
                    list.add(t);
                    i++;
                }
                mList = list;
                break;
            case("GET_ANALYSIS"):
                List<CategorySum> csl= new ArrayList<>();
                List<CategoryModal> cml = categoryDAO.getAllCategories();
                for(CategoryModal cmt : cml){
                    String cname = cmt.getCategoryName();
                    List<TransactionModal> tml = transactionDAO.getCreditTransaction(cmt.getId());
                    long positive = getTransactionSum(tml);
                    tml = transactionDAO.getDebitTransaction(cmt.getId());
                    long negative = getTransactionSum(tml);
                    long balance = positive - negative;
                    CategorySum cs = new CategorySum(cmt.getId(),cname,Long.toString(balance));
                    csl.add(cs);
                }
                mList = csl;
                break;
            case("GET_CATEGORY_LIST"):
                List<CategoryModal> cml0 = categoryDAO.getAllCategories();
                mList = cml0;
                break;
            case("DELETE_CATEGORY"):
                ListCategory listCategory = (ListCategory) mActivity;
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
                break;
            case("DELETE_TRANSACTION"):
                TransactionsActivity transactionsActivity = (TransactionsActivity)mActivity;
                transactionDAO.deleteTransactionById(transactionsActivity.temp.getId());
                break;
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
                List<Transaction> tList1 = (List<Transaction>)mList;
                ((TransactionsActivity)mActivity).createTransactionList(tList1);
                ExportTransactions.createExcel(tList1);
                break;
            case("GET_TRANSACTIONS"):
                List<Transaction> tList = (List<Transaction>)mList;
                ((TransactionsActivity)mActivity).createTransactionList(tList);
                break;
            case("GET_ANALYSIS"):
                List<CategorySum> csl = (List<CategorySum>)mList;
                ((AnalysisActivity)mActivity).createAnalysisList(csl);
                break;
            case("GET_CATEGORY_LIST"):
                List<CategoryModal> allCategoryList = (List<CategoryModal>)mList;
                ((ListCategory)mActivity).setList(allCategoryList);
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