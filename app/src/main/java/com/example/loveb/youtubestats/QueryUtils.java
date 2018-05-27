package com.example.loveb.youtubestats;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mortbay.jetty.Main;
import org.mortbay.jetty.handler.StatisticsHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by loveb on 26-05-2018.
 */

public class QueryUtils  {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static  String title;
    public static String channelid;
    public static String url;



public static     ArrayList<Channel>  channelList;

    public QueryUtils()
    {

    }
    public static URL createURL(String stringUrl){
        URL url = null;
        try
        {
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error in Creating URL",e);
        }
        return url;
    }

    public  static List<Channel> fetchYouTubeData(String Stringurl)
    {
        URL url=createURL(Stringurl);
        String jsonResponse=null;

        try {
            jsonResponse=makehttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Channel> channelList=extractChannel(jsonResponse);
        return channelList;
    }

    public static String makehttpRequest(URL url) throws IOException
    {
    String jsonResponse="";
    if(url==null)
    {
        return jsonResponse;
    }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error in connection!! Bad Response ");
            }

        }catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    public static List<Channel> extractChannel(String jsonResponse)
    {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
       channelList     = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject currentChannel =itemsArray.getJSONObject(i);
                JSONObject snippet = currentChannel.getJSONObject("snippet");
                 title = snippet.getString("channelTitle");
                 channelid=snippet.getString("channelId");
                JSONObject thumbnails=snippet.getJSONObject("thumbnails");
                JSONObject defaults=thumbnails.getJSONObject("default");
                url=defaults.getString("url");

                Channel channel=new Channel(title,url,channelid,-1,-1,-1);
                channelList.add(channel);
            }

        }catch (JSONException e){
            Log.e(LOG_TAG,"Error in fetching data",e);
        }

        return channelList;
    }


}
