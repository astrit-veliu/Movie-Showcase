package com.av.movieshowcase.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.av.movieshowcase.data.local.dao.FavoriteItemDao;
import com.av.movieshowcase.data.local.dao.FavoriteListDao;
import com.av.movieshowcase.data.local.dao.HistoryDao;
import com.av.movieshowcase.data.local.dao.MovieDao;
import com.av.movieshowcase.data.local.dao.TvDao;
import com.av.movieshowcase.data.local.entity.FavoriteItemEntity;
import com.av.movieshowcase.data.local.entity.FavoriteListEntity;
import com.av.movieshowcase.data.local.entity.HistoryEntity;
import com.av.movieshowcase.data.local.entity.MovieEntity;
import com.av.movieshowcase.data.local.entity.TvEntity;

/**
 * Created by aveliu2 on 01,October,2019
 */

@Database(entities = {MovieEntity.class, TvEntity.class, HistoryEntity.class, FavoriteListEntity.class, FavoriteItemEntity.class},
        version = 4,  exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();
    public abstract TvDao tvDao();
    public abstract HistoryDao historyDao();
    public abstract FavoriteListDao favoriteListDao();
    public abstract FavoriteItemDao favoriteItemDao();


    private static volatile MovieDatabase INSTANCE;
    public static MovieDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MovieDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context);
                }
            }
        }
        return INSTANCE;
    }

    private static MovieDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context,
                MovieDatabase.class, "Entertainment.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration() //prevents migration error
                .build();
    }

}
