package org.xdq.screenadpter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * 文件描述: 百分比适配方案
 * 作者: Created by xdq on 2018/4/26
 * 版本号：v1.0
 * 组织名称: xiangdingquan.github.com
 * 包名：org.xdq.screenadpter
 * 项目名称：ScreenAdapter
 * 版权申明：暂无
 */
public class ScreenAdaper {

    private volatile static ScreenAdaper instance;

    private Application mApp;

    /**
     * 设计尺寸
     */
    private float mDesignWidth;

    /**
     * 屏幕的实际宽
     */
    private float mWidth;

    /**
     * activity
     */
    private Application.ActivityLifecycleCallbacks mCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            resetDensity(mApp);
            resetDensity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

    /**
     * 构造器
     * @param application 传入的application类
     * @param designWidth 传入的设计稿的宽度尺寸
     */
    private ScreenAdaper(Application application, float designWidth){
        mApp = application;
        mDesignWidth = designWidth;
        Point point = new Point();
        if (mApp !=null){
            WindowManager windowManager = (WindowManager) mApp.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            display.getSize(point);
            mWidth = point.x;
        }
    }

    /**
     * 获取类的实例子
     * @param application application 实例
     * @return 该类的实例
     */
    public static ScreenAdaper getInstance(Application application,float designWidth){
        if (instance == null){
            synchronized (ScreenAdaper.class){
                if (instance==null)
                    instance = new ScreenAdaper(application,designWidth);
            }
        }
        return instance;
    }


    /**
     * 重新计算 displayMetrics.xdpi 使其为设计稿的相对长度
     *
     * @param context 上下文  可以是application 或者activity的 只要用于获取对应实例的 resource 对象
     */
    private void resetDensity(Context context){
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        displayMetrics.xdpi = mWidth/mDesignWidth*72.0f;
        DisplayMetrics metrics = getMetricsOnMiui(resources);
        if (metrics!=null)
            metrics.xdpi = mWidth/mDesignWidth*72.0f;

    }


    /**
     * 恢复系统原来的设置
     * @param context 上下文  可以是application 或者activity的 只要用于获取对应实例的 resource 对象
     */
    private void restoreDensity(Context context){
        context.getResources().getDisplayMetrics().setToDefaults();
        DisplayMetrics metrics = getMetricsOnMiui(context.getResources());
        if (metrics!=null)
            metrics.setToDefaults();
    }


    /**
     * 适配部分miui机型
     * @param resources   resources对象
     * @return DisplayMetrics
     */
    private  DisplayMetrics getMetricsOnMiui(Resources resources){
        if("MiuiResources".equals(resources.getClass().getSimpleName()) || "XResources".equals(resources.getClass().getSimpleName())){
            try {
                Field field = Resources.class.getDeclaredField("mTmpMetrics");
                field.setAccessible(true);
                return  (DisplayMetrics) field.get(resources);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 激活本方案
     */
    public void activate(){
        mApp.registerActivityLifecycleCallbacks(mCallbacks);
    }

    /**
     * 关闭本方案
     */
    public void inactivate(){
        restoreDensity(mApp);
        mApp.unregisterActivityLifecycleCallbacks(mCallbacks);
    }


}
