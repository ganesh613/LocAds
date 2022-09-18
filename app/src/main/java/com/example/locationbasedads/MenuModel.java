package com.example.locationbasedads;

import android.net.Uri;

public class MenuModel {

    private String  title,cost,img;
    public MenuModel() {
    }
    public MenuModel(String img, String title, String cost) {
        this.title = title;
        this.cost = cost;
        this.img = img;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCost() {return cost;}
    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getImg(){return img;}
    public void setImg(String img){this.img=img;}

//    public String getGenre() {
//        return genre;
//    }
//    public void setGenre(String genre) {
//        this.genre = genre;
//    }
}
