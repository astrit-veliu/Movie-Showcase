package com.av.movieshowcase.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.av.movieshowcase.data.local.entity.FavoriteItemEntity;
import com.av.movieshowcase.data.local.entity.MovieEntity;

import java.util.List;

@Dao
public interface FavoriteItemDao {

    @Query("SELECT * FROM item WHERE `type`=:type AND `list`=:list")
    LiveData<List<FavoriteItemEntity>> getItemsByTypeInList(String type, String list);

    @Query("SELECT * FROM item ")
    LiveData<List<FavoriteItemEntity>> getAllItemsFromAllLists();

    @Query("SELECT * FROM item WHERE `itemId`=:itemId AND `list`=:list")
    int isInList(long itemId, String list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addInList(FavoriteItemEntity favoriteItemEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateItem(FavoriteItemEntity favoriteItemEntity);


    @Delete
    void removeItem(FavoriteItemEntity movie_item);

    @Query("DELETE FROM item WHERE `itemId`=:itemId AND `list`=:list")
    int removeItemByIdInList(long itemId, String list);

    @Query("DELETE FROM item WHERE `list`=:list")
    int removeItemsByList(String list);

    @Query("DELETE FROM item")
    void deleteAll();

}
