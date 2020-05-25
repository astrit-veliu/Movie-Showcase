package com.av.movieshowcase.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.av.movieshowcase.R;
import com.av.movieshowcase.data.local.entity.FavoriteItemEntity;
import com.av.movieshowcase.databinding.ListFavoritePersonItemBinding;
import com.av.movieshowcase.ui.activity.person.PersonActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterFavoritePersons extends RecyclerView.Adapter<AdapterFavoritePersons.CustomViewHolder> {

    private List<FavoriteItemEntity> data;
    private Activity context;


    public AdapterFavoritePersons(Activity mContext) {
        data = new ArrayList<>();
        this.context = mContext;
    }

    public void refreshAdapter(List<FavoriteItemEntity> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public AdapterFavoritePersons.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListFavoritePersonItemBinding itemBinding = ListFavoritePersonItemBinding.inflate(layoutInflater, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(itemBinding);
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
    public FavoriteItemEntity getItem(int position) {
        return data.get(position);
    }


    @Override
    public void onBindViewHolder(@NonNull AdapterFavoritePersons.CustomViewHolder holder, int position) {
        holder.bindTo(getItem(position),position);
    }



    protected class CustomViewHolder extends RecyclerView.ViewHolder {

        private ListFavoritePersonItemBinding binding;
        public CustomViewHolder(ListFavoritePersonItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(FavoriteItemEntity favoriteItemEntity, int position) {

            if (favoriteItemEntity != null) {
                new Boom(binding.container);

                Picasso.get().load(favoriteItemEntity.getPosterPath())
                        .placeholder(R.color.soft_grey)
                        .into(binding.imgPoster);

                binding.txtTitleFront.setText(favoriteItemEntity.getTitle());
                binding.txtDepartment.setText(favoriteItemEntity.getReleaseDate());

                Long personID = favoriteItemEntity.getItemId();

                binding.container.setOnClickListener(v -> {
                    Intent intent = new Intent(context, PersonActivity.class);
                    intent.putExtra("person_id", personID.intValue());

                    System.out.println("personID "+favoriteItemEntity.getItemId());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.imgPoster.setTransitionName("thumbnailTransition");
                        Pair<View, String> pair1 = Pair.create((View) binding.imgPoster, binding.imgPoster.getTransitionName());

                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, pair1);
                        context.startActivity(intent, optionsCompat.toBundle());
                    } else {
                        context.startActivity(intent);
                    }
                });

                animateItemView(binding.container,position);

            }

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
