package com.apponative.committee_app.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.utils.CAUtility;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;
import com.apponative.committee_app.utils.LocalStorageUtility;

/**
 * Created by Muhammad Waqas on 6/8/2017.
 */

public class PasscodeDialog extends Dialog implements View.OnClickListener {

    TextView dtitle, dmessage, dbutton1, dbutton2;
    EditText input_passcode, input_confirmpasscode;
    String currentPasscode;
    Context context;
    boolean isTurnedOff;

    CommitteeCallBack.DialogInteractionListener dialogInteractionListener;

    public PasscodeDialog(@NonNull Context context
            , CommitteeCallBack.DialogInteractionListener dialogInteractionListener) {
        super(context, R.style.DialogSlideAnim);
        this.context = context;
        this.dialogInteractionListener = dialogInteractionListener;
    }

    public PasscodeDialog(@NonNull Context context, boolean isTurnedOff
            , CommitteeCallBack.DialogInteractionListener dialogInteractionListener) {
        super(context, R.style.DialogSlideAnim);
        this.context = context;
        this.dialogInteractionListener = dialogInteractionListener;
        this.isTurnedOff = isTurnedOff;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.passcode_dialog);
        setViews();
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.getWindow().setGravity(Gravity.CENTER);
        this.setCancelable(false);
    }

    void setViews() {
        dtitle = (TextView) findViewById(R.id.dialog_title);
        dmessage = (TextView) findViewById(R.id.dialog_message);
        dbutton1 = (TextView) findViewById(R.id.dialog_btn1);
        dbutton2 = (TextView) findViewById(R.id.dialog_btn2);

        input_passcode = (EditText) findViewById(R.id.input_passcode);
        input_confirmpasscode = (EditText) findViewById(R.id.input_confirmpasscode);

        input_passcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    input_confirmpasscode.requestFocus();
                }
            }
        });


        currentPasscode = LocalStorageUtility.getFromSharedPref(context, Constants.PASSCODE);
        if (currentPasscode != null) {
            input_confirmpasscode.setVisibility(View.GONE);
            dbutton2.setText("Enter");
        }
        dbutton1.setOnClickListener(this);
        dbutton2.setOnClickListener(this);
    }

    void checkPassCode() {
        if (currentPasscode != null) {
            if (CAUtility.notIsEmpty(input_passcode)
                    && input_passcode.getText().toString().equalsIgnoreCase(currentPasscode)) {
                if (isTurnedOff) {
                    LocalStorageUtility.saveBoolToSharedPref(context, Constants.PROTECTED, false);
                    LocalStorageUtility.saveToSharedPref(context, Constants.PASSCODE, null);
                }
                dismiss();
            } else {
                input_passcode.setError("Please Enter a valid passcode");
            }
        } else {
            if (CAUtility.notIsEmpty(input_passcode)
                    && CAUtility.notIsEmpty(input_confirmpasscode)
                    && input_passcode.getText().toString().equalsIgnoreCase(input_confirmpasscode.getText().toString())) {
                LocalStorageUtility.saveToSharedPref(context, Constants.PASSCODE, input_passcode.getText().toString());
                LocalStorageUtility.saveBoolToSharedPref(context, Constants.PROTECTED, true);
                dismiss();
            } else {
                if (input_passcode.getText().length() < 4) {
                    input_passcode.setError("Passcode must be 4 digits");
                } else if (!input_passcode.getText().toString().equalsIgnoreCase(input_confirmpasscode.getText().toString())) {
                    input_passcode.setError("Passcode does not match");
                    input_confirmpasscode.setError("Passcode does not match");
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == dbutton1) {
            dialogInteractionListener.OnButton1Click(this);
            dismiss();
        } else if (v == dbutton2) {
            checkPassCode();
            dialogInteractionListener.OnButton2Click(this);
        }
    }

    public void clearscreen() {
        if (input_passcode != null && input_confirmpasscode != null) {
            input_passcode.setText("");
            input_confirmpasscode.setText("");

        }
    }

    @Override
    public void show() {
        if (!isShowing()) {
            super.show();
        }
    }
}
