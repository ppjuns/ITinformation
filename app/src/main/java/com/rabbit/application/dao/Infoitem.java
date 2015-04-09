package com.rabbit.application.dao;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Rabbit on 2015/3/25.
 */
public class Infoitem {


    private int id;
    @DatabaseField
    private String title;

    @DatabaseField
    private String datetime;

    @DatabaseField
    private String url;

    @DatabaseField
    private String img_url;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
