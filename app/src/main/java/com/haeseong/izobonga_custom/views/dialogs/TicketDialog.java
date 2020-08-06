package com.haeseong.izobonga_custom.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.haeseong.izobonga_custom.R;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class TicketDialog extends BaseDialog {
    private int mTicket;
    private View.OnClickListener mPositiveListener;
    private TimerTask mTimerTask;
    private Timer mTimer = new Timer();
    private int mCount;
    private Handler handler ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_ticket);
        setFullscreen(true);
        setTranslucentBlack();
        TextView tvWaitingNumber = findViewById(R.id.waiting_dialog_number);
        ImageView mPositiveButton = findViewById(R.id.waiting_dialog_finish);
//        ImageView mIvTitle = findViewById(R.id.waiting_dialog_title);
        String ticket = String.valueOf(mTicket);
        tvWaitingNumber.setText(ticket);
        mPositiveButton.setOnClickListener(mPositiveListener);

        handler = new Handler();
    }

    public TicketDialog(@NonNull Context context, View.OnClickListener positiveListener, int mTicket) {
        super(context);
        this.mPositiveListener = positiveListener;
        this.mTicket = mTicket;
    }

}


