package com.apponative.committee_app.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.People;
import com.apponative.committee_app.ui.custom.CircleTransform;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;
import com.apponative.committee_app.utils.DateUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Muhammad Waqas on 8/18/2016.
 */
public class RandomMembersAdapter extends RecyclerView.Adapter<RandomMembersAdapter.ContactViewHolder>
        implements CommitteeCallBack.QueryResultDelegate {

    private ArrayList<People> memberList = new ArrayList<>();
    private int interval, turndays;
    private Date startdate;
    Context context;

    public RandomMembersAdapter(Context context, ArrayList<People> memberList, Date startdate, int interval) {
        this.memberList = memberList;
        this.interval = interval;
        this.startdate = startdate;
        this.context = context;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        People contactVO = memberList.get(position);
        contactVO.setTurn(DateUtils.addDaysToDate(startdate, interval, position + 1));
        holder.tvContactName.setText(contactVO.getContactName());
        holder.tvPhoneNumber.setText(contactVO.getContactNumber());
        holder.tvTurn.setText(position + 1 + ".");
        holder.tvTurnDate.setText("Payment Date: "+contactVO.getTurn());

        Glide.with(context).using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference().child(Constants.ST_PP + contactVO.getContactNumber() + Constants.PP_TYPE))
                .transform(new CircleTransform(context))
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.ivContactImage);
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    @Override
    public void OnQueryResult(DataSnapshot dataSnapshot, String queryRef) {

    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;
        TextView tvTurn;
        TextView tvTurnDate;

        public ContactViewHolder(View itemView) {
            super(itemView);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
            tvTurn = (TextView) itemView.findViewById(R.id.member_turn);
            tvTurnDate = (TextView) itemView.findViewById(R.id.turn_date);
        }
    }

}