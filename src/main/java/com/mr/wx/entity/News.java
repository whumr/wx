package com.mr.wx.entity;

/**
 * Created by Administrator on 2015/2/2.
 */
public class News {
    private String title, date, url;

    public News() {
    }

    public News(String title, String date, String url) {
        this.title = title;
        this.date = date;
        this.url = url;
    }

    public News(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return this.title + "\t" + this.url + "\t" + this.date;
    }
}
