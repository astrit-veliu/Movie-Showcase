package com.av.movieshowcase.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.av.movieshowcase.R;
import com.av.movieshowcase.data.remote.model.movie_details.Genre;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class AdapterKnownAs extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> data;
    private Activity context;


    public AdapterKnownAs(Activity mContext) {
        data = new ArrayList<>();
        this.context = mContext;
    }

    public void refreshAdapter(List<String> data) {
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
        String item = data.get(position);

        if (item != null) {
            viewHolder.chipGenre.setText(item);
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


}
