package com.kmb.budget;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BudgetTransactionDAO {

    @Query("SELECT * FROM budgetTransactions ORDER BY transactionDate")
    List<BudgetTransactionModal> getAllTransactions();

    @Query("Select * from budgetTransactions where _id= :transactionId")
    BudgetTransactionModal getTransaction(long transactionId);

    @Query("SELECT * FROM budgetTransactions WHERE transactionDate BETWEEN :from AND :to")
    List<BudgetTransactionModal> getTransactions(long from, long to);

    @Query("SELECT * FROM budgetTransactions WHERE transactionDate BETWEEN :from AND :to AND ( toId = :categoryId or fromId= :categoryId ) ORDER BY transactionDate")
    List<BudgetTransactionModal> getTransactions(long from, long to, long categoryId);

    @Query("Select COUNT(*) from budgetTransactions")
    int countTransactions();

    @Query("Select * from budgetTransactions where toId = :toCategoryId")
    List<BudgetTransactionModal> getCreditTransaction(long toCategoryId);

    @Query("Select * from budgetTransactions where fromId = :fromCategoryId")
    List<BudgetTransactionModal> getDebitTransaction(long fromCategoryId);

    @Query("Delete from budgetTransactions where _id= :transactionId")
    int deleteTransactionById(Long transactionId);

    @Delete
    int delete(BudgetTransactionModal transaction) ;

    @Insert
    long insert(BudgetTransactionModal transaction);

    @Update
    int update(BudgetTransactionModal transaction);

    @Query("Select * from budgetTransactions where toId = :categoryId or fromId= :categoryId ORDER BY transactionDate")
    List<BudgetTransactionModal> getAllTransactionsByCategory(Long categoryId);
}
