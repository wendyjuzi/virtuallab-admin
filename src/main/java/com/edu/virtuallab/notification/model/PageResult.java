package com.edu.virtuallab.notification.model;

import java.util.List;

public class PageResult<T> {
    private List<T> records;        // 当前页数据
    private long total;             // 总记录数
    private int currentPage;        // 当前页码
    private int pageSize;           // 每页大小
    private int totalPages;         // 总页数

    // 构造方法
    public PageResult() {}

    public PageResult(List<T> records, long total, int currentPage, int pageSize, int totalPages) {
        this.records = records;
        this.total = total;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
    }

    // 常用构造（只传 records 和 total）
    public PageResult(long total, List<T> records) {
        this.records = records;
        this.total = total;
    }
    public static <T> PageResult<T> build(List<T> records, long total) {
        return new PageResult<>(records, total, 0, 0, 0);
    }

    // 如果只传 records，也可以这样：
    public static <T> PageResult<T> build(List<T> records) {
        return new PageResult<>(records, records == null ? 0 : records.size(), 0, 0, 0);
    }
    // Getters and Setters
    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}