package com.av.movieshowcase.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.av.movieshowcase.AppConstants;


public class NavigationUtils implements AppConstants {


    public static void shareTextUrl(Activity activity,String title, String url) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TITLE, "Share movie");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Checkout "+title+"from MovieShowcase : "+url);
        shareIntent.setType("text/plain");
        activity.startActivity(Intent.createChooser(shareIntent, "Share "+title));

    }

    public static void browserIntent(Activity activity, String url){
        if (!url.startsWith("https://") && !url.startsWith("http://")){
            url = "http://" + url;
        }

        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }else{
            //Page not found
        }
    }


  /*  public static void redirectToMovieSearchScreen(Activity activity) {
        Intent intent = new Intent(activity, MovieSearchActivity.class);
        activity.startActivity(intent);
    }

    public static void redirectToTvSearchScreen(Activity activity) {
        Intent intent = new Intent(activity, TvSearchActivity.class);
        activity.startActivity(intent);
    }

    public static void redirectToVideoScreen(Context context,
                                             String videoKey) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(INTENT_VIDEO_KEY, videoKey);
        context.startActivity(intent);
    }


    public static void redirectToDetailScreen(Activity activity,
                                              MovieEntity movie,
                                              ActivityOptionsCompat options) {
        Intent intent = new Intent(activity, MovieDetailActivity.class);
        intent.putExtra(INTENT_MOVIE, movie);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    public static void redirectToTvDetailScreen(Activity activity,
                                                TvEntity tvEntity,
                                                ActivityOptionsCompat options) {
        Intent intent = new Intent(activity, TvDetailActivity.class);
        intent.putExtra(INTENT_MOVIE, tvEntity);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    public static void replaceFragment(Activity activity,
                                       int navId,
                                       int selectedPosition) {
        Bundle bundle = new Bundle();
        bundle.putInt(INTENT_CATEGORY, selectedPosition);
        Navigation.findNavController(activity, R.id.fragment_nav_host)
                .navigate(navId, bundle, new NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setEnterAnim(R.anim.flip_right_in)
                        .setExitAnim(R.anim.flip_right_out)
                        .setPopEnterAnim(R.anim.flip_left_in)
                        .setPopExitAnim(R.anim.flip_left_out)
                        .build());
    }*/
}
