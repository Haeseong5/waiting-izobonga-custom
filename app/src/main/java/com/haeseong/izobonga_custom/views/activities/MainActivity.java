package com.haeseong.izobonga_custom.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.haeseong.izobonga_custom.BaseActivity;
import com.haeseong.izobonga_custom.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClickListener(View view) {
        super.onClickListener(view);
        switch (view.getId()){
            case R.id.main_call_button:
                startActivity(new Intent(MainActivity.this, CallActivity.class));
                break;
            case R.id.main_customer_button:
                startActivity(new Intent(MainActivity.this, WaitingActivity.class));
                break;
            case R.id.main_manage_button:
                startActivity(new Intent(MainActivity.this, ManagerActivity.class));
                break;
            default:
                break;
        }
    }
}
