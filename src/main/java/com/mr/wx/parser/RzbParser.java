package com.mr.wx.parser;

import com.mr.wx.common.Cache;
import com.mr.wx.entity.Rzb;
import com.mr.wx.util.HtmlUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/2/2.
 */
public class RzbParser {
//    static {
//        PropertyConfigurator.configure("log4j.properties");
//    }
    private static String encoding = "UTF-8", url = "http://www.352.com/trading3/webTradingRzbList.do", host = "www.352.com";
    private static Logger log;

    private static Logger getLogger() {
        if (log == null)
            log = Logger.getLogger(RzbParser.class);
        return log;
    }

    public static void parse() {
        getLogger().info("开始获取最新融资包...");
        try {
            //融资包
            Parser parser = new Parser();
            parser.setInputHTML(HtmlUtil.get(url, host, encoding));
            parser.setEncoding(encoding);
            NodeList rzb_rows = parser.extractAllNodesThatMatch(new CssSelectorNodeFilter("div[class='rzbcollect_list']"));
            ArrayList<Rzb> rzbs = new ArrayList<Rzb>();
            for (int i = 0; i < rzb_rows.size(); i++) {
                rzbs.add(parseRzb(rzb_rows.elementAt(i)));
            }
            Cache.rzbs = rzbs;
            getLogger().info("融资包解析完成...");
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().error("融资包解析出错", e);
        }
    }

    private static Rzb parseRzb(Node node) {
        Rzb rzb = new Rzb();
        for (int i = 0; i < node.getChildren().size(); i++) {
            Node child = node.getChildren().elementAt(i);
            if (child instanceof Div) {
                String css = ((Div)child).getAttribute("class");
                //融资包名称、状态
                if ("title".equals(css)) {
                    for (int j = 0; j < child.getChildren().size(); j++) {
                        Node n = child.getChildren().elementAt(j);
                        if (n instanceof Span) {
                            rzb.name = ((Span) n).getAttribute("title");
//                            System.out.println("name:" + ((Span) n).getAttribute("title"));
                            for (int x = 0; x < n.getChildren().size(); x++) {
                                Node nn = n.getChildren().elementAt(x);
                                if (nn instanceof Span) {
                                    rzb.status = ((Span) nn).getStringText().trim();
//                                    System.out.println("status:" + ((Span) nn).getStringText().trim());
                                }
                            }
                        }
                    }
                //查看详情
                } else if ("more".equals(css)) {
                    for (int j = 0; j < child.getChildren().size(); j++) {
                        Node n = child.getChildren().elementAt(j);
                        if (n instanceof LinkTag) {
                            rzb.url = ((LinkTag) n).getAttribute("href");
//                            System.out.println("url:" + ((LinkTag) n).getAttribute("href"));
                        }
                    }
                }
            } else if (child instanceof TableTag) {
                TableTag table = (TableTag)child;
                rzb.publish_date = table.getRow(0).getColumns()[3].getStringText().trim();
                rzb.manager = table.getRow(1).getColumns()[1].getStringText().split(" ")[0].trim();
                rzb.total_number = table.getRow(2).getColumns()[1].getStringText().trim().split(" ")[0].trim();
//                System.out.println("发布时间\t" + table.getRow(0).getColumns()[3].getStringText().trim());
//                System.out.println("项目管理方\t" + table.getRow(1).getColumns()[1].getStringText().split(" ")[0].trim());
//                System.out.println("总份数\t" + table.getRow(2).getColumns()[1].getStringText().trim().split(" ")[0].trim());
                String price = table.getRow(2).getColumns()[3].getStringText().trim();
                price = price.substring(price.indexOf(">") + 1, price.lastIndexOf("<")).trim();
                price = price.substring(0, price.indexOf(".")).replace(",", "");
                rzb.price = price;
//                System.out.println("单价\t" + price);
                String total_money = table.getRow(3).getColumns()[1].getStringText().trim();
                total_money = total_money.substring(total_money.indexOf(">") + 1, total_money.lastIndexOf("<")).trim();
                total_money = total_money.substring(0, total_money.indexOf("."));
                rzb.total_money = total_money;
                rzb.days = table.getRow(3).getColumns()[3].getStringText().trim().split(" ")[0].trim();
//                System.out.println("总金额\t" + total_money);
//                System.out.println("合作天数\t" + table.getRow(3).getColumns()[3].getStringText().trim().split(" ")[0].trim());
            }
        }
        return rzb;
    }

    public static void main(String[] args) {
        parse();
    }
}
