package com.prac.onlinesql.vo;

import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019-01-31 16:57
 * @Description:
 */
public class ColumnsVO {
    //表的列数据
    private List<ColumnVO> columns;
    //主键
    private List<String> primarys;
    //外键
    private List<String> foreigns;
    //索引
    private List<String> indexs;

    public List<ColumnVO> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnVO> columns) {
        this.columns = columns;
    }

    public List<String> getPrimarys() {
        return primarys;
    }

    public void setPrimarys(List<String> primarys) {
        this.primarys = primarys;
    }

    public List<String> getForeigns() {
        return foreigns;
    }

    public void setForeigns(List<String> foreigns) {
        this.foreigns = foreigns;
    }

    public List<String> getIndexs() {
        return indexs;
    }

    public void setIndexs(List<String> indexs) {
        this.indexs = indexs;
    }
}
