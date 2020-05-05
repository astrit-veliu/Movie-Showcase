package com.av.movieshowcase.di.component;

import android.app.Application;

import com.av.movieshowcase.MovieApplication;
import com.av.movieshowcase.di.module.ActivityModule;
import com.av.movieshowcase.di.module.DbModule;
import com.av.movieshowcase.di.module.FragmentModule;
import com.av.movieshowcase.di.module.NetworkModule;
import com.av.movieshowcase.di.module.ViewModelModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by aveliu2 on 01,October,2019
 */

@Singleton
@Component(modules = {NetworkModule.class, DbModule.class, ViewModelModule.class,
        AndroidSupportInjectionModule.class, ActivityModule.class, FragmentModule.class})
public interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        @BindsInstance
        Builder networkModule(NetworkModule apiModule);

        @BindsInstance
        Builder dbModule(DbModule dbModule);

        ApplicationComponent build();
    }

    void inject(MovieApplication movieApplication);

}
