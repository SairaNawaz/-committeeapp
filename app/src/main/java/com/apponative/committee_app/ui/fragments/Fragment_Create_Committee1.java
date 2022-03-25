package com.apponative.committee_app.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.Committee;
import com.apponative.committee_app.datamodles.People;
import com.apponative.committee_app.ui.MainActivity;
import com.apponative.committee_app.ui.custom.NothingSelectedSpinnerAdapter;
import com.apponative.committee_app.ui.dialogs.DayChooserDialog;
import com.apponative.committee_app.utils.CAUtility;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;
import com.apponative.committee_app.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Fragment_Create_Committee1 extends BaseFragment implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener {
    View v;

    EditText input_title, input_membercount,
            input_memberpayment, input_totalamount, collection_dateof, input_startdate;
    RadioGroup selector_mode;
    TextView c1_next, c1_clear;
    CommitteeCallBack.CreateCommitteCallBacks createCommitteCallBacks;
    CheckBox admin_first_check;
    Spinner currencylist, intervallist;
    DatePickerDialog datePickerDialog;
    Calendar calendar;
    static Fragment_Create_Committee1 fragment_create_committee1;
    boolean savedInstance = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_create_committee_1, container, false);

        initViews();

        if (savedInstanceState != null) {
            setSaveInterval(savedInstanceState.getInt("intervalSelected"), savedInstanceState.getString("intervalName"));
        }
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!savedInstance) {
            outState.putInt("intervalSelected", intervallist.getSelectedItemPosition());
            outState.putString("intervalName", collection_dateof.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    public static Fragment_Create_Committee1 getFragmentCreateCommittee1() {
        if (fragment_create_committee1 == null) {
            fragment_create_committee1 = new Fragment_Create_Committee1();
        }
        return fragment_create_committee1;
    }

    public void setFragmentCreateCommittee1() {
        fragment_create_committee1 = null;
    }

    void initViews() {
        input_title = (EditText) v.findViewById(R.id.input_title);
        input_membercount = (EditText) v.findViewById(R.id.input_membercount);
        input_memberpayment = (EditText) v.findViewById(R.id.input_memberpayment);
        input_totalamount = (EditText) v.findViewById(R.id.input_totalamount);
        input_startdate = (EditText) v.findViewById(R.id.input_startdate);
        input_startdate.setOnClickListener(this);
        collection_dateof = (EditText) v.findViewById(R.id.collection_dateof);
        collection_dateof.setOnClickListener(this);
        selector_mode = (RadioGroup) v.findViewById(R.id.selector_mode);
        c1_next = (TextView) v.findViewById(R.id.c1_next);
        c1_next.setOnClickListener(this);
        c1_clear = (TextView) v.findViewById(R.id.c1_clear);
        c1_clear.setOnClickListener(this);
        admin_first_check = (CheckBox) v.findViewById(R.id.admin_first_check);
        intervallist = (Spinner) v.findViewById(R.id.ineterval_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.interval_list,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intervallist.setPrompt("Select Interval");

        intervallist.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_row_nothing_selected,
                        R.layout.spinner_nothing_selected_dropdown, // Optional
                        getActivity()));
        intervallist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    collection_dateof.setText("Daily");
                    collection_dateof.setEnabled(false);
                } else if (position == 2) {
                    collection_dateof.setText("");
                    collection_dateof.setHint(getResources().getString(R.string.collection_day));
                    collection_dateof.setEnabled(true);
                } else {
                    collection_dateof.setText("");
                    collection_dateof.setHint(getResources().getString(R.string.choose_collection_date));
                    collection_dateof.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        currencylist = (Spinner) v.findViewById(R.id.currency_spinner);

        calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        input_totalamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && input_membercount.getText().length() > 0) {
                    input_memberpayment.setText(Integer.parseInt(s.toString()) / Integer.parseInt(input_membercount.getText().toString()) + "");
                    input_memberpayment.setError(null);
                }
            }
        });

        input_membercount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && input_totalamount.getText().length() > 0) {
                    input_memberpayment.setText(Integer.parseInt(input_totalamount.getText().toString()) / Integer.parseInt(s.toString()) + "");
                    input_memberpayment.setError(null);
                }
            }
        });
    }

    void setSaveInterval(int interval, String intervalval) {
        intervallist.setSelection(interval);
        collection_dateof.setText(intervalval);
    }

    void validationCheck() {

        if (CAUtility.notIsEmpty(input_title) && CAUtility.notIsEmpty(input_memberpayment) && CAUtility.notIsEmpty(input_membercount)
                && CAUtility.notIsEmpty(input_totalamount)
                && CAUtility.notIsEmpty(collection_dateof)
                && CAUtility.notIsEmpty(input_startdate)) {
            getFormData();
        } else {
            if (!CAUtility.notIsEmpty(input_title)) {
                input_title.setError(Constants.INCOMPLETE_FIELDS);
            }
            if (!CAUtility.notIsEmpty(input_memberpayment)) {
                input_memberpayment.setError(Constants.INCOMPLETE_FIELDS);
            }
            if (!CAUtility.notIsEmpty(input_membercount)) {
                input_membercount.setError(Constants.INCOMPLETE_FIELDS);
            }
            if (!CAUtility.notIsEmpty(input_totalamount)) {
                input_totalamount.setError(Constants.INCOMPLETE_FIELDS);
            }
            if (!CAUtility.notIsEmpty(collection_dateof)) {
                collection_dateof.setError(Constants.INCOMPLETE_FIELDS);
            }
            if (!CAUtility.notIsEmpty(input_startdate)) {
                input_startdate.setError(Constants.INCOMPLETE_FIELDS);
            }
        }
    }

    void getFormData() {

        createCommitteCallBacks = (CommitteeCallBack.CreateCommitteCallBacks) getParentFragment();
        Committee committee = new Committee();
        committee.setAdmin(MainActivity.signedInUser.getUserId());
        committee.setAdminName(MainActivity.signedInUser.getUsername());
        committee.setCname(input_title.getText().toString());
        committee.setMember_payment(Integer.valueOf(input_memberpayment.getText().toString()));
        committee.setMember_count(Integer.valueOf(input_membercount.getText().toString()));
        committee.setAmount(Integer.valueOf(input_totalamount.getText().toString()));
        String interval = intervallist.getSelectedItem().toString();
        if (interval.equalsIgnoreCase("daily")) {
            committee.setInterval(1);
        } else if (interval.equalsIgnoreCase("weekly")) {
            committee.setInterval(7);
        } else if (interval.equalsIgnoreCase("monthly")) {
            committee.setInterval(30);
        }

        committee.setCurrency(currencylist.getSelectedItem().toString());
        committee.setStatus(0);
        committee.setStart_date(input_startdate.getText().toString());
        committee.setCreate_date(new Date());
        committee.setPayment_collection_date(collection_dateof.getText().toString());
        RadioButton checkedOption = (RadioButton) v.findViewById(selector_mode.getCheckedRadioButtonId());
        committee.setPayment_collection_mode(selector_mode.indexOfChild(checkedOption));
        committee.setAdminfirst(admin_first_check.isChecked());
        committee.setC_desc(committee.getCname() + "\nMembers " + committee.getMember_count()
                + "\n" + committee.getCurrency() + " " + committee.getAmount());
        HashMap<String, People> membersconfirmed = new HashMap<>();
        membersconfirmed.put(committee.getAdmin(), new People(committee.getAdmin(), MainActivity.signedInUser.getUsername()));
        committee.setMembers_confirmed(membersconfirmed);
        createCommitteCallBacks.OnC1Next(committee);
    }

    void clearAllFields() {
        input_title.setText("");
        input_membercount.setText("");
        input_memberpayment.setText("");
        input_totalamount.setText("");
        input_startdate.setText("");
        intervallist.setSelection(0);
        currencylist.setSelection(0);
        admin_first_check.setChecked(false);
        selector_mode.check(R.id.byadmin);
    }

    void showDayChooser() {
        if(intervallist.getSelectedItemId() >0){
            DayChooserDialog dayChooserDialog = new DayChooserDialog(getActivity(),
                    intervallist.getSelectedItemPosition(), new CommitteeCallBack.DialogDayChooserListener() {
                @Override
                public void OnDaySelected(String dayid) {
                    collection_dateof.setText(dayid);
                }
            });
            dayChooserDialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == c1_next) {
            validationCheck();
        } else if (v == input_startdate) {
            datePickerDialog.show();
        } else if (v == collection_dateof) {
            showDayChooser();
        } else if (v == c1_clear) {
            clearAllFields();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //    input_startdate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        input_startdate.setText(DateUtils.getStringInFormatedDateString(dayOfMonth + "/" + (month + 1) + "/" + year));
        input_startdate.setError(null);
    }

    @Override
    public void onResume() {
        if (input_title != null) {
            input_title.requestFocus();
        }
        super.onResume();
    }
}
