package com.mr.wx.common;

import com.mr.wx.entity.News;
import com.mr.wx.entity.Rzb;
import com.mr.wx.entity.Zcb;
import com.mr.wx.parser.NewsParser;
import com.mr.wx.parser.PacketParser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/2/2.
 */
public class Cache {
    public static List<News> current_news, top_news;
    public static List<Rzb> rzbs;
    public static List<Zcb> zcbs;
    public static long news_update_ms, packet_update_ms, GAP = 5 * 60 * 1000L;
    private static SimpleDateFormat SDF = new SimpleDateFormat("MM-dd HH:mm:ss");

    public static List<News> getCurrent_news() {
        if (current_news == null || System.currentTimeMillis() - news_update_ms > GAP)
            NewsParser.parse();
        return current_news;
    }

    public static List<News> getTop_news() {
        if (top_news == null || System.currentTimeMillis() - news_update_ms > GAP)
            NewsParser.parse();
        return top_news;
    }

    public static List<Rzb> getRzbs() {
        if (rzbs == null || System.currentTimeMillis() - packet_update_ms > GAP)
            PacketParser.parse();
        return rzbs;
    }

    public static List<Zcb> getZcbs() {
        if (zcbs == null || System.currentTimeMillis() - packet_update_ms > GAP)
            PacketParser.parse();
        return zcbs;
    }

    public static String getNewsUpdate() {
        return SDF.format(new Date(news_update_ms));
    }

    public static String getPacketUpdate() {
        return SDF.format(new Date(packet_update_ms));
    }
}
