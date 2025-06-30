package com.edu.virtuallab.common.api;

import java.util.List;

public class PageResult<T> {
    private long total;
    private List<T> list;
    
    public PageResult() {}
    
    public PageResult(long total, List<T> list) {
        this.total = total;
        this.list = list;
    }
    
    // getter/setter
    public long getTotal() {
        return total;
    }
    
    public void setTotal(long total) {
        this.total = total;
    }
    
    public List<T> getList() {
        return list;
    }
    
    public void setList(List<T> list) {
        this.list = list;
    }
    
    // 添加一些便利方法
    public int getSize() {
        return list != null ? list.size() : 0;
    }
    
    public boolean isEmpty() {
        return list == null || list.isEmpty();
    }
    
    public boolean hasData() {
        return list != null && !list.isEmpty();
    }
} 