package com.av.movieshowcase.data.remote.api;

import com.av.movieshowcase.data.remote.model.MovieTrendingResponse;
import com.av.movieshowcase.data.remote.model.PersonResponse;
import com.av.movieshowcase.data.remote.model.movie_details.MovieDetailResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by aveliu2 on 01,October,2019
 */
public interface ApiMovie {


    @GET("movie/{type}")
    Observable<MovieTrendingResponse> fetchMoviesByType(@Path("type") String type,
                                                   @Query("primary_release_year") String year,
                                                   @Query("page") long page);

    @GET("tv/on_the_air")
    Observable<MovieTrendingResponse> fetchTvByType(@Query("page") long page);

    @GET("/3/movie/latest")
    Observable<MovieTrendingResponse> getLatestMovies();


    @GET("/3/tv/latest")
    Observable<MovieTrendingResponse> getLatestTV();

    @GET("trending/movie/day")
    Observable<MovieTrendingResponse> getTrendingMovies();


    @GET("trending/tv/day")
    Observable<MovieTrendingResponse> getTrendingTv();


    @GET("/3/movie/{movieId}?append_to_response=videos,credits,reviews,similar")
    Observable<MovieDetailResponse> fetchMovieDetail(@Path("movieId") String movieId);


    @GET("/movie/{movieId}/similar")
    Observable<MovieTrendingResponse> getSimilarMovies(@Path("movieId") String movieId);


    @GET("/tv/{tv_id}/similar")
    Observable<MovieTrendingResponse> getSimilarTV(@Path("tv_id") String tv_id);

    @GET("/3/person/{person_id}")
    Observable<PersonResponse> getPerson(@Path("person_id") int person_id);


    @GET("/3/search/movie")
    Observable<MovieTrendingResponse> searchMoviesByQuery(@Query("query") String query,
                                                     @Query("page") String page);

/*    @GET("movie/{type}?language=en-US&region=US")
    Observable<MovieApiResponse> fetchMoviesByType(@Path("type") String type,
                                                   @Query("page") long page);


    @GET("/3/movie/{movieId}")
    Observable<MovieEntity> fetchMovieDetail(@Path("movieId") String movieId);


    @GET("/3/movie/{movieId}/videos")
    Observable<VideoResponse> fetchMovieVideo(@Path("movieId") String movieId);

    @GET("/3/movie/{movieId}/credits")
    Observable<CreditResponse> fetchCastDetail(@Path("movieId") String movieId);


    @GET("/3/movie/{movieId}/similar")
    Observable<MovieApiResponse> fetchSimilarMovie(@Path("movieId") String movieId,
                                                   @Query("page") long page);


    @GET("/3/movie/{movieId}/reviews")
    Observable<ReviewApiResponse> fetchMovieReviews(@Path("movieId") String movieId);


    @GET("/3/search/movie")
    Observable<MovieApiResponse> searchMoviesByQuery(@Query("query") String query,
                                                     @Query("page") String page);*/


}
