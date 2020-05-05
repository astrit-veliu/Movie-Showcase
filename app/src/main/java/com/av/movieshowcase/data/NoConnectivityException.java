package com.av.movieshowcase.data;

import java.io.IOException;

/**
 * Created by aveliu2 on 03,April,2020
 */
public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No Internet Connection";
        // You can send any message whatever you want from here.
    }
}