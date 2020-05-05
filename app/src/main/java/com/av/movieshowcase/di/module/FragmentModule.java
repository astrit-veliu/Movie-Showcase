package com.av.movieshowcase.di.module;

import com.av.movieshowcase.ui.fragment.CategoriesFragment;
import com.av.movieshowcase.ui.fragment.FavouritesFragment;
import com.av.movieshowcase.ui.fragment.SearchFragment;
import com.av.movieshowcase.ui.fragment.TrendingFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by aveliu2 on 01,October,2019
 */

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract CategoriesFragment contributeCategoriesFragment();

    @ContributesAndroidInjector
    abstract FavouritesFragment contributeFavouritesFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();

    @ContributesAndroidInjector
    abstract TrendingFragment contributeTrendingFragment();
}
