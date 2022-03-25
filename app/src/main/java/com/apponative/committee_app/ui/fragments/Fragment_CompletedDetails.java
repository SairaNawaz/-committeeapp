package com.apponative.committee_app.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.Committee;
import com.apponative.committee_app.datamodles.People;
import com.apponative.committee_app.firebase.FireBaseDbHandler;
import com.apponative.committee_app.ui.MainActivity;
import com.apponative.committee_app.ui.adapters.MembersListAdapter;
import com.apponative.committee_app.utils.CAUtility;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;
import com.apponative.committee_app.utils.DateUtils;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Fragment_CompletedDetails extends BaseFragment implements CommitteeCallBack.QueryResultDelegate {

    View v;

    Committee committee;
    Bundle b;
    RecyclerView particpant_list;
    MembersListAdapter mebersAdapter;
    //    TextView committee_admin_val, committee_title_val, committee_slots_val,
//            total_amount_val, payment_per_member, currency_val, start_date_val, end_date_val, collect_interval_val, collect_date_val, draw_mode_val;
    String cid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_completed_details, container, false);
        initViews();
        return v;
    }

    void initViews() {
        b = getmArguments();
        //   committee = b.getParcelable("Committee");
        cid = b.getString("cid");

//        committee_admin_val = (TextView) v.findViewById(R.id.committee_admin_val);
//        committee_title_val = (TextView) v.findViewById(R.id.committee_title_val);
//        committee_slots_val = (TextView) v.findViewById(R.id.committee_slots_val);
//        total_amount_val = (TextView) v.findViewById(R.id.total_amount_val);
//        payment_per_member = (TextView) v.findViewById(R.id.payment_per_member_val);
//        currency_val = (TextView) v.findViewById(R.id.currency_val);
//        start_date_val = (TextView) v.findViewById(R.id.start_date_val);
//        end_date_val = (TextView) v.findViewById(R.id.end_date_val);
//        collect_interval_val = (TextView) v.findViewById(R.id.collect_interval_val);
//        collect_date_val = (TextView) v.findViewById(R.id.collect_date_val);
//        draw_mode_val = (TextView) v.findViewById(R.id.draw_mode_val);

        loadSummaryFragment();
        CAUtility.removeNotification(getActivity(), cid);
        getCommitteeDetails(cid);

        particpant_list = (RecyclerView) v.findViewById(R.id.particpant_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        particpant_list.setLayoutManager(layoutManager);
    }

    void getCommitteeDetails(String key) {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        FireBaseDbHandler.getDbHandler(getActivity()).getCommitteeDetails(key, this);
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
        fragmentTransaction.add(R.id.container_summary, fragment,getResources().getString(R.string.tag_summary));
        fragmentTransaction.addToBackStack(backstackName);
        fragmentTransaction.commit();
//
//            }
//        };

//        if (mPendingRunnable != null) {
//            mHandler.post(mPendingRunnable);
//        }
    }

    void updateData() {
//        committee_admin_val.setText(committee.getAdminName());
//        committee_title_val.setText(committee.getCname());
//        committee_slots_val.setText(committee.getMember_count() + "");
//        total_amount_val.setText(committee.getAmount() + "");
//        payment_per_member.setText(committee.getMember_payment() + "");
//        currency_val.setText(committee.getCurrency());
//        start_date_val.setText(committee.getStart_date());
//        end_date_val.setText(committee.getEnd_date());
//        collect_date_val.setText(committee.getPayment_collection_date() + "");
//        draw_mode_val.setText(committee.isAdminfirst() ? "Admin First" : "Random Draw");
//        collect_interval_val.setText(committee.getInterval() == 1 ? "Daily" : committee.getInterval() == 7 ? "Weekly" : "Monthly");

        Fragment_Summary fSummary = (Fragment_Summary) getChildFragmentManager()
                .findFragmentByTag("summaryfragment");
        if(fSummary != null){
            fSummary.updateBundle(committee);
        }
        FireBaseDbHandler.getDbHandler(getActivity()).getMembersbyCommitteeKey(committee.getCid(), this);
    }

    void updateMembers(ArrayList<People> members) {
        mebersAdapter = new MembersListAdapter(getActivity(), committee, members, null);
        particpant_list.setAdapter(mebersAdapter);
    }

    @Override
    public void OnQueryResult(DataSnapshot dataSnapshot, String queryRef) {
        MainActivity.progressBar.setVisibility(View.GONE);
        if (dataSnapshot.exists()) {
            if (queryRef.contains(Constants.TBL_COM_MEMBERS)) {
                ArrayList<People> members = new ArrayList<>();

                DataSnapshot childSnapShot = dataSnapshot.getChildren().iterator().next();
                for (DataSnapshot memberSnapshot : childSnapShot.getChildren()) {
                    members.add(memberSnapshot.getValue(People.class));
                }
                Collections.sort(members, new Comparator<People>() {
                    @Override
                    public int compare(People o1, People o2) {
                        return DateUtils.getStringInDate(o1.getTurn()).compareTo(DateUtils.getStringInDate(o2.getTurn()));
                    }
                });
                updateMembers(members);

            } else {
                committee = dataSnapshot.getValue(Committee.class);
                committee.setCid(cid);
                updateData();
            }
        } else {
            CAUtility.showDataNoFound(getActivity());
        }

    }
}
