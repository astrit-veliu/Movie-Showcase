package com.av.movieshowcase.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.av.movieshowcase.data.Resource;
import com.av.movieshowcase.data.callbacks.TrendingCallback;
import com.av.movieshowcase.data.local.dao.FavoriteListDao;
import com.av.movieshowcase.data.local.dao.MovieDao;
import com.av.movieshowcase.data.local.entity.FavoriteListEntity;
import com.av.movieshowcase.data.remote.api.ApiMovie;
import com.av.movieshowcase.data.remote.model.MovieTrendingResponse;
import com.av.movieshowcase.data.remote.model.MovieTrendingResultsResponse;
import com.av.movieshowcase.data.repository.MovieRepository;
import com.av.movieshowcase.ui.base.BaseViewModel;

import org.reactivestreams.Subscriber;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aveliu2 on 03,October,2019
 */

public class TrendingMovieViewModel extends BaseViewModel {


  //  private final LiveData<Resource<MovieTrendingResponse>> popularMovies;
    private MovieRepository movieRepository;
    private FavoriteListDao favoriteListDao;
    private ApiMovie apiMovie;
    private TrendingCallback callback;

    @Inject
    public TrendingMovieViewModel(FavoriteListDao favoriteListDao, ApiMovie movieApiService) {
       // movieRepository = new MovieRepository(movieDao, movieApiService);
       // popularMovies = movieRepository.loadPopularMovies();
        this.favoriteListDao = favoriteListDao;
        this.apiMovie = movieApiService;
    }


    public void setCallback(TrendingCallback callback) {
        this.callback = callback;
    }

    List<MovieTrendingResultsResponse> resultsResponses = Collections.emptyList();

    public void fetchMovieDetail() {

      apiMovie.getTrendingMovies()
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
                            callback.showError(e);
                        }

                        @Override
                        public void onNext(MovieTrendingResponse response) {
                            callback.showTrendingResponse(response);
                            //resultsResponses = response.getResults();
                        }
                    });
    }



    public void getLatestMovies() {

      //  apiMovie.fetchMoviesByType("popular",1)
        apiMovie.fetchMoviesByType("upcoming","2020",1)
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
                        callback.showError(e);
                    }

                    @Override
                    public void onNext(MovieTrendingResponse response) {
                        callback.showLatestMovies(response);
                        //resultsResponses = response.getResults();
                    }
                });
    }


    public void getLatestTV() {

        apiMovie.fetchTvByType(1)
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
                        callback.showError(e);
                    }

                    @Override
                    public void onNext(MovieTrendingResponse response) {
                        callback.showLatestTv(response);
                        //resultsResponses = response.getResults();
                    }
                });
    }



    public List<MovieTrendingResultsResponse> getData() {
        return resultsResponses;
    }

    public void initDefaultLists(){
        FavoriteListEntity myWatchlist = new FavoriteListEntity("My WatchList");
        FavoriteListEntity favorites = new FavoriteListEntity("Favorites");
        FavoriteListEntity persons = new FavoriteListEntity("Persons");
        favoriteListDao.insertList(myWatchlist);
        favoriteListDao.insertList(favorites);
        favoriteListDao.insertList(persons);
    }
   /* public LiveData<Resource<MovieTrendingResponse>> getPopularMovies() {
        return popularMovies;
    } */

}