package com.av.movieshowcase.data.local.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = ("log"))
public class HistoryEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String query;
    public String last_search;
    public String type;

    public HistoryEntity() {
    }

    public HistoryEntity(long id, String query, String last_search, String type) {
        this.id = id;
        this.query = query;
        this.last_search = last_search;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getLast_search() {
        return last_search;
    }

    public void setLast_search(String last_search) {
        this.last_search = last_search;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
