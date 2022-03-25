package com.apponative.committee_app.ui.fragments;

import android.app.Dialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.firebase.AutomaticReminderJobService;
import com.apponative.committee_app.ui.MainActivity;
import com.apponative.committee_app.ui.custom.CircleTransform;
import com.apponative.committee_app.ui.dialogs.CustomDialog;
import com.apponative.committee_app.ui.dialogs.PasscodeDialog;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;
import com.apponative.committee_app.utils.LocalStorageUtility;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Locale;

public class Fragment_Settings extends BaseFragment implements View.OnClickListener {
    View v;
    TextView title;
    ImageView profilephoto;
    ImageView editprofile;
    TextView btn_save;
    RadioGroup selector_language, selector_password, selector_reminder;
    RadioButton lang_en, lang_ur, pass_on, pass_off, reminder_on, reminder_off;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_settings, container, false);
        initViews();
        return v;
    }

    void initViews() {

        title = (TextView) v.findViewById(R.id.username);
        profilephoto = (ImageView) v.findViewById(R.id.profileimage);
        title.setText(MainActivity.signedInUser.getUsername() + "\n " + MainActivity.signedInUser.getUserId());
        btn_save = (TextView) v.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        editprofile = (ImageView) v.findViewById(R.id.edit_profile);
        editprofile.setOnClickListener(this);
        selector_language = (RadioGroup) v.findViewById(R.id.selector_language);
        selector_password = (RadioGroup) v.findViewById(R.id.selector_password);
        selector_reminder = (RadioGroup) v.findViewById(R.id.selector_reminder);

        lang_en = (RadioButton) v.findViewById(R.id.lang_en);
        lang_ur = (RadioButton) v.findViewById(R.id.lang_ur);

        pass_on = (RadioButton) v.findViewById(R.id.pass_on);
        pass_off = (RadioButton) v.findViewById(R.id.pass_off);

        reminder_on = (RadioButton) v.findViewById(R.id.reminder_on);
        reminder_off = (RadioButton) v.findViewById(R.id.reminder_off);

        lang_en.setChecked(!LocalStorageUtility.getBooleanFromSharedPref(getActivity(), Constants.TRANSLATED));
        lang_ur.setChecked(LocalStorageUtility.getBooleanFromSharedPref(getActivity(), Constants.TRANSLATED));

        setPassCodePref();

        pass_on.setOnClickListener(this);
        pass_off.setOnClickListener(this);

        reminder_on.setChecked(LocalStorageUtility.getBooleanFromSharedPref(getActivity(), Constants.AUTOREMINDER));
        reminder_off.setChecked(!LocalStorageUtility.getBooleanFromSharedPref(getActivity(), Constants.AUTOREMINDER));

        Glide.with(getActivity())
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference().child(Constants.ST_PP
                        + MainActivity.signedInUser.getUserId() + Constants.PP_TYPE))
                .transform(new CircleTransform(getActivity()))
                .placeholder(R.drawable.placeholder).into(profilephoto);

    }

    void setPreferences() {
        RadioButton language = (RadioButton) v.findViewById(selector_language.getCheckedRadioButtonId());
        RadioButton reminder = (RadioButton) v.findViewById(selector_reminder.getCheckedRadioButtonId());

        LocalStorageUtility.saveBoolToSharedPref(getActivity(), Constants.TRANSLATED, selector_language.indexOfChild(language) == 1 ? true : false);
        LocalStorageUtility.saveBoolToSharedPref(getActivity(), Constants.AUTOREMINDER,
                selector_reminder.indexOfChild(reminder) == 0 ? true : false);

        //    setLocale(selector_language.indexOfChild(language) == 1 ? "ur" : "en");

        if (selector_reminder.indexOfChild(reminder) == 0) {
            AutomaticReminderJobService.cancelAutomaticReminder();
            AutomaticReminderJobService.scheduleJobAutomatic(MainActivity.signedInUser.getUserId());
        } else {
            AutomaticReminderJobService.cancelAutomaticReminder();
        }

        MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_home, null);
    }

    void setLocale(String local) {

        final Resources resources = getResources();
        final Configuration configuration = resources.getConfiguration();
        final Locale locale = new Locale(local);
        if (!configuration.locale.equals(locale)) {
            configuration.setLocale(locale);
            resources.updateConfiguration(configuration, null);
        }
    }

    void setPassCodePref() {
        pass_on.setChecked(LocalStorageUtility.getBooleanFromSharedPref(getActivity(), Constants.PROTECTED));
        pass_off.setChecked(!LocalStorageUtility.getBooleanFromSharedPref(getActivity(), Constants.PROTECTED));

    }

//    void setPassCode() {
//        new PasscodeDialog(getActivity(), new CommitteeCallBack.DialogInteractionListener() {
//            @Override
//            public void OnButton1Click(Dialog dialog) {
//                setPassCodePref();
//            }
//
//            @Override
//            public void OnButton2Click(Dialog dialog) {
//
//            }
//        }).show();
//    }

    void confirmPassCode(boolean isTurnedOff) {

        new PasscodeDialog(getActivity(), isTurnedOff, new CommitteeCallBack.DialogInteractionListener() {
            @Override
            public void OnButton1Click(Dialog dialog) {
                setPassCodePref();
            }

            @Override
            public void OnButton2Click(Dialog dialog) {

            }
        }).show();

    }

    void confirmSave() {
        CustomDialog dialog =
                new CustomDialog(getActivity(), new CommitteeCallBack.DialogInteractionListener() {
                    @Override
                    public void OnButton1Click(Dialog dialog) {

                    }

                    @Override
                    public void OnButton2Click(Dialog dialog) {
                        setPreferences();
                    }
                });
        dialog.setContent("Save Settings", "Are you sure you want to save it?", "Cancel", "Ok");
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == editprofile) {
            MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_setprofile, null);
        } else if (v == btn_save) {
            confirmSave();
        } else if (v == pass_on) {
            confirmPassCode(false);
        } else if (v == pass_off) {
            confirmPassCode(true);
        }
    }
}
