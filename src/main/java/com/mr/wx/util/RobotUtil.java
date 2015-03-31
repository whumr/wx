package com.mr.wx.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PropKit;

/**
 * Created by Administrator on 2015/1/30.
 */
public class RobotUtil {

    private static String getURL;

    public static void init() {
        getURL = PropKit.get("robotUrl");
    }

    public static String getResult(String question) {
        try {
            URL getUrl = new URL(getURL + URLEncoder.encode(question, "utf-8"));
            HttpURLConnection connection = (HttpURLConnection)getUrl.openConnection();
            connection.connect();
            // 取得输入流，并使用Reader读取
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line);
            reader.close();
            // 断开连接
            connection.disconnect();
            return parseResult(sb.toString());
        } catch (IOException e) {
            return null;
        }
    }

    /**
     100000	 文本类数据
     200000	 网址类数据
     301000	 小说
     302000	 新闻
     304000	 应用、软件、下载
     305000	 列车
     306000	 航班
     307000	 团购
     308000	 优惠
     309000	 酒店
     310000	 彩票
     311000	 价格
     312000	 餐厅
     40001	 key的长度错误（32位）
     40002	 请求内容为空
     40003	 key错误或帐号未激活
     40004	 当天请求次数已用完
     40005	 暂不支持该功能
     40006	 服务器升级中
     40007	 服务器数据格式异常
     50000	 机器人设定的“学用户说话”或者“默认回答”
     * @param str
     * @return
     */
    private static String parseResult(String str) {
        JSONObject json = JSON.parseObject(str);
        if ("100000".equals(json.getString("code")))
            return json.getString("text");
        return null;
    }
}
