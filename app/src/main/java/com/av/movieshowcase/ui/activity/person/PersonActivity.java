package com.av.movieshowcase.ui.activity.person;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.av.movieshowcase.R;
import com.av.movieshowcase.data.callbacks.PersonCallback;
import com.av.movieshowcase.data.local.entity.FavoriteItemEntity;
import com.av.movieshowcase.data.remote.model.PersonResponse;
import com.av.movieshowcase.databinding.PersonActivityBinding;
import com.av.movieshowcase.factory.ViewModelFactory;
import com.av.movieshowcase.ui.adapter.AdapterGenre;
import com.av.movieshowcase.ui.adapter.AdapterKnownAs;
import com.av.movieshowcase.ui.base.BaseActivity;
import com.av.movieshowcase.ui.main.DetailMovieViewModel;
import com.av.movieshowcase.ui.main.PersonViewModel;
import com.av.movieshowcase.utils.CustomAppBarOffset;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class PersonActivity extends BaseActivity implements PersonCallback {

    private String PERSONS = "Persons";
    private PersonActivityBinding binding;

    @Inject
    ViewModelFactory viewModelFactory;
    PersonViewModel personViewModel;
    private AdapterKnownAs adapterNames;
    private int personId = 0;
    private boolean isFavorite = false;
    private PersonResponse personDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemePerson);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_person);
        AndroidInjection.inject(this);

        enableSharedTransitions();
        setLightStatusBar();
        setLightNavigationBar();

        initView();
        initialiseViewModel();
        checkFabFavoriteColor();


        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void initView() {

        personId = getIntent().getExtras().getInt("person_id");
        System.out.println("personID "+personId);

        adapterNames = new AdapterKnownAs(activity);
        binding.nameList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.nameList.setNestedScrollingEnabled(false);
        binding.nameList.setAdapter(adapterNames);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.imgPoster.setTransitionName("thumbnailTransition");
        }
    }


    private void checkFabFavoriteColor(){
        if (personViewModel.isInList(Integer.valueOf(personId),PERSONS)){
            binding.fabFavorite.setColorFilter(getResources().getColor(R.color.materialRed));
            isFavorite = true;
        }  else {
            binding.fabFavorite.setColorFilter(getResources().getColor(R.color.white));
            isFavorite = false;
        }
    }




    private void initialiseViewModel() {
        personViewModel = ViewModelProviders.of(this, viewModelFactory).get(PersonViewModel.class);
        personViewModel.setCallback(this);
        showLoadingAnimation();
        personViewModel.fetchPersonDetails(personId);
    }

    @Override
    public void showPersonResponse(PersonResponse response) {
        hideLoadingAnimation();
        personDetails = response;
        fillUI(response);
    }

    private void fillUI(PersonResponse response) {

        Picasso.get().load(response.getProfilePath())
                .into(binding.imgPoster);

        binding.appbar.addOnOffsetChangedListener(new CustomAppBarOffset(this, binding.textToolbar, binding.txtName, binding.toolbar, response.getName(), binding.imgPoster));

        binding.txtName.setText(response.getName());
        binding.txtPopularity.setText("Popularity "+response.getPopularity()+"");
        binding.txtKnownFor.setText(response.getKnownForDepartment());
        binding.txtBorn.setText(response.getBirthday());
        binding.txtBiography.setText(response.getBiography());
        binding.txtOrigin.setText(response.getPlaceOfBirth());

        if(response.getAlsoKnownAs()!=null && response.getAlsoKnownAs().size()>0){
            adapterNames.refreshAdapter(response.getAlsoKnownAs());
            binding.txtAKA.setVisibility(View.VISIBLE);
        } else binding.txtAKA.setVisibility(View.GONE);


        binding.fabFavorite.setOnClickListener(v-> {
            if(personDetails!=null){
                if (isFavorite) personViewModel.removeItemFromListById(Integer.valueOf(personId),PERSONS);
                else {
                    FavoriteItemEntity favoriteItemEntity = new FavoriteItemEntity(Integer.valueOf(personId), Double.valueOf(personDetails.getPopularity())
                            , "person", personDetails.getName(),  personDetails.getKnownForDepartment(), personDetails.getProfilePath(), PERSONS);
                    personViewModel.insertItemInList(favoriteItemEntity, PERSONS);
                }

                checkFabFavoriteColor();
            }
        });

    }

    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        hideLoadingAnimation();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
        }
        return true;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }
}
