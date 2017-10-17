package com.adabala.firechat;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.adabala.firechat.databinding.ActivityAccessBinding;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class AccessActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    ActivityAccessBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(AccessActivity.this, R.layout.activity_access);
        mBinding.setHandlers(AccessActivity.this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
