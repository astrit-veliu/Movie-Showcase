package com.av.movieshowcase.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.av.movieshowcase.AppConstants;
import com.av.movieshowcase.R;
import com.av.movieshowcase.data.remote.model.GenreDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.av.movieshowcase.ui.base.BaseActivity.getPixelFromInt;


public class BaseFragment extends Fragment implements AppConstants {

    protected Activity activity;
    private DialogLoading loadingDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
        createDialogLoading();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }



    public static void setMargins (View v, int left, int top, int right, int bottom) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(getPixelFromInt(left), getPixelFromInt(top), getPixelFromInt(right), getPixelFromInt(bottom));
            v.requestLayout();
        }
    }



    private String getGenresFromJson() {
        String json = null;
        try {
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
                loadingDialog.show(getFragmentManager(), "loading");
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
            loadingDialog = new DialogLoading();
    }



    public static class DialogLoading extends DialogFragment {

        LottieAnimationView animationView;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
           // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));

            setCancelable(false);

            return dialog;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_loading, container, false);

            animationView = (LottieAnimationView) view.findViewById(R.id.animation_view);
            animationView.setAnimation(R.raw.feed_loading);
            animationView.playAnimation();

            return view;
        }
    }


    public void closeKeyboard() {
        final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

}
