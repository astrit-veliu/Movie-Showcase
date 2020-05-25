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

}
