package com.kmb.budget;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName="propertyTable",indices = {@Index(value = {"propName"},
        unique = true)})
public class PropertyModal {

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    @PrimaryKey()
    @ColumnInfo(name="_id")
    private long _id;

    @ColumnInfo(name="type")
    @NonNull
    private String type;


    @ColumnInfo(name="propName")
    @NonNull
    private String propName;

    @ColumnInfo(name="propValue")
    @NonNull
    private String propValue;

}
