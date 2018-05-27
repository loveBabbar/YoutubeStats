package com.example.loveb.youtubestats;

/**
 * Created by loveb on 26-05-2018.
 */

public class Statistics {

    private long subCount;
    private long vidCount;
    private long viewCount;
    Statistics(long s,long vid,long view)
    {
        subCount=s;
        vidCount=vid;
        viewCount=view;
    }
    public long getSubCount()
    {
        return subCount;
    }
    public long getVidCount()
    {
        return vidCount;
    }
    public long getViewCount()
    {
        return viewCount;
    }
}
