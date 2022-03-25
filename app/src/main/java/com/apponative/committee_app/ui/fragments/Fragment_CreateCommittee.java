package com.apponative.committee_app.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.Committee;
import com.apponative.committee_app.datamodles.CommitteeReference;
import com.apponative.committee_app.datamodles.Notification;
import com.apponative.committee_app.firebase.FireBaseDbHandler;
import com.apponative.committee_app.ui.MainActivity;
import com.apponative.committee_app.ui.adapters.TabbedPagerAdapter;
import com.apponative.committee_app.ui.custom.CustomViewPager;
import com.apponative.committee_app.ui.dialogs.CustomDialog;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;
import com.apponative.committee_app.utils.DateUtils;
import com.apponative.committee_app.utils.TabLayoutUtils;

import java.util.ArrayList;
import java.util.List;

public class Fragment_CreateCommittee extends BaseFragment implements CommitteeCallBack.CreateCommitteCallBacks, CommitteeCallBack.InvitesDelegate {
    View v;

    CustomViewPager create_committee_steps;
    TabLayout committee_tabs;
    Committee committee;
    String selectedContacts;
    TabbedPagerAdapter tabbedadapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_createcommittee, container, false);
        initViews();
        return v;
    }

    void initViews() {
        create_committee_steps = (CustomViewPager) v.findViewById(R.id.create_committee_steps);
        create_committee_steps.canSwipe(false);
        committee_tabs = (TabLayout) v.findViewById(R.id.committee_tabs);
        setupViewPager(create_committee_steps);
        committee_tabs.setupWithViewPager(create_committee_steps);
        TabLayoutUtils.enableTabs(committee_tabs, false);
    }

    private void setupViewPager(CustomViewPager viewPager) {
        tabbedadapter = new TabbedPagerAdapter(getChildFragmentManager());
        tabbedadapter.addFragments(getFragments(), getFragmentsTitle());
        viewPager.setAdapter(tabbedadapter);
    }

    List<BaseFragment> getFragments() {
        List<BaseFragment> fList = new ArrayList<>();

        //  fList.add(new Fragment_Create_Committee1());
        fList.add(Fragment_Create_Committee1.getFragmentCreateCommittee1());
        fList.add(new Fragment_Create_Committee2());
        fList.add(new Fragment_Create_Committee3());
        return fList;
    }

    List<String> getFragmentsTitle() {
        List<String> fListTitle = new ArrayList<>();
        fListTitle.add(getResources().getString(R.string.enter_info));
        fListTitle.add(getResources().getString(R.string.invites));
        fListTitle.add(getResources().getString(R.string.summary));
        return fListTitle;
    }

    @Override
    public void OnC1Next(Committee committee) {
        committee.setEnd_date(DateUtils.addDaysToDate(DateUtils.getStringInDate(committee.getStart_date())
                , committee.getInterval(), committee.getMember_count()));
        this.committee = committee;
        committee_tabs.getTabAt(1).select();
    }

    @Override
    public void OnC2Next(String selectedContacts) {
        committee_tabs.getTabAt(2).select();
        this.selectedContacts = selectedContacts;
        ((Fragment_Create_Committee3) tabbedadapter.getFragmentItem(2)).refresh(committee);
    }

    @Override
    public void OnC3Submit() {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        FireBaseDbHandler.getDbHandler(getActivity()).createCommittee(committee, this);
    }

    @Override
    public void OnCommitteeCreated(String key, String admin, String description) {

        FireBaseDbHandler.getDbHandler(getActivity()).updateCommitteeStatus(this, Constants.PENDING, key, new CommitteeReference(MainActivity.signedInUser.getUserId(), description));
    }

    @Override
    public void OnCommitteeAdminCreated(CommitteeReference committeeReference) {
        Notification notification = new Notification();
        notification.setSentFrom(MainActivity.signedInUser.getUsername());
        notification.setSentTo(selectedContacts);
        notification.setTitle(committee.getCname());
        notification.setMessage("Join " + committeeReference.getDescription());
        notification.setAdmin(committeeReference.getAdmin());
        notification.setCid(committeeReference.getCid());
        notification.setDesc(committeeReference.getDescription());
        notification.setType(Constants.NTYPE.INVITE);
        FireBaseDbHandler.getDbHandler(getActivity()).sendNotificationToUser(this, notification);
    }

    @Override
    public void OnInvitesSent() {
        MainActivity.progressBar.setVisibility(View.GONE);
        Fragment_Create_Committee1.getFragmentCreateCommittee1().setFragmentCreateCommittee1();
        CustomDialog dialog = new CustomDialog(getActivity(), new CommitteeCallBack.DialogInteractionListener() {
            @Override
            public void OnButton1Click(Dialog dialog) {

            }

            @Override
            public void OnButton2Click(Dialog dialog) {
                getActivity().onBackPressed();
                Bundle b = new Bundle();
                b.putInt("redirection", 2);
                MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_committee, b);
            }
        });
        dialog.setContent("Committee Created", "Your Committee has been created.", "", "Ok");
        dialog.show();
    }
}
