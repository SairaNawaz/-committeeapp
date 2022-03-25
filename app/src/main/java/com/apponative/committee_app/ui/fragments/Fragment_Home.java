package com.apponative.committee_app.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.ui.MainActivity;

public class Fragment_Home extends BaseFragment implements View.OnClickListener {
    View v;
    ImageView home_menu1, home_menu2, home_menu3, home_menu4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_home, container, false);
        initViews();
        return v;
    }

    void initViews() {
        home_menu1 = (ImageView) v.findViewById(R.id.icon_menu1);
        home_menu2 = (ImageView) v.findViewById(R.id.icon_menu2);
        home_menu3 = (ImageView) v.findViewById(R.id.icon_menu3);
        home_menu4 = (ImageView) v.findViewById(R.id.icon_menu4);

        home_menu1.setOnClickListener(this);
        home_menu2.setOnClickListener(this);
        home_menu3.setOnClickListener(this);
        home_menu4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == home_menu1) {
            MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_create, null);
        } else if (v == home_menu2) {
            MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_committee, null);
        } else if (v == home_menu3) {
             MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_contact, null);
        } else if (v == home_menu4) {
            MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_calculator, null);
        }
    }
}
