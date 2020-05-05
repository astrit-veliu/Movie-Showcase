package com.av.movieshowcase.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.av.movieshowcase.R;
import com.av.movieshowcase.data.local.model.EventSender;
import com.av.movieshowcase.data.remote.model.MovieTrendingResultsResponse;
import com.av.movieshowcase.databinding.HomeListItemBinding;
import com.av.movieshowcase.ui.activity.details.DetailsActivity;
import com.av.movieshowcase.utils.CustomBounceInterpolator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aveliu2 on 19,December,2019
 */
public class TrendingMovieAdapter extends RecyclerView.Adapter<TrendingMovieAdapter.CustomViewHolder> {

    private Activity activity;
    private List<MovieTrendingResultsResponse> movies;
    private Gson gson;
    private Animation bounceAnim;
    private CustomBounceInterpolator interpolator;
    private String movieEvent;

    public TrendingMovieAdapter(Activity activity) {
        this.activity = activity;
        this.movies = new ArrayList<>();
        bounceAnim = AnimationUtils.loadAnimation( activity.getApplicationContext(), R.anim.bounce_animation);
        interpolator = new CustomBounceInterpolator(0.1, 20);
        bounceAnim.setInterpolator(interpolator);
        gson = new GsonBuilder().create();
    }

    @NonNull
    @Override
    public TrendingMovieAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        HomeListItemBinding itemBinding = HomeListItemBinding.inflate(layoutInflater, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(itemBinding);
        return viewHolder;
    }

    public void setItems(List<MovieTrendingResultsResponse> movies) {
        this.movies.clear();
        int startPosition = this.movies.size();
        this.movies.addAll(movies);
        if(movies.size()>1) notifyItemRangeChanged(startPosition, movies.size());
        else notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public MovieTrendingResultsResponse getItem(int position) {
        return movies.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingMovieAdapter.CustomViewHolder holder, int position) {
        holder.bindTo(getItem(position),position);
    }

    protected class CustomViewHolder extends RecyclerView.ViewHolder {


        private HomeListItemBinding binding;
        public CustomViewHolder(HomeListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;



           /* DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            itemView.setLayoutParams(new RecyclerView.LayoutParams(new Float(width * 0.85f).intValue(),
                    RecyclerView.LayoutParams.WRAP_CONTENT));*/
        }

        public void bindTo(MovieTrendingResultsResponse movie, int position) {

            binding.imgPoster.setOnLongClickListener(v -> {
                movieEvent = gson.toJson(movie);
                binding.mainCard.startAnimation(bounceAnim);
                return true;
            });

            bounceAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    EventBus.getDefault().post(new EventSender(movieEvent));
                }

                @Override
                public void onAnimationEnd(Animation arg0) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            binding.txtVoteAverage.setText(movie.getVoteAverage()+" ★");

            Picasso.get().load(movie.getPosterPath())
                    .into(binding.imgPoster);

            binding.imgPoster.setOnClickListener(v -> {

                Intent intent = new Intent(activity, DetailsActivity.class);
                intent.putExtra("movie_detail", gson.toJson(movie));
                intent.putExtra("origin", "trending");


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.mainCard.setTransitionName("thumbnailTransition");
                    binding.txtVoteAverage.setTransitionName("voteTransition");
                    Pair<View, String> pair1 = Pair.create((View) binding.mainCard, binding.mainCard.getTransitionName());
                    Pair<View, String> pair2 = Pair.create((View) binding.txtVoteAverage, binding.txtVoteAverage.getTransitionName());

                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) activity, pair1,pair2);
                    activity.startActivity(intent, optionsCompat.toBundle());
                } else {
                    activity.startActivity(intent);
                }
            });


            animateItemView(binding.mainCard, position);

        }
    }



    private void animateItemView(View view, int position) {

        view.setScaleY(0);
        view.setScaleX(0);
        view.animate()
                .scaleY(1)
                .scaleX(1)
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(null)
                .start();
    }


}
