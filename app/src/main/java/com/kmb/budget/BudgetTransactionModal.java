package com.kmb.budget;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName="budgetTransactions")
public class BudgetTransactionModal {
    public String getComment() {
        return comment;
    }

    public BudgetTransactionModal(TransactionModal tModal){
        this._id = tModal.get_id();
        this.amount = tModal.getAmount();
        this.comment = tModal.getComment();
        this.createDate = tModal.getCreateDate();
        this.transactionDate = tModal.getTransactionDate();
        this.fromId = tModal.getFromId();
        this.toId = tModal.getToId();
    }
    public BudgetTransactionModal ()
    {

    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @PrimaryKey()
    @ColumnInfo(name="_id")
    private long _id;

    @ColumnInfo(name="toId")
    private long toId;

    @ColumnInfo(name="fromId")
    private long fromId;

    @ColumnInfo(name="amount")
    private int amount;

    @ColumnInfo(name="comment")
    private String comment;

    @ColumnInfo(name="transactionDate")
    private Date transactionDate;

    @ColumnInfo(name="createDate")
    private Date createDate;


    public int getAmount() { return amount; }

    public void setAmount(int amount)   { this.amount = amount; }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getToId() { return toId; }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
