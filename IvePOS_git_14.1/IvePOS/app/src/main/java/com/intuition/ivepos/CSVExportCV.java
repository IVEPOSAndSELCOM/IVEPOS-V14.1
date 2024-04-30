package com.intuition.ivepos;

import android.content.ContentValues;

public class CSVExportCV {

    ContentValues cv;
    String tableName;

    public CSVExportCV(ContentValues cv, String tableName) {
        this.cv = cv;
        this.tableName = tableName;
    }

    public ContentValues getCv() {
        return cv;
    }

    public void setCv(ContentValues cv) {
        this.cv = cv;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
