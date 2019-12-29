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
        this.operation = "TRANSACTION";
        this.comment = comment;
        this.amount = amount;
        this.createDate = createDate;
        this.transactionDate = transactionDate;
    }
    @Override
    protected List<?> doInBackground(Void... voids) {
        int st = 0;
        switch(operation){
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
                List<TransactionModal> tmlist = filterId == 0?transactionDAO.getAllTransactions():transactionDAO.getAllTransactionsByCategory(filterId);
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
                categoryDAO.deleteCategory(listCategory.categoryModal);
                CategoryModal cm  = categoryDAO.getCategoryById(listCategory.categoryModal.getId());
                Log.e("Transaction","Deleted"+cm==null?" real":"not Again");
                break;
            case("DELETE_TRANSACTION"):
                TransactionsActivity transactionsActivity = (TransactionsActivity)mActivity;
                transactionDAO.deleteTransactionById(transactionsActivity.temp.getId());
                //TransactionModal transactionModal = transactionDAO.getTransaction(transactionsActivity.temp.getId());
                //transactionDAO.delete(transactionModal);
                TransactionModal tm = transactionDAO.getTransaction(transactionsActivity.temp.getId());

                Log.e("Transaction","Deleted"+tm==null?" real":"not Again");
                break;
        }
        return mList;
    }
    @Override
    protected void onPostExecute(List<?> list) {
        switch(operation) {
            case("GET_CATEGORIES"):
                List<String> cList = (List<String>)list;
                ((MainActivity)mActivity).setList(cList);
                break;
            case("GET_TRANSACTIONS"):
                List<Transaction> tList = (List<Transaction>)list;
                ((TransactionsActivity)mActivity).createTransactionList(tList);
                break;
            case("GET_ANALYSIS"):
                List<CategorySum> csl = (List<CategorySum>)mList;
                ((AnalysisActivity)mActivity).createAnalysisList(csl);
                break;
            case("GET_CATEGORY_LIST"):
                List<CategoryModal> allCategoryList = (List<CategoryModal>)list;
                ((ListCategory)mActivity).setList(allCategoryList);
                break;
        }
    }

    private long getTransactionSum(List<TransactionModal> tml){
        long sum = 0;
        for(TransactionModal tm : tml){
            sum += tm.getAmount();
        }
        return sum;
    }


}