package com.av.movieshowcase.data.callbacks;

import com.av.movieshowcase.data.remote.model.PersonResponse;

/**
 * Created by aveliu2 on 31,January,2020
 */
public interface PersonCallback {
    void showPersonResponse(PersonResponse response);
    void showError(String message);
}
