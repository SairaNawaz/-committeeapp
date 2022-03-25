package com.apponative.committee_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.apponative.committee_app.utils.CAUtility;
import com.apponative.committee_app.utils.LocalStorageUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity implements
        View.OnClickListener {


    private ImageView progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
//        init();
//        initCallbacks();
    }






    void postActivityResult() {

        progressBar.setVisibility(View.GONE);
        Intent data = new Intent();
    //    String phoneNumber = previousNumber;
        //---set the data to pass back---
    //    data.putExtra("phonenumber", phoneNumber);
        data.putExtra("verified", true);
        setResult(RESULT_OK, data);
        //---close the activity---
        finish();
    }


    @Override
    public void onClick(View view) {
    }
}