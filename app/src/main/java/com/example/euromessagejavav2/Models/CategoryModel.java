package com.example.euromessagejavav2.Models;

public class CategoryModel {
    private String name;
    private String sku;
    private String id;

    public String getChildren_data() {
        return children_data;
    }

    public void setChildren_data(String children_data) {
        this.children_data = children_data;
    }

    private String children_data;

    public CategoryModel(){

    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CategoryModel(String name, String id, String children_data) {
        this.name = name;
        this.id = id;
        this.children_data = children_data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
