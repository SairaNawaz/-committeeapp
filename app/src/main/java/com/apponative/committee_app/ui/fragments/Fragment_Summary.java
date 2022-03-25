package com.apponative.committee_app.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.Committee;

public class Fragment_Summary extends Fragment {
    View v;
    TextView committee_admin_val, committee_title_val, committee_slots_val,
            total_amount_val, invites_accepted_val, invites_remaining_val, payment_per_member, currency_val, collect_interval_val, start_date_val, end_date_val,
            collect_mode_val, collect_date_val, draw_mode_val;
    RelativeLayout invites_cal_area;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_summary, container, false);
        setUpViews();
        return v;
    }

    void setUpViews() {
        committee_admin_val = (TextView) v.findViewById(R.id.committee_admin_val);
        committee_title_val = (TextView) v.findViewById(R.id.committee_title_val);
        committee_slots_val = (TextView) v.findViewById(R.id.committee_slots_val);
        total_amount_val = (TextView) v.findViewById(R.id.total_amount_val);
        payment_per_member = (TextView) v.findViewById(R.id.payment_per_member_val);
        currency_val = (TextView) v.findViewById(R.id.currency_val);
        start_date_val = (TextView) v.findViewById(R.id.start_date_val);
        end_date_val = (TextView) v.findViewById(R.id.end_date_val);
        collect_date_val = (TextView) v.findViewById(R.id.collect_date_val);
        draw_mode_val = (TextView) v.findViewById(R.id.draw_mode_val);
        collect_mode_val = (TextView) v.findViewById(R.id.collection_mode_val);
        collect_interval_val = (TextView) v.findViewById(R.id.collect_interval_val);

        // Remaining Invites Setup
        invites_cal_area = (RelativeLayout) v.findViewById(R.id.invites_cal_area);
        invites_cal_area.setVisibility(View.GONE);
        invites_accepted_val = (TextView) v.findViewById(R.id.invites_accepted_val);
        invites_remaining_val = (TextView) v.findViewById(R.id.invites_remaining_val);
    }

    void updateBundle(Committee committee) {
        collect_mode_val.setText(committee.getPayment_collection_mode() == 0 ? "By Admin" : "By Members");
        committee_admin_val.setText(committee.getAdminName());
        committee_title_val.setText(committee.getCname());
        committee_slots_val.setText(committee.getMember_count() + "");
        total_amount_val.setText(committee.getAmount() + "");
        payment_per_member.setText(committee.getMember_payment() + "");
        currency_val.setText(committee.getCurrency());
        start_date_val.setText(committee.getStart_date());
        end_date_val.setText(committee.getEnd_date());
        collect_date_val.setText(committee.getPayment_collection_date() + "");
        draw_mode_val.setText(committee.isAdminfirst() ? "Admin First" : "Random Draw");
        collect_interval_val.setText(committee.getInterval() == 1 ? "Daily" : committee.getInterval() == 7 ? "Weekly" : "Monthly");
    }

    void updateCommitteeInvites(int accepted, int remaining) {
        invites_cal_area.setVisibility(View.VISIBLE);
        invites_accepted_val.setText(accepted + "");
        invites_remaining_val.setText(remaining + "");
    }
}
