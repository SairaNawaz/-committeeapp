package com.apponative.committee_app.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.widget.EditText;

import com.apponative.committee_app.datamodles.Notification;
import com.apponative.committee_app.datamodles.People;
import com.apponative.committee_app.ui.MainActivity;
import com.apponative.committee_app.ui.dialogs.CustomDialog;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Muhammad Waqas on 4/20/2017.
 */

public class CAUtility {

    public static void startImageCapture(Context context, Fragment fragment, Uri picUri) {
        //use standard intent to capture an image
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //we will handle the returned data in onActivityResult
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        captureIntent.putExtra("return-data", true);
        if (fragment != null) {
            fragment.startActivityForResult(captureIntent, Constants.PICK_FROM_CAMERA);
        } else {
            ((Activity) context).startActivityForResult(captureIntent, Constants.PICK_FROM_CAMERA);
        }
    }

    public static File getOutputMediaFile(Context context) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        // String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "CA_Img" + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);

        return mediaFile;
    }

    private static final String[] PROJECTION = new String[]{
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    public static HashMap<String, People> getAllContacts(Context context) {
        String plus = "+";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryIso = telephonyManager.getNetworkCountryIso().toUpperCase();
        String countrycode = plus + Constants.countrymap.get(countryIso); //"92";

        HashMap<String, People> contactVOList = new HashMap<>();
        People contactVO;
        ContentResolver contentResolver = context.getContentResolver();
        ContentProviderClient mCProviderClient = contentResolver.acquireContentProviderClient(ContactsContract.Contacts.CONTENT_URI);
        Cursor cursor = null;
        try {
            cursor = mCProviderClient.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    contactVO = new People();
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[\\-\\.\\#\\*\\@\\!\\^\\%\\[\\]\\s]", "");
                    number = number.replaceAll(" ", "");

                    if (number.startsWith("00")) {
                        number = number.replaceFirst("00", plus);
                    } else if (number.startsWith("0")) {
                        number = number.replaceFirst("0", countrycode);
                    }

                    if (number != null && !number.equalsIgnoreCase(MainActivity.signedInUser.getUserId())) {
                        contactVO.setContactName(name);
                        contactVO.setContactUsingApp(0);
                        contactVOList.put(number, contactVO);
                    }
                }
            }
            cursor.close();
        }

        return contactVOList;
    }

    @SuppressWarnings("unchecked")
    public static <T> T mergeObjects(T first, T second) {
        Class<?> clazz = first.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Object returnValue = null;
        try {
            returnValue = clazz.newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value1 = field.get(first);
                Object value2 = field.get(second);
                Object value = (value1 != null) ? value1 : value2;
                field.set(returnValue, value);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return (T) returnValue;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showGeneralAlert(Context context, String title
            , String message, String btn1, String btn2) {
        final CustomDialog dialog = new CustomDialog(context, new CommitteeCallBack.DialogInteractionListener() {
            @Override
            public void OnButton1Click(Dialog dialog) {

            }

            @Override
            public void OnButton2Click(Dialog dialog) {

            }
        });
        dialog.setContent(title, message, btn1, btn2);
        dialog.show();
    }

    public static void showDataNoFound(final Context context) {
        final CustomDialog dialog = new CustomDialog(context, new CommitteeCallBack.DialogInteractionListener() {
            @Override
            public void OnButton1Click(Dialog dialog) {

            }

            @Override
            public void OnButton2Click(Dialog dialog) {
                ((Activity) context).onBackPressed();
            }
        });
        dialog.setContent("Invalid Request!",
                "Data May have been deleted or not properly synced. Please check your internet connection and try again", "", "Ok");
        dialog.show();
    }

    public static boolean notIsEmpty(EditText editText) {
        return editText.getText().length() > 0 && !editText.getText().toString().trim().equalsIgnoreCase("");
    }

    public static void removeNotification(Context context, final String cid) {
        ArrayList<Notification> existingNotifications = new Gson().fromJson(LocalStorageUtility.getFromSharedPref(context,
                Constants.NOTIFICATIONSLIST), new TypeToken<ArrayList<Notification>>() {
        }.getType());
        if (existingNotifications != null) {
            ArrayList<Notification> openedNotification = new ArrayList<>(Collections2.filter(existingNotifications, new Predicate<Notification>() {
                @Override
                public boolean apply(Notification candidate) {
                    return candidate.getCid().contains(cid);
                }
            }));
            existingNotifications.removeAll(openedNotification);
            LocalStorageUtility.saveToSharedPref(context,
                    Constants.NOTIFICATIONSLIST, new Gson().toJson(existingNotifications));
            ((MainActivity) context).updateNotificationBar();
            if(openedNotification.size()>0){
                NotificationManager nManager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
                nManager.cancelAll();
            }
        }
    }
}
