package com.av.movieshowcase.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = ("list"))
public class FavoriteListEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String listName;

    public FavoriteListEntity() {
    }

    public FavoriteListEntity(String listName) {
        this.listName = listName;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
