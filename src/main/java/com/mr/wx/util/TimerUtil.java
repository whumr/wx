package com.mr.wx.util;

import com.mr.wx.parser.NewsParser;
import com.mr.wx.parser.PacketParser;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/2/2.
 */
public class TimerUtil {

    private static Timer timer = new Timer();

    public static void start() {
        //获取资讯
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                NewsParser.parse();
            }
        }, 10000L, 5 * 60 * 1000L);
        //获取资产包、融资包
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                PacketParser.parse();
            }
        }, 20000L, 5 * 60 * 1000L);
    }
}