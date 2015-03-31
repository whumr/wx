package com.mr.wx.msg;

import org.apache.log4j.Logger;
import com.jfinal.weixin.sdk.jfinal.MsgController;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import com.jfinal.weixin.sdk.msg.out.OutMsg;
import com.jfinal.weixin.sdk.msg.out.OutMusicMsg;
import com.jfinal.weixin.sdk.msg.out.OutNewsMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;
import com.mr.wx.common.BaseMsgHandler;
import com.mr.wx.common.Cache;
import com.mr.wx.common.KeyWords;
import com.mr.wx.entity.News;
import com.mr.wx.entity.Rzb;
import com.mr.wx.entity.Zcb;
import com.mr.wx.util.RobotUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/1/30.
 */
public class TextMsgHandler<M extends InTextMsg> extends BaseMsgHandler<M> {
	
	private static Logger log;

    private static int NEWS_COUNT = 5, RZB_COUNT = 5, ZCB_COUNT = 5;

    private static String title_img = "http://www.352.com/images/top2013/top2013_01.jpg";

    private static Logger getLogger() {
        if (log == null)
            log = Logger.getLogger(TextMsgHandler.class);
        return log;
    }

    public TextMsgHandler(MsgController controller) {
        super(controller);
    }

    @Override
    public void handleMsg(InTextMsg msg) {
        String msgContent = msg.getContent().trim();
        getLogger().info("get msg : " + msgContent);
        OutMsg outMsg;
        //最新资讯
        if (KeyWords.KEY_ZXZX.equalsIgnoreCase(msgContent)) {
            OutTextMsg no_msg = new OutTextMsg(msg);
            no_msg.setContent("暂时无法获取最新资讯");
            outMsg = no_msg;
            List<News> news = Cache.getCurrent_news();
            if (news != null && !news.isEmpty()) {
                OutNewsMsg out_news = new OutNewsMsg(msg);
                out_news.addNews("融资城最新咨询\t" + Cache.getNewsUpdate(), null, title_img, "http://www.352.com/news/newslist_ttid-428.html");
                for (int i = 0; i < Math.min(news.size(), NEWS_COUNT); i++) {
                    News n = news.get(i);
                    out_news.addNews(n.getTitle(), null, null, n.getUrl());
                }
                outMsg = out_news;
            }
        }
        //热门咨询
        else if (KeyWords.KEY_RMZX.equalsIgnoreCase(msgContent)) {
            OutTextMsg no_msg = new OutTextMsg(msg);
            no_msg.setContent("暂时无法获取热门资讯");
            outMsg = no_msg;
            List<News> news = Cache.getTop_news();
            if (news != null && !news.isEmpty()) {
                OutNewsMsg out_news = new OutNewsMsg(msg);
                out_news.addNews("融资城热门咨询\t" + Cache.getNewsUpdate(), null, title_img, "http://www.352.com/news/newslist_ttid-428.html");
                for (int i = 0; i < Math.min(news.size(), NEWS_COUNT); i++) {
                    News n = news.get(i);
                    out_news.addNews(n.getTitle(), null, null, n.getUrl());
                }
                outMsg = out_news;
            }
        }
        //最新融资包
        else if (KeyWords.KEY_RZB.equalsIgnoreCase(msgContent)) {
            OutTextMsg no_msg = new OutTextMsg(msg);
            no_msg.setContent("暂时无法获取最新融资包");
            outMsg = no_msg;
            List<Rzb> rzbs = Cache.getRzbs();
            if (rzbs != null && !rzbs.isEmpty()) {
                OutNewsMsg out_news = new OutNewsMsg(msg);
                out_news.addNews("融资城最新融资包\t" + Cache.getPacketUpdate(), null, title_img, "http://www.352.com/trading3/webTradingRzbList.do");
                for (int i = 0; i < Math.min(rzbs.size(), RZB_COUNT); i++) {
                    Rzb r = rzbs.get(i);
                    out_news.addNews(r.name + " " + r.status + " " + r.total_number + "份", null, null, r.url);
                }
                outMsg = out_news;
            }
        }
        //最新资产包
        else if (KeyWords.KEY_ZCB.equalsIgnoreCase(msgContent)) {
            OutTextMsg no_msg = new OutTextMsg(msg);
            no_msg.setContent("暂时无法获取最新资产包");
            outMsg = no_msg;
            List<Zcb> zcbs = Cache.getZcbs();
            if (zcbs != null && !zcbs.isEmpty()) {
                OutNewsMsg out_news = new OutNewsMsg(msg);
                out_news.addNews("融资城最新资产包\t" + Cache.getPacketUpdate(), null, title_img, "http://www.352.com/trade/assetInfoPlatform.x");
                for (int i = 0; i < Math.min(zcbs.size(), RZB_COUNT); i++) {
                    Zcb z = zcbs.get(i);
                    out_news.addNews(z.name + " " + z.status + " " + z.total_number + "份", null, null, z.url);
                }
                outMsg = out_news;
            }
        }
        //如何使用
        else if (KeyWords.KEY_RHSY.equalsIgnoreCase(msgContent)) {
            OutNewsMsg about = new OutNewsMsg(msg);
            about.addNews("如何使用融资城", null, "http://www.352.com/uploadfiles/member/1423188510156.png",
                    "http://mingrui.vip.352.com/harborII/newsAction!detailOneHarborNew.dox?id=85777&harborMemberId=4106&source=HARBOR");
            outMsg = about;
        }
        //关于融资城
        else if (KeyWords.KEY_ABOUT.equalsIgnoreCase(msgContent)) {
            OutNewsMsg about = new OutNewsMsg(msg);
            about.addNews("关于融资城", null, "http://www.352.com/images/top2013/top2013_01.jpg", "http://www.352.com/about/index.jsp");
            outMsg = about;
        }
        //帮助
        else if (KeyWords.KEY_HELP.equalsIgnoreCase(msgContent)) {
            OutTextMsg textMsg = new OutTextMsg(msg);
            textMsg.setContent(KeyWords.HELP_STR);
            outMsg = textMsg;
        }
        //其它文本消息，调用图灵机器人
        else {
            OutTextMsg textMsg = new OutTextMsg(msg);
            String robot_result = RobotUtil.getResult(msgContent);
            log.info("robot response:" + robot_result);
            if (robot_result != null && !"".equals(robot_result))
                textMsg.setContent(robot_result);
            else
                textMsg.setContent(KeyWords.HELP_STR);
            outMsg = textMsg;
        }
        controller.render(outMsg);
    }
}
