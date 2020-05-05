package com.av.movieshowcase.data.remote.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by aveliu2 on 10,January,2020
 */
public class GenresList {

    @SerializedName("genres")
    @Expose
    private List<GenreDetail> genres = null;

    public List<GenreDetail> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreDetail> genres) {
        this.genres = genres;
    }

}
