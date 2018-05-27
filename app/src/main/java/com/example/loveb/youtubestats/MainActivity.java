package com.example.loveb.youtubestats;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;


import com.google.api.client.auth.oauth2.Credential;

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.*;
import com.google.api.services.youtube.YouTube;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.mortbay.jetty.Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String Query_url1= "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=50&q=";
    String Query_url2="&type=channel&key=AIzaSyDypE-NXnCNmZjsY1vgGVXUP-7i3vnbqY0";
    String Query_url;
    public static String stats_url1 = "https://www.googleapis.com/youtube/v3/channels?part=contentDetails%2Cstatistics&id=";
    public static String stats_url2="&key=AIzaSyDypE-NXnCNmZjsY1vgGVXUP-7i3vnbqY0";
    public static int channel_no = 0;
    static String stats_url="";
    static Statistics obj;
    private static ChannelAdapter mAdapter;
    ListView listView;
    Button Click;
    com.miguelcatalan.materialsearchview.MaterialSearchView searchView;
    TextView ntw;

    public static List<Channel> channels_duplicate = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("YouTube Stats");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        ntw=(TextView)findViewById(R.id.ntw);
        searchView=(com.miguelcatalan.materialsearchview.MaterialSearchView)findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ntw.setVisibility(View.GONE);
                Query_url=Query_url1+query+Query_url2;
                final FetchAsyncTask task = new FetchAsyncTask();
                task.execute(Query_url);
                ListView listView = (ListView) findViewById(R.id.listview1);
                mAdapter = new ChannelAdapter(MainActivity.this, new ArrayList<Channel>());
                listView.setAdapter(mAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {

                        Intent intent =new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("icon",channels_duplicate.get(pos).getUrl());
                        intent.putExtra("name",channels_duplicate.get(pos).getName());
                        intent.putExtra("Subs",""+channels_duplicate.get(pos).getSubCount());
                        intent.putExtra("Video",""+channels_duplicate.get(pos).getVideoCount());
                        intent.putExtra("View",""+channels_duplicate.get(pos).getViewCount());
                        startActivity(intent);
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });




    }

    public class FetchAsyncTask extends AsyncTask<String, Void, List<Channel>> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Channel> channels) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (channels != null && !channels.isEmpty()) {
                 mAdapter.addAll(channels);
            }
            Toast.makeText(MainActivity.this,"Data has been Created",Toast.LENGTH_SHORT).show();
           // System.out.println("data  has been created");
            //System.out.println(channels_duplicate.size()+" ");

            StatsFetchAsyncTask task1=new StatsFetchAsyncTask();
            stats_url=stats_url1+channels_duplicate.get(0).getChannelId()+stats_url2;
            task1.execute(stats_url);

        }

        @Override
        protected List<Channel> doInBackground(String... strings) {
            channels_duplicate = QueryUtils.fetchYouTubeData(Query_url);
            return channels_duplicate;
        }
    }

    public class StatsFetchAsyncTask extends AsyncTask<String, Void, Statistics> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Statistics statistics) {

           super.onPostExecute(statistics);
           Toast.makeText(MainActivity.this,"Data has been updated",Toast.LENGTH_SHORT).show();
        /*   for(Channel i:channels_duplicate)
           {
               System.out.println(i.getName());
               System.out.println(i.getSubCount());
           }*/
        }

        @Override
        protected Statistics doInBackground(String... strings) {
            for(int i=0;i<channels_duplicate.size();i++)
            {
                channel_no=i;
                String id=channels_duplicate.get(i).getChannelId();
                stats_url=stats_url1+id+stats_url2;
                Statistics statistics=StatsUtils.fetchStats(stats_url);
                Channel dummy=new Channel(channels_duplicate.get(channel_no).getName(),
                        channels_duplicate.get(channel_no).getUrl(),
                        channels_duplicate.get(channel_no).getChannelId(),
                        statistics.getSubCount(),
                        statistics.getVidCount(),
                        statistics.getViewCount());
                channels_duplicate.set(channel_no,dummy);
            }
           // System.out.println("SUCCESSFUL");
            return obj;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem item=menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

}