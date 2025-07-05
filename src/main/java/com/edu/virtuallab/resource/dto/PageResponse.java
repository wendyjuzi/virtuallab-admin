package com.edu.virtuallab.resource.dto;

import java.util.List;

/**
 * 分页响应DTO
 */
public class PageResponse<T> {
    private List<T> records;
    private long total;
    private int page;
    private int size;
    private int pages;

    public PageResponse() {}

    public PageResponse(List<T> records, long total, int page, int size) {
        this.records = records;
        this.total = total;
        this.page = page;
        this.size = size;
        this.pages = (int) Math.ceil((double) total / size);
    }

    // Getters and Setters
    public List<T> getRecords() { return records; }
    public void setRecords(List<T> records) { this.records = records; }
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public int getPages() { return pages; }
    public void setPages(int pages) { this.pages = pages; }
} 