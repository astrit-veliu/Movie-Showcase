package com.av.movieshowcase.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.av.movieshowcase.data.local.entity.HistoryEntity;
import com.av.movieshowcase.data.local.entity.MovieEntity;

import java.util.List;

/**
 * Created by aveliu2 on 01,October,2019
 */

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie where type=:type")
    LiveData<List<MovieEntity>> getFavoritesByType(String type);

    @Query("SELECT * FROM movie ")
    LiveData<List<MovieEntity>> getAllFavorites();

    @Query("SELECT * FROM movie WHERE `movie_id`=:movie_id")
    int isFavorite(long movie_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addFavorite(MovieEntity movie_item);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateMovie(MovieEntity movie_item);


    @Delete
    void removeFavorite(MovieEntity movie_item);

    @Query("DELETE FROM movie WHERE `type`=:list")
    int removeFavoritesByList(String list);

    @Query("DELETE FROM movie")
    void deleteAll();
}
