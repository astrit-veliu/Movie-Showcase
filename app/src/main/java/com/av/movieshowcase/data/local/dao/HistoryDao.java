package com.av.movieshowcase.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.av.movieshowcase.data.local.entity.HistoryEntity;

import java.util.List;

import io.reactivex.Flowable;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface HistoryDao {

    @Query("SELECT * FROM log where type=:type")
    LiveData<List<HistoryEntity>> getHistoryByType(String type);

    @Query("SELECT * FROM log ")
    LiveData<List<HistoryEntity>> getAllHistory();

    @Query("SELECT * FROM log WHERE `query`=:query")
    int findLogById(String query);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLog(HistoryEntity search_item);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateLog(HistoryEntity search_item);

   /* @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateLog(List<HistoryEntity> search_item);*/

    @Delete
    void deleteLog(HistoryEntity search_item);

    @Query("DELETE FROM log")
    void deleteAll();
}


