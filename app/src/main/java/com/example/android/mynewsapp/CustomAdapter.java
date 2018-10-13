package com.example.android.mynewsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CustomAdapter extends ArrayAdapter<News> {
    public CustomAdapter(@NonNull Context context, @NonNull List<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View newsItem = convertView;

        if(newsItem == null){
            newsItem = LayoutInflater.from(getContext()).inflate(R.layout.news_list,parent, false);
        }

        News news = getItem(position);

        // Set the title text
        TextView titleView = (TextView) newsItem.findViewById(R.id.title);
        titleView.setText(news.getTitle());

        // Get authors name if available
        TextView authorName = (TextView) newsItem.findViewById(R.id.author);
        if(news.hasAuthor()){
            authorName.setText(news.getmAuthor());
        }else{
            authorName.setText(R.string.noAuthor);
        }
        // Split the date and set each to its view
        TextView dateView = (TextView) newsItem.findViewById(R.id.date);
        TextView timeView = (TextView) newsItem.findViewById(R.id.time);

        String[] dateTime = news.getDate().split("T");

        String datex = dateTime[0];
        dateView.setText(datex);

        String time = dateTime[1].substring(0,5);
        timeView.setText(time);

        return newsItem;
    }

}
