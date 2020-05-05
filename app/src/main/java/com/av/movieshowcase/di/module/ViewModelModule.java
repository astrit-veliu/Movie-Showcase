package com.av.movieshowcase.di.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.av.movieshowcase.factory.ViewModelFactory;
import com.av.movieshowcase.ui.main.DetailMovieViewModel;
import com.av.movieshowcase.ui.main.PersonViewModel;
import com.av.movieshowcase.ui.main.ProfileViewModel;
import com.av.movieshowcase.ui.main.SearchMovieViewModel;
import com.av.movieshowcase.ui.main.TrendingMovieViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by aveliu2 on 01,October,2019
 */

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(TrendingMovieViewModel.class)
    protected abstract ViewModel trendingMovieViewModel(TrendingMovieViewModel moviesDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DetailMovieViewModel.class)
    protected abstract ViewModel detailMovieViewModel(DetailMovieViewModel moviesDetailViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(PersonViewModel.class)
    protected abstract ViewModel personViewModel(PersonViewModel personViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(SearchMovieViewModel.class)
    protected abstract ViewModel searchViewModel(SearchMovieViewModel searchViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    protected abstract ViewModel profileViewModel(ProfileViewModel profileViewModel);

}
