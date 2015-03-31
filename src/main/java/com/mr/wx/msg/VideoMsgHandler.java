package com.mr.wx.msg;

import com.jfinal.weixin.sdk.jfinal.MsgController;
import com.jfinal.weixin.sdk.msg.in.InVideoMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;
import com.mr.wx.common.BaseMsgHandler;

/**
 * Created by Administrator on 2015/1/30.
 */
public class VideoMsgHandler<M extends InVideoMsg> extends BaseMsgHandler<M> {

    public VideoMsgHandler(MsgController controller) {
        super(controller);
    }

    @Override
    public void handleMsg(InVideoMsg msg) {
        /* 腾讯 api 有 bug，无法回复视频消息，暂时回复文本消息代码测试
		OutVideoMsg outMsg = new OutVideoMsg(inVideoMsg);
		outMsg.setTitle("OutVideoMsg 发送");
		outMsg.setDescription("刚刚发来的视频再发回去");
		// 将刚发过来的视频再发回去，经测试证明是腾讯官方的 api 有 bug，待 api bug 却除后再试
		outMsg.setMediaId(inVideoMsg.getMediaId());
		render(outMsg);
		*/
        OutTextMsg outMsg = new OutTextMsg(msg);
        outMsg.setContent("\t视频消息已成功接收，该视频的 mediaId 为: " + msg.getMediaId());
        controller.render(outMsg);
    }
}
