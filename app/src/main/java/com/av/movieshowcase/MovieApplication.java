package com.av.movieshowcase;

import android.app.Activity;
import android.app.Application;


import androidx.fragment.app.Fragment;

import com.av.movieshowcase.di.component.DaggerApplicationComponent;
import com.av.movieshowcase.di.module.DbModule;
import com.av.movieshowcase.di.module.NetworkModule;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;


public class MovieApplication extends Application implements HasActivityInjector, HasSupportFragmentInjector {



    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentInjector;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mFragmentInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerApplicationComponent.builder()
                .application(this)
                .networkModule(new NetworkModule())
                .dbModule(new DbModule())
                .build()
            .inject(this);
    }
}
