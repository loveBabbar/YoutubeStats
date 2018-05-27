package com.example.loveb.youtubestats;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    String name,iconurl,sub,vid,viewcount;
    TextView subs,video,view;
    URL url;
    Bitmap bmp;
    com.mikhaellopez.circularimageview.CircularImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        name=getIntent().getStringExtra("name");
        iconurl=getIntent().getStringExtra("icon");
        sub=getIntent().getStringExtra("Subs");
        vid=getIntent().getStringExtra("Video");
        viewcount=getIntent().getStringExtra("View");

        android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subs=(TextView)findViewById(R.id.subscriberView);
        video=(TextView)findViewById(R.id.videoView);
        view=(TextView)findViewById(R.id.viewcountView);

        subs.setText(sub);
        video.setText(vid);
        view.setText(viewcount);
        imageView=( com.mikhaellopez.circularimageview.CircularImageView) findViewById(R.id.channel_imageview);

        new DownloadImageTask((ImageView) findViewById(R.id.channel_imageview))
                .execute(iconurl);

    }
    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
