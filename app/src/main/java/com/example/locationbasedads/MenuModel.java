package com.example.locationbasedads;
public class MenuModel {

    private String title, cost;

    public MenuModel(String title, String cost) {
        this.title = title;
        this.cost = cost;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String name) {
        this.title = name;
    }
    public String getCost() {
        return cost;
    }
    public void setCost(String year) {
        this.cost = year;
    }

}