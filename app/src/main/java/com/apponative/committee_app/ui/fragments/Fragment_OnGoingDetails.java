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
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.Committee;
import com.apponative.committee_app.datamodles.CommitteeReference;
import com.apponative.committee_app.datamodles.People;
import com.apponative.committee_app.firebase.FireBaseDbHandler;
import com.apponative.committee_app.firebase.OkkHttpBuilder;
import com.apponative.committee_app.ui.MainActivity;
import com.apponative.committee_app.ui.adapters.MembersListAdapter;
import com.apponative.committee_app.utils.CAUtility;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;
import com.apponative.committee_app.utils.DateUtils;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Fragment_OnGoingDetails extends BaseFragment
        implements View.OnClickListener, CommitteeCallBack.QueryResultDelegate, CommitteeCallBack.CommitteeCompleteCallBack {

    View v;
    TextView btn_complete;
    Committee committee;
    Bundle b;

    //    TextView committee_admin_val, committee_title_val, committee_slots_val,
//            total_amount_val, payment_per_member, currency_val, collect_interval_val, start_date_val, end_date_val,
//            collect_mode_val, collect_date_val, draw_mode_val;
    String cid;
    RecyclerView particpant_list;
    MembersListAdapter mebersAdapter;
//    Handler mHandler;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_ongoing_details, container, false);
        initViews();
        return v;
    }

    void initViews() {
        b = getmArguments();
        cid = b.getString("cid");

        btn_complete = (TextView) v.findViewById(R.id.btn_complete);
        btn_complete.setOnClickListener(this);

//        committee_admin_val = (TextView) v.findViewById(R.id.committee_admin_val);
//        committee_title_val = (TextView) v.findViewById(R.id.committee_title_val);
//        committee_slots_val = (TextView) v.findViewById(R.id.committee_slots_val);
//        total_amount_val = (TextView) v.findViewById(R.id.total_amount_val);
//        payment_per_member = (TextView) v.findViewById(R.id.payment_per_member);
//        currency_val = (TextView) v.findViewById(R.id.currency_val);
//        start_date_val = (TextView) v.findViewById(R.id.start_date_val);
//        end_date_val = (TextView) v.findViewById(R.id.end_date_val);
//        collect_date_val = (TextView) v.findViewById(R.id.collect_date_val);
//        draw_mode_val = (TextView) v.findViewById(R.id.draw_mode_val);
//        collect_mode_val = (TextView) v.findViewById(R.id.collection_mode_val);
//        collect_interval_val = (TextView) v.findViewById(R.id.collect_interval_val);

 //       mHandler = new Handler();

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
                fragmentTransaction.add(R.id.container_summary, fragment, getResources().getString(R.string.tag_summary));
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
//        collect_mode_val.setText(committee.getPayment_collection_mode() == 0 ? "By Admin" : "By Members");
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
        if(fSummary !=null){
            fSummary.updateBundle(committee);
        }
        FireBaseDbHandler.getDbHandler(getActivity()).getMembersbyCommitteeKey(committee.getCid(), this);
    }

    void updateMembers(ArrayList<People> members) {
        mebersAdapter = new MembersListAdapter(getActivity(), committee, members, this);
        particpant_list.setAdapter(mebersAdapter);
    }

    void completeCommittee() {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        ArrayList<String> membersid = new ArrayList<>(Lists.transform(new ArrayList<>
                (committee.getMembers_confirmed().values()), new Function<People, String>() {
            @javax.annotation.Nullable
            @Override
            public String apply(@javax.annotation.Nullable People input) {
                return input.getContactNumber();
            }
        }));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("members", new Gson().toJson(membersid));
            jsonObject.put("cid", committee.getCid());
            jsonObject.put("committeedetails", new Gson().toJson(new CommitteeReference(committee.getAdmin(), committee.getC_desc())));
            jsonObject.put("sender", MainActivity.signedInUser.getUserId());
            jsonObject.put("type", Constants.NTYPE.COMPLETED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            OkkHttpBuilder.getInstance(getActivity()).completeCommittee(jsonObject.toString(), this);
        } catch (Exception e) {
            MainActivity.progressBar.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn_complete) {
            completeCommittee();
        }
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


    @Override
    public void OnAllMembersTurned(boolean AllTurned) {
        boolean isAdmin = committee.getAdmin().equalsIgnoreCase(MainActivity.signedInUser.getUserId());
        btn_complete.setVisibility((AllTurned && committee.getStatus() == 0
                && isAdmin) ? View.VISIBLE : View.GONE);
        btn_complete.setEnabled(AllTurned);
    }

    @Override
    public void OnCommitteeComplete(boolean result) {
        MainActivity.progressBar.setVisibility(View.GONE);
        try {
            if (result) {
                MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_committee, null);
            } else {
                CAUtility.showGeneralAlert(getActivity(),
                        "No Internet", "Please Check Your internet Connection and try again", "", "Ok");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
