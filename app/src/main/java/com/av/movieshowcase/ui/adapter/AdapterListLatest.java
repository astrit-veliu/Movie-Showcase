package com.av.movieshowcase.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.av.movieshowcase.data.remote.model.MovieTrendingResultsResponse;
import com.av.movieshowcase.databinding.ListStructureMoviesItemBinding;
import com.av.movieshowcase.ui.activity.details.DetailsActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterListLatest extends RecyclerView.Adapter<AdapterListLatest.CustomViewHolder> {

    private Activity activity;
    private List<MovieTrendingResultsResponse> movies;
    private Gson gson;
    private Boolean seeAll = false;

    public AdapterListLatest(Activity activity) {
        this.activity = activity;
        this.movies = new ArrayList<>();
        gson = new GsonBuilder().create();
    }

    @NonNull
    @Override
    public AdapterListLatest.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListStructureMoviesItemBinding itemBinding = ListStructureMoviesItemBinding.inflate(layoutInflater, parent, false);
        AdapterListLatest.CustomViewHolder viewHolder = new AdapterListLatest.CustomViewHolder(itemBinding);
        return viewHolder;
    }

    public void setItems(List<MovieTrendingResultsResponse> movies) {
        int startPosition = this.movies.size();
        this.movies.addAll(movies);
        notifyItemRangeChanged(startPosition, movies.size());
    }


    @Override
    public int getItemCount() {
        //return movies.size();
        if (seeAll)
            return movies.size();
        else
            return movies.size() > 4 ? 4 : movies.size();
    }



    public void setSeeAll( boolean seeAll){
        this.seeAll=seeAll;
        notifyDataSetChanged();
    }


    public MovieTrendingResultsResponse getItem(int position) {
        return movies.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterListLatest.CustomViewHolder holder, int position) {
        holder.bindTo(getItem(position),position);
    }

    protected class CustomViewHolder extends RecyclerView.ViewHolder {


        private ListStructureMoviesItemBinding binding;
        public CustomViewHolder(ListStructureMoviesItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

        public void bindTo(MovieTrendingResultsResponse movie, int position) {

            // animateItemView(binding.,1);
            new Boom(binding.getRoot());


            binding.txtVoteAverage.setText(movie.getVoteAverage()+" ★");
            binding.txtTitleFront.setText(movie.getTitle());
            binding.txtHouse.setText(movie.getReleaseDate());

            Picasso.get().load(movie.getPosterPath())
                    .into(binding.imgPoster);

            binding.getRoot().setOnClickListener(v -> {

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