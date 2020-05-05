package com.av.movieshowcase.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.av.movieshowcase.AppConstants;
import com.av.movieshowcase.R;
import com.av.movieshowcase.data.remote.model.GenreDetail;
import com.av.movieshowcase.ui.fragment.CustomDialogFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import static com.av.movieshowcase.utils.AnimUtils.animateView;


public class BaseActivity extends AppCompatActivity implements AppConstants {

    private CustomDialogFragment loadingDialog;
    public static Activity activity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDialogLoading();
        activity = this;
    }

    public void setTransparentStatusBar(Boolean isEnlightedStatusBar){
        //make translucent statusBar on kitkat devices
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        if(isEnlightedStatusBar){
            setLightStatusBar();
        }

    }

    public void setLightNavigationBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View view = getWindow().getDecorView().findViewById(android.R.id.content);
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setNavigationBarColor(Color.WHITE);
        }
    }

    public void enableSharedTransitions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transition));
        }
    }

    private void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    public void setLightStatusBar(){
        View view = getWindow().getDecorView().findViewById(android.R.id.content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            //activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }



    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private String getGenresFromJson() {
        String json = null;
        try {
            //InputStream is = getAssets().open("assets/genres.json");
            InputStream is = getResources().openRawResource(R.raw.genres);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public List<GenreDetail> loadGenres(){
        List<GenreDetail> feedItem = new ArrayList<>();
        String jsonString = getGenresFromJson();
        Gson gson = new Gson();
        feedItem = gson.fromJson(jsonString,new TypeToken<List<GenreDetail>>(){}.getType());
        return feedItem;
    }


    public void showLoadingAnimation() {
        if (loadingDialog != null)
        {
                try {
                    loadingDialog.show(getSupportFragmentManager(), "loading");
                } catch (IllegalStateException ignored) {

                }

        }
        else
            Log.d("LoadingDialog","Cannot show loading dialog");
    }



    public void hideLoadingAnimation() {
        if (loadingDialog != null) {
            try {
                loadingDialog.dismissAllowingStateLoss();
            } catch (IllegalStateException ex) {}
            catch (NullPointerException e){}
        }
    }

    protected void createDialogLoading() {
        if (loadingDialog==null)
            loadingDialog = new CustomDialogFragment();
    }



    public int getScreenHeight(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        return height>500 ? height-200 : height-100;
    }


    public static void setMargins (View v, int left, int top, int right, int bottom) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(getPixelFromInt(left), top, getPixelFromInt(right), bottom);
            v.requestLayout();
        }
    }

    public static int getPixelFromInt(int value){
        Resources r = activity.getResources();
        int px = 0;

        try {
           px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    value,
                    r.getDisplayMetrics()
            );
        } catch (Exception e){
            px = 0;
        }

        return px;
    }


    public static void removeAppBarOutline(CollapsingToolbarLayout appBarLayout){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout.setOutlineProvider(null);
        }
    }


    public static void removeAppBarOutline(AppBarLayout appBarLayout){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout.setOutlineProvider(null);
        }
    }

}
