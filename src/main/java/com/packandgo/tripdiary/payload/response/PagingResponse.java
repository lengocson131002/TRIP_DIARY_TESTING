package com.packandgo.tripdiary.payload.response;

import com.packandgo.tripdiary.model.Trip;

import java.util.List;

public class PagingResponse<T> {
    public int currentPage;
    public int size;
    public int total;
    public List<T> data;

    public PagingResponse() {}
    public PagingResponse(int currentPage, int size, int total, List<T> data) {
        this.currentPage = currentPage;
        this.size = size;
        this.total = total;
        this.data = data;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
