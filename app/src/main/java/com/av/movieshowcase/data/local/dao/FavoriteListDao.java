package com.av.movieshowcase.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.av.movieshowcase.data.local.entity.FavoriteListEntity;
import com.av.movieshowcase.data.local.entity.MovieEntity;

import java.util.List;

@Dao
public interface FavoriteListDao {

    @Query("SELECT * FROM list ")
    LiveData<List<FavoriteListEntity>> getAllFavoriteList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertList(FavoriteListEntity list_item);

    @Query("SELECT * FROM list WHERE `listName`=:list")
    int existList(String list);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateList(FavoriteListEntity list_item);

    @Delete
    void removeList(FavoriteListEntity list_item);


    @Query("DELETE FROM list")
    void deleteAll();

}
