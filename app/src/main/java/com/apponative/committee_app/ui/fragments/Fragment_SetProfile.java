package com.apponative.committee_app.ui.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apponative.committee_app.CommitteeApplication;
import com.apponative.committee_app.R;
import com.apponative.committee_app.datamodles.User;
import com.apponative.committee_app.firebase.FireBaseDbHandler;
import com.apponative.committee_app.ui.MainActivity;
import com.apponative.committee_app.utils.CAUtility;
import com.apponative.committee_app.ui.custom.CircleTransform;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;
import com.apponative.committee_app.utils.PermissionUtils;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.soundcloud.android.crop.Crop;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class Fragment_SetProfile extends BaseFragment implements View.OnClickListener, CommitteeCallBack.ProfileChangeDelegate {

    View v;

    TextView saveprofile;
    EditText username;
    Uri picUri;
    String filepath;
    ImageView profileImage, uploadImage;
    User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_setprofile, container, false);
        initViews();
        return v;
    }

    void initViews() {
        saveprofile = (TextView) v.findViewById(R.id.save_button);
        saveprofile.setOnClickListener(this);
        username = (EditText) v.findViewById(R.id.user_Name);
        uploadImage = (ImageView) v.findViewById(R.id.btn_upload);
        uploadImage.setOnClickListener(this);
        profileImage = (ImageView) v.findViewById(R.id.profileimage);

        if (MainActivity.signedInUser != null && MainActivity.signedInUser.getUsername() != null) {
            username.setText(MainActivity.signedInUser.getUsername() + "");
        }

        Glide.with(getActivity())
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference().child(Constants.ST_PP
                        + MainActivity.signedInUser.getUserId() + Constants.PP_TYPE))
                .transform(new CircleTransform(getActivity()))
                .placeholder(R.drawable.placeholder).into(profileImage);
    }

    public void setUpUserProfile() {
        if (CAUtility.notIsEmpty(username)) {
            MainActivity.progressBar.setVisibility(View.VISIBLE);
            FireBaseDbHandler.getDbHandler(getActivity()).setUserProfile(username.getText().toString().trim(), filepath, this);
        } else {
            CAUtility.showGeneralAlert(getActivity(), "Error", Constants.INCOMPLETE_FIELDS, "", "Ok");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == saveprofile) {
            setUpUserProfile();
        } else if (v == uploadImage) {
            launchImageChoser();
        }
    }

    void launchImageChoser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select an image");
        final CharSequence[] items = {
                "Take a photo", "Choose from library"
        };
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                CommitteeApplication.isExternalIntentOpened = true;
                if (item == 0) {
                    try {
                        if (PermissionUtils.hasCameraAndWritePermission(getActivity()) || !PermissionUtils.isPermissionRequired()) {
                            startCapture();
                        } else
                            PermissionUtils.askCameraAndWritePermission(Fragment_SetProfile.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //display an error message
                        String errorMessage = "Whoops - your device doesn't support capturing images!";
                        Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                if (item == 1) {
                    try {
                        if (PermissionUtils.hasWriteStoragePermission(getActivity())) {
                            Crop.pickImage(getActivity(), Fragment_SetProfile.this);
                        } else {
                            PermissionUtils.askWriteStoragePermission(Fragment_SetProfile.this);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void startCapture() {
        picUri = Uri.fromFile(CAUtility.getOutputMediaFile(getActivity()));
        CAUtility.startImageCapture(getActivity(), Fragment_SetProfile.this, picUri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == Constants.PICK_FROM_CAMERA) {
                CropImage.activity(picUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true).setAspectRatio(1, 1)
                        .start(getContext(), this);
            } else if (requestCode == Crop.REQUEST_PICK) {
                picUri = data.getData();
                CropImage.activity(picUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true).setAspectRatio(1, 1)
                        .start(getContext(), this);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CommitteeApplication.isExternalIntentOpened = false;
                filepath = CropImage.getActivityResult(data).getUri().getPath();
                Glide.with(getActivity())
                        .load(filepath)
                        .transform(new CircleTransform(getActivity()))
                        .placeholder(R.drawable.placeholder)
                        .into(profileImage);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.PERMISSION_REQUEST_CAMERA:
                startCapture();
                break;
            case Constants.PERMISSION_REQUEST_EXTERNAL_STORAGE:
                Crop.pickImage(getActivity(), Fragment_SetProfile.this);
                break;
        }
    }

    @Override
    public void onProfileSaved(User user) {
        if (user != null) {
            this.user = user;
            ((MainActivity) getActivity()).updateUser(user);
        }
        MainActivity.menuItemListener.OnMenuItemSelected(R.string.tag_settings, null);
    }
}
