package com.mr.wx.msg;

import com.jfinal.weixin.sdk.jfinal.MsgController;
import com.jfinal.weixin.sdk.msg.in.InLinkMsg;
import com.jfinal.weixin.sdk.msg.out.OutNewsMsg;
import com.mr.wx.common.BaseMsgHandler;

/**
 * Created by Administrator on 2015/1/30.
 */
public class LinkMsgHandler<M extends InLinkMsg> extends BaseMsgHandler<M> {

    public LinkMsgHandler(MsgController controller) {
        super(controller);
    }

    /**
     * 实现父类抽方法，处理链接消息
     * 特别注意：测试时需要发送我的收藏中的曾经收藏过的图文消息，直接发送链接地址会当做文本消息来发送
     */
    @Override
    public void handleMsg(InLinkMsg msg) {
        OutNewsMsg outMsg = new OutNewsMsg(msg);
        outMsg.addNews("链接消息已成功接收", "链接使用图文消息的方式发回给你，还可以使用文本方式发回。点击图文消息可跳转到链接地址页面，是不是很好玩 :)" ,
                "http://mmbiz.qpic.cn/mmbiz/zz3Q6WSrzq1ibBkhSA1BibMuMxLuHIvUfiaGsK7CC4kIzeh178IYSHbYQ5eg9tVxgEcbegAu22Qhwgl5IhZFWWXUw/0", msg.getUrl());
        controller.render(outMsg);
    }
}
