package com.mr.wx.common;

import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.jfinal.MsgController;
import com.jfinal.weixin.sdk.msg.in.*;
import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
import com.jfinal.weixin.sdk.msg.in.event.InLocationEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMenuEvent;
import com.jfinal.weixin.sdk.msg.in.event.InQrCodeEvent;
import com.jfinal.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;
import com.mr.wx.msg.*;

/**
 * Created by Administrator on 2015/1/30.
 */
public class BaseMsgController extends MsgController {

    private FollowMsgHandler<InFollowEvent> followMsgHandler;
    private TextMsgHandler<InTextMsg> textMsgHandler;
    private ImageMsgHandler<InImageMsg> imageMsgHandler;
    private LocationMsgHandler<InLocationMsg> locationMsgHandler;
    private VoiceMsgHandler<InVoiceMsg> voiceMsgHandler;
    private VideoMsgHandler<InVideoMsg> videoMsgHandler;
    private LinkMsgHandler<InLinkMsg> linkMsgHandler;

    public BaseMsgController() {
        this.followMsgHandler = new FollowMsgHandler<InFollowEvent>(this);
        this.textMsgHandler = new TextMsgHandler<InTextMsg>(this);
        this.imageMsgHandler = new ImageMsgHandler<InImageMsg>(this);
        this.locationMsgHandler = new LocationMsgHandler<InLocationMsg>(this);
        this.voiceMsgHandler = new VoiceMsgHandler<InVoiceMsg>(this);
        this.videoMsgHandler = new VideoMsgHandler<InVideoMsg>(this);
        this.linkMsgHandler = new LinkMsgHandler<InLinkMsg>(this);
    }

    @Override
    protected void processInTextMsg(InTextMsg inTextMsg) {
        textMsgHandler.handleMsg(inTextMsg);
    }

    @Override
    protected void processInImageMsg(InImageMsg inImageMsg) {
        imageMsgHandler.handleMsg(inImageMsg);
    }

    @Override
    protected void processInVoiceMsg(InVoiceMsg inVoiceMsg) {
        voiceMsgHandler.handleMsg(inVoiceMsg);
    }

    @Override
    protected void processInVideoMsg(InVideoMsg inVideoMsg) {
        videoMsgHandler.handleMsg(inVideoMsg);
    }

    @Override
    protected void processInLocationMsg(InLocationMsg inLocationMsg) {
        locationMsgHandler.handleMsg(inLocationMsg);
    }

    @Override
    protected void processInLinkMsg(InLinkMsg inLinkMsg) {
        linkMsgHandler.handleMsg(inLinkMsg);
    }

    @Override
    protected void processInFollowEvent(InFollowEvent inFollowEvent) {
        followMsgHandler.handleMsg(inFollowEvent);
    }

    @Override
    protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent) {
    }

    @Override
    protected void processInLocationEvent(InLocationEvent inLocationEvent) {
    }

    @Override
    protected void processInMenuEvent(InMenuEvent inMenuEvent) {
    }

    @Override
    protected void processInSpeechRecognitionResults(InSpeechRecognitionResults inSpeechRecognitionResults) {
    }

    @Override
    public ApiConfig getApiConfig() {
        ApiConfig ac = new ApiConfig();

        // 配置微信 API 相关常量
        ac.setToken(PropKit.get("token"));
        ac.setAppId(PropKit.get("appId"));
        ac.setAppSecret(PropKit.get("appSecret"));

        /**
         *  是否对消息进行加密，对应于微信平台的消息加解密方式：
         *  1：true进行加密且必须配置 encodingAesKey
         *  2：false采用明文模式，同时也支持混合模式
         */
        ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
        ac.setEncodingAesKey(PropKit.get("encodingAesKey", "encodingAesKey"));
        return ac;
    }
}
