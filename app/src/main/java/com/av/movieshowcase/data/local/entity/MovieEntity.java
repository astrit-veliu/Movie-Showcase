package com.av.movieshowcase.data.local.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = ("movie"))
public class MovieEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public long movie_id;
    public String movie_detail;
    public String type;
    public String list;

    public MovieEntity() {
    }

    public MovieEntity(long id, long movie_id, String movie_detail, String type, String list) {
        this.id = id;
        this.movie_id = movie_id;
        this.movie_detail = movie_detail;
        this.type = type;
        this.list = list;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(long movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie_detail() {
        return movie_detail;
    }

    public void setMovie_detail(String movie_detail) {
        this.movie_detail = movie_detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }
}
