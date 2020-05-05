package com.av.movieshowcase.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.astritveliu.boom.Boom;
import com.av.movieshowcase.R;
import com.av.movieshowcase.data.local.entity.FavoriteItemEntity;
import com.av.movieshowcase.data.remote.model.MovieTrendingResultsResponse;
import com.av.movieshowcase.databinding.FavoriteListItemBinding;
import com.av.movieshowcase.databinding.HomeListItemBinding;
import com.av.movieshowcase.ui.activity.details.DetailsActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class AdapterFavorite extends RecyclerView.Adapter<AdapterFavorite.CustomViewHolder> {

    private Activity activity;
    private List<FavoriteItemEntity> items;
    private Gson gson;

    public AdapterFavorite(Activity activity) {
        this.activity = activity;
        this.items = new ArrayList<>();
        gson = new GsonBuilder().create();
    }

    @NonNull
    @Override
    public AdapterFavorite.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FavoriteListItemBinding itemBinding = FavoriteListItemBinding.inflate(layoutInflater, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(itemBinding);
        return viewHolder;
    }

    public void setItems(List<FavoriteItemEntity> items) {
        this.items.clear();
        int startPosition = this.items.size();
        this.items.addAll(items);
        if(items.size()>1) notifyItemRangeChanged(startPosition, items.size());
        else notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public FavoriteItemEntity getItem(int position) {
        return items.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFavorite.CustomViewHolder holder, int position) {
        //If the current layout asks waterfall flow layout, set random height
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getRandomHight());
        holder.itemView.setLayoutParams(params);
        holder.bindTo(getItem(position),position);

    }


    private int getRandomHight()
    {
        int randomHeight = (int )(Math.random()*1000);
        System.out.println("heightValue "+randomHeight);
        return randomHeight<300 ? 300 : randomHeight;
    }


    protected class CustomViewHolder extends RecyclerView.ViewHolder {


        private FavoriteListItemBinding binding;
        public CustomViewHolder(FavoriteListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(FavoriteItemEntity movie, int position) {

            binding.txtVoteAverage.setText(movie.getVoteAverage()+" ★");

            Picasso.get().load(movie.getPosterPath())
                    .into(binding.imgPoster);

            binding.imgPoster.setOnClickListener(v -> {

                Intent intent = new Intent(activity, DetailsActivity.class);
                intent.putExtra("movie_detail", gson.toJson(movie));
                intent.putExtra("origin", "favorite");


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
