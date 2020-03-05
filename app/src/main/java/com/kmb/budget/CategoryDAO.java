package com.kmb.budget;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoryDAO {


    @Query("Select categoryName from category ORDER BY categoryName")
    List<String> getAllCategoryNames();

    @Query("Select * from category ORDER BY categoryName")
    List<CategoryModal> getAllCategories();

    @Query("select Id from Category where categoryName = :categoryName")
    Long getCategoryId(String categoryName);

    @Query("select categoryName from category where Id = :categoryId")
    String getCategoryName(Long categoryId);

    @Query("select * from Category where Id = :id ")
    CategoryModal getCategoryById(Long id);

    @Insert
    void insertCategory(CategoryModal categoryModal);

    @Delete
    void deleteCategory(CategoryModal categoryModal);

    @Update
    void updateCategory(CategoryModal categoryModal);
}

