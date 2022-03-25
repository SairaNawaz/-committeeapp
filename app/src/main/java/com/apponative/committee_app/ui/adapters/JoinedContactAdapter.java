package com.apponative.committee_app.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.People;
import com.apponative.committee_app.firebase.FireBaseDbHandler;
import com.apponative.committee_app.ui.MainActivity;
import com.apponative.committee_app.ui.custom.CircleTransform;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Muhammad Waqas on 8/18/2016.
 */
public class JoinedContactAdapter extends RecyclerView.Adapter<JoinedContactAdapter.ContactViewHolder>
        implements CommitteeCallBack.QueryResultDelegate {

    private ArrayList<People> filterList = new ArrayList<>();
    Context context;
    String cId;
    String contactCase;
    boolean isSelectable;
    String cname;
    CommitteeCallBack.CommitteeUpdateCallBacks committeeUpdateCallBacks;

    public JoinedContactAdapter(Context context, String contactCase, String cId,
                                boolean isSelectable, String cname, CommitteeCallBack.CommitteeUpdateCallBacks committeeUpdateCallBacks) {
        this.context = context;
        this.committeeUpdateCallBacks = committeeUpdateCallBacks;
        this.contactCase = contactCase;
        this.cId = cId;
        this.isSelectable = isSelectable;
        this.cname = cname;
    }

    public void getContactsByCase() {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        switch (contactCase) {
            case "Confirmed":
                FireBaseDbHandler.getDbHandler(context).getConfirmedUsers(cId, this);
                break;
            case "Joined":
                FireBaseDbHandler.getDbHandler(context).getJoinedUsers(cId, this);
                break;
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {
        final People contactVO = filterList.get(position);
        holder.tvContactName.setText(contactVO.getContactName());
        holder.tvPhoneNumber.setText(contactVO.getContactNumber());

        Glide.with(context).using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference().child(Constants.ST_PP + contactVO.getContactNumber() + Constants.PP_TYPE))
                .placeholder(R.drawable.placeholder)
                .transform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.ivContactImage);

        holder.btn_invite.setVisibility(View.GONE);

        if (isSelectable) {
            holder.contact_checkbox.setVisibility(View.VISIBLE);
        } else {
            holder.contact_checkbox.setVisibility(View.GONE);
        }

        if (contactCase.equalsIgnoreCase("Confirmed") && isSelectable) {
            if (contactVO.getContactNumber().equalsIgnoreCase(MainActivity.signedInUser.getUserId())) {
                holder.contact_checkbox.setVisibility(View.GONE);
            } else {
                holder.contact_checkbox.setVisibility(View.VISIBLE);
            }
            holder.contact_checkbox.setChecked(true);
        } else if (contactCase.equalsIgnoreCase("Joined")) {
            holder.contact_checkbox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    @Override
    public void OnQueryResult(DataSnapshot dataSnapshot, String queryRef) {
        filterList.clear();
        if (dataSnapshot.exists()) {
            for (DataSnapshot contact : dataSnapshot.getChildren()) {
                //case of joined/confirmed members
                People contactVo = contact.getValue(People.class);
                filterList.add(contactVo);
            }
        }
        if (contactCase.equalsIgnoreCase("Confirmed")) {
            committeeUpdateCallBacks.OnUserUpdate(true, filterList);
        } else {
            committeeUpdateCallBacks.OnUserUpdate(false, filterList);
        }
        Collections.sort(filterList, new Comparator<People>() {
            @Override
            public int compare(People o1, People o2) {
                return o1.getContactName().toLowerCase().compareTo(o2.getContactName().toLowerCase());
            }
        });
        MainActivity.progressBar.setVisibility(View.GONE);
        notifyDataSetChanged();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;
        CheckBox contact_checkbox;
        TextView btn_invite;

        public ContactViewHolder(View itemView) {
            super(itemView);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
            contact_checkbox = (CheckBox) itemView.findViewById(R.id.contact_checkbox);
            btn_invite = (TextView) itemView.findViewById(R.id.btn_invite);

            contact_checkbox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            People contactVO = filterList.get(getAdapterPosition());
            if (contact_checkbox.isChecked()) {
                FireBaseDbHandler.getDbHandler(context).confirmMember(cId, cname, contactVO);
            } else {
                FireBaseDbHandler.getDbHandler(context).unConfirmMember(cId, cname, contactVO);
            }
        }
    }
}