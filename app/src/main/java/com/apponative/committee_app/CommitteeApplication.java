package com.apponative.committee_app;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;

import com.apponative.committee_app.ui.MainActivity;
import com.crashlytics.android.Crashlytics;
import com.digits.sdk.android.Digits;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Muhammad Waqas on 5/31/2017.
 */

public class CommitteeApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {

    private static boolean isActive;
    private static Activity currentActivity;
    public static boolean isExternalIntentOpened = false;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.twitter_key), getString(R.string.twitter_secret_key));
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().withTheme(R.style.CustomDigitsTheme).build(), new Crashlytics(), new Digits.Builder().build());
    }

    public static boolean isActive() {
        return isActive;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//        if (isExternalIntentOpened) return;
//        if (activity instanceof MainActivity) {
//            isActive = true;
//            currentActivity = activity;
//        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (isExternalIntentOpened) return;
        if (activity instanceof MainActivity) {
            isActive = true;
            currentActivity = activity;
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (isExternalIntentOpened) return;
        if (activity instanceof MainActivity) {
            isActive = true;
            currentActivity = activity;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (isExternalIntentOpened) return;
        if (activity instanceof MainActivity) {
            isActive = false;
            currentActivity = null;
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (isExternalIntentOpened) return;
        if (activity instanceof MainActivity) {
            isActive = false;
            currentActivity = null;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        if (isExternalIntentOpened) return;
        if (activity instanceof MainActivity) {
            isActive = false;
            currentActivity = null;
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (isExternalIntentOpened) return;
        if (activity instanceof MainActivity) {
            isActive = false;
            currentActivity = null;
        }
    }


}
