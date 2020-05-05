package com.av.movieshowcase.data.callbacks;

import com.av.movieshowcase.data.remote.model.MovieTrendingResponse;
import com.av.movieshowcase.data.remote.model.movie_details.MovieDetailResponse;

/**
 * Created by aveliu2 on 15,January,2020
 */
public interface DetailCallback {
    void showDetailResponse(MovieDetailResponse response);
    void showError(String message);
}
