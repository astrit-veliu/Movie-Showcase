package com.av.movieshowcase.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.astritveliu.boom.Boom;
import com.av.movieshowcase.R;
import com.av.movieshowcase.data.NoConnectivityException;
import com.av.movieshowcase.data.callbacks.FragmentBackPressed;
import com.av.movieshowcase.data.callbacks.MainActivityBehaviourCallback;
import com.av.movieshowcase.data.callbacks.TrendingCallback;
import com.av.movieshowcase.data.remote.model.MovieTrendingResponse;
import com.av.movieshowcase.databinding.TrendingFragmentBinding;
import com.av.movieshowcase.factory.ViewModelFactory;
import com.av.movieshowcase.ui.adapter.AdapterListLatest;
import com.av.movieshowcase.ui.adapter.TrendingMovieAdapter;
import com.av.movieshowcase.ui.base.BaseFragment;
import com.av.movieshowcase.ui.main.TrendingMovieViewModel;
import com.av.movieshowcase.utils.SaveData;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;


public class TrendingFragment extends BaseFragment implements TrendingCallback, FragmentBackPressed {


    @Inject
    ViewModelFactory viewModelFactory;
    private TrendingFragmentBinding binding;
    TrendingMovieViewModel trendingMovieViewModel;
    private TrendingMovieAdapter moviesListAdapter;
    private AdapterListLatest latestmoviesListAdapter;
    private AdapterListLatest latestTvListAdapter;
    private Boolean seeAllMovies = false, seeAllTV = false, isFirstLoad = true;
    private MainActivityBehaviourCallback callback;
    private SaveData saveData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        initialiseViewModel();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trending, container, false);

        binding.mainRelative.getLayoutTransition().setAnimateParentHierarchy(false);
        saveData = new SaveData(getContext());

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialiseView();

    }


    private void initialiseView() {

        if (getArguments() != null) {
            int topPadding = getArguments().getInt("toolbar_height");
            binding.moviesList.setPadding(0,(topPadding+30),0,topPadding);
        }

        moviesListAdapter = new TrendingMovieAdapter(activity);
        latestmoviesListAdapter = new AdapterListLatest(activity);
        latestTvListAdapter = new AdapterListLatest(activity);

        binding.animationView.setAnimation(R.raw.no_connection);

        binding.moviesList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        //binding.moviesList.setNestedScrollingEnabled(false);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);
        binding.moviesList.setLayoutAnimation(controller);

        binding.moviesList.setAdapter(moviesListAdapter);

        if(saveData.isFirstTime()) {
            trendingMovieViewModel.initDefaultLists();
            saveData.setFirstTime(false);
        }

    }



    public void setCallback(MainActivityBehaviourCallback callback) {
        this.callback = callback;
    }


/*    private String check_string(String string){
        if (string != null && (!TextUtils.equals(string ,"null")) && (!TextUtils.isEmpty(string))&&!string.equalsIgnoreCase("")){
            return string;
        } else return "";
    }*/


    private void initialiseViewModel() {
        trendingMovieViewModel = ViewModelProviders.of(this, viewModelFactory).get(TrendingMovieViewModel.class);
        trendingMovieViewModel.setCallback(this);
        if(callback!=null) callback.hideUI();
        showLoadingAnimation();
        trendingMovieViewModel.getLatestMovies();
    }

    public void getMovies(){
        showLoadingAnimation();
        trendingMovieViewModel.getLatestMovies();
    }


    public void getTvShows(){
        showLoadingAnimation();
        trendingMovieViewModel.getLatestTV();
    }


    @Override
    public void showTrendingResponse(MovieTrendingResponse response) {
       // binding.relContent.setVisibility(View.VISIBLE);
        moviesListAdapter.setItems(response.getResults());
    }

    @Override
    public void showLatestMovies(MovieTrendingResponse response) {
        binding.moviesList.setVisibility(View.VISIBLE);
        moviesListAdapter.setItems(response.getResults());
        hideErrorLayout();
        hideLoadingAnimation();
        if(isFirstLoad){
            if(callback!=null)  callback.showUI();
            isFirstLoad = false;
        }
    }

    @Override
    public void showLatestTv(MovieTrendingResponse response) {
        binding.moviesList.setVisibility(View.VISIBLE);
        moviesListAdapter.setItems(response.getResults());
        hideErrorLayout();
        hideLoadingAnimation();
    }

    @Override
    public void showError(Throwable message) {
        if(message instanceof NoConnectivityException) {
            showErrorLayout();
        }

        Toast.makeText(activity, message.getMessage(), Toast.LENGTH_SHORT).show();
        hideLoadingAnimation();
    }

    private void showErrorLayout(){
        binding.moviesList.setVisibility(View.GONE);
        binding.errorView.setVisibility(View.VISIBLE);
        binding.animationView.playAnimation();
        binding.btnRetry.setOnClickListener(v->  trendingMovieViewModel.getLatestMovies());
    }

    private void hideErrorLayout(){
        binding.animationView.cancelAnimation();
        binding.errorView.setVisibility(View.GONE);
    }

    @Override
    public boolean onBackPressed() {

        LinearLayoutManager layoutManager = (LinearLayoutManager) binding.moviesList.getLayoutManager();
        if(layoutManager.findFirstCompletelyVisibleItemPosition()==0){
            return false;
        }else {
            binding.moviesList.smoothScrollToPosition(0);
            return true;
        }
    }
}
