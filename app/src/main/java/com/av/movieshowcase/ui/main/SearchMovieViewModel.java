package com.av.movieshowcase.ui.main;

import com.av.movieshowcase.data.callbacks.PersonCallback;
import com.av.movieshowcase.data.callbacks.SearchCallback;
import com.av.movieshowcase.data.local.dao.HistoryDao;
import com.av.movieshowcase.data.local.dao.MovieDao;
import com.av.movieshowcase.data.local.entity.HistoryEntity;
import com.av.movieshowcase.data.remote.api.ApiMovie;
import com.av.movieshowcase.data.remote.model.MovieTrendingResponse;
import com.av.movieshowcase.data.remote.model.MovieTrendingResultsResponse;
import com.av.movieshowcase.data.remote.model.PersonResponse;
import com.av.movieshowcase.data.repository.MovieRepository;
import com.av.movieshowcase.ui.base.BaseViewModel;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchMovieViewModel extends BaseViewModel {

    private MovieRepository movieRepository;
    private ApiMovie apiMovie;
    private SearchCallback callback;
    private HistoryDao historyDao;

    @Inject
    public SearchMovieViewModel(MovieDao movieDao, HistoryDao historyDao, ApiMovie movieApiService) {
        movieRepository = new MovieRepository(movieDao, movieApiService);
        this.apiMovie = movieApiService;
        this.historyDao = historyDao;
    }


    public void setCallback(SearchCallback callback) {
        this.callback = callback;
    }

    List<MovieTrendingResultsResponse> resultsResponses = Collections.emptyList();

    public void fetchSearchResults(String query) {

        apiMovie.searchMoviesByQuery(query,"1")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieTrendingResponse>() {
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
                    public void onNext(MovieTrendingResponse response) {
                        callback.showSearchResponse(response);
                        //resultsResponses = response.getResults();
                    }
                });
    }


    public void insertSearch(HistoryEntity historyEntity){
        if(historyDao.findLogById(historyEntity.getQuery())>0) historyDao.updateLog(historyEntity);
        else historyDao.insertLog(historyEntity);
    }

    public void deleteSearch(HistoryEntity historyEntity){
        historyDao.deleteLog(historyEntity);
    }

    public void clearSearchHistory(){
        historyDao.deleteAll();
    }


    public void getData() {
        historyDao.getAllHistory().observeForever(log -> callback.showHistory(log));
    }

}