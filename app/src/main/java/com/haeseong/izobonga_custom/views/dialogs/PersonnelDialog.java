package com.haeseong.izobonga_custom.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.haeseong.izobonga_custom.R;

import java.util.Objects;

public class PersonnelDialog extends BaseDialog {
    public TextView mTvNumber;
    private View.OnClickListener nextButtonListener;
    private View.OnClickListener preButtonListener;
    private Drawable drawable;

    public PersonnelDialog(@NonNull Context context,
                           View.OnClickListener positiveListener, View.OnClickListener preButtonListener,
                           Drawable drawable) {
        super(context);
        this.nextButtonListener = positiveListener;
        this.preButtonListener = preButtonListener;
        this.drawable = drawable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_personnel);
        setCancelable(false);
        setFullscreen(true);
        setTranslucentBlack();

        //initView
        ImageView mIvPlus = findViewById(R.id.personnel_minus);
        ImageView mIvMinus = findViewById(R.id.child_plus);
        mTvNumber = findViewById(R.id.child_number);
        Button mBtnPrevious = findViewById(R.id.personnel_previous);
        Button mBtnNext = findViewById(R.id.personnel_next);
        ImageView mIvTitle = findViewById(R.id.personnel_dialog_iv_title);

        mIvTitle.setImageDrawable(drawable);

        mIvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(mTvNumber.getText().toString());
                if (n > 0) {
                    n = n - 1;
                    mTvNumber.setText(String.valueOf(n));
                }
            }
        });

        mIvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(mTvNumber.getText().toString());
                n = n + 1;
                mTvNumber.setText(String.valueOf(n));
            }
        });

        mBtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonnelDialog.this.dismiss();
            }
        });

        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        mBtnNext.setOnClickListener(nextButtonListener);
        mBtnPrevious.setOnClickListener(preButtonListener);
    }

    public void dismissDialog(){
        mTvNumber.setText("0");
        dismiss();
    }

}
