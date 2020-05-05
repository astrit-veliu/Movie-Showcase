package com.av.movieshowcase.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.av.movieshowcase.R;
import com.av.movieshowcase.data.remote.model.movie_details.Cast;
import com.av.movieshowcase.data.remote.model.movie_details.SimilarResult;
import com.av.movieshowcase.ui.activity.details.DetailsActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aveliu2 on 28,January,2020
 */
public class AdapterSimilarMovies extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SimilarResult> data;
    private Activity context;
    private Gson gson;


    public AdapterSimilarMovies(Activity mContext) {
        data = new ArrayList<>();
        this.context = mContext;
        gson = new GsonBuilder().create();
    }

    public void refreshAdapter(List<SimilarResult> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_similar, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        SimilarResult item = data.get(position);

        if (item != null) {
            new Boom(viewHolder.itemView);

            Picasso.get().load(item.getPosterPath())
                    .placeholder(R.color.soft_grey)
                    .into(viewHolder.imgPoster);


            viewHolder.txtName.setText(item.getTitle());

            viewHolder.imgPoster.setOnClickListener(v -> {

                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("movie_detail", gson.toJson(item));
                intent.putExtra("origin", "similar");


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    viewHolder.imgPoster.setTransitionName("thumbnailTransitionSimilar");
                    Pair<View, String> pair1 = Pair.create((View) viewHolder.imgPoster, viewHolder.imgPoster.getTransitionName());

                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, pair1);
                    context.startActivity(intent, optionsCompat.toBundle());
                } else {
                    context.startActivity(intent);
                }
            });

            animateItemView(viewHolder.itemView,position);

        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPoster;
        private TextView txtName;

        public ViewHolder(View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            txtName = itemView.findViewById(R.id.txtName);
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
