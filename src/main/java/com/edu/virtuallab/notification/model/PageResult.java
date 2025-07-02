package com.edu.virtuallab.notification.model;

import java.util.List;

public class PageResult<T> {
    private List<T> list;        // 当前页数据
    private long total;          // 总记录数
    private int currentPage;     // 当前页码
    private int pageSize;        // 每页大小
    private int totalPages;      // 总页数

    // 构造方法
    public PageResult(List<T> list, long total, int currentPage, int pageSize, int totalPages) {
        this.list = list;
        this.total = total;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
    }

    // Getters
    public List<T> getList() { return list; }
    public long getTotal() { return total; }
    public int getCurrentPage() { return currentPage; }
    public int getPageSize() { return pageSize; }
    public int getTotalPages() { return totalPages; }
}
