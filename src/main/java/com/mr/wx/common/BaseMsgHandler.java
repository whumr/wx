package com.mr.wx.common;

import com.jfinal.weixin.sdk.jfinal.MsgController;
import com.jfinal.weixin.sdk.msg.in.InMsg;

/**
 * Created by Administrator on 2015/1/30.
 */
public abstract class BaseMsgHandler<M extends InMsg> {

    protected MsgController controller;

    public BaseMsgHandler(MsgController controller) {
        this.controller = controller;
    }

    public abstract void handleMsg(M msg);
}
