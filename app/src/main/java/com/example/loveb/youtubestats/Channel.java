package com.example.loveb.youtubestats;

/**
 * Created by loveb on 26-05-2018.
 */

public class Channel {

    private String  Channel_name;
    private String Iurl;
    private long subCount;
    private long vidCount;
    private long viewCount;

    String channelId;
    public Channel(String name,String url,String id,long sub,long video,long view)
    {
        Channel_name=name;
        Iurl=url;
    channelId=id;
    subCount=sub;
    vidCount=video;
    viewCount=view;

    }

    public  String getName()
    {
        return Channel_name;
    }
    public String getUrl()
    {
        return Iurl;
    }
    public String getChannelId()
{
    return channelId;
}
    public  long getSubCount()
    {
        return subCount;
    } public  long getVideoCount()
    {
        return vidCount;
    } public  long getViewCount()
    {
        return viewCount;
    }

}
