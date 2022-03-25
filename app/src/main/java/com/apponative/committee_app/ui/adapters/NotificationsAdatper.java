package com.apponative.committee_app.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.Notification;
import java.util.ArrayList;

public class NotificationsAdatper extends BaseAdapter {

    ArrayList<Notification> notifications;
    Context context;
    private final LayoutInflater mInflater;

    public NotificationsAdatper(Context context, ArrayList<Notification> notifications) {
        this.context = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.notifications = notifications;
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Notification getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.item_notification, null);
            holder.notificationmessage = (TextView) convertView.findViewById(R.id.notification_item);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.notificationmessage.setText(notifications.get(position).getMessage());
        return convertView;
    }

    public class ViewHolder {
        TextView  notificationmessage;
    }
}