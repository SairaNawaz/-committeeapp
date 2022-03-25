package com.apponative.committee_app.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.utils.CommitteeCallBack;

/**
 * Created by Muhammad Waqas on 6/8/2017.
 */

public class CustomDialog extends Dialog implements View.OnClickListener {

    TextView dtitle, dmessage, dbutton1, dbutton2;
    CommitteeCallBack.DialogInteractionListener dialogInteractionListener;

    String title, message, button1, button2;

    public CustomDialog(@NonNull Context context,
                        CommitteeCallBack.DialogInteractionListener dialogInteractionListener) {
        super(context);
        this.dialogInteractionListener = dialogInteractionListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        setViews();
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        this.getWindow().setGravity(Gravity.CENTER);
        this.setCancelable(false);
    }

    void setViews() {
        dtitle = (TextView) findViewById(R.id.dialog_title);
        dmessage = (TextView) findViewById(R.id.dialog_message);
        dbutton1 = (TextView) findViewById(R.id.dialog_btn1);
        dbutton2 = (TextView) findViewById(R.id.dialog_btn2);

        dtitle.setText(title);
        dmessage.setText(message);
        if (button1.length() > 0) {
            dbutton1.setText(button1);
        } else {
            dbutton1.setVisibility(View.GONE);
        }
        if (button2.length() > 0) {
            dbutton2.setText(button2);
        } else {
            dbutton2.setVisibility(View.GONE);
        }

        dbutton1.setOnClickListener(this);
        dbutton2.setOnClickListener(this);
    }

    public void setContent(String title, String message, String button1, String button2) {

        this.title = title;
        this.message = message;
        this.button1 = button1;
        this.button2 = button2;
    }

    @Override
    public void onClick(View v) {
        if (v == dbutton1) {
            dialogInteractionListener.OnButton1Click(this);
            dismiss();
        } else if (v == dbutton2) {
            dialogInteractionListener.OnButton2Click(this);
            dismiss();
        }
    }

    @Override
    public void show() {
        if(!isShowing()){
            super.show();
        }
    }
}
