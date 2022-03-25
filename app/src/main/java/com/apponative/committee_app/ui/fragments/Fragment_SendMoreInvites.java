package com.apponative.committee_app.ui.fragments;

import android.content.Intent;
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
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.Committee;
import com.apponative.committee_app.datamodles.Notification;
import com.apponative.committee_app.firebase.FireBaseDbHandler;
import com.apponative.committee_app.ui.MainActivity;
import com.apponative.committee_app.ui.adapters.ContactAdapter;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;

public class Fragment_SendMoreInvites extends BaseFragment
        implements View.OnClickListener, CommitteeCallBack.InvitesDelegate, TextWatcher {

    View v;
    RecyclerView contact_list;
    ContactAdapter contactAdapter;
    TextView moreinvite_send;

    ImageView share_committee;
    EditText search_contact;
    ImageView search_image_icon;
    String selectedContacts;
    Committee committee;
    Bundle b;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_sendmoreinvites, container, false);
        initViews();
        return v;
    }

    void initViews() {
        b = getmArguments();
        committee = b.getParcelable("committee");

        moreinvite_send = (TextView) v.findViewById(R.id.moreinvite_send);
        moreinvite_send.setOnClickListener(this);
        contact_list = (RecyclerView) v.findViewById(R.id.contact_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        contact_list.setLayoutManager(layoutManager);
        contactAdapter = new ContactAdapter(getActivity(), true);
        contactAdapter.getContactsByCase();
        contact_list.setAdapter(contactAdapter);
        share_committee = (ImageView) v.findViewById(R.id.share_committee);
        share_committee.setOnClickListener(this);
        search_contact = (EditText) v.findViewById(R.id.search_contact);
        search_contact.addTextChangedListener(this);
        search_image_icon = (ImageView) v.findViewById(R.id.search_imag_icon);
        search_image_icon.setOnClickListener(this);

    }


    void launchShareIntent() {
        Intent intent2 = new Intent();
        intent2.setAction(Intent.ACTION_SEND);
        intent2.setType("text/plain");
        intent2.putExtra(Intent.EXTRA_TEXT, Constants.SHARE_APP);
        startActivity(Intent.createChooser(intent2, "Share via"));
    }

    void sendInvites() {
        selectedContacts = contactAdapter.getSelectedItems();
        Notification notification = new Notification();
        notification.setType(Constants.NTYPE.INVITE);
        notification.setSentFrom(MainActivity.signedInUser.getUsername());
        notification.setSentTo(selectedContacts);
        notification.setTitle(committee.getCname());
        notification.setMessage("Join " + committee.getC_desc());
        notification.setAdmin(committee.getAdmin());
        notification.setCid(committee.getCid());
        notification.setDesc(committee.getC_desc());
        FireBaseDbHandler.getDbHandler(getActivity()).sendNotificationToUser(this, notification);
    }

    @Override
    public void onClick(View v) {
        if (v == moreinvite_send) {
            sendInvites();
        } else if (v == share_committee) {
            launchShareIntent();
        } else if (v == search_image_icon) {
            contactAdapter.getFilter().filter(search_contact.getText());
        }
    }

    @Override
    public void OnInvitesSent() {
        getActivity().onBackPressed();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        contactAdapter.getFilter().filter(s);
    }
}
