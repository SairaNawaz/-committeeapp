package com.apponative.committee_app.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.utils.CAUtility;
import com.apponative.committee_app.utils.Constants;

public class Fragment_Calculator extends BaseFragment implements View.OnClickListener {
    View v;
    EditText cal_val1, cal_val2;
    TextView calculate;
    TextView cal_result;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_calculator, container, false);
        initViews();
        return v;
    }

    void initViews() {
        cal_val1 = (EditText) v.findViewById(R.id.cal_val1);
        cal_val2 = (EditText) v.findViewById(R.id.cal_val2);
        calculate = (TextView) v.findViewById(R.id.calculate);
        cal_result = (TextView) v.findViewById(R.id.cal_result);

        calculate.setOnClickListener(this);
    }

    void calculate() {
        if (cal_val1.getText().length() > 0 && cal_val2.getText().length() > 0) {
            int slots = Integer.valueOf(cal_val1.getText().toString());
            int amount = Integer.valueOf(cal_val2.getText().toString());
            cal_result.setText(Constants.CAL_RES + amount / slots);
        } else {
            CAUtility.showGeneralAlert(getActivity(), "Error", Constants.INCOMPLETE_FIELDS, "", "Ok");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == calculate) {
            calculate();
        }

    }
}
