package com.av.movieshowcase.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.av.movieshowcase.R;
import com.av.movieshowcase.data.callbacks.ProfileCallback;
import com.av.movieshowcase.data.local.entity.FavoriteItemEntity;
import com.av.movieshowcase.data.local.entity.FavoriteListEntity;
import com.av.movieshowcase.data.local.model.EventSender;
import com.av.movieshowcase.databinding.FavouriteFragmentBinding;
import com.av.movieshowcase.factory.ViewModelFactory;
import com.av.movieshowcase.ui.adapter.AdapterFavorite;
import com.av.movieshowcase.ui.adapter.AdapterFavoritePersons;
import com.av.movieshowcase.ui.base.BaseFragment;
import com.av.movieshowcase.ui.main.ProfileViewModel;
import com.av.movieshowcase.ui.base.custom.recyclerview.RecyclerViewStaggeredAnimationLayoutManager;
import com.av.movieshowcase.utils.SaveData;
import com.av.movieshowcase.ui.base.custom.recyclerview.RecyclerViewLinearManagerAnimation;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;


public class FavouritesFragment extends BaseFragment implements ProfileCallback {

    private static final int CAMERA_PIC_REQUEST = 0;
    private static final int SELECT_PICTURE = 1;
    private FavouriteFragmentBinding binding;
    @Inject
    ViewModelFactory viewModelFactory;
    ProfileViewModel profileViewModel;
    List<FavoriteListEntity> listFeedItem;
    private String LIST = "My WatchList", TYPE = "movie";
    private SaveData saveData;
    private AdapterFavorite adapterFavorite;
    private AdapterFavoritePersons adapterFavoritePersons;
    RecyclerViewStaggeredAnimationLayoutManager staggeredGridLayoutManager;
    RecyclerViewLinearManagerAnimation recyclerViewLinearManagerAnimation;
    private boolean shouldReload = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidSupportInjection.inject(this);
        initialiseViewModel();

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites, container, false);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialiseView();
    }

    private void initialiseView() {
        adapterFavorite = new AdapterFavorite(activity);
        adapterFavoritePersons = new AdapterFavoritePersons(activity);
        listFeedItem = new ArrayList<>();
        //binding.txtHeader.setPadding(0,getStatusBarHeight()*2,0,0);
        binding.toolbarStrip.setPadding(0,getStatusBarHeight()*2,0,0);
        saveData = new SaveData(getContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.appBar.setOutlineProvider(null);
        }

        staggeredGridLayoutManager = new RecyclerViewStaggeredAnimationLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewLinearManagerAnimation = new RecyclerViewLinearManagerAnimation(activity, LinearLayoutManager.VERTICAL, false);
        binding.recyclerLists.setLayoutManager(staggeredGridLayoutManager);
        binding.recyclerLists.setNestedScrollingEnabled(false);
        binding.recyclerLists.setAdapter(adapterFavorite);

        binding.headerTextview.setText(saveData.getName());

        binding.profileImageView.setOnClickListener( v-> {
            Toast.makeText(activity, "should open dialog", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder getImageFrom = new AlertDialog.Builder(getContext());
            getImageFrom.setTitle("Select:");
            final CharSequence[] opsChars = {"Take a picture", "Choose from gallery"};
            getImageFrom.setItems(opsChars, (dialog, which) -> {
                if(which == 0){
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                }else
                if(which == 1){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Pick from gallery"), SELECT_PICTURE);
                }
                dialog.dismiss();
            });

        });
    }


    private void initialiseViewModel() {
        profileViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel.class);
        profileViewModel.setCallback(this);
        profileViewModel.getAllLists();
    }


    private void manageTabBar() {
        for (int i = 0; i < binding.tabs.getTabCount(); i++) {

            TabLayout.Tab tab = binding.tabs.getTabAt(i);
            if (tab != null) {

                TextView tabTextView = new TextView(getContext());
                tab.setCustomView(tabTextView);

                tabTextView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

                tabTextView.setText(tab.getText());

                // First tab is the selected tab, so if i==0 then set BOLD typeface
                if (i == 0) {
                    tabTextView.setTypeface(null, Typeface.BOLD);
                    tabTextView.setTextColor(getResources().getColor(R.color.black));
                    tabTextView.setTextSize(18);
                    LIST = tabTextView.getText().toString();
                    profileViewModel.getListItems(TYPE, LIST);
                } else {
                    tabTextView.setTextSize(14);
                    tabTextView.setTextColor(getResources().getColor(R.color.lightgrey));
                }

            }

        }

        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView text = (TextView) tab.getCustomView();
                if(text != null){
                text.setTypeface(null, Typeface.BOLD);
                text.setTextColor(getResources().getColor(R.color.black));
                text.setTextSize(18);

                if(text.getText().toString().equalsIgnoreCase("Persons")) TYPE = "person";
                else if (TYPE.equalsIgnoreCase("person")) TYPE = "movie";

                LIST = text.getText().toString();
                profileViewModel.getListItems(TYPE, LIST);
                manageFilterVisibility(LIST);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView text = (TextView) tab.getCustomView();
                text.setTypeface(null, Typeface.NORMAL);
                text.setTextColor(getResources().getColor(R.color.lightgrey));
                text.setTextSize(14);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setupTypeTabBar();
    }

    private void manageFilterVisibility(String list) {
        if(isCustomList(list)){
            //show filter widgets
        } else {
            //hide filter widgets
        }
    }


    private void setupTypeTabBar() {
        binding.tabsType.removeAllTabs();

        binding.tabsType.addTab(binding.tabsType.newTab().setIcon(R.drawable.ic_theaters_24px),0);
        binding.tabsType.addTab(binding.tabsType.newTab().setIcon(R.drawable.ic_tv_24px),1);

        int tabIconColor_anctive = ContextCompat.getColor(getContext(), R.color.materialBlue);
        int tabIconColor_ianctive = ContextCompat.getColor(getContext(), R.color.soft_grey);
        binding.tabsType.getTabAt(1).getIcon().setColorFilter(tabIconColor_ianctive, PorterDuff.Mode.SRC_IN);
        binding.tabsType.getTabAt(0).getIcon().setColorFilter(tabIconColor_anctive, PorterDuff.Mode.SRC_IN);

        binding.tabsType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getContext(), R.color.materialBlue);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

                switch (tab.getPosition()) {
                    case 0:
                        TYPE = "movie";
                        profileViewModel.getListItems(TYPE, LIST);
                        break;
                    case 1:
                        TYPE = "tv";
                        profileViewModel.getListItems(TYPE, LIST);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getContext(), R.color.lightgrey);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public void showListNames(List<FavoriteListEntity> allLists) {
        listFeedItem = allLists;
        if(isAdded()){
        binding.tabs.removeAllTabs();
        for (FavoriteListEntity list : listFeedItem) {
            binding.tabs.addTab(binding.tabs.newTab().setText(list.listName));
        }

            manageTabBar();
        }

    }

    @Override
    public void showFavorites(List<FavoriteItemEntity> favoriteItems) {
        if(favoriteItems !=null && !favoriteItems.isEmpty()){
            if(LIST.equalsIgnoreCase("Persons")){
                binding.tabsType.setVisibility(View.GONE);
                binding.recyclerLists.setLayoutManager(recyclerViewLinearManagerAnimation);
                binding.recyclerLists.setAdapter(adapterFavoritePersons);
                adapterFavoritePersons.refreshAdapter(favoriteItems);
            } else {
                binding.recyclerLists.setLayoutManager(staggeredGridLayoutManager);
                binding.recyclerLists.setAdapter(adapterFavorite);
                adapterFavorite.setItems(favoriteItems);
                binding.tabsType.setVisibility(View.VISIBLE);
            }
            hideEmptyState();
        } else showEmptyState();
    }


    private void showEmptyState(){
        binding.recyclerLists.setVisibility(View.GONE);
        binding.imgNoData.setVisibility(View.VISIBLE);
    }


    private void hideEmptyState(){
        binding.recyclerLists.setVisibility(View.VISIBLE);
        binding.imgNoData.setVisibility(View.GONE);
    }


    private boolean isCustomList(String listName){
        if (listName.equalsIgnoreCase(WATCHLIST) || listName.equalsIgnoreCase(FAVORITES)
                || listName.equalsIgnoreCase("Persons")) return false;
        else return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final EventSender message) {
        if (message.getMessage().startsWith("update")){
            //handle eventbus
           shouldReload = true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(shouldReload) {
            profileViewModel.getAllLists();
            shouldReload = false;
        }

    }
}
