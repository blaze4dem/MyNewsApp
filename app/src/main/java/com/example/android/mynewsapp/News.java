package com.example.android.mynewsapp;


public class News {

    private String mTitle;
    private String mDate = null;
    private String mUrl;
    private String mAuthor = null;

    public News(String title, String author, String date, String url){
        mTitle = title;
        mDate = date;
        mUrl = url;
        mAuthor = author;
    }

    public News(String title, String date, String url){
        mTitle = title;
        mUrl = url;
        mDate = date;
    }
    // Return the title text
    public String getTitle(){
        return mTitle;
    }

    // Return Author name
    public String getmAuthor(){
        return mAuthor;
    }
    // Return the date and time
    public String getDate(){
        return mDate;
    }
    // Return the url string
    public String getUrl(){
        return mUrl;
    }

    public boolean hasDate(){
        return mDate != null;
    }
    public boolean hasAuthor(){
        return mAuthor != null;
    }
}
