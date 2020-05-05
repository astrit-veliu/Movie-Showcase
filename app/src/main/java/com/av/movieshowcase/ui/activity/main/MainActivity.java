package com.av.movieshowcase.ui.activity.main;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.av.movieshowcase.R;
import com.av.movieshowcase.data.callbacks.FragmentBackPressed;
import com.av.movieshowcase.data.callbacks.MainActivityBehaviourCallback;
import com.av.movieshowcase.data.local.entity.FavoriteItemEntity;
import com.av.movieshowcase.data.local.model.EventSender;
import com.av.movieshowcase.data.remote.model.MovieTrendingResultsResponse;
import com.av.movieshowcase.databinding.MainActivityBinding;
import com.av.movieshowcase.factory.ViewModelFactory;
import com.av.movieshowcase.ui.base.BaseActivity;
import com.av.movieshowcase.ui.fragment.FavouritesFragment;
import com.av.movieshowcase.ui.fragment.SearchFragment;
import com.av.movieshowcase.ui.fragment.TrendingFragment;
import com.av.movieshowcase.ui.main.DetailMovieViewModel;
import com.av.movieshowcase.utils.BottomNavigationViewBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import static com.av.movieshowcase.utils.AnimUtils.animateView;


public class MainActivity extends BaseActivity implements MainActivityBehaviourCallback {


    private MainActivityBinding binding;

    @Inject
    ViewModelFactory viewModelFactory;
    DetailMovieViewModel detailMovieViewModel;

