package com.apponative.committee_app.ui.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.Notification;
import com.apponative.committee_app.utils.CommitteeCallBack;

import java.util.ArrayList;

/**
 * Created by Muhammad Waqas on 6/8/2017.
 */

public class NotificationsDialog {

    TextView dtitle;
    RecyclerView notificationlist;
    Context context;
    ArrayList<Notification> notifications;
    CommitteeCallBack.NotificationInterfaceListener notificationInterfaceListener;
    TextView clearAllbtn;
    View v;
    PopupWindow notificationPopup;

    public NotificationsDialog(@NonNull Context context,
                               ArrayList<Notification> notifications
            , CommitteeCallBack.NotificationInterfaceListener notificationInterfaceListener) {
        //    super(context);
        this.context = context;
        this.notifications = notifications;
        this.notificationInterfaceListener = notificationInterfaceListener;
        v = LayoutInflater.from(context).inflate(R.layout.notifications_dialog, null);
        //   setContentView(v);
        setViews();
    }


    void setViews() {
        dtitle = (TextView) v.findViewById(R.id.dialog_title);
        clearAllbtn = (TextView) v.findViewById(R.id.clearbtn);
        notificationlist = (RecyclerView) v.findViewById(R.id.dialog_daychooser);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        notificationlist.setLayoutManager(layoutManager);
        notificationlist.setAdapter(new NotificationAdapter());

        clearAllbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationInterfaceListener.OnClearAllClick();
                notificationPopup.dismiss();
            }
        });
    }

    public void showPopupWindow(View anchorview) {
        notificationPopup = new PopupWindow(v, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        notificationPopup.showAsDropDown(anchorview);
    }

    public void dismissPopup() {
        notificationPopup.dismiss();
    }
    public boolean isShowing(){
        return notificationPopup.isShowing();
    }

    class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {


        @Override
        public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification, viewGroup, false);

            return new NotificationAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NotificationAdapter.ViewHolder viewHolder, int i) {
            viewHolder.notification_item.setText(notifications.get(i).getMessage());
        }

        @Override
        public int getItemCount() {
            return notifications.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView notification_item;

            public ViewHolder(View view) {
                super(view);
                view.setOnClickListener(this);
                notification_item = (TextView) view.findViewById(R.id.notification_item);
            }

            @Override
            public void onClick(View view) {
                notificationInterfaceListener.OnNotificationSelected(getAdapterPosition());
                notificationPopup.dismiss();
            }
        }
    }
}
