package com.haeseong.izobonga_custom.views.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.haeseong.izobonga_custom.R;

public class CheckDialog extends BaseDialog {

    private View.OnClickListener nextButtonListener;
    private View.OnClickListener preButtonListener;
    public TextView tvContent;

    public CheckDialog(@NonNull Context context, View.OnClickListener positiveListener, View.OnClickListener preButtonListener) {
        super(context);
        this.nextButtonListener = positiveListener;
        this.preButtonListener = preButtonListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_check);
        setCancelable(false);
        //initView
        Button btCancel = findViewById(R.id.check_dialog_bt_cancel);
        Button btCall = findViewById(R.id.check_dialog_bt_call);
        tvContent = findViewById(R.id.check_dialog_tv_content);

        btCall.setOnClickListener(nextButtonListener);
        btCancel.setOnClickListener(preButtonListener);
    }
}
