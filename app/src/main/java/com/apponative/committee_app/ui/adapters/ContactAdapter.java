package com.apponative.committee_app.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ComparisonChain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Muhammad Waqas on 8/18/2016.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>
        implements Filterable, CommitteeCallBack.QueryResultDelegate {
    private ArrayList<People> dataList = new ArrayList<>();
    private ArrayList<People> filterList = new ArrayList<>();
    private boolean isSelectable;
    Context context;
    ArrayList<String> selectedItems = new ArrayList<>();


    public ContactAdapter(Context context, boolean isSelectable) {
        this.context = context;
        this.isSelectable = isSelectable;
    }

    public void getContactsByCase() {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        FireBaseDbHandler.getDbHandler(context).getUserContactList(this);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        final People contactVO = filterList.get(position);
        holder.tvContactName.setText(contactVO.getContactName());
        holder.tvPhoneNumber.setText(contactVO.getContactNumber());
        if (contactVO.getContactUsingApp() == 1) {
            Glide.with(context).using(new FirebaseImageLoader())
                    .load(FirebaseStorage.getInstance().getReference().child(Constants.ST_PP + contactVO.getContactNumber() + Constants.PP_TYPE))
                    .transform(new CircleTransform(context)).placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.ivContactImage);
        } else {
            Glide.with(context).load(R.drawable.placeholder)
                    .transform(new CircleTransform(context)).into(holder.ivContactImage);
        }
        if (!isSelectable) {
            holder.contact_checkbox.setVisibility(View.GONE);
        } else {
            holder.contact_checkbox.setVisibility(View.VISIBLE);
        }
        if (contactVO.getContactUsingApp() == 0) {
            holder.btn_invite.setVisibility(View.VISIBLE);
        } else if (contactVO.getContactUsingApp() == 1) {
            holder.btn_invite.setVisibility(View.GONE);
        }

        holder.btn_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchShareIntent(contactVO.getContactNumber());
            }
        });
        holder.contact_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedItems.add(contactVO.getContactNumber());
                } else {
                    selectedItems.remove(contactVO.getContactNumber());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    @Override
    public void OnQueryResult(DataSnapshot dataSnapshot, String queryRef) {
        filterList.clear();
        dataList.clear();
        if (dataSnapshot.exists()) {
            for (DataSnapshot contact : dataSnapshot.getChildren()) {
                People contactVo = contact.getValue(People.class);
                contactVo.setContactNumber(contact.getKey());
                dataList.add(contactVo);
            }
        }
        filterList.addAll(dataList);
        Collections.sort(filterList, new Comparator<People>() {
            @Override
            public int compare(People o1, People o2) {
                return ComparisonChain.start()
                        .compare(o2.getContactUsingApp(), o1.getContactUsingApp())
                        .compare(o1.getContactName().toLowerCase(), o2.getContactName().toLowerCase()).result();
            }
        });
        MainActivity.progressBar.setVisibility(View.GONE);
        notifyDataSetChanged();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

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
        }
    }

    @Override
    public Filter getFilter() {
        final Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(final CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint.length() > 0) {
                    filterList.clear();
                    filterList.addAll(Collections2.filter(dataList, new Predicate<People>() {
                        @Override
                        public boolean apply(People candidate) {
                            return candidate.getContactName().toLowerCase().contains(constraint.toString());
                        }
                    }));
                } else {
                    filterList.clear();
                    filterList.addAll(dataList);
                }

                Collections.sort(filterList, new Comparator<People>() {
                    @Override
                    public int compare(People o1, People o2) {
                        return ComparisonChain.start()
                                .compare(o2.getContactUsingApp(), o1.getContactUsingApp())
                                .compare(o1.getContactName().toLowerCase(), o2.getContactName().toLowerCase()).result();
                    }
                });

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };

        return filter;
    }

    void launchShareIntent(String number) {
    //    Uri uri = Uri.parse("smsto:" + number);
        Intent intent2 = new Intent();
        intent2.setAction(Intent.ACTION_SEND);
        intent2.setType("text/plain");
        intent2.putExtra(Intent.EXTRA_TEXT, Constants.SHARE_APP);
        context.startActivity(Intent.createChooser(intent2, "Share via"));
    }

    public String getSelectedItems() {
        return android.text.TextUtils.join(",", selectedItems);
    }
}