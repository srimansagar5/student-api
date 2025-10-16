package com.vidyasagar.attendance.api.v1.dto.request;

import java.util.List;
import java.util.Map;

public class StudentSearchRequest {
    // page, size, sorting - List, search and filters - Map.
    private int page;
    private int size;
    private List<SortField> sorting;
    private String search;
    private Map<String, Object> filters;

    // subclass - SortField - column name and order
        // getter and setter
    public  static class SortField {
        private String columnName;
        private String order;

        public String getColumnName() { return columnName; }
        public void setColumnName(String columnName) { this.columnName = columnName;}

        public String getOrder() { return  order; }
        public void setOrder(String order) { this.order = order; }

    }

    //getter and setter
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public List<SortField> getSorting() { return sorting; }
    public void setSorting(List<SortField> sorting) { this.sorting = sorting;}

    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }

    public Map<String, Object> getFilters() { return filters; }
    public void setFilters(Map<String, Object> filters) {this.filters = filters; }
}
