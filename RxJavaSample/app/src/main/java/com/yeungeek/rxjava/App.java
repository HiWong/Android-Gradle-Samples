package com.yeungeek.rxjava;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by yeungeek on 2015/9/29.
 */
public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }
}
