package com.mr.wx.msg;

import com.jfinal.weixin.sdk.jfinal.MsgController;
import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;
import com.mr.wx.common.BaseMsgHandler;
import com.mr.wx.common.KeyWords;

/**
 * Created by Administrator on 2015/1/30.
 */
public class FollowMsgHandler<M extends InFollowEvent> extends BaseMsgHandler<M> {

    public FollowMsgHandler(MsgController controller) {
        super(controller);
    }

    @Override
    public void handleMsg(InFollowEvent msg) {
        OutTextMsg outMsg = new OutTextMsg(msg);
        outMsg.setContent(KeyWords.WELCOME_STR + "\n" + KeyWords.HELP_STR);
        // 如果为取消关注事件，将无法接收到传回的信息
        controller.render(outMsg);
    }
}
