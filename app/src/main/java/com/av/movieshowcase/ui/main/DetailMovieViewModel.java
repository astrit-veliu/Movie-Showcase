package com.av.movieshowcase.ui.main;

import com.av.movieshowcase.data.callbacks.DetailCallback;
import com.av.movieshowcase.data.callbacks.TrendingCallback;
import com.av.movieshowcase.data.local.dao.FavoriteItemDao;
import com.av.movieshowcase.data.local.dao.FavoriteListDao;
import com.av.movieshowcase.data.local.dao.MovieDao;
import com.av.movieshowcase.data.local.entity.FavoriteItemEntity;
import com.av.movieshowcase.data.local.entity.FavoriteListEntity;
import com.av.movieshowcase.data.local.entity.HistoryEntity;
import com.av.movieshowcase.data.remote.api.ApiMovie;
import com.av.movieshowcase.data.remote.model.MovieTrendingResponse;
import com.av.movieshowcase.data.remote.model.MovieTrendingResultsResponse;
import com.av.movieshowcase.data.remote.model.movie_details.MovieDetailResponse;
import com.av.movieshowcase.data.repository.MovieRepository;
import com.av.movieshowcase.ui.base.BaseViewModel;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aveliu2 on 15,January,2020
 */
public class DetailMovieViewModel  extends BaseViewModel {


    //  private final LiveData<Resource<MovieTrendingResponse>> popularMovies;
    private MovieRepository movieRepository;
    private FavoriteListDao favoriteListDao;
    private FavoriteItemDao favoriteItemDao;
    private ApiMovie apiMovie;
    private DetailCallback callback;

    @Inject
    public DetailMovieViewModel(FavoriteListDao favoriteListDao, FavoriteItemDao favoriteItemDao, ApiMovie movieApiService) {
        //movieRepository = new MovieRepository(movieDao, movieApiService);
        this.favoriteListDao = favoriteListDao;
        this.favoriteItemDao = favoriteItemDao;
        this.apiMovie = movieApiService;
    }

    public void setCallback(DetailCallback callback) {
        this.callback = callback;
    }

    MovieDetailResponse resultsResponses = new MovieDetailResponse();

    public void fetchMovieDetail(String movieID) {

        apiMovie.fetchMovieDetail(movieID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieDetailResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        // Updates UI with data
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(MovieDetailResponse response) {
                        callback.showDetailResponse(response);
                        //resultsResponses = response.getResults();
                    }
                });
    }



    public void insertItemInList(FavoriteItemEntity favoriteItemEntity,String list){
        if(isInList(favoriteItemEntity.getItemId(),list)) favoriteItemDao.updateItem(favoriteItemEntity);
        else favoriteItemDao.addInList(favoriteItemEntity);
    }

    public void createList(String list){
        if(!existList(list)) favoriteListDao.insertList(new FavoriteListEntity(list));
    }

    public boolean isInList(long id, String list){
        if(favoriteItemDao.isInList(id,list)>0) return true;
        else return false;
    }

    public boolean existList(String list){
        if(favoriteListDao.existList(list)>0) return true;
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
        //favoriteItemDao.getItemsByTypeInList(type, list).observe(log -> callback.showHistory(log));
    }
}
