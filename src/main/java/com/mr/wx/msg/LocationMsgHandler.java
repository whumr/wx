package com.mr.wx.msg;

import com.jfinal.weixin.sdk.jfinal.MsgController;
import com.jfinal.weixin.sdk.msg.in.InLocationMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;
import com.mr.wx.common.BaseMsgHandler;

/**
 * Created by Administrator on 2015/1/30.
 */
public class LocationMsgHandler<M extends InLocationMsg> extends BaseMsgHandler<M> {

    public LocationMsgHandler(MsgController controller) {
        super(controller);
    }

    @Override
    public void handleMsg(InLocationMsg msg) {
        OutTextMsg outMsg = new OutTextMsg(msg);
        outMsg.setContent("已收到地理位置消息:" +
                "\nlocation_X = " + msg.getLocation_X() +
                "\nlocation_Y = " + msg.getLocation_Y() +
                "\nscale = " + msg.getScale() +
                "\nlabel = " + msg.getLabel());
        controller.render(outMsg);
    }
}
