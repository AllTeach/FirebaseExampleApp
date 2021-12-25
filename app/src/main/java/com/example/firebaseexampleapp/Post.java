package com.example.firebaseexampleapp;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

public class Post
{
    private String title;
    private String body;
    private Bitmap bitmap;
    private String bitmapUrl;
    private String ownerMail;


    public Post() {
    }

    public Post(String title, String body, String bitmapUrl, String ownerMail) {

        this.title = title;
        this.body = body;
     //   this.bitmap = bitmap;
        this.bitmapUrl = bitmapUrl;
        this.ownerMail = ownerMail;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public String getBitmapUrl() {
        return bitmapUrl;
    }

    public void setBitmapUrl(String bitmapUrl) {
        this.bitmapUrl = bitmapUrl;
    }

    public String getOwnerMail() {
        return ownerMail;
    }

    public void setOwnerMail(String ownerMail) {
        this.ownerMail = ownerMail;
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if(obj instanceof Post)
        {
            Post p = (Post)obj;
            return this.getTitle().equals(p.getTitle()) &&
                    this.getBody().equals(p.getBody()) &&
                    this.bitmapUrl.equals(p.getBitmapUrl());
        }
        return false;

    }


}
