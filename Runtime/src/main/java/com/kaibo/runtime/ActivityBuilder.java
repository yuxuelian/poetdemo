package com.kaibo.runtime;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author kaibo
 * @date 2018/11/24 22:43
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

public class ActivityBuilder {

    private static final String BUNDLE_NAME_POSIX = "Builder";

    public static final ActivityBuilder INSTANCE = new ActivityBuilder();

    private Application application;

    private ActivityBuilder() {

    }

    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            performInject(activity, savedInstanceState);
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
            performSaveState(activity, outState);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

    private void performInject(Activity activity, Bundle savedInstanceState) {
        try {
            Class
                    .forName(activity.getClass().getName() + BUNDLE_NAME_POSIX)
                    .getDeclaredMethod("inject", Activity.class, Bundle.class)
                    .invoke(null, activity, savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void performSaveState(Activity activity, Bundle outState) {
        try {
            Class
                    .forName(activity.getClass().getName() + BUNDLE_NAME_POSIX)
                    .getDeclaredMethod("savedInstanceState", Activity.class, Bundle.class)
                    .invoke(null, activity, outState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(Context context) {
        if (application == null) {
            this.application = (Application) context.getApplicationContext();
            // 注册回调监听
            this.application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        }
    }

    public static void startActivity(Context context, Intent intent) {
        if (!(context instanceof Activity)) {
            // context不是Activity需要 启动新栈
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

}
