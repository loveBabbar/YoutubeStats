package com.example.loveb.youtubestats;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by loveb on 26-05-2018.
 */

public class StatsUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public StatsUtils()
    {

    }

    public  static Statistics fetchStats(String Stringurl)
    {
        URL url=QueryUtils.createURL(Stringurl);
        String jsonResponse=null;

        try {
            jsonResponse=QueryUtils.makehttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Statistics obj=extractStats(jsonResponse);
        return obj;
    }
    public static Statistics extractStats(String jsonResponse)
    {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        Statistics result=new Statistics(0,0,0);

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");
            JSONObject FirstItem = itemsArray.getJSONObject(0);
            JSONObject stats=FirstItem.getJSONObject("statistics");
            String sub=stats.getString("subscriberCount");
             String vid=stats.getString("videoCount");
            String view=stats.getString("viewCount");

            result=new Statistics(Long.parseLong(sub),Long.parseLong(vid),Long.parseLong(view));
        }catch (JSONException e){
            Log.e(LOG_TAG,"Error in fetching data",e);
        }

        return result;
    }



}
