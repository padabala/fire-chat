package com.adabala.firechat;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.adabala.firechat.database.ApplicationAccess;
import com.adabala.firechat.databinding.ActivityRegistrationBinding;
import com.adabala.firechat.di.Injector;
import com.adabala.firechat.interfaces.RegistrationCompletionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import timber.log.Timber;

public class RegistrationActivity extends AppCompatActivity implements RegistrationCompletionListener{

    ActivityRegistrationBinding mBinding;

    private String mVerificationId;

    @Inject
    ApplicationAccess applicationAccess;

    @Inject
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.INSTANCE.getAppComponent().inject(RegistrationActivity.this);

        mBinding = DataBindingUtil.setContentView(RegistrationActivity.this, R.layout.activity_registration);
        mBinding.setHandlers(RegistrationActivity.this);
        mBinding.setShowPhoneNumberInput(true);
    }

    public void onVerifyClicked(View view) {
        Timber.d("onVerifyClicked");
        startPhoneNumberAutoVerification(mBinding.getPhoneNumber());
    }

    public void onRegisterClicked(View view) {
        Timber.d("onRegisterClicked");
        if (mVerificationId != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, mBinding.getVerificationCode());
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Timber.d("signInWithPhoneAuthCredential");
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Timber.d("signInWithPhoneAuthCredential Successful");
                            applicationAccess.registerUser(mBinding.getPhoneNumber(), RegistrationActivity.this);
                        } else {
                            Timber.d("%s",task.getResult());
                            Timber.d("signInWithPhoneAuthCredential Failed");
                            Toast.makeText(RegistrationActivity.this, R.string.registration_failed_text, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void startPhoneNumberAutoVerification(final String phoneNumber) {

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Timber.d("onVerificationCompleted");
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                e.printStackTrace();
                Timber.d("onVerificationFailed");
                Toast.makeText(RegistrationActivity.this, R.string.registration_failed_text, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Timber.d("onCodeSent %s", verificationId);
                mBinding.setShowPhoneNumberInput(false);
                mBinding.codeInput.requestFocus();
                mVerificationId = verificationId;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String verificationId) {
                Timber.d("onCodeAutoRetrievalTimeOut %s", verificationId);
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, this, mCallbacks);
    }

    @Override
    public void onRegistrationSuccess() {
        Intent intent = new Intent(RegistrationActivity.this, AccessActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegistrationFailed() {

    }
}
