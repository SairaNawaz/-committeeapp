package com.apponative.committee_app.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.Committee;
import com.apponative.committee_app.datamodles.People;
import com.apponative.committee_app.ui.MainActivity;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;
import com.apponative.committee_app.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Muhammad Waqas on 8/18/2016.
 */
public class MembersListAdapter extends RecyclerView.Adapter<MembersListAdapter.MemebersViewHolder> {


    private ArrayList<People> members = new ArrayList<>();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    Context context;
    String committeeId;
    Committee committee;
    CommitteeCallBack.CommitteeCompleteCallBack committeeCompleteCallBack;
    int isAllDrawn = 0;
    boolean isNameDesc;
    boolean isTurnDesc = true;

    public MembersListAdapter(Context context,
                              Committee committee, ArrayList<People> members
            , CommitteeCallBack.CommitteeCompleteCallBack committeeCompleteCallBack) {
        this.context = context;
        this.committee = committee;
        this.committeeCompleteCallBack = committeeCompleteCallBack;
        this.committeeId = committee.getCid();
        this.members = members;
    }

    @Override
    public MemebersViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if (i == TYPE_HEADER) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_header_participant, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_participant, viewGroup, false);
        }
        return new MemebersViewHolder(view, i);
    }

    public class MemebersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout members_viewholder;
        TextView member_name;
        TextView member_turn;
        TextView member_count;

        //Decorating views
        View bar_top, bar_bottom, bar_left;

        TextView header_name, header_turn;

        public MemebersViewHolder(View itemView, int itemType) {
            super(itemView);
            if (itemType != TYPE_HEADER) {
                members_viewholder = (RelativeLayout) itemView.findViewById(R.id.members_viewholder);
                members_viewholder.setOnClickListener(this);
                member_name = (TextView) itemView.findViewById(R.id.member_name);
                member_turn = (TextView) itemView.findViewById(R.id.member_turn);
                member_count = (TextView) itemView.findViewById(R.id.member_count);
                bar_top = itemView.findViewById(R.id.bar_top);
                bar_bottom = itemView.findViewById(R.id.bar_bottom);
                bar_left = itemView.findViewById(R.id.bar_left);
            } else {
                header_name = (TextView) itemView.findViewById(R.id.header_name);
                header_turn = (TextView) itemView.findViewById(R.id.header_turn);
                header_name.setOnClickListener(this);
                header_turn.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (v == header_name) {
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
            } else if (v == header_turn) {
                if (isTurnDesc) {
                    Collections.sort(members, new Comparator<People>() {
                        @Override
                        public int compare(People o1, People o2) {
                            return DateUtils.getStringInDate(o1.getTurn()).compareTo(DateUtils.getStringInDate(o2.getTurn()));
                        }
                    });
                    isTurnDesc = false;
                } else {
                    Collections.sort(members, new Comparator<People>() {
                        @Override
                        public int compare(People o1, People o2) {
                            return DateUtils.getStringInDate(o2.getTurn()).compareTo(DateUtils.getStringInDate(o1.getTurn()));
                        }
                    });
                    isTurnDesc = true;
                }
                dataSetChanged();
            } else if (v == members_viewholder) {
                Bundle b = new Bundle();
                b.putParcelable("committee", committee);
                b.putParcelable("member_detail", members.get(getAdapterPosition() - 1));
                MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_members, b);
            }
        }
    }

    @Override
    public void onBindViewHolder(final MemebersViewHolder viewHolder, int i) {
        if (i != TYPE_HEADER) {
            final People people = members.get(i - 1);
            viewHolder.member_name.setText(people.getContactName());
            viewHolder.member_turn.setText(people.getTurn());
            viewHolder.member_count.setText(i + "");
            final int turnstatus = DateUtils.turnStatus(people.getTurn(), committee.getInterval());
            if (turnstatus == Constants.PAST) {
                isAllDrawn++;
                if (isAllDrawn == members.size() && committeeCompleteCallBack != null) {
                    committeeCompleteCallBack.OnAllMembersTurned(true);
                }
            }

            if (i == 1) {
                viewHolder.bar_top.setVisibility(View.GONE);
            } else if (i == members.size()) {
                viewHolder.bar_bottom.setVisibility(View.GONE);
            }

            if (turnstatus == Constants.PAST) {
                //   viewHolder.members_viewholder.setBackgroundResource(R.drawable.view_disabled);
                setViewsSelected(viewHolder);
            } else if (turnstatus == Constants.CURRENT) {
                setViewsSelected(viewHolder);
                //  viewHolder.members_viewholder.setBackgroundResource(R.drawable.member_selected);
            } else {
                setViewsUnSelected(viewHolder);
                //   viewHolder.members_viewholder.setBackgroundResource(R.drawable.member_unselected);
            }

        }
    }

    void setViewsSelected(MemebersViewHolder viewHolder) {
        viewHolder.bar_top.setBackgroundColor(context.getResources().getColor(R.color.yellow));
        viewHolder.bar_left.setBackgroundColor(context.getResources().getColor(R.color.yellow));
        viewHolder.bar_bottom.setBackgroundColor(context.getResources().getColor(R.color.yellow));
        viewHolder.member_count.setBackgroundResource(R.drawable.members_circle_selected);
        viewHolder.member_count.setTextColor(context.getResources().getColor(R.color.white));
    }

    void setViewsUnSelected(MemebersViewHolder viewHolder) {
        viewHolder.bar_top.setBackgroundColor(context.getResources().getColor(R.color.graph_color1));
        viewHolder.bar_left.setBackgroundColor(context.getResources().getColor(R.color.graph_color1));
        viewHolder.bar_bottom.setBackgroundColor(context.getResources().getColor(R.color.graph_color1));
        viewHolder.member_count.setBackgroundResource(R.drawable.members_circle_unselected);
        viewHolder.member_count.setTextColor(context.getResources().getColor(R.color.black));
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