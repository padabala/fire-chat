package com.adabala.firechat;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.adabala.firechat.contacts.ContactsActivity;
import com.adabala.firechat.databinding.ActivityAccessBinding;
import com.adabala.firechat.utils.Constants;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

/*
* This Activity checks the required permission needed
* for the app before user proceeds to contacts list.
*/

public class AccessActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    ActivityAccessBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(AccessActivity.this, R.layout.activity_access);
        mBinding.setHandlers(AccessActivity.this);

        mBinding.setShowPermissionsView(false);
        checkPermissions();
    }

    @AfterPermissionGranted(Constants.PERMISSION_REQUEST_CODE)
    private void checkPermissions() {
        if (EasyPermissions.hasPermissions(AccessActivity.this, Constants.REQUIRED_PERMISSIONS)) {
            Intent intent = new Intent(AccessActivity.this, ContactsActivity.class);
            startActivity(intent);
            finish();
        } else {
            Timber.d("No permissions granted thus requesting");
            EasyPermissions.requestPermissions(AccessActivity.this, getString(R.string.permission_req_rationale), Constants.PERMISSION_REQUEST_CODE, Constants.REQUIRED_PERMISSIONS);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Timber.d("onPermissionsGranted");
        mBinding.setShowPermissionsView(false);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Timber.d("onPermissionsDenied");
        mBinding.setShowPermissionsView(true);
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Timber.d("permanently denied");
            mBinding.setPermissionsPermanentlyDenied(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, AccessActivity.this);
    }

    public void onOKButtonClicked(View view) {
        Timber.d("onOKButtonClicked");
        if(mBinding.getPermissionsPermanentlyDenied()){
            Timber.d("Show app settings");
            new AppSettingsDialog.Builder(AccessActivity.this).build().show();
        } else {
            checkPermissions();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
           checkPermissions();
        }
    }
}
