package com.av.movieshowcase.di.module;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.av.movieshowcase.data.local.MovieDatabase;
import com.av.movieshowcase.data.local.dao.FavoriteItemDao;
import com.av.movieshowcase.data.local.dao.FavoriteListDao;
import com.av.movieshowcase.data.local.dao.HistoryDao;
import com.av.movieshowcase.data.local.dao.MovieDao;
import com.av.movieshowcase.data.local.dao.TvDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aveliu2 on 01,October,2019
 */

@Module
public class DbModule {

    @Provides
    @Singleton
    MovieDatabase provideDatabase(@NonNull Application application) {
        return Room.databaseBuilder(application,
                MovieDatabase.class, "Entertainment.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration() //prevents migration error
                .build();
    }

    @Provides
    @Singleton
    MovieDao provideMovieDao(@NonNull MovieDatabase appDatabase) {
        return appDatabase.movieDao();
    }


    @Provides
    @Singleton
    TvDao provideTvDao(@NonNull MovieDatabase appDatabase) {
        return appDatabase.tvDao();
    }


    @Provides
    @Singleton
    HistoryDao provideHistoryDao(@NonNull MovieDatabase appDatabase) {
        return appDatabase.historyDao();
    }


    @Provides
    @Singleton
    FavoriteListDao provideFavoriteListDao(@NonNull MovieDatabase appDatabase) {
        return appDatabase.favoriteListDao();
    }

    @Provides
    @Singleton
    FavoriteItemDao provideFavoriteItemDao(@NonNull MovieDatabase appDatabase) {
        return appDatabase.favoriteItemDao();
    }

}
