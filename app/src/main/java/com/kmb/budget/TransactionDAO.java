package com.kmb.budget;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TransactionDAO {

    @Query("SELECT * FROM transactions ORDER BY transactionDate")
    List<TransactionModal> getAllTransactions();

    @Query("Select * from transactions where _id= :transactionId")
    TransactionModal getTransaction(long transactionId);

    @Query("SELECT * FROM transactions WHERE transactionDate BETWEEN :from AND :to")
    List<TransactionModal> getTransactions(long from, long to);

    @Query("SELECT * FROM transactions WHERE transactionDate BETWEEN :from AND :to AND ( toId = :categoryId or fromId= :categoryId ) ORDER BY transactionDate")
    List<TransactionModal> getTransactions(long from, long to, long categoryId);

    @Query("Select COUNT(*) from transactions")
    int countTransactions();

    @Query("Select * from transactions where toId = :toCategoryId")
    List<TransactionModal> getCreditTransaction(long toCategoryId);

    @Query("Select * from transactions where fromId = :fromCategoryId")
    List<TransactionModal> getDebitTransaction(long fromCategoryId);

    @Query("Delete from transactions where _id= :transactionId")
    void deleteTransactionById(Long transactionId);

    @Delete
    void delete(TransactionModal transaction) ;

    @Insert
    void insert(TransactionModal transaction);

    @Update
    void update(TransactionModal transaction);

    @Query("Select * from transactions where toId = :categoryId or fromId= :categoryId ORDER BY transactionDate")
    List<TransactionModal> getAllTransactionsByCategory(Long categoryId);
}
