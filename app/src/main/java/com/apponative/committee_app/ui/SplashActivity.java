package com.apponative.committee_app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.utils.CAUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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

public class SplashActivity extends Activity
        implements GoogleApiClient.OnConnectionFailedListener, Animation.AnimationListener, View.OnClickListener { //CommitteeCallBack.FireBaseAuthCallBack,AuthCallback,

    ImageView splash_hand1, splash_hand2, splash_hand3, splash_plant;
    Animation slideInLeft, slideInRight, slideInTop, slideInBottom;
    private ImageView progressBar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    RelativeLayout splashAnimView, phoneAuthView;

    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    EditText inputPhoneNumber, inputVerificationCode;
    TextView sendCode, verfiyCode;
    String phoneverificationId;
    boolean codeSent = false;
    String previousNumber;
    Spinner country_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        initViews();
        initCallbacks();
        fireBaseDigitsAuth();
    }

    void initViews() {

        phoneAuthView = (RelativeLayout) findViewById(R.id.phoneAuthView);
        splashAnimView = (RelativeLayout) findViewById(R.id.splashAnim);
        splash_hand1 = (ImageView) findViewById(R.id.splash_hand1);
        splash_hand2 = (ImageView) findViewById(R.id.splash_hand2);
        splash_hand3 = (ImageView) findViewById(R.id.splash_hand3);
        splash_plant = (ImageView) findViewById(R.id.splash_plant);
        progressBar = (ImageView) findViewById(R.id.progressbar);
        country_code = (Spinner) findViewById(R.id.country_code);
        inputPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        inputVerificationCode = (EditText) findViewById(R.id.verificationCode);
        sendCode = (TextView) findViewById(R.id.send_button);
        sendCode.setOnClickListener(this);
        verfiyCode = (TextView) findViewById(R.id.verify_button);
        verfiyCode.setOnClickListener(this);


        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(progressBar);
        Glide.with(this).load(R.raw.loading_small).into(imageViewTarget);

        slideInLeft = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.splash_in_left);
        slideInLeft.setAnimationListener(this);
        slideInRight = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.splash_in_right);
        slideInRight.setAnimationListener(this);
        slideInTop = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.splash_in_top);
        slideInTop.setAnimationListener(this);
        slideInBottom = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.splash_in_bottom);
        slideInBottom.setAnimationListener(this);
    }

    void fireBaseDigitsAuth() {

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {

                }
            }
        };

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startAnimation();
        } else {
            phoneAuthView.setVisibility(View.VISIBLE);
        }
    }

    void initCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressBar.setVisibility(View.GONE);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    CAUtility.showGeneralAlert(SplashActivity.this, "Error", e.getMessage().toString(), "Ok", "");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    CAUtility.showGeneralAlert(SplashActivity.this, "Error", e.getMessage().toString(), "Ok", "");
                } else {
                    CAUtility.showGeneralAlert(SplashActivity.this, "Error", e.getMessage().toString(), "Ok", "");
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                CAUtility.showGeneralAlert(SplashActivity.this, "Code Sent", "A Code is sent,Please verify", "Ok", "");
                progressBar.setVisibility(View.GONE);
                codeSent = true;
                sendCode.setText("Resend");
                Log.d("Phone", "Resend Token:" + token);
                phoneverificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        if (code.equals(mResendToken) && verificationId.equals(phoneverificationId)) {
            String abc = "true";
        }
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startAnimation();
                        } else {
                            CAUtility.showGeneralAlert(SplashActivity.this, "Error", "Invalid Code, try again", "Ok", "");
                        }
                    }
                });
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = inputPhoneNumber.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            inputPhoneNumber.setError("Invalid phone number.");
            return false;
        }
        return true;
    }

    void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    void startAnimation() {
        progressBar.setVisibility(View.GONE);
        splashAnimView.setVisibility(View.VISIBLE);
        phoneAuthView.setVisibility(View.GONE);
        splash_hand1.setVisibility(View.VISIBLE);
        splash_hand1.startAnimation(slideInTop);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == slideInTop) {
            splash_hand2.setVisibility(View.VISIBLE);
            splash_hand2.startAnimation(slideInLeft);
        } else if (animation == slideInLeft) {
            splash_hand3.setVisibility(View.VISIBLE);
            splash_hand3.startAnimation(slideInRight);
        } else if (animation == slideInRight) {
            splash_plant.setVisibility(View.VISIBLE);
            splash_plant.startAnimation(slideInBottom);
        } else if (animation == slideInBottom) {
            startMainActivity();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onClick(View view) {
        if (view == sendCode) {
            if (validatePhoneNumber()) {
                progressBar.bringToFront();
                progressBar.setVisibility(View.VISIBLE);
                if (codeSent) {
                    if (previousNumber.equalsIgnoreCase(inputPhoneNumber.getText().toString())) {
                        resendVerificationCode(inputPhoneNumber.getText().toString(), mResendToken);
                    } else {
                        previousNumber = country_code.getSelectedItem() + inputPhoneNumber.getText().toString();
                        startPhoneNumberVerification(previousNumber);
                    }
                } else {
                    previousNumber = country_code.getSelectedItem() + inputPhoneNumber.getText().toString();
                    startPhoneNumberVerification(previousNumber);
                }
            }
        } else if (view == verfiyCode) {
            progressBar.setVisibility(View.VISIBLE);
            verifyPhoneNumberWithCode(phoneverificationId, inputVerificationCode.getText().toString());
        }
    }

}
