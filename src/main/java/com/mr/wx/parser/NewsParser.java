package com.mr.wx.parser;

import com.mr.wx.common.Cache;
import com.mr.wx.entity.News;
import com.mr.wx.util.HtmlUtil;
import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/2/2.
 */
public class NewsParser {
    private static String encoding = "UTF-8", url = "http://www.352.com/news/newslist_ttid-428.html", host = "www.352.com", base_link = "http://" + host;
    private static Logger log;

    private static Logger getLogger() {
        if (log == null)
            log = Logger.getLogger(NewsParser.class);
        return log;
    }

    public static void parse() {
        getLogger().info("开始获取最新资讯");
        try {
            String html = HtmlUtil.get(url, host, encoding);
            //系统咨询
            Parser parser = new Parser();
            parser.setInputHTML(html);
            parser.setEncoding(encoding);
            NodeList news_rows = parser.extractAllNodesThatMatch(new CssSelectorNodeFilter("div[class='forum_24_1'] div[class='row']"));
            ArrayList<News> news = new ArrayList<News>();
            for (int i = 0; i < news_rows.size(); i ++) {
                Node node = news_rows.elementAt(i);
                NodeList l_div = new NodeList();
                node.collectInto(l_div, new TagNameFilter("div"));
                NodeList l_a = new NodeList();
                node.collectInto(l_a, new TagNameFilter("a"));
                if (l_div != null && l_div.size() > 2 && l_a != null && l_a.size() > 1) {
                    Div date_div = (Div) l_div.elementAt(2);
                    LinkTag title_link = (LinkTag) l_a.elementAt(1);
                    news.add(new News(title_link.getAttribute("title"), date_div.getStringText(), base_link + title_link.getAttribute("href")));
                }
            }
            //热门咨询
            parser = new Parser();
            parser.setInputHTML(html);
            parser.setEncoding(encoding);
            NodeList top_rows = parser.extractAllNodesThatMatch(new CssSelectorNodeFilter("div[class='forum_19'] div[class='row']"));
            ArrayList<News> top_news = new ArrayList<News>();
            for (int i = 0; i < top_rows.size(); i ++) {
                Node node = top_rows.elementAt(i);
                NodeList l_a = new NodeList();
                node.collectInto(l_a, new TagNameFilter("a"));
                if (l_a != null && l_a.size() > 0) {
                    LinkTag title_link = (LinkTag) l_a.elementAt(0);
                    top_news.add(new News(title_link.getAttribute("title"), base_link + title_link.getAttribute("href")));
                }
            }
            Cache.current_news = news;
            Cache.top_news = top_news;
            Cache.news_update_ms = System.currentTimeMillis();
            getLogger().info("系统资讯解析完成...");
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().error("系统资讯解析失败..", e);
        }
    }

    public static void main(String[] args) {
        NewsParser.parse();
        List<News> cs = Cache.current_news;
        List<News> ts = Cache.top_news;
        for (News n : cs) {
            System.out.println(n);
        }
        for (News n : ts) {
            System.out.println(n);
        }
    }
}
