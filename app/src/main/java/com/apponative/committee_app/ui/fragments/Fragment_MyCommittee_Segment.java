package com.apponative.committee_app.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.apponative.committee_app.R;
import com.apponative.committee_app.ui.adapters.CommitteeAdapter;
import com.apponative.committee_app.utils.Constants;

/**
 * Created by Muhammad Waqas on 4/25/2017.
 */

public class Fragment_MyCommittee_Segment extends BaseFragment {

    RecyclerView committee_list, committee_list_completed;
    RelativeLayout completed_area;
    View v;
    CommitteeAdapter committeeAdapter1, committeeAdapter2;
    Bundle arguments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mycommittee_segment, container, false);
        initViews();
        return v;
    }

    void initViews() {
        arguments = getmArguments();
        committee_list = (RecyclerView) v.findViewById(R.id.committee_list);
        committee_list_completed = (RecyclerView) v.findViewById(R.id.committee_list_completed);
        completed_area = (RelativeLayout) v.findViewById(R.id.completedarea);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        committee_list.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        committee_list_completed.setLayoutManager(layoutManager1);

        committeeAdapter1 = new CommitteeAdapter(getActivity(), arguments.getString(Constants.C_TYPE));
        committeeAdapter1.getCommitteeByStatus();
        committee_list.setAdapter(committeeAdapter1);

        if (arguments.containsKey(Constants.C_TYPE1)) {
            committeeAdapter2 = new CommitteeAdapter(getActivity(), arguments.getString(Constants.C_TYPE1));
            committeeAdapter2.getCommitteeByStatus();
            committee_list_completed.setAdapter(committeeAdapter2);
        } else {
            completed_area.setVisibility(View.GONE);
        }
    }
}