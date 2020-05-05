package com.av.movieshowcase.di.module;

import com.av.movieshowcase.ui.activity.main.MainActivity;
import com.av.movieshowcase.ui.activity.details.DetailsActivity;
import com.av.movieshowcase.ui.activity.person.PersonActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by aveliu2 on 01,October,2019
 */

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector()
    abstract DetailsActivity contributeDetailsActivity();


    @ContributesAndroidInjector()
    abstract PersonActivity contributePersonActivity();

   /* @ContributesAndroidInjector()
    abstract MovieDetailActivity contributeMovieDetailActivity();

    @ContributesAndroidInjector()
    abstract TvDetailActivity contributeTvDetailActivity();

    @ContributesAndroidInjector()
    abstract MovieSearchActivity contributeMovieSearchActivity();

    @ContributesAndroidInjector()
    abstract TvSearchActivity contributeTvSearchActivity();*/

}
