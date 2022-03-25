package com.apponative.committee_app.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.ui.adapters.ContactAdapter;

public class Fragment_AppContacts extends BaseFragment implements View.OnClickListener, TextWatcher {

    View v;
    RecyclerView contact_list;
    ContactAdapter contactAdapter;
    EditText search_contact;
    ImageView search_image_icon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_app_contacts, container, false);
        initViews();
        return v;
    }

    void initViews() {

        contact_list = (RecyclerView) v.findViewById(R.id.contact_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        contact_list.setLayoutManager(layoutManager);
        contactAdapter = new ContactAdapter(getActivity(), false);
        contactAdapter.getContactsByCase();
        contact_list.setAdapter(contactAdapter);
        search_contact = (EditText) v.findViewById(R.id.search_contact);
        search_contact.addTextChangedListener(this);
        search_image_icon = (ImageView) v.findViewById(R.id.search_imag_icon);
        search_image_icon.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        contactAdapter.getFilter().filter(search_contact.getText());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
