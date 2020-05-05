package com.av.movieshowcase.data.remote.model.movie_details;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by aveliu2 on 15,January,2020
 */
public class Videos {

    @SerializedName("results")
    @Expose
    private List<VideoResult> results = null;

    public List<VideoResult> getResults() {
        return results;
    }

    public void setResults(List<VideoResult> results) {
        this.results = results;
    }

}
