package com.mr.wx.parser;

import org.apache.log4j.Logger;
import com.mr.wx.common.Cache;
import com.mr.wx.entity.News;
import com.mr.wx.entity.Rzb;
import com.mr.wx.entity.Zcb;
import com.mr.wx.util.HtmlUtil;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/2/2.
 */
public class ZcbParser {
    private static String encoding = "UTF-8", url = "http://www.352.com/trade/assetInfoPlatform.x", host = "www.352.com";
    private static Logger log;
    private static Logger getLogger() {
        if (log == null)
            log = Logger.getLogger(ZcbParser.class);
        return log;
    }

    public static void parse() {
        System.out.println("开始获取最新资产包");
        getLogger().info("开始获取最新资产包");
        try {
            Parser parser = new Parser();
            parser.setInputHTML(HtmlUtil.get(url, host, encoding));
//            parser.setResource("zcb.html");
            parser.setEncoding(encoding);
            getLogger().info("log:获取资产包页面成功");
            System.out.println("获取资产包页面成功");
            NodeList zcb_rows = parser.extractAllNodesThatMatch(new CssSelectorNodeFilter("div[class='rzbcollect_list']"));
            ArrayList<Zcb> zcbs = new ArrayList<Zcb>();
            for (int i = 0; i < zcb_rows.size(); i++) {
//            for (int i = 0; i < 1; i++) {
                zcbs.add(parseZcb(zcb_rows.elementAt(i)));
            }
            Cache.zcbs = zcbs;
            getLogger().info("资产包解析完成...");
            System.out.println("资产包解析完成");
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().error("资产包解析出错.", e);
            System.out.println("资产包解析出错\t" + e.getMessage());
        }
    }

    private static Zcb parseZcb(Node node) {
        Zcb zcb = new Zcb();
        for (int i = 0; i < node.getChildren().size(); i++) {
            Node child = node.getChildren().elementAt(i);
            if (child instanceof Div) {
                String css = ((Div)child).getAttribute("class");
                //融资包名称、状态
                if ("title".equals(css)) {
                    for (int j = 0; j < child.getChildren().size(); j++) {
                        Node n = child.getChildren().elementAt(j);
                        if (n instanceof Span) {
                            zcb.name = ((Span) n).getAttribute("title");
                            System.out.println("name:" + ((Span) n).getAttribute("title"));
                            for (int x = 0; x < n.getChildren().size(); x++) {
                                Node nn = n.getChildren().elementAt(x);
                                if (nn instanceof Span) {
                                    zcb.status = ((Span) nn).getStringText().trim();
//                                    System.out.println("status:" + ((Span) nn).getStringText().trim());
                                } else if (nn instanceof LinkTag) {
                                    zcb.status = "路演中";
                                }
                            }
                        }
                    }
                    //查看详情
                } else if ("more".equals(css)) {
                    for (int j = 0; j < child.getChildren().size(); j++) {
                        Node n = child.getChildren().elementAt(j);
                        if (n instanceof LinkTag) {
                            zcb.url = ((LinkTag) n).getAttribute("href");
//                            System.out.println("url:" + ((LinkTag) n).getAttribute("href"));
                        }
                    }
                }
            } else if (child instanceof TableTag) {
                TableTag table = (TableTag)child;
                zcb.publish_date = table.getRow(1).getColumns()[3].getStringText().trim();
                zcb.type = table.getRow(1).getColumns()[1].getStringText().trim();
                zcb.manager = table.getRow(2).getColumns()[1].getStringText().replaceAll("&nbsp;", " ") .trim();
                zcb.total_number = table.getRow(3).getColumns()[1].getStringText().trim().split("份")[0].trim();
//                System.out.println("发布时间\t" + table.getRow(0).getColumns()[3].getStringText().trim());
//                System.out.println("项目管理方\t" + table.getRow(1).getColumns()[1].getStringText().split(" ")[0].trim());
//                System.out.println("总份数\t" + table.getRow(2).getColumns()[1].getStringText().trim().split(" ")[0].trim());
                String price = table.getRow(3).getColumns()[3].getStringText().trim();
                price = price.substring(price.indexOf(">") + 1, price.lastIndexOf("<")).trim();
                price = price.substring(0, price.indexOf(".")).replace(",", "");
                zcb.price = price;
//                System.out.println("单价\t" + price);
                int p = Integer.parseInt(price), n = Integer.parseInt(zcb.total_number);
                zcb.total_money = p * n / 10000 + "";
//                zcb.days = table.getRow(3).getColumns()[3].getStringText().trim().split(" ")[0].trim();
//                System.out.println("总金额\t" + total_money);
//                System.out.println("合作天数\t" + table.getRow(3).getColumns()[3].getStringText().trim().split(" ")[0].trim());
            }
        }
        return zcb;
    }

    public static void main(String[] args) {
        parse();
    }
}
