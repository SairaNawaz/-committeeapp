package com.apponative.committee_app.utils;

import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Muhammad Waqas on 5/13/2017.
 */

public class TabLayoutUtils {

    public static void enableTabs(TabLayout tabLayout, boolean enable) {
        LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
        if (tabStrip != null)
            for (int childIndex = 0; childIndex < tabStrip.getChildCount(); childIndex++) {
                tabStrip.getChildAt(childIndex).setClickable(false);
            }
    }

    public static void enableTabs(TabLayout tabLayout, int position, boolean enable) {
        getTabView(tabLayout, position).setClickable(enable);
    }

    public static View getTabView(TabLayout tabLayout, int position) {
        View tabView = null;
        ViewGroup viewGroup = getTabViewGroup(tabLayout);
        if (viewGroup != null && viewGroup.getChildCount() > position)
            tabView = viewGroup.getChildAt(position);

        return tabView;
    }

    private static ViewGroup getTabViewGroup(TabLayout tabLayout) {
        ViewGroup viewGroup = null;

        if (tabLayout != null && tabLayout.getChildCount() > 0) {
            View view = tabLayout.getChildAt(0);
            if (view != null && view instanceof ViewGroup)
                viewGroup = (ViewGroup) view;
        }
        return viewGroup;
    }

}