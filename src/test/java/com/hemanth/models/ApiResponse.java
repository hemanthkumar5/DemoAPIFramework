package com.hemanth.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<T> {

    @JsonProperty("data")
    private T data;
    
    @JsonProperty("support")
    private Support support;
    
    @JsonProperty("page")
    private Integer page;
    
    @JsonProperty("per_page")
    private Integer perPage;
    
    @JsonProperty("total")
    private Integer total;
    
    @JsonProperty("total_pages")
    private Integer totalPages;
    
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("job")
    private String job;
    
    @JsonProperty("createdAt")
    private String createdAt;
    
    @JsonProperty("updatedAt")
    private String updatedAt;

    // Default constructor
    public ApiResponse() {}

    // Constructor for single user response
    public ApiResponse(T data, Support support) {
        this.data = data;
        this.support = support;
    }

    // Constructor for user list response
    public ApiResponse(T data, Integer page, Integer perPage, Integer total, Integer totalPages, Support support) {
        this.data = data;
        this.page = page;
        this.perPage = perPage;
        this.total = total;
        this.totalPages = totalPages;
        this.support = support;
    }

    // Constructor for create/update response
    public ApiResponse(Integer id, String name, String job, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.job = job;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public T getData() {
        return data;
    }

    public ApiResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Support getSupport() {
        return support;
    }

    public ApiResponse<T> setSupport(Support support) {
        this.support = support;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public ApiResponse<T> setPage(Integer page) {
        this.page = page;
        return this;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public ApiResponse<T> setPerPage(Integer perPage) {
        this.perPage = perPage;
        return this;
    }

    public Integer getTotal() {
        return total;
    }

    public ApiResponse<T> setTotal(Integer total) {
        this.total = total;
        return this;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public ApiResponse<T> setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public ApiResponse<T> setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ApiResponse<T> setName(String name) {
        this.name = name;
        return this;
    }

    public String getJob() {
        return job;
    }

    public ApiResponse<T> setJob(String job) {
        this.job = job;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public ApiResponse<T> setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public ApiResponse<T> setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "data=" + data +
                ", support=" + support +
                ", page=" + page +
                ", perPage=" + perPage +
                ", total=" + total +
                ", totalPages=" + totalPages +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", job='" + job + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
