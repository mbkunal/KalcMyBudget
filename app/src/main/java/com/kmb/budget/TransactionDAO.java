package com.kmb.budget;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransactionDAO {

    @Query("SELECT * FROM transactions order by transactionDate")
    List<TransactionModal> getAllTransactions();

    @Query("Select * from transactions where _id= :id")
    TransactionModal getTransaction(long id);

    @Query("Select COUNT(*) from transactions")
    int countTransactions();

    @Query("Select * from transactions where toId = :toId")
    List<TransactionModal> getCreditTransaction(long toId);

    @Query("Select * from transactions where fromId = :fromId")
    List<TransactionModal> getDebitTransaction(long fromId);

    @Delete
    void delete(TransactionModal transaction) ;

    @Insert
    void insert(TransactionModal transaction);


}
