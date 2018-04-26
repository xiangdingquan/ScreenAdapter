package org.xdq.screenadapter.app;

import android.app.Application;

import org.xdq.screenadpter.ScreenAdaper;

/**
 * 文件描述: 介绍类的详细作用
 * 作者: Created by xdq on 2018/4/26
 * 版本号：v1.0
 * 组织名称: xiangdingquan.github.com
 * 包名：org.xdq.screenadapter.app
 * 项目名称：ScreenAdapter
 * 版权申明：暂无
 */
public class ScreenApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        ScreenAdaper.getInstance(this,720).activate();


    }
}
