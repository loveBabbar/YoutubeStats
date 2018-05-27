package com.example.loveb.youtubestats;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

/**
 * Created by loveb on 26-05-2018.
 */

public class ChannelAdapter extends ArrayAdapter<Channel> {

    public ChannelAdapter(Context context, List<Channel> channels)
    {
        super(context,0,channels);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_layout, parent, false);
        }
        Channel channel = getItem(position);
        TextView channelName = (TextView) listItemView.findViewById(R.id.channel_nameView);
        channelName.setText(channel.getName());
        com.mikhaellopez.circularimageview.CircularImageView imageview=(com.mikhaellopez.circularimageview.CircularImageView)listItemView.findViewById(R.id.user_image);
        new DownloadImageTask((ImageView) listItemView.findViewById(R.id.user_image))
                .execute(channel.getUrl());


        return listItemView;
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

