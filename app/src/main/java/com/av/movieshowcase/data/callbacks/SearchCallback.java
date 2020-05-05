package com.av.movieshowcase.data.callbacks;

import com.av.movieshowcase.data.local.entity.HistoryEntity;
import com.av.movieshowcase.data.remote.model.MovieTrendingResponse;

import java.util.List;

public interface SearchCallback {
    void showSearchResponse(MovieTrendingResponse response);
    void showHistory(List<HistoryEntity> searchList);
    void showError(String message);
}