    private int toolbarHeight=0;
    int bottomCollapseBarMargin;
    boolean isFavorite = false, isWatchlist = false;
    private boolean isCreated = false;
    TrendingFragment trendingFragment;
    private Gson gson;
    private String homeTitle = "Latest Movies";
    private String LIST = "My WatchList", TYPE = "movie";
    private Animation slide_down, slide_up;
    CoordinatorLayout.LayoutParams bottomNavParams;
    BottomNavigationViewBehavior bottomViewNavigationBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        getSupportActionBar().hide();
        AndroidInjection.inject(this);
        setTransparentStatusBar(true);
        setLightNavigationBar();
        removeAppBarOutline(binding.appBar);
        setupTabBar();
        initView();
        initialiseViewModel();

        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.materialBlue);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

                switch (tab.getPosition()) {
                    case 0:
                        homeTitle = "Latest Movies";
                        TYPE = "movie";
                        binding.subHeaderTextview.setText(homeTitle);
                        trendingFragment.getMovies();
                        animateView(binding.subHeaderTextview);
                        break;
                    case 1:
                        homeTitle = "TV shows";
                        TYPE = "tv";
                        binding.subHeaderTextview.setText(homeTitle);
                        trendingFragment.getTvShows();
                        animateView(binding.subHeaderTextview);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.lightgrey);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int checked_id=R.id.action_trending;
                switch (position) {
                    case 0:
                        setMargins(binding.htabCollapseToolbar,0,0,0, bottomCollapseBarMargin);
                        checked_id = R.id.action_trending;
                        manageHeaderItems(homeTitle, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE, 0);
                        animateView(binding.imageEdge);
                        break;
                    case 1:
                        checked_id = R.id.action_search;
                        manageHeaderItems("", View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,0);
                        break;
                    case 2:
                        checked_id = R.id.action_profile;
                        manageHeaderItems("", View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,0);
                        break;

                }
                bottomViewNavigationBehavior.slideUp(binding.bottomNavigation);
                animateView(binding.subHeaderTextview);
                binding.bottomNavigation.setSelectedItemId(checked_id);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });


        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_trending:
                    binding.viewPager.setCurrentItem(0);
                    return true;
                case R.id.action_search:
                    binding.viewPager.setCurrentItem(1);
                    return true;
                case R.id.action_profile:
                    binding.viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        });

    }


    private void setupTabBar() {
        binding.tabs.addTab(binding.tabs.newTab().setIcon(R.drawable.ic_theaters_24px),0);
        binding.tabs.addTab(binding.tabs.newTab().setIcon(R.drawable.ic_tv_24px),1);

        int tabIconColor_anctive = ContextCompat.getColor(getApplicationContext(), R.color.materialBlue);
        int tabIconColor_ianctive = ContextCompat.getColor(getApplicationContext(), R.color.soft_grey);
        binding.tabs.getTabAt(1).getIcon().setColorFilter(tabIconColor_ianctive, PorterDuff.Mode.SRC_IN);
        binding.tabs.getTabAt(0).getIcon().setColorFilter(tabIconColor_anctive, PorterDuff.Mode.SRC_IN);
    }


    private void initView() {
        bottomNavParams = (CoordinatorLayout.LayoutParams) binding.bottomNavigation.getLayoutParams();
        bottomViewNavigationBehavior = new BottomNavigationViewBehavior();
        bottomNavParams.setBehavior(bottomViewNavigationBehavior);


        slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);

        bottomCollapseBarMargin = ((-getStatusBarHeight())+20); //18 is height of underline

        animateView(binding.bottomNavigation);

        binding.toolbarStrip.setPadding(0,getStatusBarHeight(),0,0);
        setMargins(binding.tabs,20,getStatusBarHeight(),0,8);
        setMargins(binding.htabCollapseToolbar,0,0,0, bottomCollapseBarMargin);
        //binding.toolbarStrip.setPadding(0,0,0,0);

        final ViewTreeObserver observer= binding.toolbarStrip.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(() -> {
            toolbarHeight = binding.appBar.getHeight();
            if(!isCreated) setupViewPager(binding.viewPager);
            isCreated = true;
        });

    }


    private void setupViewPager(ViewPager2 viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
    }

    private void initialiseViewModel() {
        detailMovieViewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailMovieViewModel.class);
        gson = new Gson();
    }






    @Override
    public void showUI() {
        binding.bottomNavigation.setVisibility(View.VISIBLE);
        binding.appBar.setVisibility(View.VISIBLE);
        slideDown(binding.appBar);
        slideUp(binding.bottomNavigation);
    }

    @Override
    public void hideUI() {
        //slideUp(binding.bottomNavigation);
        //slideDown(binding.appBar);
    }


    private void slideUp(View child) {
        child.startAnimation(slide_up);
    }

    private void slideDown(View child) {
        child.startAnimation(slide_down);
    }



    private class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    Bundle bundle_trending = new Bundle();
                    bundle_trending.putInt("toolbar_height", toolbarHeight);
                    trendingFragment = new TrendingFragment();
                    trendingFragment.setCallback(MainActivity.this);
                    trendingFragment.setArguments(bundle_trending);
                    return trendingFragment;
                case 1:
                    Bundle bundle_search = new Bundle();
                    bundle_search.putInt("toolbar_height", toolbarHeight);
                    SearchFragment searchFragment = new SearchFragment();
                    searchFragment.setArguments(bundle_search);
                    return searchFragment;
                case 2:
                    Bundle bundle_favourites = new Bundle();
                    bundle_favourites.putInt("toolbar_height", toolbarHeight);
                    FavouritesFragment favouritesFragment = new FavouritesFragment();
                    favouritesFragment.setArguments(bundle_favourites);
                    return favouritesFragment;

            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }


    private void manageHeaderItems(String headText, int headTextVisibility, int lineVisibility, int tabsVisibility, int imageVisibility, int discoverVisibility, int imageResource){
        binding.subHeaderTextview.setText(headText);
        binding.lineIndicator.setVisibility(lineVisibility);
        binding.tabs.setVisibility(tabsVisibility);
        binding.imageEdge.setVisibility(imageVisibility);
        binding.subHeaderTextview.setVisibility(headTextVisibility);
        binding.headerTxtview.setVisibility(discoverVisibility);
        binding.imageEdge.setImageResource(imageResource);
    }


    private void showBottomSheetSave(MovieTrendingResultsResponse item) {

        View view = getLayoutInflater().inflate(R.layout.fragment_favorites_modal, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this,R.style.SheetDialog);
        dialog.setContentView(view);
        dialog.show();

        final TextInputLayout text_input_name = dialog.findViewById(R.id.text_input_name);
        final TextView txtWatchList = (TextView) dialog.findViewById(R.id.txtWatchList);
        final TextView txtFavorite = (TextView) dialog.findViewById(R.id.txtFavorite);
        final MaterialButton btnCreateList = (MaterialButton) dialog.findViewById(R.id.btnCreateList);

        if (detailMovieViewModel.isInList(item.getId(),WATCHLIST)) {
            txtWatchList.setText(getString(R.string.remove_from_watchlist));
            isWatchlist = true;
        }  else {
            txtWatchList.setText(getString(R.string.add_to_watchlist));
            isWatchlist = false;
        }

        if (detailMovieViewModel.isInList(item.getId(),FAVORITES)){
            txtFavorite.setText(getString(R.string.remove_from_favorites));
            isFavorite = true;
        }  else {
            txtFavorite.setText(getString(R.string.add_to_favorites));
            isFavorite = false;
        }

        txtWatchList.setOnClickListener(v-> {
            if (isWatchlist) detailMovieViewModel.removeItemFromListById(item.getId(),WATCHLIST);
            else {
                FavoriteItemEntity favoriteItemEntity = new FavoriteItemEntity(item.getId(),
                        item.getVoteAverage(), TYPE, item.getTitle(),  item.getReleaseDate(), item.getPosterPath(), WATCHLIST);
                detailMovieViewModel.insertItemInList(favoriteItemEntity, WATCHLIST);
            }
            dialog.dismiss();
        });


        txtFavorite.setOnClickListener(v-> {
            if (isFavorite) detailMovieViewModel.removeItemFromListById(item.getId(),FAVORITES);
            else {
                FavoriteItemEntity favoriteItemEntity = new FavoriteItemEntity(item.getId(),
                        item.getVoteAverage(), TYPE, item.getTitle(),  item.getReleaseDate(), item.getPosterPath(), FAVORITES);
                detailMovieViewModel.insertItemInList(favoriteItemEntity, FAVORITES);
            }
            EventBus.getDefault().post(new EventSender("update"));
            dialog.dismiss();
        });


        btnCreateList.setOnClickListener( v->{
            String listName = text_input_name.getEditText().getText().toString();
            if(!listName.equalsIgnoreCase("") && !listName.isEmpty()){
                detailMovieViewModel.createList(listName);
                FavoriteItemEntity favoriteItemEntity = new FavoriteItemEntity(item.getId(),
                        item.getVoteAverage(), TYPE, item.getTitle(),  item.getReleaseDate(), item.getPosterPath(), listName);
                detailMovieViewModel.insertItemInList(favoriteItemEntity, listName);
                EventBus.getDefault().post(new EventSender("update"));
                dialog.dismiss();
            }

        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final EventSender message) {
        if (!message.getMessage().startsWith("update")){
            MovieTrendingResultsResponse movie = gson.fromJson(message.getMessage(), MovieTrendingResultsResponse.class);
            showBottomSheetSave(movie);
            pulseEffect();
        }
    }


    private void pulseEffect(){
        long[] pattern = {0, 40, 50, 80};
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createWaveform(pattern,-1));
        } else {
            v.vibrate(pattern, -1); //-1 is important
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        //if user is not in first page, then set viewpager current page to 0
        if(binding.viewPager!=null && binding.viewPager.getCurrentItem()>0) binding.viewPager.setCurrentItem(0);
        else {
            //if the trending recycler view first item is visible we proceed with on back pressed
            if (!(trendingFragment instanceof FragmentBackPressed) || !((FragmentBackPressed) trendingFragment).onBackPressed()) {
                super.onBackPressed();
            }else {
                bottomViewNavigationBehavior.slideUp(binding.bottomNavigation);
                binding.appBar.setExpanded(true);
            }
        }
    }

}

