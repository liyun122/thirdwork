package com.example.androidclass2;

import android.app.Application;
import android.content.Context;

/**
 * Created by xing on 2020/4/17.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
