package com.av.movieshowcase.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.av.movieshowcase.R;
import com.av.movieshowcase.data.remote.model.movie_details.Cast;
import com.av.movieshowcase.data.remote.model.movie_details.Genre;
import com.av.movieshowcase.ui.activity.person.PersonActivity;
import com.av.movieshowcase.utils.CircleImageView;
import com.google.android.material.chip.Chip;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterCast extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Cast> data;
    private Activity context;


    public AdapterCast(Activity mContext) {
        data = new ArrayList<>();
        this.context = mContext;
    }

    public void refreshAdapter(List<Cast> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cast, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Cast item = data.get(position);

        if (item != null) {
            new Boom(viewHolder.imgPeople);

            Picasso.get().load(item.getProfilePath())
                    .placeholder(R.color.soft_grey)
                    .into(viewHolder.imgPeople);

            viewHolder.txtName.setText(item.getName());
            viewHolder.txtRole.setText(item.getCharacter());
            viewHolder.imgPeople.setOnClickListener(v -> {

                Intent intent = new Intent(context, PersonActivity.class);
                intent.putExtra("person_id", item.getId());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    viewHolder.imgPeople.setTransitionName("thumbnailTransition");
                    Pair<View, String> pair1 = Pair.create((View) viewHolder.imgPeople, viewHolder.imgPeople.getTransitionName());

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
        private CircleImageView imgPeople;
        private TextView txtName;
        private TextView txtRole;

        public ViewHolder(View itemView) {
            super(itemView);
            imgPeople = itemView.findViewById(R.id.imgPeople);
            txtName = itemView.findViewById(R.id.txtName);
            txtRole = itemView.findViewById(R.id.txtRole);
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
