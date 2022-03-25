package com.apponative.committee_app.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.Committee;
import com.apponative.committee_app.datamodles.CommitteeReference;
import com.apponative.committee_app.datamodles.People;
import com.apponative.committee_app.firebase.FireBaseDbHandler;
import com.apponative.committee_app.ui.MainActivity;
import com.apponative.committee_app.ui.adapters.TabbedPagerAdapter;
import com.apponative.committee_app.ui.custom.WrappingViewPager;
import com.apponative.committee_app.ui.dialogs.CustomDialog;
import com.apponative.committee_app.utils.CAUtility;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Fragment_NewCommittee extends BaseFragment implements View.OnClickListener,
        CommitteeCallBack.QueryResultDelegate, CommitteeCallBack.CommitteeUpdateCallBacks {

    View v;
    WrappingViewPager pending_member_view;
    TabLayout tabs_pendingcommittee;
    Bundle b;
    Committee committee;
    TextView btn_decline, btn_join;
    boolean joined;
    String cid;
    boolean isAskedDetails;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_newcommittee, container, false);
        initViews();
        return v;
    }

    void initViews() {
        b = getmArguments();
        joined = b.getBoolean("joined");
        cid = b.getString("cid");
        loadSummaryFragment();
        btn_decline = (TextView) v.findViewById(R.id.btn_decline);
        btn_join = (TextView) v.findViewById(R.id.btn_join);
        btn_decline.setOnClickListener(this);
        btn_join.setOnClickListener(this);
        pending_member_view = (WrappingViewPager) v.findViewById(R.id.pending_member_view);
        tabs_pendingcommittee = (TabLayout) v.findViewById(R.id.tabs_pendingcommittee);

        CAUtility.removeNotification(getActivity(), cid);

        getCommitteeDetails(cid);
    }

    void getCommitteeDetails(String key) {
        isAskedDetails = true;
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        FireBaseDbHandler.getDbHandler(getActivity()).getCommitteeDetails(key, this);
    }

    private void updateData() {
        Fragment_Summary fSummary = (Fragment_Summary) getChildFragmentManager()
                .findFragmentByTag(getResources().getString(R.string.tag_summary));
        if (fSummary != null) {
            fSummary.updateBundle(committee);
            fSummary.updateCommitteeInvites(0, committee.getMember_count());
        }

        if (joined) {
            btn_join.setText(getString(R.string.joined));
            btn_join.setEnabled(false);
        } else {
            btn_join.setText("Join");
        }
        setupViewPager(pending_member_view);
        tabs_pendingcommittee.setupWithViewPager(pending_member_view);
    }

    void updateInvites(int accepted, int remaining) {
        Fragment_Summary fSummary = (Fragment_Summary) getChildFragmentManager()
                .findFragmentByTag("summaryfragment");
        if (fSummary != null) {
            fSummary.updateCommitteeInvites(accepted, remaining);
        }
    }

    void loadSummaryFragment() {

        final Fragment fragment = new Fragment_Summary();
        final String backstackName = fragment.getClass().getName();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                R.anim.slide_out_left, R.anim.slide_in_left,
                R.anim.slide_out_right);
        fragmentTransaction.add(R.id.container_summary, fragment, getResources().getString(R.string.tag_summary));
        fragmentTransaction.addToBackStack(backstackName);
        fragmentTransaction.commit();
    }

    private void setupViewPager(WrappingViewPager viewPager) {
        TabbedPagerAdapter adapter = new TabbedPagerAdapter(getChildFragmentManager());
        adapter.addFragments(getFragments(), getFragmentsTitle());
        viewPager.setAdapter(adapter);
    }

    List<BaseFragment> getFragments() {
        Bundle b = new Bundle();
        List<BaseFragment> fList = new ArrayList<>();

        b.putString("Case", "Confirmed");
        b.putString("CID", committee.getCid());
        b.putBoolean("selectable", false);
        b.putString("cname", committee.getCname());
        fList.add(new Fragment_CommitteeContacts());
        fList.get(0).setmArguments(b);


        return fList;
    }

    List<String> getFragmentsTitle() {
        List<String> fListTitle = new ArrayList<>();
        fListTitle.add(getResources().getString(R.string.joined_members));
        return fListTitle;
    }

    void declineCommittee() {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        if (joined) {
            FireBaseDbHandler.getDbHandler(getActivity()).declineCommittee(committee.getCid(), Constants.PENDING, this);
        } else {
            FireBaseDbHandler.getDbHandler(getActivity()).declineCommittee(committee.getCid(), Constants.NEWINVITES, this);
        }

    }

    void joinCommittee() {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        FireBaseDbHandler.getDbHandler(getActivity()).joinCommittee(committee.getCid(),
                new CommitteeReference(committee.getAdmin(), committee.getC_desc()), this);
    }

    void confirmDecline() {
        CustomDialog dialog =
                new CustomDialog(getActivity(), new CommitteeCallBack.DialogInteractionListener() {
                    @Override
                    public void OnButton1Click(Dialog dialog) {

                    }

                    @Override
                    public void OnButton2Click(Dialog dialog) {
                        declineCommittee();
                    }
                });
        dialog.setContent("Decline Committee", "Are you sure you want to decline it?", "Cancel", "Yes");
        dialog.show();
    }

    @Override
    public void onClick(View v) {

        if (v == btn_decline) {
            confirmDecline();
        } else if (v == btn_join) {
            joinCommittee();
        }
    }

    @Override
    public void OnQueryResult(DataSnapshot dataSnapshot, String queryRef) {
        if (isAskedDetails) {
            MainActivity.progressBar.setVisibility(View.GONE);
            if (dataSnapshot.exists()) {
                committee = dataSnapshot.getValue(Committee.class);
                committee.setCid(cid);
                updateData();
                isAskedDetails = false;
            } else {
                CAUtility.showDataNoFound(getActivity());
            }
        } else {
            Bundle b = new Bundle();
            b.putInt("redirection", 2);
            MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_committee, b);
        }
    }

    @Override
    public void OnUserUpdate(boolean isConfirmed, ArrayList<People> members) {
        if (isConfirmed) {
            updateInvites(members.size(), committee.getMember_count() - members.size());
            if (committee.getMember_count() == members.size()) {
                btn_join.setEnabled(false);
                btn_decline.setEnabled(false);
            }
        }
    }
}
