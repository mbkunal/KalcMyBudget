package com.kmb.budget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class DBClass extends AsyncTask<Void,Void,List<?>> {

    private MainDatabase db ;
    private MainActivity mActivity;
    private TransactionsActivity tActivity;
    private CategoryDAO categoryDAO;
    private TransactionDAO transactionDAO;
    private String nm;
    private String tp;
    private String operation;
    private String to;
    private String from;
    private String comment;
    private int amount;
    private Date createDate;
    private Date transactionDate;
    List mList = null;

    public DBClass(Context context,TransactionsActivity activity,String operation){
        this.db = MainDatabase.getMainDatabase(context);
        this.categoryDAO = db.categoryDAO();
        this.transactionDAO = db.transactionDAO();
        this.tActivity = activity;
        this.operation = operation;
    }

    public DBClass(Context context,MainActivity activity,String operation){
        this.db = MainDatabase.getMainDatabase(context);
        this.categoryDAO = db.categoryDAO();
        this.mActivity = activity;
        this.operation = operation;
    }
    public DBClass(Context context, String nm, String tp){
        this.db = MainDatabase.getMainDatabase(context);
        this.categoryDAO = db.categoryDAO();
        this.nm = nm;
        this.tp = tp;
        this.operation = "CATEGORY";
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
            case("CATEGORY"):
                CategoryModal category = new CategoryModal();
                category.setCategoryName(nm);
                category.setType(tp);
                categoryDAO.insertCategory(category);
                break;
            case("TRANSACTION"):
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
                List<TransactionModal> tmlist = transactionDAO.getAllTransactions();
                List<Transaction> list = new ArrayList<>();
                int i = 1;
                List<CategoryModal> cm = categoryDAO.getAllCategories();
                for(TransactionModal tm : tmlist){
                    Long fromId = tm.getFromId();
                    Long toId = tm.getToId();
                    Transaction t = new Transaction(Integer.toString(i),categoryDAO.getCategoryName(fromId),categoryDAO.getCategoryName(toId),tm.getComment(),tm.getTransactionDate().toString(),Integer.toString(tm.getAmount()));
                    list.add(t);
                    i++;
                }
                mList = list;
                break;
        }
        return mList;
    }
    @Override
    protected void onPostExecute(List<?> list) {
        switch(operation) {
            case("GET_CATEGORIES"):
                List<String> cList = (List<String>)list;
                mActivity.setList(cList);
                break;
            case("GET_TRANSACTIONS"):
                List<Transaction> tList = (List<Transaction>)list;
                tActivity.createTransactionList(tList);
                break;
        }
    }


}