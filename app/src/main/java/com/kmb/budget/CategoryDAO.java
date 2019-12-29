package com.kmb.budget;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoryDAO {


    @Query("Select categoryName from category")
    List<String> getAllCategoryNames();

    @Query("Select * from category")
    List<CategoryModal> getAllCategories();


    @Query("Select categoryName from category where Id = :Id")
    String getCategoryNameById(Long Id);

    @Query("select Id from Category where categoryName = :name")
    Long getCategoryId(String name);

    @Query("select categoryName from category where Id = :Id")
    String getCategoryName(Long Id);

    @Query("select * from Category where Id = :id ")
    CategoryModal getCategoryById(Long id);

    @Insert
    void insertCategory(CategoryModal categoryModal);

    @Delete
    void deleteCategory(CategoryModal categoryModal);

    @Update
    void updateCategory(CategoryModal categoryModal);
}

