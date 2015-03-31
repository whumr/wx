package com.mr.wx.parser;

import com.mr.wx.common.Cache;
import com.mr.wx.entity.News;
import com.mr.wx.entity.Rzb;
import com.mr.wx.entity.Zcb;
import com.mr.wx.util.HtmlUtil;
import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/2/2.
 */
public class PacketParser {
    private static String encoding = "UTF-8", url = "http://www.352.com", host = "www.352.com", base_link = "http://" + host;
    private static Logger log;
    private static String[] status = {"待定","待开放","申购中","路演中","已满额"};

    private static Logger getLogger() {
        if (log == null)
            log = Logger.getLogger(PacketParser.class);
        return log;
    }

    public static void parse() {
        getLogger().info("开始获取最新资产包、融资包");
        try {
            String html = HtmlUtil.get(url, host, encoding);
            getLogger().info("已获取到页面内容");
            Parser parser = new Parser();
            parser.setInputHTML(html);
//            parser.setResource("index.html");
            parser.setEncoding(encoding);
            NodeList boxes = parser.extractAllNodesThatMatch(new CssSelectorNodeFilter("div[class='box_l']"));
            ArrayList<Rzb> news = new ArrayList<Rzb>();
            for (int i = 0; i < boxes.size(); i ++) {
                Node node = boxes.elementAt(i);
                //寻找标题，定位融资包、资产包
                String box_title = "";
                NodeList div_list = node.getChildren();
                for (int n = 0; n < div_list.size(); n++) {
                    Node nn = div_list.elementAt(n);
                    if (nn instanceof Div) {
                        String css = ((Div) nn).getAttribute("class");
                        if ("title_l".equals(css)) {
                            NodeList t_list = nn.getChildren();
                            for (int j = 0; j < t_list.size(); j++) {
                                Node nj = t_list.elementAt(j);
                                if (nj instanceof Div && "l2".equals(((Div) nj).getAttribute("class"))) {
                                    NodeList nj_list = nj.getChildren();
                                    for (int k = 0; k < nj_list.size(); k++) {
                                        Node nk = nj_list.elementAt(k);
                                        if (nk instanceof LinkTag) {
                                            box_title = ((LinkTag) nk).getLinkText().trim();
                                        }
                                    }
                                }
                            }
                        }
                        if ("".equals(box_title) || "资产包商城".equals(box_title) || "融资包商城".equals(box_title)) {
                            //列表
                            if ("list".equals(css)) {
                                //资产包
                                if ("资产包商城".equals(box_title) && "list".equals(css)) {
                                    parseZcb(nn.getChildren());
                                    //融资包
                                } else if ("融资包商城".equals(box_title)) {
                                    parseRzb(nn.getChildren());
                                }
                            }
                        } else
                            continue;
                    }
                }
            }
            getLogger().info("最新资产包、融资包解析完成...");
            Cache.packet_update_ms = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().error("最新资产包、融资包解析失败..", e);
        }
    }

    private static void parseZcb(NodeList list) {
        ArrayList<Zcb> zcbs = new ArrayList<Zcb>();
        for (int i = 0; i < list.size(); i++) {
            Node n = list.elementAt(i);
            if (n instanceof Div) {
                Zcb zcb = new Zcb();
                NodeList l1 = n.getChildren();
                for (int j = 0; j < l1.size(); j++) {
                    Node n1 = l1.elementAt(j);
                    if (n1 instanceof Div) {
                        String css = ((Div) n1).getAttribute("class");
                        //名称
                        if ("w9".equals(css)) {
                            NodeList l2 = n1.getChildren();
                            for (int k = 0; k < l2.size(); k++) {
                                Node n2 = l2.elementAt(k);
                                if (n2 instanceof LinkTag) {
                                    zcb.name = ((LinkTag) n2).getAttribute("title");
                                    zcb.url = base_link + ((LinkTag) n2).getAttribute("href");
                                }
                            }
                            //份数
                        } else if ("w10".equals(css)) {
                            NodeList l2 = n1.getChildren();
                            for (int k = 0; k < l2.size(); k++) {
                                Node n2 = l2.elementAt(k);
                                if (n2 instanceof Span) {
                                    zcb.total_number = ((Span) n2).getStringText().trim();
                                }
                            }
                            //类型
                        } else if ("w12".equals(css)) {
                            zcb.type = ((Div) n1).getStringText().trim();
                            //状态
                        } else if ("w11".equals(css)) {
                            zcb.status = getStatus((Div) n1);
                        }
                    }
                }
                zcbs.add(zcb);
            }
        }
        Cache.zcbs = zcbs;
    }

    private static void parseRzb(NodeList list) {
        ArrayList<Rzb> rzbs = new ArrayList<Rzb>();
        for (int i = 0; i < list.size(); i++) {
            Node n = list.elementAt(i);
            if (n instanceof Div) {
                Rzb rzb = new Rzb();
                NodeList l1 = n.getChildren();
                for (int j = 0; j < l1.size(); j++) {
                    Node n1 = l1.elementAt(j);
                    if (n1 instanceof Div) {
                        String css = ((Div) n1).getAttribute("class");
                        //名称
                        if ("w9".equals(css)) {
                            NodeList l2 = n1.getChildren();
                            for (int k = 0; k < l2.size(); k++) {
                                Node n2 = l2.elementAt(k);
                                if (n2 instanceof LinkTag) {
                                    rzb.name = ((LinkTag) n2).getAttribute("title");
                                    rzb.url = base_link + ((LinkTag) n2).getAttribute("href");
                                }
                            }
                            //份数
                        } else if ("w10".equals(css)) {
                            NodeList l2 = n1.getChildren();
                            for (int k = 0; k < l2.size(); k++) {
                                Node n2 = l2.elementAt(k);
                                if (n2 instanceof Span) {
                                    rzb.total_number = ((Span) n2).getStringText().trim();
                                }
                            }
                            //类型
                        } else if ("w12".equals(css)) {
//                        rzb.type = ((Div) n1).getStringText().trim();
                            //状态
                        } else if ("w11".equals(css)) {
                            rzb.status = getStatus((Div) n1);
                        }
                    }
                }
                rzbs.add(rzb);
            }
        }
        Cache.rzbs = rzbs;
    }

    private static String getStatus(Div status_div) {
        String text = status_div.getStringText();
        for (String s : status) {
            if (text.indexOf(s) > 0)
                return s;
        }
        return "";
    }

    public static void main(String[] args) {
        parse();
    }
}
