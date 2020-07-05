package com.haeseong.izobonga_custom.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.haeseong.izobonga_custom.R;

import java.util.Objects;

public class TableDialog extends Dialog {
    public CheckBox cbTable6, cbTable4;
    private Button mBtnPrevious, mBtnNext;
    private View.OnClickListener nextButtonListener;
    private View.OnClickListener preButtonListener;


    public TableDialog(@NonNull Context context,
                       View.OnClickListener positiveListener, View.OnClickListener preButtonListener) {
        super(context);
        this.nextButtonListener = positiveListener;
        this.preButtonListener = preButtonListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        hideSoftKey();
        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        Objects.requireNonNull(getWindow()).setAttributes(layoutParams);

        setContentView(R.layout.dialog_table);
        setCancelable(false);

        mBtnPrevious = findViewById(R.id.personnel_previous);
        mBtnNext = findViewById(R.id.personnel_next);
        cbTable6 = findViewById(R.id.table_cb_6);
        cbTable4 = findViewById(R.id.table_cb_4);

        mBtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableDialog.this.dismiss();
            }
        });
        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        mBtnNext.setOnClickListener(nextButtonListener);
        mBtnPrevious.setOnClickListener(preButtonListener);
    }

    public void dismissDialog(){
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
