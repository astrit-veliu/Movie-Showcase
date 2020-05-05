package com.av.movieshowcase.data.callbacks;

import com.av.movieshowcase.data.local.entity.FavoriteItemEntity;
import com.av.movieshowcase.data.local.entity.FavoriteListEntity;

import java.util.List;

public interface ProfileCallback {
    void showListNames(List<FavoriteListEntity> allLists);
    void showFavorites(List<FavoriteItemEntity> favoriteItems);
}
