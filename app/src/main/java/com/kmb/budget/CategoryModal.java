package com.kmb.budget;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Category")
public class CategoryModal {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="Id")
    private Long Id;

    @ColumnInfo(name = "categoryName")
    private String categoryName;

    @ColumnInfo(name = "type")
    private String type;


    public Long getId() {
        return Id;
    }
    public void setId(Long Id) {
        this.Id = Id;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String category) {
        this.categoryName = category;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
