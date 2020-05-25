package com.av.movieshowcase.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.av.movieshowcase.data.NetworkBoundResource;
import com.av.movieshowcase.data.Resource;
import com.av.movieshowcase.data.local.dao.MovieDao;
import com.av.movieshowcase.data.remote.api.ApiMovie;
import com.av.movieshowcase.data.remote.model.MovieTrendingResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Call;

/**
 * Created by aveliu2 on 03,October,2019
 */

@Singleton
public class MovieRepository {


    private MovieDao movieDao;
    private ApiMovie apiMovie;

    public MovieRepository(MovieDao movieDao, ApiMovie apiMovie) {
        this.movieDao = movieDao;
        this.apiMovie = apiMovie;
    }


    //todo completly redesign api with rxjava comined with room for offline
/*
    public LiveData<Resource<MovieTrendingResponse>> loadPopularMovies() {
        return new NetworkBoundResource<MovieTrendingResponse, MovieTrendingResponse>() {

            @Override
            protected void saveCallResult(@NonNull MovieTrendingResponse item) {
                //movieDao.saveMovies(item.getResults());
            }

            @NonNull
            @Override
            protected LiveData<MovieTrendingResponse> loadFromDb() {
                //return movieDao.loadMovies(); get items from room db
                return null;
            }

            @NonNull
            @Override
            protected Call<MovieTrendingResponse> createCall() {
                return apiMovie.getTrendingMovies();
            }
        }.getAsLiveData();
    }

   public LiveData<MovieTrendingResponse> getMovie(int id){
        return movieDao.getMovie(id);
    }


    public Observable<Resource<List<MovieTrendingResponse>>> getTrending() {
        return new NetworkBoundResource<List<MovieTrendingResponse>, MovieTrendingResponse>() {


            @Override
            protected void saveCallResult(@NonNull MovieTrendingResponse item) {

            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @NonNull
            @Override
            protected Flowable<List<MovieTrendingResponse>> loadFromDb() {
                return null;
            }

            @NonNull
            @Override
            protected Observable<Resource<MovieTrendingResponse>> createCall() {
                return apiMovie.getTrendingMovies()
                        .flatMap(movieApiResponse -> Observable.just(movieApiResponse == null
                                ? Resource.error("", new MovieTrendingResponse())
                                : Resource.success(movieApiResponse)));
            }
        }.getAsObservable();
    }



    public Observable<Resource<MovieTrendingResponse>> fetchMovie() {
        return new NetworkBoundResource<MovieTrendingResponse, MovieTrendingResponse>() {
            @Override
            protected void saveCallResult(@NonNull MovieTrendingResponse item) {
                MovieTrendingResponse movieEntity = item;
            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @NonNull
            @Override
            protected Flowable<MovieTrendingResponse> loadFromDb() {

                return Flowable.empty();
            }


            //multi-call method, it merges more than one call into one which latter merges the responses and check them
            //this one is used for movie details
            @NonNull
            @Override
            protected Observable<Resource<MovieTrendingResponse>> createCall() {
                return apiMovie.getTrendingMovies()
                        .flatMap(movieApiResponse -> Observable.just(movieApiResponse == null
                                ? Resource.error("", new MovieTrendingResponse())
                                : Resource.success(movieApiResponse)));
            }
        }.getAsObservable();
    }


    */

}
