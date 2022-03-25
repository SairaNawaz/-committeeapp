package com.apponative.committee_app.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Muhammad Waqas on 8/15/2017.
 */

public class BaseFragment extends Fragment {

    Bundle mArguments;

    public Bundle getmArguments() {
        if (mArguments == null) {
            mArguments = new Bundle();
        }
        return mArguments;
    }

    public void setmArguments(Bundle mArguments) {
        this.mArguments = mArguments;
    }
}
