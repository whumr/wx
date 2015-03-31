package com.mr.wx.msg;

import com.jfinal.weixin.sdk.jfinal.MsgController;
import com.jfinal.weixin.sdk.msg.in.InVoiceMsg;
import com.jfinal.weixin.sdk.msg.out.OutVoiceMsg;
import com.mr.wx.common.BaseMsgHandler;

/**
 * Created by Administrator on 2015/1/30.
 */
public class VoiceMsgHandler<M extends InVoiceMsg> extends BaseMsgHandler<M> {

    public VoiceMsgHandler(MsgController controller) {
        super(controller);
    }

    @Override
    public void handleMsg(InVoiceMsg msg) {
        OutVoiceMsg outMsg = new OutVoiceMsg(msg);
        // 将刚发过来的语音再发回去
        outMsg.setMediaId(msg.getMediaId());
        controller.render(outMsg);
    }
}
