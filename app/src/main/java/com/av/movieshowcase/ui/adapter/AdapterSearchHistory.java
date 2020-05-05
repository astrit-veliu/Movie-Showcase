package com.av.movieshowcase.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.av.movieshowcase.R;
import com.av.movieshowcase.data.callbacks.SearchAdapterCallback;
import com.av.movieshowcase.data.local.entity.HistoryEntity;
import com.av.movieshowcase.data.remote.model.movie_details.Cast;
import com.av.movieshowcase.ui.activity.person.PersonActivity;
import com.av.movieshowcase.utils.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterSearchHistory extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HistoryEntity> data;
    private Activity context;
    private SearchAdapterCallback callback;


    public AdapterSearchHistory(Activity mContext) {
        data = new ArrayList<>();
        this.context = mContext;
    }

    public void refreshAdapter(List<HistoryEntity> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        HistoryEntity item = data.get(position);

        if (item != null) {
            new Boom(viewHolder.itemView);
            viewHolder.numberTxt.setText((position+1)+"");
            viewHolder.queryTxt.setText(item.getQuery());
            viewHolder.typeTxt.setText(item.getType());
            viewHolder.timeTxt.setText(item.getLast_search());
            viewHolder.charTxt.setText(item.getQuery().substring(0,1));

            viewHolder.itemView.setOnClickListener( v-> callback.searchFromAdapter(item.getQuery()));

            viewHolder.imgMore.setOnClickListener( v-> {
                popUpMenu(viewHolder.imgMore, item);
            });


        }

    }


    public void setCallback(SearchAdapterCallback callback) {
        this.callback = callback;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView numberTxt;
        private TextView queryTxt;
        private TextView typeTxt;
        private TextView timeTxt;
        private TextView charTxt;
        private ImageView imgMore;

        public ViewHolder(View itemView) {
            super(itemView);
            numberTxt = itemView.findViewById(R.id.numberTxt);
            queryTxt = itemView.findViewById(R.id.queryTxt);
            typeTxt = itemView.findViewById(R.id.typeTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            charTxt = itemView.findViewById(R.id.charTxt);
            imgMore = itemView.findViewById(R.id.imgMore);
        }
    }


    private void popUpMenu(final ImageView holder, HistoryEntity item) {
        final PopupMenu popupMenu = new PopupMenu(context, holder);
        popupMenu.inflate(R.menu.search_menu);
        //popupMenu.getMenu().getItem(0).setTitle("Go to " + setSrc);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //You can add any options you want according to your need.
                    case R.id.delete_search:
                        callback.deleteSearchItem(item);
                        break;

                    case R.id.delete_all:
                        callback.deleteAllSearchItems();
                        break;

                    default:
                        return false;
                }
                return false;
            }
        });
        popupMenu.show();
    }

}
