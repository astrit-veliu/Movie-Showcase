package com.av.movieshowcase.ui.activity.details;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.astritveliu.boom.Boom;
import com.av.movieshowcase.R;
import com.av.movieshowcase.data.callbacks.DetailCallback;
import com.av.movieshowcase.data.local.entity.FavoriteItemEntity;
import com.av.movieshowcase.data.local.model.EventSender;
import com.av.movieshowcase.data.remote.model.MovieTrendingResultsResponse;
import com.av.movieshowcase.data.remote.model.movie_details.Cast;
import com.av.movieshowcase.data.remote.model.movie_details.Crew;
import com.av.movieshowcase.data.remote.model.movie_details.Genre;
import com.av.movieshowcase.data.remote.model.movie_details.MovieDetailResponse;
import com.av.movieshowcase.data.remote.model.movie_details.ReviewResult;
import com.av.movieshowcase.data.remote.model.movie_details.SimilarResult;
import com.av.movieshowcase.data.remote.model.movie_details.VideoResult;
import com.av.movieshowcase.databinding.DetailsActivityBinding;
import com.av.movieshowcase.factory.ViewModelFactory;
import com.av.movieshowcase.ui.adapter.AdapterCast;
import com.av.movieshowcase.ui.adapter.AdapterCrew;
import com.av.movieshowcase.ui.adapter.AdapterGenre;
import com.av.movieshowcase.ui.adapter.AdapterReview;
import com.av.movieshowcase.ui.adapter.AdapterSimilarMovies;
import com.av.movieshowcase.ui.adapter.AdapterVideo;
import com.av.movieshowcase.ui.base.BaseActivity;
import com.av.movieshowcase.ui.main.DetailMovieViewModel;
import com.av.movieshowcase.utils.NavigationUtils;
import com.av.movieshowcase.ui.base.custom.recyclerview.RecyclerViewLinearManagerAnimation;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import javax.inject.Inject;
import dagger.android.AndroidInjection;

import static com.av.movieshowcase.utils.ViewUtils.isViewVisibleOnScreen;

public class DetailsActivity extends BaseActivity implements DetailCallback {

    private DetailsActivityBinding binding;

    private MovieTrendingResultsResponse movieTrending;
    private SimilarResult movieSimilar;
    private FavoriteItemEntity movieFavorite;
    private Animation aniFade;
    Animation expandIn;
    Animation expandOut;
    private String movieUrl = "",movieTitle = "", moviePoster = "", movieID = "0", movieDate = "", type = "movie";
    private String WATCHLIST = "My WatchList";
    private String FAVORITES = "Favorites";
    private Float movieRating;
    private boolean isCardShown = false;
    private boolean isOpen = false;
    boolean isWatchlist = false;
    boolean isFavorite = false;

    @Inject
    ViewModelFactory viewModelFactory;

    DetailMovieViewModel detailMovieViewModel;
    private AdapterGenre adapterGenre;
    private AdapterVideo adapterVideo;
    private AdapterCast adapterCast;
    private AdapterCrew adapterCrew;
    private AdapterReview adapterReview;
    private AdapterSimilarMovies adapterSimilarMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        AndroidInjection.inject(this);
        initView();
        initialiseViewModel();


        getSupportActionBar().hide();

        setTransparentStatusBar(false);
        enableSharedTransitions();
        removeAppBarOutline(binding.appBar);


        binding.rating.setRating(movieRating);
        binding.ratingFav.setRating(movieRating);
        binding.ratingFavFooter.setRating(movieRating);
        binding.txtTitle.setText(movieTitle);
        binding.txtTitleFront.setText(movieTitle);
        binding.txtTitleFooter.setText(movieTitle);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.statusbarShadow.getLayoutParams();
        params.height = getStatusBarHeight()+30;
        binding.statusbarShadow.setLayoutParams(params);



        Picasso.get().load(moviePoster)
                .into(binding.imgPoster);

