package com.av.movieshowcase.ui.main;

import com.av.movieshowcase.data.callbacks.ProfileCallback;
import com.av.movieshowcase.data.local.dao.FavoriteItemDao;
import com.av.movieshowcase.data.local.dao.FavoriteListDao;
import com.av.movieshowcase.data.local.entity.FavoriteItemEntity;
import com.av.movieshowcase.ui.base.BaseViewModel;

import javax.inject.Inject;

public class ProfileViewModel extends BaseViewModel {

    private FavoriteListDao favoriteListDao;
    private FavoriteItemDao favoriteItemDao;
    private ProfileCallback callback;

    @Inject
    public ProfileViewModel(FavoriteListDao favoriteListDao, FavoriteItemDao favoriteItemDao) {
        this.favoriteListDao = favoriteListDao;
        this.favoriteItemDao = favoriteItemDao;
    }

    public void setCallback(ProfileCallback callback) {
        this.callback = callback;
    }

    public void insertItemInList(FavoriteItemEntity favoriteItemEntity, String list){
        if(isInList(favoriteItemEntity.getItemId(),list)) favoriteItemDao.updateItem(favoriteItemEntity);
        else favoriteItemDao.addInList(favoriteItemEntity);
    }

    public boolean isInList(long id, String list){
        if(favoriteItemDao.isInList(id,list)>0) return true;
        else return false;
    }

    public void removeItemFromList(FavoriteItemEntity favoriteItemEntity){
        favoriteItemDao.removeItem(favoriteItemEntity);
    }

    public void removeItemFromListById(long id, String list){
        favoriteItemDao.removeItemByIdInList(id, list);
    }

    

    public void removeAllItemsFromList(String listName){
        favoriteItemDao.removeItemsByList(listName);
    }

    public void getListItems(String type, String list) {
        favoriteItemDao.getItemsByTypeInList(type, list).observeForever(log -> callback.showFavorites(log));
    }

    public void getAllLists() {
        favoriteListDao.getAllFavoriteList().observeForever(log -> callback.showListNames(log));
    }
}
