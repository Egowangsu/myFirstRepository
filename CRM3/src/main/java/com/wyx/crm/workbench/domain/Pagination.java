package com.wyx.crm.workbench.domain;

import java.util.List;

public class Pagination<T> {
    private int total;
    private List<T> dataList;   //这里的泛型不能写死，到时候只需在声明时加入泛型即可

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "total='" + total + '\'' +
                ", dataList=" + dataList +
                '}';
    }
}
