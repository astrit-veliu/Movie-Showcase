package com.av.movieshowcase.data.callbacks;

import com.av.movieshowcase.data.remote.model.MovieTrendingResponse;

public interface TrendingCallback {
    void showTrendingResponse(MovieTrendingResponse response);
    void showLatestMovies(MovieTrendingResponse response);
    void showLatestTv(MovieTrendingResponse response);
    void showError(Throwable message);
}
