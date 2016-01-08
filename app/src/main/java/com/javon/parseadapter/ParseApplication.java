package com.javon.parseadapter;

import android.app.Application;

import com.parse.Parse;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 12/11/2015.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, getString(R.string.app_id), getString(R.string.client_key));
    }
}
