package com.prac.onlinesql.vo;

/**
 * @Auther: Administrator
 * @Date: 2018-12-29 14:12
 * @Description:数据表中列信息
 */
public class ColumnVO {

    private String columnName;

    private boolean isPrimary;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }
}
