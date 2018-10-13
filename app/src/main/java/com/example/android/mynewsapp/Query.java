package com.example.android.mynewsapp;

import android.util.Log;

import com.example.android.mynewsapp.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Query {

    private static final String LOG_TAG = Query.class.getName();


    private Query(){
    }

    public static List<News> fetchNews(String url){

        String jsonResponse = "";
        List<News> news = null;

        // Connect to the API and return the Json DATA
        try {
            jsonResponse = connectToGuardianApi(createUrl(url));
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Malformed url thing ............. what do you know ");
        } catch (IOException e) {
            Log.e(LOG_TAG, "Something went wrong with the inputStream Reading or something like that");
        }

        try {
            news = extractNews(jsonResponse);
        } catch (JSONException e) {
            Log.e(LOG_TAG,"JsonException Error.... Could not convert the json file");
        }

        return news;
    }

    public static URL createUrl(String string) throws MalformedURLException {
        URL url = new URL(string);

        return url;
    }

    public static String connectToGuardianApi(URL url) throws IOException {
        String jsonData = "";
        HttpURLConnection apiConnection = null;
        InputStream apiStream = null;

        try {
            apiConnection = (HttpURLConnection) url.openConnection();
            apiConnection.setRequestMethod("GET");
            apiConnection.setReadTimeout(15000);
            apiConnection.setConnectTimeout(15000);
            apiConnection.connect();

            if (apiConnection.getResponseCode() == 200){
                apiStream = apiConnection.getInputStream();

                if (apiStream != null){
                    jsonData = readStream(apiStream);
                }
            }
        } catch (IOException e) {
            Log.e(LOG_TAG,"Connection to the Guarding API problem no JSON response to use");
        } finally {
            if(apiStream != null){
                apiStream.close();
            }
            if (apiConnection != null){
                apiConnection.disconnect();
            }
        }

        return jsonData;
    }

    public static String readStream(InputStream string) throws IOException {
        StringBuilder output = new StringBuilder();

        if (string != null){
            InputStreamReader stream = new InputStreamReader(string, "UTF-8");
            BufferedReader reader = new BufferedReader(stream);

            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    public static List<News> extractNews(String jsonData) throws JSONException {

        List<News> news = new ArrayList<>();
        int index = 0;

        JSONObject root = new JSONObject(jsonData);
        JSONObject response = root.getJSONObject("response");
        JSONArray resultsArray = response.getJSONArray("results");

        while (index < resultsArray.length()){

            JSONObject newsItem = resultsArray.getJSONObject(index);
            JSONArray authorTag = newsItem.getJSONArray("tags");

            String title = newsItem.getString("webTitle");
            String date = newsItem.getString("webPublicationDate");
            String url = newsItem.getString("webUrl");

            try {
                JSONObject tagObject = authorTag.getJSONObject(0);
                String authorName = tagObject.getString("webTitle");
                news.add(new News(title, authorName, date, url));

            }catch (JSONException e){ // if no author found... catch error and add item anyways
                news.add(new News(title, date, url));
            }

            index++ ;
        }

        return news;
    }
}
