package com.av.movieshowcase.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.Toast;

import com.av.movieshowcase.R;
import com.av.movieshowcase.data.callbacks.SearchAdapterCallback;
import com.av.movieshowcase.data.callbacks.SearchCallback;
import com.av.movieshowcase.data.local.entity.HistoryEntity;
import com.av.movieshowcase.data.remote.model.MovieTrendingResponse;
import com.av.movieshowcase.databinding.SearchFragmentBinding;
import com.av.movieshowcase.databinding.TrendingFragmentBinding;
import com.av.movieshowcase.factory.ViewModelFactory;
import com.av.movieshowcase.ui.adapter.AdapterListLatest;
import com.av.movieshowcase.ui.adapter.AdapterSearch;
import com.av.movieshowcase.ui.adapter.AdapterSearchHistory;
import com.av.movieshowcase.ui.adapter.TrendingMovieAdapter;
import com.av.movieshowcase.ui.base.BaseFragment;
import com.av.movieshowcase.ui.main.SearchMovieViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;


public class SearchFragment extends BaseFragment implements SearchCallback, SearchAdapterCallback {

    private SearchFragmentBinding binding;
    @Inject
    ViewModelFactory viewModelFactory;
    SearchMovieViewModel searchMovieViewModel;
    private AdapterSearch moviesListAdapter;
    private AdapterSearchHistory adapterSearchHistory;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidSupportInjection.inject(this);
        initialiseViewModel();

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);

        binding.mainRelative.getLayoutTransition().setAnimateParentHierarchy(false);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialiseView();

        binding.searchQuery.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                closeKeyboard();
                binding.searchQuery.clearFocus();
                initSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });



        ImageView clearButton = binding.searchQuery.findViewById(androidx.appcompat.R.id.search_close_btn);
        clearButton.setOnClickListener(v -> {
            if(binding.searchQuery.getQuery().length() == 0) {
                binding.searchQuery.setIconified(true);
            } else {
                closeKeyboard();
                searchMovieViewModel.getData();
                binding.searchQuery.setQuery("", false);
            }
        });



    }

    private void initialiseView() {
        //binding.multiSearchView.setPadding(0,getStatusBarHeight()*2,0,0);
        binding.linHead.setPadding(0,getStatusBarHeight()*2,0,0);

        moviesListAdapter = new AdapterSearch(activity);
        adapterSearchHistory = new AdapterSearchHistory(activity);
        adapterSearchHistory.setCallback(this);

        binding.moviesList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        binding.moviesList.setNestedScrollingEnabled(false);
        binding.moviesList.setAdapter(moviesListAdapter);


        binding.searchList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        binding.searchList.setNestedScrollingEnabled(false);
        binding.searchList.setAdapter(adapterSearchHistory);


        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);
        binding.searchList.setLayoutAnimation(controller);

        new PagerSnapHelper().attachToRecyclerView(binding.moviesList);


        binding.moviesList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);

                        break;

                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }


    private void initialiseViewModel() {
        searchMovieViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchMovieViewModel.class);
        searchMovieViewModel.setCallback(this);
        searchMovieViewModel.getData();
    }


    private void initSearch(String movieName){
        showLoadingAnimation();

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());

        HistoryEntity searchItem = new HistoryEntity();
        searchItem.setQuery(movieName);
        searchItem.setType("Movie");
        searchItem.setLast_search(date);

        searchMovieViewModel.insertSearch(searchItem);
        searchMovieViewModel.fetchSearchResults(movieName);
    }


    @Override
    public void showSearchResponse(MovieTrendingResponse response) {
        closeKeyboard();
        binding.searchQuery.clearFocus();
        manageUiVisibilities(View.VISIBLE, View.GONE, View.GONE, "Results");
        moviesListAdapter.setItems(response.getResults());
        hideLoadingAnimation();
    }

    @Override
    public void showHistory(List<HistoryEntity> searchList) {
        if(searchList.size()>0){
            manageUiVisibilities(View.GONE, View.VISIBLE, View.GONE, "History");
            adapterSearchHistory.refreshAdapter(searchList);
        } else  manageUiVisibilities(View.GONE, View.GONE, View.VISIBLE, "History");
    }


    private void manageUiVisibilities(int movieListVisibility, int searchListVisibility, int relEmptyVisibility, String txtHeaderText){
        binding.relEmpty.setVisibility(relEmptyVisibility);
        binding.searchList.setVisibility(searchListVisibility);
        binding.moviesList.setVisibility(movieListVisibility);
        binding.resultTxt.setText(txtHeaderText);
    }


    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        hideLoadingAnimation();
    }

    @Override
    public void searchFromAdapter(String query) {
        initSearch(query);
    }

    @Override
    public void deleteSearchItem(HistoryEntity item) {
        searchMovieViewModel.deleteSearch(item);
        searchMovieViewModel.getData();
    }

    @Override
    public void deleteAllSearchItems() {
        searchMovieViewModel.clearSearchHistory();
        searchMovieViewModel.getData();
    }
}
