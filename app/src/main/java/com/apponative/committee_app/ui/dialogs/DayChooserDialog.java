package com.apponative.committee_app.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.utils.CommitteeCallBack;

/**
 * Created by Muhammad Waqas on 6/8/2017.
 */

public class DayChooserDialog extends Dialog {

    TextView dtitle;
    RecyclerView daychooser;
    Context context;
    int itemType;
    String[] daylist;
    CommitteeCallBack.DialogDayChooserListener dialogDayChooserListener;

    public DayChooserDialog(@NonNull Context context, int itemType
            , CommitteeCallBack.DialogDayChooserListener dialogDayChooserListener) {
        super(context);
        this.context = context;
        this.itemType = itemType;
        this.dialogDayChooserListener = dialogDayChooserListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.daychooser_dialog);
        setViews();
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        this.getWindow().setGravity(Gravity.CENTER);
    }

    void setViews() {
        dtitle = (TextView) findViewById(R.id.dialog_title);
        daychooser = (RecyclerView) findViewById(R.id.dialog_daychooser);

        if (itemType == 2) {  //2 -> weekly
            // weekly
            daylist = context.getResources().getStringArray(R.array.daylist_weekly);
        } else if (itemType == 3) { //3 -> monthly
            daylist = context.getResources().getStringArray(R.array.daylist_monthly);
        }
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, itemType == 1 ? 3 : 4);
        daychooser.setLayoutManager(layoutManager);
        daychooser.setAdapter(new DayChooserAdapter());
    }

    @Override
    public void show() {
        if (!isShowing()) {
            super.show();
        }
    }

    class DayChooserAdapter extends RecyclerView.Adapter<DayChooserAdapter.ViewHolder> {


        @Override
        public DayChooserAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_daychooser, viewGroup, false);

            return new DayChooserAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DayChooserAdapter.ViewHolder viewHolder, int i) {
            viewHolder.day_item.setText(daylist[i]);
        }

        @Override
        public int getItemCount() {
            return daylist.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView day_item;

            public ViewHolder(View view) {
                super(view);
                view.setOnClickListener(this);
                day_item = (TextView) view.findViewById(R.id.day_item);
            }

            @Override
            public void onClick(View view) {
                dialogDayChooserListener.OnDaySelected(daylist[getAdapterPosition()]);
                dismiss();
            }
        }
    }
}
