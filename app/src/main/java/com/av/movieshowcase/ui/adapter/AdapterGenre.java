package com.av.movieshowcase.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.av.movieshowcase.R;
import com.av.movieshowcase.data.remote.model.GenresList;
import com.av.movieshowcase.data.remote.model.movie_details.Genre;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aveliu2 on 15,January,2020
 */
public class AdapterGenre extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Genre> data;
    private Activity context;


    public AdapterGenre(Activity mContext) {
        data = new ArrayList<>();
        this.context = mContext;
    }

    public void refreshAdapter(List<Genre> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Genre item = data.get(position);

        if (item != null) {
            new Boom(viewHolder.chipGenre);
            viewHolder.chipGenre.setText(item.getName());
           animateItemView(viewHolder.itemView,position);

        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private Chip chipGenre;

        public ViewHolder(View itemView) {
            super(itemView);
            chipGenre = itemView.findViewById(R.id.chipGenre);
        }
    }


    private void animateItemView(View view, int position) {
        long animationDelay = 600;

        animationDelay += position * 30;

        view.setScaleY(0);
        view.setScaleX(0);
        view.animate()
                .scaleY(1)
                .scaleX(1)
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(null)
                .setStartDelay(animationDelay)
                .start();
    }
}
