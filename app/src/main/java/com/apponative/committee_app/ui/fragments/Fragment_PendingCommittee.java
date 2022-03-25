package com.apponative.committee_app.ui.fragments;

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
import com.apponative.committee_app.utils.CAUtility;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.DateUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fragment_PendingCommittee extends BaseFragment implements View.OnClickListener,
        CommitteeCallBack.QueryResultDelegate, CommitteeCallBack.CommitteeUpdateCallBacks {
    View v;

    WrappingViewPager pending_member_view;
    TabLayout tabs_pendingcommittee;
    Bundle b;
    Committee committee;
    /*TextView committee_admin_val, committee_title_val, committee_slots_val,
            total_amount_val, payment_memeber_val, currency_val, start_date_val, end_date_val, collect_date_val, draw_mode_val,
            invites_accepted_val, invites_remaining_val, collect_interval_val;*/
    TextView start_draw, sendMoreInvites;
    ArrayList<People> committee_members = new ArrayList<>();
    String cid;
    boolean isAskedDetails;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_pendingcommittee, container, false);
        initViews();
        return v;
    }

    void initViews() {
        b = getmArguments();
        //   committee = b.getParcelable("Committee");
        cid = b.getString("cid");
/*        committee_admin_val = (TextView) v.findViewById(R.id.committee_admin_val);
        committee_title_val = (TextView) v.findViewById(R.id.committee_title_val);
        committee_slots_val = (TextView) v.findViewById(R.id.committee_slots_val);
        invites_accepted_val = (TextView) v.findViewById(R.id.invites_accepted_val);
        invites_remaining_val = (TextView) v.findViewById(R.id.invites_remaining_val);
        total_amount_val = (TextView) v.findViewById(R.id.total_amount_val);
        draw_mode_val = (TextView) v.findViewById(R.id.draw_mode_val);
        payment_memeber_val = (TextView) v.findViewById(R.id.payment_memeber_val);
        currency_val = (TextView) v.findViewById(R.id.currency_val);
        start_date_val = (TextView) v.findViewById(R.id.start_date_val);
        end_date_val = (TextView) v.findViewById(R.id.end_date_val);
        collect_date_val = (TextView) v.findViewById(R.id.collect_date_val);
        collect_interval_val = (TextView) v.findViewById(R.id.collect_interval_val);*/

        loadSummaryFragment();

        start_draw = (TextView) v.findViewById(R.id.btn_startdraw);
        sendMoreInvites = (TextView) v.findViewById(R.id.btn_moreinvites);
        start_draw.setOnClickListener(this);
        start_draw.setEnabled(false);
        sendMoreInvites.setOnClickListener(this);
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

    void updateData() {

        /*committee_admin_val.setText(committee.getAdminName());
        committee_title_val.setText(committee.getCname());
        committee_slots_val.setText(committee.getMember_count() + "");
        invites_accepted_val.setText(0 + "");
        invites_remaining_val.setText(committee.getMember_count() + "");
        total_amount_val.setText(committee.getAmount() + "");
        draw_mode_val.setText(committee.isAdminfirst() ? "Admin First" : "Random Draw");
        payment_memeber_val.setText(committee.getMember_payment() + "");
        currency_val.setText(committee.getCurrency());
        start_date_val.setText(committee.getStart_date());
        end_date_val.setText(committee.getEnd_date());
        collect_date_val.setText(committee.getPayment_collection_date());
        collect_interval_val.setText(committee.getInterval() == 1 ? "Daily" : committee.getInterval() == 7 ? "Weekly" : "Monthly");*/

        Fragment_Summary fSummary = (Fragment_Summary) getChildFragmentManager()
                .findFragmentByTag(getResources().getString(R.string.tag_summary));
        if (fSummary != null) {
            fSummary.updateBundle(committee);
            fSummary.updateCommitteeInvites(0, committee.getMember_count());
        }

        setupViewPager(pending_member_view);
        tabs_pendingcommittee.setupWithViewPager(pending_member_view);
    }

    void updateInvites(int accepted, int remaining) {
        if (isAdded()) {
            Fragment_Summary fSummary = (Fragment_Summary) getChildFragmentManager()
                    .findFragmentByTag(getResources().getString(R.string.tag_summary));
            if (fSummary != null) {
                fSummary.updateCommitteeInvites(accepted, remaining);
            }
        }
    }

    void loadSummaryFragment() {

        final Fragment fragment = new Fragment_Summary();
//        if (b != null) {
//            fragment.setArguments(b);
//        }
        final String backstackName = fragment.getClass().getName();
//        Runnable mPendingRunnable = new Runnable() {
//            @Override
//            public void run() {

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                R.anim.slide_out_left, R.anim.slide_in_left,
                R.anim.slide_out_right);
        fragmentTransaction.add(R.id.container_summary, fragment, getResources().getString(R.string.tag_summary));
        fragmentTransaction.addToBackStack(backstackName);
        fragmentTransaction.commit();
        getChildFragmentManager().executePendingTransactions();
//
//            }
//        };

//        if (mPendingRunnable != null) {
//            mHandler.post(mPendingRunnable);
//        }
    }

    private void setupViewPager(WrappingViewPager viewPager) {
        TabbedPagerAdapter adapter = new TabbedPagerAdapter(getChildFragmentManager());
        adapter.addFragments(getFragments(), getFragmentsTitle());
        viewPager.setAdapter(adapter);
    }

    List<BaseFragment> getFragments() {
        Bundle b = new Bundle();
        Bundle b1 = new Bundle();
        List<BaseFragment> fList = new ArrayList<>();

        b.putString("Case", "Confirmed");
        b.putString("CID", committee.getCid());
        b.putBoolean("selectable", true);
        b.putString("cname", committee.getCname());
        fList.add(new Fragment_CommitteeContacts());
        fList.get(0).setmArguments(b);
        b1.putString("Case", "Joined");
        b1.putString("CID", committee.getCid());
        b1.putBoolean("selectable", true);
        b1.putString("cname", committee.getCname());
        fList.add(new Fragment_CommitteeContacts());
        fList.get(1).setmArguments(b1);

        return fList;
    }

    List<String> getFragmentsTitle() {
        List<String> fListTitle = new ArrayList<>();
        fListTitle.add(getResources().getString(R.string.confirm));
        fListTitle.add(getResources().getString(R.string.joined));
        return fListTitle;
    }

    @Override
    public void onClick(View v) {

        if (v == start_draw) {
            Bundle b = new Bundle();
            b.putParcelableArrayList("committee_members", committee_members);
            b.putInt("interval", committee.getInterval());
            b.putString("startdate", committee.getStart_date());
            b.putString("cid", committee.getCid());
            b.putBoolean("isAdminFirst", committee.isAdminfirst());
            b.putString("committeedetails", new Gson().toJson(new CommitteeReference(committee.getAdmin(), committee.getC_desc())));
            MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_randomdraw, b);
            ;
        } else if (v == sendMoreInvites) {
            sendMoreInvites();
        }
    }

    void sendMoreInvites() {
        Bundle b = new Bundle();
        b.putParcelable("committee", committee);
        MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_sendmoreinvites, b);
    }

    @Override
    public void OnQueryResult(DataSnapshot dataSnapshot, String queryRef) {
        MainActivity.progressBar.setVisibility(View.GONE);
        if (isAskedDetails) {
            if (dataSnapshot.exists()) {
                committee = dataSnapshot.getValue(Committee.class);
                committee.setCid(cid);
                updateData();
                isAskedDetails = false;
            } else {
                CAUtility.showDataNoFound(getActivity());
            }
        } else {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void OnUserUpdate(boolean isConfirmed, ArrayList<People> members) {
        String date = DateUtils.getDateInString(new Date());
        if (isConfirmed) {
//            invites_accepted_val.setText(members.size() + "");
//            invites_remaining_val.setText((committee.getMember_count() - members.size()) + "");
            updateInvites(members.size(), committee.getMember_count() - members.size());
            if (members.size() == committee.getMember_count()) {
                sendMoreInvites.setEnabled(false);
                start_draw.setEnabled(true);
                this.committee_members = members;
            } else {
                sendMoreInvites.setEnabled(true);
                start_draw.setEnabled(false);
                this.committee_members = members;
            }
        }
    }
}
