package com.apponative.committee_app.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.Committee;
import com.apponative.committee_app.datamodles.Notification;
import com.apponative.committee_app.datamodles.People;
import com.apponative.committee_app.firebase.FireBaseDbHandler;
import com.apponative.committee_app.ui.MainActivity;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;
import com.apponative.committee_app.utils.DateUtils;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Muhammad Waqas on 8/18/2016.
 */
public class MembersDetailAdapter extends RecyclerView.Adapter<MembersDetailAdapter.MemebersViewHolder> implements CommitteeCallBack.QueryResultDelegate {


    private ArrayList<People> members = new ArrayList<>();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    Context context;
    String committeeId;
    Committee committee;
    int isAllDrawn = 0;
    boolean isNameDesc, isStatusDesc = false;

    People currentMember;

    public MembersDetailAdapter(Context context,
                                Committee committee, People currentMember) {
        this.context = context;
        this.committee = committee;
        this.committeeId = committee.getCid();
        this.currentMember = currentMember;

        updateData(currentMember.getTurn());
    }

    @Override
    public MemebersViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if (i == TYPE_HEADER) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_header_participantdetail, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_participantdetail, viewGroup, false);
        }
        return new MemebersViewHolder(view, i);
    }

    @Override
    public void OnQueryResult(DataSnapshot dataSnapshot, String queryRef) {
        MainActivity.progressBar.setVisibility(View.GONE);
        if (dataSnapshot != null) {
            members.clear();
            for (DataSnapshot childsnapshot : dataSnapshot.getChildren()) {
                members.add(childsnapshot.getValue(People.class));
            }

            Collections.sort(members, new Comparator<People>() {
                @Override
                public int compare(People o1, People o2) {
                    return DateUtils.getStringInDate(o1.getTurn()).compareTo(DateUtils.getStringInDate(o2.getTurn()));
                }
            });
            dataSetChanged();
        }
    }

    public class MemebersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout members_viewholder;
        TextView member_name;
        CheckBox member_paymentstatus;
        CheckBox member_reminder;

        TextView header_name, header_status;

        public MemebersViewHolder(View itemView, int itemType) {
            super(itemView);
            if (itemType != TYPE_HEADER) {
                members_viewholder = (LinearLayout) itemView.findViewById(R.id.members_viewholder);
                member_name = (TextView) itemView.findViewById(R.id.member_name);
                member_paymentstatus = (CheckBox) itemView.findViewById(R.id.member_paymentstatus);
                member_reminder = (CheckBox) itemView.findViewById(R.id.member_reminder);
                member_paymentstatus.setOnClickListener(this);
                member_reminder.setOnClickListener(this);
            } else {
                header_name = (TextView) itemView.findViewById(R.id.header_name);
                header_status = (TextView) itemView.findViewById(R.id.header_status);

                header_name.setOnClickListener(this);
                header_status.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (v == member_paymentstatus) {
                People people = members.get(getAdapterPosition() - 1);
                people.setPaymentStatus(member_paymentstatus.isChecked());
                FireBaseDbHandler.getDbHandler(context)
                        .setPaymentStatus(committeeId, committee.getCname(), people, currentMember.getTurn().replaceAll("/", "-"));

            } else if (v == member_reminder) {
                if (member_reminder.isChecked()) {
                    People people = members.get(getAdapterPosition() - 1);
                    FireBaseDbHandler.getDbHandler(context).sendGeneralNotification(new Notification(Constants.NTYPE.P_REMINDER,
                            committeeId, "Payment Reminder", "Your Payment for turn "
                            + currentMember.getTurn() + " in committee " + committee.getCname() + " is Pending",
                            MainActivity.signedInUser.getUserId(), people.getContactNumber()));
                }
            } else if (v == header_name) {
                if (isNameDesc) {
                    Collections.sort(members, new Comparator<People>() {
                        @Override
                        public int compare(People o1, People o2) {
                            return o1.getContactName().compareTo(o2.getContactName());
                        }
                    });
                    isNameDesc = false;
                } else {
                    Collections.sort(members, new Comparator<People>() {
                        @Override
                        public int compare(People o1, People o2) {
                            return o2.getContactName().compareTo(o1.getContactName());
                        }
                    });
                    isNameDesc = true;
                }
                dataSetChanged();
            } else if (v == header_status) {
                if (isStatusDesc) {
                    Collections.sort(members, new Comparator<People>() {
                        @Override
                        public int compare(People o1, People o2) {
                            return Boolean.compare(o1.isPaymentStatus(), o2.isPaymentStatus());
                        }
                    });
                    isStatusDesc = false;
                } else {
                    Collections.sort(members, new Comparator<People>() {
                        @Override
                        public int compare(People o1, People o2) {
                            return Boolean.compare(o2.isPaymentStatus(), o1.isPaymentStatus());
                        }
                    });
                    isStatusDesc = true;
                }

                dataSetChanged();
            }
        }
    }

    @Override
    public void onBindViewHolder(final MemebersViewHolder viewHolder, int i) {
        if (i != TYPE_HEADER) {
            final People people = members.get(i - 1);
            viewHolder.member_name.setText(people.getContactName());

            viewHolder.member_reminder.setChecked(false);
            viewHolder.member_paymentstatus.setChecked(people.isPaymentStatus());

            final int turnstatus
                    = DateUtils.turnStatus(people.getTurn(), committee.getInterval());

            if (turnstatus == Constants.PAST) {
                isAllDrawn++;
            }
            if (turnstatus == Constants.PAST) {
                viewHolder.members_viewholder.setBackgroundResource(R.drawable.view_disabled);
            } else if (turnstatus == Constants.CURRENT) {
                viewHolder.members_viewholder.setBackgroundResource(R.drawable.member_selected);
            } else {
                viewHolder.members_viewholder.setBackgroundResource(R.drawable.member_unselected);
            }

            //    committeeCompleteCallBack.OnAllMembersTurned((i == (this.getItemCount() - 1) && isAllDrawn == members.size()));
            //Payment mode admin
            if (committee.getPayment_collection_mode() == 0) {
                if (committee.getAdmin().equalsIgnoreCase(MainActivity.signedInUser.getUserId())) {
                    viewHolder.member_reminder.setEnabled(true);
                    viewHolder.member_paymentstatus.setEnabled(true);
                }
            } else {
                if (currentMember.getContactNumber().equalsIgnoreCase(MainActivity.signedInUser.getUserId())) {
                    viewHolder.member_reminder.setEnabled(true);
                    viewHolder.member_paymentstatus.setEnabled(true);
                } else {
                    viewHolder.member_reminder.setEnabled(false);
                    viewHolder.member_paymentstatus.setEnabled(false);
                }
            }
        }
    }

    void updateData(String currentTurn) {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        FireBaseDbHandler.getDbHandler(context)
                .getCommitteeMembersByTurn(committeeId, currentTurn.replaceAll("/", "-"), this);
    }

    private void dataSetChanged() {
        isAllDrawn = 0;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return members.size() + 1;
    }
}