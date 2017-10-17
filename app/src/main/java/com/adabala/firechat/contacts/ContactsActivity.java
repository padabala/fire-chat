package com.adabala.firechat.contacts;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.adabala.firechat.R;
import com.adabala.firechat.databinding.ActivityContactsBinding;

public class ContactsActivity extends AppCompatActivity {

    ActivityContactsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(ContactsActivity.this, R.layout.activity_contacts);
        mBinding.setHandlers(ContactsActivity.this);
    }
}
