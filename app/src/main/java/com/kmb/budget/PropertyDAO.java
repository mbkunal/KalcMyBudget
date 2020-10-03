package com.kmb.budget;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PropertyDAO {


    @Query("Select DISTINCT Type from propertyTable")
    List<String> getAllTypes();

    @Query("Select * from propertyTable")
    List<PropertyModal> getAllProperties();

    @Query("select * from propertyTable where propName = :propName")
    PropertyModal getPropertyByName(String propName);

    @Query("select propName from propertyTable where _id = :id")
    String getPropertyName(Long id);

    @Query("select * from propertyTable where _id = :id ")
    PropertyModal getPropertyById(Long id);

    @Query("select * from propertyTable where type = :type ")
    List<PropertyModal> getPropertiesByType(String type);

    @Insert
    void insertProperty(PropertyModal propertyModal);

    @Delete
    void deleteProperty(PropertyModal propertyModal);

    @Update
    void updateProperty(PropertyModal propertyModal);
}

