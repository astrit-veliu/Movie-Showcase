package com.av.movieshowcase.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.av.movieshowcase.R;
import com.av.movieshowcase.data.remote.model.movie_details.Crew;
import com.av.movieshowcase.data.remote.model.movie_details.ReviewResult;
import com.av.movieshowcase.utils.ExpandableTextView;
import com.av.movieshowcase.utils.NavigationUtils;
import com.av.movieshowcase.utils.TextViewMore;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aveliu2 on 27,January,2020
 */
public class AdapterReview extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ReviewResult> data;
    private Activity context;


    public AdapterReview(Activity mContext) {
        data = new ArrayList<>();
        this.context = mContext;
    }

    public void refreshAdapter(List<ReviewResult> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ReviewResult item = data.get(position);

        if (item != null) {
            new Boom(viewHolder.relBrowser);


            viewHolder.txtName.setText(item.getAuthor());
            viewHolder.txtFirstLetter.setText(item.getAuthor().substring(0,1));
            viewHolder.txtReview.setText(item.getContent());
           // viewHolder.txtReview.setShowingLine(10);

            if(item.getUrl()!=null && !item.getUrl().equalsIgnoreCase("")) {
                viewHolder.relBrowser.setOnClickListener(v -> {
                    //open url of review in browser
                    NavigationUtils.browserIntent(context, item.getUrl());
                });
            }

            animateItemView(viewHolder.itemView,position);
        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtReview;
      //  private TextViewMore txtReview;
        private View shadowView;
        private RelativeLayout relBrowser;
        private TextView txtFirstLetter;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtReview = itemView.findViewById(R.id.txtReview);
            shadowView = itemView.findViewById(R.id.shadowView);
            relBrowser = itemView.findViewById(R.id.relBrowser);
            txtFirstLetter = itemView.findViewById(R.id.txtFirstLetter);
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
