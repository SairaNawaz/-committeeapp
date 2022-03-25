package com.apponative.committee_app.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.Committee;
import com.apponative.committee_app.datamodles.CommitteeReference;
import com.apponative.committee_app.firebase.FireBaseDbHandler;
import com.apponative.committee_app.ui.MainActivity;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

/**
 * Created by Muhammad Waqas on 8/18/2016.
 */
public class CommitteeAdapter extends RecyclerView.Adapter<CommitteeAdapter.ViewHolder> implements CommitteeCallBack.QueryResultDelegate {


    ArrayList<CommitteeReference> committees = new ArrayList<>();
    String ctype;
    Context context;
    boolean isAskedDetail = false;
    int itemClicked;

    public CommitteeAdapter(Context context, String ctype) {
        this.ctype = ctype;
        this.context = context;
    }

    public void getCommitteeByStatus() {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        FireBaseDbHandler.getDbHandler(context).getCommitteesByStatus(ctype, this);
    }

    @Override
    public CommitteeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_committee, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommitteeAdapter.ViewHolder viewHolder, int i) {

        CommitteeReference committee = committees.get(i);
        if (committee.getDescription() != null) {
            String[] desc = committee.getDescription().split("\n");
            viewHolder.committeeTitle.setText(desc[0]);
            viewHolder.committeeMembers.setText(desc[1]);
            viewHolder.committeeAmount.setText(desc[2]);
        }
    }

    @Override
    public int getItemCount() {
        return committees.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView committeeTitle, committeeMembers, committeeAmount;

        public ViewHolder(View view) {
            super(view);
            committeeTitle = (TextView) view.findViewById(R.id.committee_title);
            committeeMembers = (TextView) view.findViewById(R.id.committee_members);
            committeeAmount = (TextView) view.findViewById(R.id.committee_amount);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            isAskedDetail = true;
            itemClicked = getAdapterPosition();
            getCommitteeDetails(committees.get(itemClicked).getCid());
        }
    }

    @Override
    public void OnQueryResult(DataSnapshot dataSnapshot, String queryRef) {
        if (dataSnapshot.exists()) {
            if (isAskedDetail) {
                Committee committee = dataSnapshot.getValue(Committee.class);
                setCommitteeDetails(dataSnapshot.getKey(), committee);

            } else {
                committees.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    CommitteeReference committeeReference = dataSnapshot1.getValue(CommitteeReference.class);
                    committeeReference.setCid(dataSnapshot1.getKey());
                    committees.add(committeeReference);
                }
                notifyDataSetChanged();
            }
        }
        MainActivity.progressBar.setVisibility(View.GONE);
    }

    void getCommitteeDetails(String key) {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        FireBaseDbHandler.getDbHandler(context).getCommitteeDetails(key, this);
    }

    void setCommitteeDetails(String key, Committee committee) {
        isAskedDetail = false;
        Bundle b = new Bundle();
        committee.setCid(key);
        b.putString("cid", key);

        if (ctype.equalsIgnoreCase(Constants.PENDING)) {

            if (committees.get(itemClicked).getAdmin().equalsIgnoreCase(MainActivity.signedInUser.getUserId())) {
                MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_pending_committee, b);
            } else {
                b.putBoolean("joined", true);
                MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_new_committee, b);
            }

        } else if (ctype.equalsIgnoreCase(Constants.NEWINVITES)) {
            b.putBoolean("joined", false);
            MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_new_committee, b);
        } else if (ctype.equalsIgnoreCase(Constants.ONGOING)) {
            MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_ongoing_detail, b);
        } else {
            MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_committee_detail, b);
        }
    }
}