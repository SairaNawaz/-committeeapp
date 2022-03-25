package com.apponative.committee_app.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.People;
import com.apponative.committee_app.firebase.OkkHttpBuilder;
import com.apponative.committee_app.ui.MainActivity;
import com.apponative.committee_app.ui.adapters.RandomMembersAdapter;
import com.apponative.committee_app.utils.CAUtility;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;
import com.apponative.committee_app.utils.DateUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Fragment_RandomDraw extends BaseFragment implements View.OnClickListener, CommitteeCallBack.CommitteeRandomDrawCallBacks {
    View v;
    RecyclerView members_list;
    Bundle b;
    RandomMembersAdapter randomMembersAdapter;
    Handler randomHandler;
    ArrayList<People> committee_member = new ArrayList<>();
    int interval;
    TextView btn_redraw, btn_sharewithmembers;
    Date startdate;
    String committeeDetails;
    String cid;
    boolean isAdminFirst;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_randomdraw, container, false);
        initViews();
        return v;
    }

    void initViews() {
        b = getmArguments();
        interval = b.getInt("interval");
        startdate = DateUtils.getStringInDate(b.getString("startdate"));
        committeeDetails = b.getString("committeedetails");
        cid = b.getString("cid");
        isAdminFirst = b.getBoolean("isAdminFirst");
        members_list = (RecyclerView) v.findViewById(R.id.members_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        members_list.setLayoutManager(layoutManager);

        randomMembersAdapter = new RandomMembersAdapter(getActivity(), committee_member, startdate, interval);
        members_list.setAdapter(randomMembersAdapter);

        committee_member = b.getParcelableArrayList("committee_members");

        Collections.sort(committee_member, new Comparator<People>() {
            @Override
            public int compare(People o1, People o2) {
                if (o1.getContactNumber().equals(MainActivity.signedInUser.getUserId())) {
                    return (-1);
                }

                if (o2.getContactNumber().equals(MainActivity.signedInUser.getUserId())) {
                    return (1);
                }
                return (o1.getContactName().toLowerCase().compareTo(o2.getContactName().toLowerCase()));
            }
        });
        btn_redraw = (TextView) v.findViewById(R.id.btn_redraw);
        btn_redraw.setOnClickListener(this);
        btn_redraw.setEnabled(false);
        btn_sharewithmembers = (TextView) v.findViewById(R.id.btn_sharewithmembers);
        btn_sharewithmembers.setOnClickListener(this);
        btn_sharewithmembers.setEnabled(false);
        randomHandler = new Handler();
        runDraw();
    }

    void runDraw() {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        randomHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAdminFirst) {
                    Collections.shuffle(committee_member.subList(1, committee_member.size()));
                } else {
                    Collections.shuffle(committee_member);
                }
                randomMembersAdapter = new RandomMembersAdapter(getActivity(), committee_member, startdate, interval);
                members_list.setAdapter(randomMembersAdapter);
                MainActivity.progressBar.setVisibility(View.GONE);
                btn_redraw.setEnabled(true);
                btn_sharewithmembers.setEnabled(true);
            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_redraw) {
            runDraw();
        } else if (v == btn_sharewithmembers) {
            if (CAUtility.isNetworkAvailable(getActivity())) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("members", new Gson().toJson(committee_member));
                    jsonObject.put("cid", cid);
                    jsonObject.put("committeedetails", committeeDetails);
                    jsonObject.put("sender", MainActivity.signedInUser.getUserId());
                    jsonObject.put("type", Constants.NTYPE.SHARE_TURN);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    MainActivity.progressBar.setVisibility(View.VISIBLE);
                    OkkHttpBuilder.getInstance(getActivity()).shareTurnsWithMembers(jsonObject.toString(), this);
                } catch (Exception e) {
                    MainActivity.progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            } else {
                CAUtility.showGeneralAlert(getActivity(), "No Internet", "Please Check Your internet Connection and try again", "", "Ok");
            }

        }
    }

    @Override
    public void OnTurnShared(boolean result) {
        MainActivity.progressBar.setVisibility(View.GONE);
        if (result) {
            Bundle b = new Bundle();
            b.putInt("redirection", 0);
            MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_committee, b);
        } else {
            CAUtility.showGeneralAlert(getActivity(), "No Internet", "Please Check Your internet Connection and try again", "", "Ok");
        }
    }
}
