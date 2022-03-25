package com.apponative.committee_app.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.Committee;
import com.apponative.committee_app.datamodles.People;
import com.apponative.committee_app.ui.adapters.MembersDetailAdapter;
import com.apponative.committee_app.utils.CAUtility;

public class Fragment_Members extends BaseFragment {
    View v;
    RecyclerView particpant_list;
    MembersDetailAdapter mebersAdapter;
    Committee committee;
    Bundle b;
    TextView currentTurn;
    People currentMember;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_participants, container, false);
        initViews();
        return v;
    }

    void initViews() {
        b = getmArguments();
        committee = b.getParcelable("committee");
        currentMember = b.getParcelable("member_detail");
        currentTurn = (TextView) v.findViewById(R.id.current_turn);
        particpant_list = (RecyclerView) v.findViewById(R.id.particpant_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        particpant_list.setLayoutManager(layoutManager);
        mebersAdapter = new MembersDetailAdapter(getActivity(), committee, currentMember);
        particpant_list.setAdapter(mebersAdapter);

        currentTurn.setText(currentMember.getContactName() + "\n" + currentMember.getTurn());

        CAUtility.removeNotification(getActivity(), committee.getCid());
    }
}
