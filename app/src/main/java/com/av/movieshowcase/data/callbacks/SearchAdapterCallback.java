package com.av.movieshowcase.data.callbacks;

import com.av.movieshowcase.data.local.entity.HistoryEntity;

public interface SearchAdapterCallback {
    void searchFromAdapter(String query);
    void deleteSearchItem(HistoryEntity item);
    void deleteAllSearchItems();
}
