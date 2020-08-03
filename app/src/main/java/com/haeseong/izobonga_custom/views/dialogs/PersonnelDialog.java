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

public class PersonnelDialog extends Dialog {
    private ImageView mIvPlus, mIvMinus, mIvTitle;
    public TextView mTvNumber;
    private Button mBtnPrevious, mBtnNext;
    private View.OnClickListener nextButtonListener;
    private View.OnClickListener preButtonListener;
    private Drawable drawable;
//    private View.OnClickListener plusListener;
//    private View.OnClickListener minusListener;
//    private String mTitle;
//    private String mSubTitle;

    public PersonnelDialog(@NonNull Context context,
                           View.OnClickListener positiveListener, View.OnClickListener preButtonListener,
//                           View.OnClickListener plusListener, View.OnClickListener minusListener,
                           Drawable drawable) {
        super(context);
        this.nextButtonListener = positiveListener;
        this.preButtonListener = preButtonListener;
        this.drawable = drawable;
//        this.plusListener = plusListener;
//        this.minusListener = minusListener;
//        this.mTitle = title;
//        this.mSubTitle = sub;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        Objects.requireNonNull(getWindow()).setAttributes(layoutParams);

        setContentView(R.layout.dialog_personnel);
        setCancelable(false);
        hideSoftKey();

        //initView
        mIvPlus = findViewById(R.id.personnel_minus);
        mIvMinus = findViewById(R.id.child_plus);
        mTvNumber = findViewById(R.id.child_number);
        mBtnPrevious = findViewById(R.id.personnel_previous);
        mBtnNext = findViewById(R.id.personnel_next);
        mIvTitle = findViewById(R.id.personnel_dialog_iv_title);

        mIvTitle.setImageDrawable(drawable);
        mIvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(mTvNumber.getText().toString());
                if (n > 0) {
                    n = n - 1;
                    mTvNumber.setText(n + "");
                }
            }
        });

        mIvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(mTvNumber.getText().toString());
                n = n + 1;
                mTvNumber.setText(n + "");
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
    public void hideSoftKey(){

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("Is on?", "Turning immersive mode mode off. ");
        } else {
            Log.i("Is on?", "Turning immersive mode mode on.");
        }

        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }


}
