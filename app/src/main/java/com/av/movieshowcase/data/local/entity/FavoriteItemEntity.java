package com.av.movieshowcase.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = ("item"))
public class FavoriteItemEntity {

        @PrimaryKey(autoGenerate = true)
        public long id;
        public long itemId;
        public Double voteAverage;
        public String type;
        public String title;
        public String releaseDate;
        public String posterPath;
        public String list;

    public FavoriteItemEntity() {
    }

    public FavoriteItemEntity(long itemId, Double voteAverage, String type, String title, String releaseDate, String posterPath, String list) {
        this.itemId = itemId;
        this.voteAverage = voteAverage;
        this.type = type;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.list = list;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }
}
