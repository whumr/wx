/**
 * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.mr.wx.common;

import com.jfinal.config.*;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.mr.wx.util.RobotUtil;
import com.mr.wx.util.TimerUtil;
import org.apache.log4j.BasicConfigurator;

import java.util.Properties;

public class WeixinConfig extends JFinalConfig {

    public Properties loadProp(String pro, String dev) {
        try {
            return loadPropertyFile(pro);
        } catch (Exception e) {
            return loadPropertyFile(dev);
        }
    }

    public void configConstant(Constants me) {
		// 如果生产环境配置文件存在，则优先加载该配置，否则加载开发环境配置文件
		loadProp("config.txt", "config.txt");
		me.setDevMode(getPropertyToBoolean("devMode", false));
		
		// ApiConfigKit 设为开发模式可以在开发阶段输出请求交互的 xml 与 json 数据
		ApiConfigKit.setDevMode(me.getDevMode());

        //配置log4j
//        BasicConfigurator.configure();

        //设置图灵机器人地址
        RobotUtil.init();

        //启动timer
//        TimerUtil.start();
    }
	
	public void configRoute(Routes me) {
		me.add("/msg", BaseMsgController.class);
		me.add("/api", BaseApiController.class, "/api");
	}
	
	public void configPlugin(Plugins me) {
	}
	
	public void configInterceptor(Interceptors me) {
	}
	
	public void configHandler(Handlers me) {
	}
}
