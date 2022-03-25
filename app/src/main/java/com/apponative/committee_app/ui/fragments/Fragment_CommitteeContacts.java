package com.apponative.committee_app.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apponative.committee_app.R;
import com.apponative.committee_app.ui.adapters.ContactAdapter;
import com.apponative.committee_app.ui.adapters.JoinedContactAdapter;
import com.apponative.committee_app.utils.CommitteeCallBack;

public class Fragment_CommitteeContacts extends BaseFragment {
    View v;
    RecyclerView contact_list;
    JoinedContactAdapter joinedcontactAdapter;
    ContactAdapter contactAdapter;
    Bundle b;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_contacts, container, false);
        initViews();
        return v;
    }

    void initViews() {

        b = getmArguments();
        contact_list = (RecyclerView) v.findViewById(R.id.contact_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        contact_list.setLayoutManager(layoutManager);

        ViewCompat.setNestedScrollingEnabled(contact_list,false);
        if (b != null && b.containsKey("Case")) {
            joinedcontactAdapter = new JoinedContactAdapter(getActivity(), b.getString("Case"), b.getString("CID"),
                    b.getBoolean("selectable"),
                    b.getString("cname"),(CommitteeCallBack.CommitteeUpdateCallBacks) getParentFragment());
            joinedcontactAdapter.getContactsByCase();
            contact_list.setAdapter(joinedcontactAdapter);
        } else {
            contactAdapter = new ContactAdapter(getActivity(), false);
            contactAdapter.getContactsByCase();
            contact_list.setAdapter(contactAdapter);
        }
    }
}
