package com.example.android.mynewsapp;


import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoaderTask extends AsyncTaskLoader<List<News>> {

    private String mUrl;

    public NewsLoaderTask(Context context,String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {

        if (mUrl == null){
            return null;
        }

        List<News> news = Query.fetchNews(mUrl);
        return news;
    }
}