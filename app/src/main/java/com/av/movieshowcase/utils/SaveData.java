package com.av.movieshowcase.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Astrit on 4/1/2018.
 */

public class SaveData {

    Context ctx;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public SaveData(Context ctx1){
        this.ctx = ctx1;
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = preferences.edit();

    }

    // SharedPreferences custom methods

    public Boolean isFirstTimeSplash(){
        return preferences.getBoolean("is_first_time_splash", true);
    }

    public void setFirstTimeSplash(Boolean first_time){
        editor.putBoolean("is_first_time_splash", first_time);
        editor.commit();
    }


    public Boolean isFirstTime(){
        return preferences.getBoolean("is_first_time", true);
    }

    public void setFirstTime(Boolean first_time){
        editor.putBoolean("is_first_time", first_time);
        editor.commit();
    }

    public void saveProfileImage(String profile_image){
        editor.putString("profile_image", profile_image);
        editor.commit();
    }

    public String getProfileImage(){
        return preferences.getString("profile_image", "");
    }


    public void saveName(String name){
        editor.putString("user_name", name);
        editor.commit();
    }

    public String getName(){
        return preferences.getString("user_name", "");
    }

    public void clearAll(){
        editor.clear();
        editor.commit();

    }
}
