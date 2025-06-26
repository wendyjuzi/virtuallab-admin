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
} 