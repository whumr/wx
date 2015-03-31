package com.mr.wx.msg;

import com.jfinal.weixin.sdk.jfinal.MsgController;
import com.jfinal.weixin.sdk.msg.in.InImageMsg;
import com.jfinal.weixin.sdk.msg.out.OutImageMsg;
import com.mr.wx.common.BaseMsgHandler;

/**
 * Created by Administrator on 2015/1/30.
 */
public class ImageMsgHandler<M extends InImageMsg> extends  BaseMsgHandler<M> {

    public ImageMsgHandler(MsgController controller) {
        super(controller);
    }

    @Override
    public void handleMsg(InImageMsg msg) {
        OutImageMsg outMsg = new OutImageMsg(msg);
        // 将刚发过来的图片再发回去
        outMsg.setMediaId(msg.getMediaId());
        controller.render(outMsg);
    }
}
