package com.apponative.committee_app.ui;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apponative.committee_app.CommitteeApplication;
import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.Notification;
import com.apponative.committee_app.datamodles.User;
import com.apponative.committee_app.firebase.FireBaseDbHandler;
import com.apponative.committee_app.firebase.OkkHttpBuilder;
import com.apponative.committee_app.ui.adapters.MenuAdapter;
import com.apponative.committee_app.ui.custom.CircleTransform;
import com.apponative.committee_app.ui.dialogs.NotificationsDialog;
import com.apponative.committee_app.ui.dialogs.PasscodeDialog;
import com.apponative.committee_app.ui.fragments.BaseFragment;
import com.apponative.committee_app.ui.fragments.Fragment_AboutUs;
import com.apponative.committee_app.ui.fragments.Fragment_AppContacts;
import com.apponative.committee_app.ui.fragments.Fragment_Calculator;
import com.apponative.committee_app.ui.fragments.Fragment_CompletedDetails;
import com.apponative.committee_app.ui.fragments.Fragment_CreateCommittee;
import com.apponative.committee_app.ui.fragments.Fragment_Home;
import com.apponative.committee_app.ui.fragments.Fragment_Members;
import com.apponative.committee_app.ui.fragments.Fragment_MyCommittee;
import com.apponative.committee_app.ui.fragments.Fragment_NewCommittee;
import com.apponative.committee_app.ui.fragments.Fragment_OnGoingDetails;
import com.apponative.committee_app.ui.fragments.Fragment_PendingCommittee;
import com.apponative.committee_app.ui.fragments.Fragment_RandomDraw;
import com.apponative.committee_app.ui.fragments.Fragment_SendMoreInvites;
import com.apponative.committee_app.ui.fragments.Fragment_SetProfile;
import com.apponative.committee_app.ui.fragments.Fragment_Settings;
import com.apponative.committee_app.utils.CAUtility;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;
import com.apponative.committee_app.utils.LocalStorageUtility;
import com.apponative.committee_app.utils.PermissionUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener
        , CommitteeCallBack.FragmentDelegate, GoogleApiClient.OnConnectionFailedListener
        , CommitteeCallBack.FireBaseAuthCallBack, CommitteeCallBack.NotificationCallBack {

    DrawerLayout drawer;
    ImageView toggle, nav_edit, nav_profileimage, icon_notification, footer_icon1, footer_icon2, footer_icon3;
    TextView title_header, nav_username, notificationcount;
    View app_bar_main;
    View app_bar_custom;
    View app_footer_custom;
    View nav_header_main;
    private Handler mHandler;
    LinearLayout navigationView;
    RecyclerView nav_menu;
    MenuAdapter menuAdapter;
    String[] menuItems;
    public static CommitteeCallBack.FragmentDelegate menuItemListener;
    public static ImageView progressBar;
    public static User signedInUser;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    NotificationsDialog notificationsDialog;
    public ArrayList<Notification> notifications = new ArrayList<>();
    PasscodeDialog passcodeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Constants.initializeCountryCodes();
        Constants.initializeNotificationIntents();
        initializeViews();
        fireBaseDigitsAuth();
    }

    void initializeViews() {

        passcodeDialog = new PasscodeDialog(this, new CommitteeCallBack.DialogInteractionListener() {
            @Override
            public void OnButton1Click(Dialog dialog) {
                finish();
            }

            @Override
            public void OnButton2Click(Dialog dialog) {

            }
        });

        progressBar = (ImageView) findViewById(R.id.progressBar);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(progressBar);
        Glide.with(this).load(R.raw.loading_small).into(imageViewTarget);
        menuItemListener = this;
        mHandler = new Handler();
        menuItems = getResources().getStringArray(R.array.menu_list);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (LinearLayout) findViewById(R.id.nav_view);
        nav_menu = (RecyclerView) findViewById(R.id.nav_menu);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        nav_menu.setLayoutManager(layoutManager);
        menuAdapter = new MenuAdapter(menuItems);
        nav_menu.setAdapter(menuAdapter);
        app_bar_main = findViewById(R.id.app_bar_main);
        app_bar_custom = app_bar_main.findViewById(R.id.app_bar_custom);
        toggle = (ImageView) app_bar_custom.findViewById(R.id.toggle);
        toggle.setOnClickListener(this);
        title_header = (TextView) app_bar_custom.findViewById(R.id.titleview);
        notificationcount = (TextView) app_bar_custom.findViewById(R.id.notification_count);
        icon_notification = (ImageView) app_bar_custom.findViewById(R.id.icon_notification);
        icon_notification.setOnClickListener(this);
        nav_header_main = findViewById(R.id.nav_header_main);
        nav_username = (TextView) findViewById(R.id.nav_username);
        nav_profileimage = (ImageView) findViewById(R.id.nav_profileimage);
        nav_edit = (ImageView) findViewById(R.id.nav_upload);
        nav_edit.setOnClickListener(this);
        app_footer_custom = app_bar_main.findViewById(R.id.app_footer_custom);
        footer_icon1 = (ImageView) app_footer_custom.findViewById(R.id.footer_icon1);
        footer_icon2 = (ImageView) app_footer_custom.findViewById(R.id.footer_icon2);
        footer_icon3 = (ImageView) app_footer_custom.findViewById(R.id.footer_icon3);
        footer_icon1.setOnClickListener(this);
        footer_icon2.setOnClickListener(this);
        footer_icon3.setOnClickListener(this);


        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toggle.setEnabled(false);
        app_footer_custom.setVisibility(View.GONE);

        updateNotificationBar();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (f != null) {
                    int resId = getResources().getIdentifier(f.getTag() + "_title", "string", getPackageName());
                    title_header.setText(resId);
                    menuAdapter.setSelected(getResources().getIdentifier(f.getTag(), "string", getPackageName()));
                  //  f.onResume();
                }
            }
        });

    }

    void fireBaseDigitsAuth() {

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                } else {
                }
            }
        };

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            FireBaseDbHandler.getDbHandler(this).userProfileCheck(mAuth.getCurrentUser().getPhoneNumber(), this);
        }
    }

    public void updateNotificationBar() {

        if (LocalStorageUtility.getFromSharedPref(getApplicationContext(), Constants.NOTIFICATIONSLIST) != null) {
            notifications = new Gson().fromJson(LocalStorageUtility.getFromSharedPref(getApplicationContext(),
                    Constants.NOTIFICATIONSLIST), new TypeToken<ArrayList<Notification>>() {
            }.getType());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notificationcount.setText(notifications.size() + "");
                }
            });

        }
    }

    private void showListMenu(final View anchor) {
        if (notificationsDialog != null && notificationsDialog.isShowing()) {
            notificationsDialog.dismissPopup();
        } else {
            if (LocalStorageUtility.getFromSharedPref(getApplicationContext(), Constants.NOTIFICATIONSLIST) != null) {
                notifications = new Gson().fromJson(LocalStorageUtility.getFromSharedPref(getApplicationContext(),
                        Constants.NOTIFICATIONSLIST), new TypeToken<ArrayList<Notification>>() {
                }.getType());
            }
            notificationsDialog = new NotificationsDialog(this,
                    notifications, new CommitteeCallBack.NotificationInterfaceListener() {
                @Override
                public void OnNotificationSelected(int id) {
                    openNotification(notifications.get(id));
                }

                @Override
                public void OnClearAllClick() {
                    notifications.clear();
                    LocalStorageUtility.saveToSharedPref(MainActivity.this, Constants.NOTIFICATIONSLIST, null);
                    notificationcount.setText(notifications.size() + "");
                }
            });

            notificationsDialog.showPopupWindow(anchor);
        }
    }

    void loadFragment(final int fTagId, Bundle b) {
        final FragmentManager fm = getSupportFragmentManager();
        final String tagString = getResources().getString(fTagId);
        BaseFragment cFragment = (BaseFragment) fm.findFragmentByTag(tagString);
        if (cFragment == null) {
            final BaseFragment fragment = getFragmentByTag(fTagId);
            if (b != null) {
             //   fragment.setArguments(b);
                fragment.setmArguments(b);
            }
            final String backstackName = fragment.getClass().getName();
            Runnable mPendingRunnable = new Runnable() {
                @Override
                public void run() {

                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                            R.anim.slide_out_left, R.anim.slide_in_left,
                            R.anim.slide_out_right);
                    fragmentTransaction.replace(R.id.fragment_container, fragment, tagString);
                    fragmentTransaction.addToBackStack(backstackName);
                    fragmentTransaction.commit();

                }
            };

            if (mPendingRunnable != null) {
                mHandler.post(mPendingRunnable);
            }
        } else {
            if(b != null){
                cFragment.getmArguments().putAll(b);
            }
            fm.popBackStackImmediate(cFragment.getClass().getName(), 0);
        }
    }

    BaseFragment getFragmentByTag(int tag) {
        BaseFragment fragment = null;

        switch (tag) {
            case R.string.tag_home:
                fragment = new Fragment_Home();
                break;
            case R.string.tag_setprofile:
                fragment = new Fragment_SetProfile();
                break;
            case R.string.tag_create:
                fragment = new Fragment_CreateCommittee();
                break;
            case R.string.tag_committee:
                fragment = new Fragment_MyCommittee();
                break;
            case R.string.tag_contact:
                //    fragment = new Fragment_CommitteeContacts();
                fragment = new Fragment_AppContacts();
                break;
            case R.string.tag_calculator:
                fragment = new Fragment_Calculator();
                break;
            case R.string.tag_aboutus:
                fragment = new Fragment_AboutUs();
                break;
            case R.string.tag_settings:
                fragment = new Fragment_Settings();
                break;
            case R.string.tag_pending_committee:
                fragment = new Fragment_PendingCommittee();
                break;
            case R.string.tag_new_committee:
                fragment = new Fragment_NewCommittee();
                break;
            case R.string.tag_ongoing_detail:
                fragment = new Fragment_OnGoingDetails();
                break;
            case R.string.tag_committee_detail:
                fragment = new Fragment_CompletedDetails();
                break;
            case R.string.tag_members:
                fragment = new Fragment_Members();
                break;
            case R.string.tag_randomdraw:
                fragment = new Fragment_RandomDraw();
                break;
            case R.string.tag_sendmoreinvites:
                fragment = new Fragment_SendMoreInvites();
                break;
        }

        return fragment;
    }

    @Override
    public void onBackPressed() {
        progressBar.setVisibility(View.GONE);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (f instanceof Fragment_Home) {
                finish();
            } else if (f instanceof Fragment_SetProfile && LocalStorageUtility.getBooleanFromSharedPref(this, Constants.ISNEW) == false) {
                CAUtility.showGeneralAlert(this, "Save Profile", "Please save profile first.", "Cancel", "Ok");
            } else if (!(f instanceof Fragment_Home) && count == 1) {
                loadFragment(R.string.tag_home, null);
            } else {
                super.onBackPressed();
            }
        }
    }

    public void updateUser(User user) {
        nav_username.setText(user.getUsername());
        signedInUser = user;
        Glide.with(MainActivity.this)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference().child(Constants.ST_PP + MainActivity.signedInUser.getUserId() + Constants.PP_TYPE))
                .transform(new CircleTransform(this))
                .placeholder(R.drawable.placeholder).into(nav_profileimage);
        String token = FirebaseInstanceId.getInstance().getToken();
        if (token != null) {
            FireBaseDbHandler.getDbHandler(this).setUserRegisterationToken(token);
        }

        if (LocalStorageUtility.getBooleanFromSharedPref(this, Constants.ISNEW) == false) {
            readContacts();
            LocalStorageUtility.saveBoolToSharedPref(this, Constants.ISNEW, true);
        }
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        toggle.setEnabled(true);
        app_footer_custom.setVisibility(View.VISIBLE);
    }

    public void readContacts() {
        if (PermissionUtils.hasReadContactPermission(this) || !PermissionUtils.isPermissionRequired()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("contacts", new Gson().toJson(CAUtility.getAllContacts(this)));
                jsonObject.put("uid", signedInUser.getUserId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                OkkHttpBuilder.getInstance(this).filterUserContacts(jsonObject.toString(), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            PermissionUtils.askReadContactsPermission(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == Constants.PERMISSION_REQUEST_CONTACTS) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContacts();
            } else {
                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void showPassCodeDialog() {
        if (LocalStorageUtility.getBooleanFromSharedPref(this, Constants.PROTECTED)) {
            passcodeDialog.clearscreen();
            passcodeDialog.show();
        }
    }

    @Override
    public void onStart() {
        if (!CommitteeApplication.isActive()) {
            showPassCodeDialog();
        }
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, new MyObserver(new Handler()));
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (!CommitteeApplication.isActive()) {
//        }
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v == toggle) {
            drawer.openDrawer(GravityCompat.START);
        } else if (v == footer_icon1) {
            menuItemListener.OnMenuItemSelected(R.string.tag_contact, null);
        } else if (v == footer_icon2) {
            menuItemListener.OnMenuItemSelected(R.string.tag_home, null);
        } else if (v == footer_icon3) {
            menuItemListener.OnMenuItemSelected(R.string.tag_calculator, null);
        } else if (v == nav_edit) {
            menuItemListener.OnMenuItemSelected(R.string.tag_setprofile, null);
        } else if (v == icon_notification) {
            showListMenu(icon_notification);
        }
    }

    @Override
    public void OnMenuItemSelected(int tagId, Bundle b) {
        progressBar.setVisibility(View.GONE);
        loadFragment(tagId, b);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void SignInSuccess(User user) {
        updateUser(user);
        if (getIntent().hasExtra("Notification")) {
            openNotification(new Gson().fromJson(getIntent().getStringExtra("Notification"), Notification.class));
        } else {
            loadFragment(R.string.tag_home, null);
        }
    }

    void openNotification(Notification notification) {

        if (LocalStorageUtility.getFromSharedPref(getApplicationContext(), Constants.NOTIFICATIONSLIST) != null) {
            notifications.remove(notification);
            LocalStorageUtility.saveToSharedPref(getApplicationContext(), Constants.NOTIFICATIONSLIST
                    , new Gson().toJson(notifications));
        }
        updateNotificationBar();
        int tag = Constants.NotificationIntents.get(notification.getType().toString());
        Bundle b = new Bundle();
        if (notification.getType() == Constants.NTYPE.INVITE) {
            b.putBoolean("joined", false);
        } else {
            b.putBoolean("joined", true);
        }

        NotificationManager nManager = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
        nManager.cancelAll();

        b.putString("cid", notification.getCid());
        loadFragment(tag, b);
    }

    @Override
    public void NewUserRegisteration() {
        loadFragment(R.string.tag_setprofile, null);
    }

    @Override
    public void OnNotificationReceived() {
        updateNotificationBar();
    }

    public class MyObserver extends ContentObserver {
        public MyObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            readContacts();
        }
    }
}