        Picasso.get().load(moviePoster)
                .into(binding.imgPosterFav);

        Picasso.get().load(moviePoster)
                .into(binding.imgPosterFooter);

        binding.imgBack.setOnClickListener( v -> onBackPressed());
        binding.imgShare.setOnClickListener( v -> NavigationUtils.shareTextUrl(this,movieTitle,movieUrl));
        binding.favoriteView.setOnClickListener( v-> showBottomSheetSave());
        binding.favoriteViewFooter.setOnClickListener( v-> showBottomSheetSave());
    }


    private void fillStrings(String title, String poster,String id, Float vote, String date){
        movieTitle = title;
        moviePoster = poster;
        movieID = id;
        movieRating = vote;
        movieDate = date;
    }

    private void initView() {
        aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        expandIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.expand_in);
        expandOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.expand_out);

        Double shadowHeight = Double.valueOf(getScreenHeight())/2;

        setMargins(binding.imgBack,25,getStatusBarHeight()+20,0,0);
        setMargins(binding.imgShare,0,getStatusBarHeight()+20,25,0);

        RelativeLayout.LayoutParams paramsPoster = (RelativeLayout.LayoutParams) binding.imgPoster.getLayoutParams();
        paramsPoster.height = getScreenHeight();
        binding.imgPoster.setLayoutParams(paramsPoster);

        RelativeLayout.LayoutParams paramsShadow = (RelativeLayout.LayoutParams) binding.bottomPosterShadow.getLayoutParams();
        paramsShadow.height = shadowHeight.intValue();
        binding.bottomPosterShadow.setLayoutParams(paramsShadow);

        if(getIntent().getExtras()!=null){
            String jsonString = getIntent().getExtras().getString("movie_detail");
            Gson gson = new Gson();
            String origin = getIntent().getExtras().getString("origin");
            if(origin.equalsIgnoreCase("trending")){
                movieTrending = gson.fromJson(jsonString, MovieTrendingResultsResponse.class);
                fillStrings(movieTrending.getTitle(),movieTrending.getPosterPath(),movieTrending.getId()+""
                        ,movieTrending.getVoteAverage().floatValue()/2,movieTrending.getReleaseDate());
            } else if(origin.equalsIgnoreCase("similar")){
                movieSimilar = gson.fromJson(jsonString, SimilarResult.class);
                fillStrings(movieSimilar.getTitle(),movieSimilar.getPosterPath(),movieSimilar.getId()+""
                        ,movieSimilar.getVoteAverage().floatValue()/2, movieSimilar.getReleaseDate());
            } else  if(origin.equalsIgnoreCase("favorite")){
                movieFavorite = gson.fromJson(jsonString, FavoriteItemEntity.class);
                fillStrings(movieFavorite.getTitle(),movieFavorite.getPosterPath(),movieFavorite.getItemId()+""
                        ,movieFavorite.getVoteAverage().floatValue()/2,movieFavorite.getReleaseDate());
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.imgPoster.setTransitionName("thumbnailTransition");
                binding.rating.setTransitionName("voteTransition");
            }
        }

        adapterGenre = new AdapterGenre(activity);
        adapterVideo = new AdapterVideo(activity);
        adapterCast = new AdapterCast(activity);
        adapterCrew = new AdapterCrew(activity);
        adapterReview = new AdapterReview(activity);
        adapterSimilarMovies = new AdapterSimilarMovies(activity);

        initHorizontalRecyclerView(binding.genreList,adapterGenre);
        initHorizontalRecyclerView(binding.videoList,adapterVideo);
        initHorizontalRecyclerView(binding.castList,adapterCast);
        initHorizontalRecyclerView(binding.crewList,adapterCrew);
        initVerticalRecyclerView(binding.reviewList,adapterReview);
        initHorizontalRecyclerView(binding.similarList, adapterSimilarMovies);


        startTransitions();
        initScrollViewListener(binding.detailsScrollView, binding.imgPoster);

        //todo check for elastic drag dissmis layout
        /*binding.detailsContainer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        final int stableListPaddingBottom = binding.detailsCoordinatorLayout.getPaddingBottom();
        final int stableListPaddingLeft = binding.detailsCoordinatorLayout.getPaddingLeft();
        final int stableListPaddingRight = binding.detailsCoordinatorLayout.getPaddingRight();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            binding.detailsContainer.setOnApplyWindowInsetsListener((v, insets) -> {
               final ViewGroup.MarginLayoutParams listLp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
               listLp.topMargin = insets.getSystemWindowInsetTop();
               v.setLayoutParams(listLp);
                binding.detailsCoordinatorLayout.setPadding(
                       stableListPaddingLeft + binding.detailsCoordinatorLayout.getPaddingLeft(),
                        binding.detailsCoordinatorLayout.getPaddingTop(),
                       stableListPaddingRight + insets.getSystemWindowInsetRight(),
                       stableListPaddingBottom + insets.getSystemWindowInsetBottom());
               final ViewGroup.MarginLayoutParams fabLp = (ViewGroup.MarginLayoutParams) fab.getLayoutParams();
               fabLp.rightMargin = stableFabMarginRight + insets.getSystemWindowInsetRight();
               fabLp.bottomMargin = stableFabMarginBottom + insets.getSystemWindowInsetBottom();
               fab.setLayoutParams(fabLp);
               if (back != null) {
                   final ViewGroup.MarginLayoutParams backLp = (ViewGroup.MarginLayoutParams) back.getLayoutParams();
                   backLp.leftMargin = stableBackMarginLeft + insets.getSystemWindowInsetLeft();
               }
               return insets;
           });
        }*/
    }


    private void initHorizontalRecyclerView(RecyclerView recyclerView, Object adapter){
        RecyclerViewLinearManagerAnimation layoutManagerHorizontal = new RecyclerViewLinearManagerAnimation(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManagerHorizontal);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);
    }

    private void initVerticalRecyclerView(RecyclerView recyclerView, Object adapter){
        RecyclerViewLinearManagerAnimation layoutManagerVertical = new RecyclerViewLinearManagerAnimation(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerVertical);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);
    }


    private void initScrollViewListener(NestedScrollView detailsScrollView,View checkView) {
        final Rect scrollBounds = new Rect();
        detailsScrollView.getHitRect(scrollBounds);

        detailsScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if(isViewVisibleOnScreen(scrollBounds,binding.videoList) || isViewVisibleOnScreen(scrollBounds,binding.relFooter)) {
                hideCardFav();
            } else {
                showCardFav();
            }
        });

    }



    private void hideCardFav(){
        if(isCardShown){
            binding.cardFavourite.startAnimation(expandOut);
            binding.cardFavourite.setVisibility(View.GONE);
        }
        isCardShown = false;
    }

    private void showCardFav(){
        if(!isCardShown){
            binding.cardFavourite.setVisibility(View.VISIBLE);
            binding.cardFavourite.startAnimation(expandIn);
        }
        isCardShown = true;
    }


    private void initialiseViewModel() {
        detailMovieViewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailMovieViewModel.class);
        detailMovieViewModel.setCallback(this);
        showShimmer();
        detailMovieViewModel.fetchMovieDetail(movieID);
    }


    private void showShimmer(){
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.relMovieDetails.setVisibility(View.GONE);
        binding.shimmerViewContainer.startShimmer();
    }

    private void hideShimmer(){
        binding.shimmerViewContainer.setVisibility(View.GONE);
        binding.relMovieDetails.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.stopShimmer();
    }

    private void startTransitions() {
        binding.bottomPosterShadow.startAnimation(aniFade);
        binding.txtTitle.startAnimation(aniFade);
        binding.rating.startAnimation(aniFade);
        binding.imgBack.startAnimation(aniFade);
        binding.imgShare.startAnimation(aniFade);
        binding.relDuration.startAnimation(aniFade);

        new Boom(binding.imgBack);
        new Boom(binding.imgShare);
    }

    @Override
    public void showDetailResponse(MovieDetailResponse response) {
        hideShimmer();

       List<VideoResult> videoData = response.getVideos().getResults();
        if(videoData !=null && !videoData.isEmpty()){ adapterVideo.refreshAdapter(videoData); }

        List<Genre> genreData = response.getGenres();
        if(genreData !=null && !genreData.isEmpty()){ adapterGenre.refreshAdapter(genreData); }

        List<Cast> castData = response.getCredits().getCast();
        if(castData !=null && !castData.isEmpty()){ adapterCast.refreshAdapter(castData); }
        else{ binding.castView.setVisibility(View.GONE);}

        List<Crew> crewData = response.getCredits().getCrew();
        if(crewData !=null && !crewData.isEmpty()){ adapterCrew.refreshAdapter(crewData); }
        else{ binding.crewView.setVisibility(View.GONE);}

        List<ReviewResult> reviewData = response.getReviews().getResults();
        if(reviewData !=null && !reviewData.isEmpty()){ adapterReview.refreshAdapter(reviewData); }
        else{ binding.reviewView.setVisibility(View.GONE);}

        List<SimilarResult> similarData = response.getSimilar().getResults();
        if(similarData !=null && !similarData.isEmpty()){ adapterSimilarMovies.refreshAdapter(similarData); }
        else{ binding.similarView.setVisibility(View.INVISIBLE);}

        binding.txtDescription.setText(response.getOverview());
        binding.txtDuration.setText(response.getRuntime()+" min");
        movieUrl = response.getHomepage();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), "Error "+message, Toast.LENGTH_SHORT).show();
        hideShimmer();
    }



    private void showBottomSheetSave() {
        View view = getLayoutInflater().inflate(R.layout.fragment_favorites_modal, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this,R.style.SheetDialog);
        dialog.setContentView(view);
        dialog.show();



        final TextView txtWatchList = (TextView) dialog.findViewById(R.id.txtWatchList);
        final TextView txtFavorite = (TextView) dialog.findViewById(R.id.txtFavorite);

        if (detailMovieViewModel.isInList(Integer.valueOf(movieID),WATCHLIST)) {
            txtWatchList.setText(getString(R.string.remove_from_watchlist));
            isWatchlist = true;
        }  else {
            txtWatchList.setText(getString(R.string.add_to_watchlist));
            isWatchlist = false;
        }

        if (detailMovieViewModel.isInList(Integer.valueOf(movieID),FAVORITES)){
            txtFavorite.setText(getString(R.string.remove_from_favorites));
            isFavorite = true;
        }  else {
            txtFavorite.setText(getString(R.string.add_to_favorites));
            isFavorite = false;
        }

        txtWatchList.setOnClickListener(v-> {
            if (isWatchlist) detailMovieViewModel.removeItemFromListById(Integer.valueOf(movieID),WATCHLIST);
            else {
                FavoriteItemEntity favoriteItemEntity = new FavoriteItemEntity(Integer.valueOf(movieID),
                        Double.valueOf(movieRating), type, movieTitle,  movieDate, moviePoster, WATCHLIST);
                detailMovieViewModel.insertItemInList(favoriteItemEntity, WATCHLIST);
            }
            EventBus.getDefault().post(new EventSender("update"));
            dialog.dismiss();
        });


        txtFavorite.setOnClickListener(v-> {
            if (isFavorite) detailMovieViewModel.removeItemFromListById(Integer.valueOf(movieID),FAVORITES);
            else {
                FavoriteItemEntity favoriteItemEntity = new FavoriteItemEntity(Integer.valueOf(movieID),
                        Double.valueOf(movieRating), type, movieTitle,  movieDate, moviePoster, FAVORITES);
                detailMovieViewModel.insertItemInList(favoriteItemEntity, FAVORITES);
            }
            dialog.dismiss();
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
