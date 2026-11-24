package com.bijlipay.Model.DTO;

import java.util.Arrays;
import java.util.List;

public class CustomExcel {
    private String sheetName;
    private String[] columnName;
    private List<Object[]> data;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String[] getColumnName() {
        return columnName;
    }

    public void setColumnName(String[] columnName) {
        this.columnName = columnName;
    }

    public List<Object[]> getData() {
        return data;
    }

    public void setData(List<Object[]> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CustomExcel{" +
                "sheetName='" + sheetName + '\'' +
                ", columnName=" + Arrays.toString(columnName) +
                ", data=" + data +
                '}';
    }
}
