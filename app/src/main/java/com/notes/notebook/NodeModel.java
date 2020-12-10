package com.notes.notebook;

import android.content.Intent;
import android.util.Log;

public class NodeModel {

    private String head,time,desc,date;
    private int color;
    private int isFav= 0;
    private long ID;

    NodeModel(){


    }

    public NodeModel(String head, String desc,String date,String time,  int color,int isFav) {
        this.head = head;
        this.time = time;
        this.desc = desc;
        this.color = color;
        this.date=date;
        this.isFav = isFav;
    }


    public NodeModel(long id ,String head, String desc,String date, String time, int color , int isFav) {
        this.ID = id;
        this.head = head;
        this.time = time;
        this.desc = desc;
        this.color = color;
        this.date=date;
        this.isFav = isFav;
    }

    public long getID() { return ID; }

    public void setID(long ID) { this.ID = ID; }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getFav() {
        return isFav;
    }

    public void setFav(int fav) {
        isFav = fav;
    }

    public String getTime() {

        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
