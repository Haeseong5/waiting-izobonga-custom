package com.haeseong.izobonga_custom.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.haeseong.izobonga_custom.R;

public class TicketDialog extends Dialog {

    private Button mPositiveButton;
    private TextView tvWaitingNumber;
    private int mTicket;

    private View.OnClickListener mPositiveListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        hideSoftKey();
        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.dialog_ticket);
        tvWaitingNumber = findViewById(R.id.waiting_dialog_number);
        mPositiveButton = findViewById(R.id.waiting_dialog_finish_button);
        String ticket = String.valueOf(mTicket);
        tvWaitingNumber.setText(ticket);
        mPositiveButton.setOnClickListener(mPositiveListener);
    }
    //생성자 생성
    public TicketDialog(@NonNull Context context, View.OnClickListener positiveListener, int mTicket) {
        super(context);
        this.mPositiveListener = positiveListener;
        this.mTicket = mTicket;
    }

    public void setTicket(int ticket){
        tvWaitingNumber.setText(String.valueOf(ticket));
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


