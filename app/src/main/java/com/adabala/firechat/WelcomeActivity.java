package com.adabala.firechat;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.adabala.firechat.database.ApplicationAccess;
import com.adabala.firechat.databinding.ActivityWelcomeBinding;
import com.adabala.firechat.di.Injector;

import javax.inject.Inject;

import timber.log.Timber;

public class WelcomeActivity extends AppCompatActivity {

    ActivityWelcomeBinding mBinding;

    @Inject
    ApplicationAccess applicationAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(WelcomeActivity.this, R.layout.activity_welcome);
        mBinding.setHandlers(WelcomeActivity.this);

        Injector.INSTANCE.getAppComponent().inject(WelcomeActivity.this);

        if (applicationAccess.getVerifiedPhoneNumber() != null) {
            Intent intent = new Intent(WelcomeActivity.this, AccessActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void onGetStartedClicked(View view) {
        Timber.d("onGetStartedClicked");
        Intent intent = new Intent(WelcomeActivity.this, RegistrationActivity.class);
        finish();
        startActivity(intent);
    }
}
