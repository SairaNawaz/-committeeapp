package com.apponative.committee_app.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.Committee;
import com.apponative.committee_app.ui.dialogs.CustomDialog;
import com.apponative.committee_app.utils.CommitteeCallBack;

public class Fragment_Create_Committee3 extends BaseFragment implements View.OnClickListener {
    View v;
    TextView c3_save, c3_back, c3_edit, c3_delete;
    CommitteeCallBack.CreateCommitteCallBacks createCommitteCallBacks;
    /*TextView committee_admin_val, committee_title_val, committee_slots_val,
            total_amount_val, payment_memeber_val, currency_val, start_date_val, end_date_val, collect_interval_val, collect_date_val, draw_mode_val;*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_create_committee_3, container, false);
        initViews();
        return v;
    }

    void initViews() {
       /* committee_admin_val = (TextView) v.findViewById(R.id.committee_admin_val);
        committee_title_val = (TextView) v.findViewById(R.id.committee_title_val);
        committee_slots_val = (TextView) v.findViewById(R.id.committee_slots_val);
        total_amount_val = (TextView) v.findViewById(R.id.total_amount_val);
        payment_memeber_val = (TextView) v.findViewById(R.id.payment_memeber_val);
        currency_val = (TextView) v.findViewById(R.id.currency_val);
        start_date_val = (TextView) v.findViewById(R.id.start_date_val);
        end_date_val = (TextView) v.findViewById(R.id.end_date_val);
        collect_interval_val = (TextView) v.findViewById(R.id.collect_interval_val);
        collect_date_val = (TextView) v.findViewById(R.id.collect_date_val);
        draw_mode_val = (TextView) v.findViewById(R.id.draw_mode_val);*/
        c3_delete = (TextView) v.findViewById(R.id.c3_delete);
        c3_delete.setOnClickListener(this);
        c3_edit = (TextView) v.findViewById(R.id.c3_edit);
        c3_edit.setOnClickListener(this);
        c3_back = (TextView) v.findViewById(R.id.c3_back);
        c3_back.setOnClickListener(this);
        c3_save = (TextView) v.findViewById(R.id.c3_save);
        c3_save.setOnClickListener(this);

        loadSummaryFragment();
    }

    void saveCommittee() {
        createCommitteCallBacks = (CommitteeCallBack.CreateCommitteCallBacks) getParentFragment();
        createCommitteCallBacks.OnC3Submit();
    }

    @Override
    public void onClick(View v) {
        if (v == c3_save) {
            saveCommittee();
        } else if (v == c3_back) {
            ((Fragment_CreateCommittee) getParentFragment()).committee_tabs.getTabAt(1).select();
        } else if (v == c3_edit) {
            ((Fragment_CreateCommittee) getParentFragment()).committee_tabs.getTabAt(0).select();
        } else if (v == c3_delete) {
            CustomDialog dialog =
                    new CustomDialog(getActivity(), new CommitteeCallBack.DialogInteractionListener() {
                        @Override
                        public void OnButton1Click(Dialog dialog) {

                        }

                        @Override
                        public void OnButton2Click(Dialog dialog) {
                            Fragment_Create_Committee1.getFragmentCreateCommittee1().setFragmentCreateCommittee1();
                            getActivity().onBackPressed();
                        }
                    });
            dialog.setContent("Delete Committee", "Are you sure you want to delete it?", "Cancel", "Ok");
            dialog.show();

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
//
//            }
//        };

//        if (mPendingRunnable != null) {
//            mHandler.post(mPendingRunnable);
//        }
    }
    public void refresh(Committee committee) {

       /* committee_admin_val.setText(committee.getAdminName());
        committee_title_val.setText(committee.getCname());
        committee_slots_val.setText(committee.getMember_count() + "");
        total_amount_val.setText(committee.getAmount() + "");
        payment_memeber_val.setText(committee.getMember_payment() + "");
        currency_val.setText(committee.getCurrency());
        start_date_val.setText(committee.getStart_date());
        end_date_val.setText(committee.getEnd_date());
        collect_date_val.setText(committee.getPayment_collection_date() + "");
        draw_mode_val.setText(committee.isAdminfirst() ? "Admin First" : "Random Draw");
        collect_interval_val.setText(committee.getInterval() == 1 ? "Daily" : committee.getInterval() == 7 ? "Weekly" : "Monthly");*/
        Fragment_Summary fSummary = (Fragment_Summary) getChildFragmentManager()
                .findFragmentByTag("summaryfragment");
        if(fSummary !=null){
            fSummary.updateBundle(committee);
        }
    }
}
