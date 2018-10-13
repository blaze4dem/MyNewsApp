package com.example.android.mynewsapp;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.mynewsapp.News;
import com.example.android.mynewsapp.NewsLoaderTask;
import com.example.android.mynewsapp.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>>{

    // Guardian news API url..................
    private String GAPI = "https://content.guardianapis.com/search?"; // Base Guardian API
    private static final String LOG_TAG = MainActivity.class.getName();

    // Custom adapter initialisation and i still dont understand why it has to be here
    private CustomAdapter mAdapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the list View
        ListView list = (ListView) findViewById(R.id.list);

        TextView empty_list = (TextView) findViewById(R.id.failed);
        list.setEmptyView(empty_list);

        // Setup a empty adapter for the list view
        mAdapt = new CustomAdapter(this, new ArrayList<News>());
        list.setAdapter(mAdapt);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Find the current news item that was clicked on
                News item = mAdapt.getItem(position);
                Uri url = Uri.parse(item.getUrl());

                Intent webIntent = new Intent(Intent.ACTION_VIEW, url);
                startActivity(webIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, this);
        } else {
            Log.e(LOG_TAG, "Connection problemo so... I think your better check your network man");
        }

    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        Uri url = Uri.parse(GAPI);

        //nigeria&tag=technology/technology&api-key=6348640e-b464-4e06-a89d-5bfb48b91362

        Uri.Builder buildUri = url.buildUpon();

        buildUri.appendQueryParameter("q", "nigeria");
        buildUri.appendQueryParameter("tag","technology/technology");
        buildUri.appendQueryParameter("show-tags", "contributor" );
        buildUri.appendQueryParameter("api-key","6348640e-b464-4e06-a89d-5bfb48b91362");

        return new NewsLoaderTask(this,buildUri.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {

        mAdapt.clear();

        if(news != null && !news.isEmpty()){
            mAdapt.addAll(news);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapt.clear();
    }
}
