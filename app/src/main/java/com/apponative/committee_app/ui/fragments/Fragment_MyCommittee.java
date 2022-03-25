package com.apponative.committee_app.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apponative.committee_app.R;
import com.apponative.committee_app.ui.MainActivity;
import com.apponative.committee_app.ui.adapters.TabbedPagerAdapter;
import com.apponative.committee_app.ui.custom.CustomViewPager;
import com.apponative.committee_app.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class Fragment_MyCommittee extends BaseFragment {
    View v;

    CustomViewPager tabSegments;
    TabLayout tabLayout;
    Bundle bundle;
    int redirection = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_mycommittee, container, false);
        initViews();
        return v;
    }

    void initViews() {
        bundle = getmArguments();
        if (bundle != null && bundle.containsKey("redirection")) {
            redirection = bundle.getInt("redirection");
        }
        tabSegments = (CustomViewPager) v.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        setupViewPager(tabSegments);
        tabLayout.setupWithViewPager(tabSegments);

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        tabLayout.getTabAt(redirection).select();
                        MainActivity.progressBar.setVisibility(View.GONE);
                    }
                }, 100);
    }

    private void setupViewPager(CustomViewPager viewPager) {
        TabbedPagerAdapter adapter = new TabbedPagerAdapter(getChildFragmentManager());
        adapter.addFragments(getFragments(), getFragmentsTitle());
        viewPager.setAdapter(adapter);
    }

    List<BaseFragment> getFragments() {
        List<BaseFragment> fList = new ArrayList<>();
        fList.add(new Fragment_MyCommittee_Segment());
        fList.add(new Fragment_MyCommittee_Segment());
        fList.add(new Fragment_MyCommittee_Segment());
        //   fList.add(new Fragment_MyCommittee_Segment());

        Bundle b0 = new Bundle();
        b0.putString(Constants.C_TYPE, Constants.ONGOING);
        fList.get(0).setmArguments(b0);

        Bundle b1 = new Bundle();
        b1.putString(Constants.C_TYPE, Constants.NEWINVITES);
        fList.get(1).setmArguments(b1);

        Bundle b2 = new Bundle();
        b2.putString(Constants.C_TYPE, Constants.PENDING);
        b2.putString(Constants.C_TYPE1, Constants.COMPLETED);
        fList.get(2).setmArguments(b2);

//        Bundle b3 = new Bundle();
//        b3.putString(Constants.C_TYPE, Constants.COMPLETED);
//        fList.get(3).setArguments(b3);

        return fList;
    }

    List<String> getFragmentsTitle() {
        List<String> fListTitle = new ArrayList<>();
        fListTitle.add(getResources().getString(R.string.on_going));
        fListTitle.add(getResources().getString(R.string.new_invites));
        fListTitle.add(getResources().getString(R.string.pending) + " & " + getResources().getString(R.string.completed));
        //    fListTitle.add(getResources().getString(R.string.completed));
        return fListTitle;
    }
}
