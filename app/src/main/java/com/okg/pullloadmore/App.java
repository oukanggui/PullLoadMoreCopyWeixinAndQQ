package com.okg.pullloadmore;

import android.app.Application;
import android.content.Context;

/**
 * @author oukanggui
 * @date 2019-07-28
 * 描述
 */
public class App extends Application {
    private static Context sInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static Context getContext(){
        return sInstance;
    }
}
