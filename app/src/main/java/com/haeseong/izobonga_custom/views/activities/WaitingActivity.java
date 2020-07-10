package com.haeseong.izobonga_custom.views.activities;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.haeseong.izobonga_custom.BaseActivity;
import com.haeseong.izobonga_custom.FireBaseHelper;
import com.haeseong.izobonga_custom.R;
import com.haeseong.izobonga_custom.databinding.ActivityWaitingBinding;
import com.haeseong.izobonga_custom.interfaces.WaitingActivityView;
import com.haeseong.izobonga_custom.services.WaitingService;
import com.haeseong.izobonga_custom.views.dialogs.PersonnelDialog;
import com.haeseong.izobonga_custom.views.dialogs.TableDialog;
import com.haeseong.izobonga_custom.views.dialogs.TicketDialog;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class WaitingActivity extends BaseActivity implements WaitingActivityView {
    private final String TAG = WaitingActivity.class.getName();
    FireBaseHelper firebaseHelper;
    ActivityWaitingBinding binding;
    ArrayList<String> numbers;
    TicketDialog mTicketDialog;
    PersonnelDialog mTotalDialog, mChildDialog;
    TableDialog mTableDialog;
    public TextToSpeech tts;
    int table4, table6;
    int mChild;
    int mTotal;

    //타이머
    Handler mHandler = new Handler();
    int mCounter =0;
    static TimerTask mTimerTask;
    Timer mTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        hideSoftKey();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_waiting);
        binding.setActivity(this);

        //set background image
        Glide.with(this).load(R.raw.background_gif).into(binding.waitingIvBackground);

        firebaseHelper = new FireBaseHelper();
        setWaitingListener(); //FireStore Change Listener
        numbers = new ArrayList<>();

        binding.waitingEtNumberBox.addTextChangedListener(new PhoneNumberFormattingTextWatcher()); //입력하면 phone number form 으로 만들기
        binding.waitingEtNumberBox.setEnabled(false); //editText 사용 불가능하게 만들기
        binding.waitingEtNumberBox.setFocusable(false);

        //현재 웨이팅만 띄우고 키패드 숨기기
        binding.waitingCountLayout.setVisibility(View.VISIBLE);
        binding.keyPadLayout.setVisibility(View.GONE);

        binding.setClickCallback(clickListener);

//        //타이머 생성
        mTimerTask = timerTaskMaker();
        mTimer.schedule(mTimerTask, 0, 3000);

        if (tts == null){
            tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    tts.setLanguage(Locale.KOREA);
                }
            });
        }
    }

    private void tryWaiting(int personnelNumber, int childNumber, boolean table6){
        WaitingService waitingService = new WaitingService(WaitingActivity.this);
        waitingService.increaseWaitingCount(
                Timestamp.now(),
                binding.waitingEtNumberBox.getText().toString(),
                personnelNumber,
                childNumber,
                table6);
    }
    private View.OnClickListener ticketListener = new View.OnClickListener() {
        public void onClick(View v) {
            mTicketDialog.dismiss();
            mTicketDialog = null;
            binding.waitingEtNumberBox.setText("010-");
            binding.waitingEtNumberBox.addTextChangedListener(new PhoneNumberFormattingTextWatcher()); //입력하면 phone number form 으로 만들기
            binding.keyPadLayout.setVisibility(View.VISIBLE);
            //타이머재생성
            mTimerTask = timerTaskMaker();
            mTimer.schedule(mTimerTask, 0, 3000);
        }
    };

    //총 인원 수 선택 Dialog 다음 버튼 클릭 시 이벤트 처리
    private View.OnClickListener adultNextListener = new View.OnClickListener() {
        public void onClick(View v) {
            String personnelNumber = mTotalDialog.mTvNumber.getText().toString();
            mTotal = Integer.parseInt(personnelNumber);
            if ( mTotal < 2){
                printToast("2인 이상부터 예약 가능합니다,");
            }else {
                mTotalDialog.hide();
                showChildDialog();
            }
        }
    };

    //아동 인원 수 선택 Dialog 다음 버튼 클릭 시 이벤트 처리
    private View.OnClickListener childNextListener = new View.OnClickListener() {
        public void onClick(View v) {
            mChild = Integer.parseInt(mChildDialog.mTvNumber.getText().toString());
            if (mTotal+mChild>4){
                mChildDialog.hide();
                showTableDialog();
            }else{
                showProgressDialog();
                tryWaiting(mTotal, mChild, false);
            }
        }
    };

    private View.OnClickListener tableNextListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (mTableDialog.cbTable4.isChecked() || mTableDialog.cbTable6.isChecked()){
                showProgressDialog();
                mTableDialog.hide();
                boolean table = mTableDialog.cbTable6.isChecked();
                tryWaiting(mTotal, mChild, table);
            } else{
                printToast("테이블을 선택해주세요!");
            }
        }
    };

    private View.OnClickListener totalPreListener = new View.OnClickListener() {
        public void onClick(View v) {
            mTotal = 0;
            if (mTotalDialog != null){
                mTotalDialog.dismissDialog();
            }
            binding.keyPadLayout.setVisibility(View.VISIBLE);
            //타이머재생성
            mTimerTask = timerTaskMaker();
            mTimer.schedule(mTimerTask, 0, 3000);
        }
    };

    //아동 인원 수 선택 Dialog 다음, 이전 클릭 시 이벤트 처리
    private View.OnClickListener childPreListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (mChildDialog != null && mChildDialog.isShowing()){
                mChildDialog.dismissDialog();
                mTotalDialog.show();
            }
        }
    };

    private View.OnClickListener tablePreListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (mTableDialog != null && mTableDialog.isShowing()){
                mTableDialog.dismissDialog();
                mChildDialog.show();
            }
        }
    };

    private void showTotalDialog(){
        if (mTotalDialog == null) {
            mTotalDialog = new PersonnelDialog(WaitingActivity.
                    this, adultNextListener, totalPreListener, getDrawable(R.drawable.adult_dialog_text));
            mTotalDialog.setCancelable(false);
            mTotalDialog.setCanceledOnTouchOutside(false);
            mTotalDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        }
        mTotal = 0;
        mTotalDialog.show();
    }
    private void showChildDialog(){
        if (mChildDialog == null){
            mChildDialog = new PersonnelDialog(WaitingActivity.this, childNextListener, childPreListener, getDrawable(R.drawable.child_dialog_text));
            mChildDialog.setCancelable(false);
            mChildDialog.setCanceledOnTouchOutside(false);
            mChildDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        }
        mChild = 0;
        mChildDialog.show();
    }
    private void showTableDialog(){
        if (mTableDialog == null){
            mTableDialog = new TableDialog(WaitingActivity.this, tableNextListener, tablePreListener, table4, table6);
            mTableDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
            mTableDialog.setCancelable(false);
            mTableDialog.setCanceledOnTouchOutside(false);
        }else{
            mTableDialog.mTvTable4.setText(String.valueOf(table4));
            mTableDialog.mTvTable6.setText(String.valueOf(table6));
        }
        mTableDialog.show();
    }
    private void showTicketDialog(int ticket){
        if (mTicketDialog == null){
            mTicketDialog = new TicketDialog(WaitingActivity.this, ticketListener, ticket);
            mTicketDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
            mTicketDialog.setCancelable(false);
            mTicketDialog.setCanceledOnTouchOutside(false);
        }
        mTicketDialog.show();
    }

    public void setWaitingListener(){
        WaitingService waitingService = new WaitingService(this);
        waitingService.setWaitingEventListener();
    }


    //NumberKeyPad ClickListener
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCounter = 0;
            if(v == null)
                return;
            switch(v.getId()) {
                case R.id.btn1:
                    binding.waitingEtNumberBox.append("1");
                    break;
                case R.id.btn2:
                    binding.waitingEtNumberBox.append("2");
                    break;
                case R.id.btn3:
                    binding.waitingEtNumberBox.append("3");
                    break;
                case R.id.btn4:
                    binding.waitingEtNumberBox.append("4");
                    break;
                case R.id.btn5:
                    binding.waitingEtNumberBox.append("5");
                    break;
                case R.id.btn6:
                    binding.waitingEtNumberBox.append("6");
                    break;
                case R.id.btn7:
                    binding.waitingEtNumberBox.append("7");
                    break;
                case R.id.btn8:
                    binding.waitingEtNumberBox.append("8");
                    break;
                case R.id.btn9:
                    binding.waitingEtNumberBox.append("9");
                    break;
                case R.id.btn_cancel:
                    int length = binding.waitingEtNumberBox.getText().length();
                    if (length > 3) {
                        binding.waitingEtNumberBox.getText().delete(length - 1, length);
                    }
                    break;
                case R.id.btn0:
                    binding.waitingEtNumberBox.append("0");
                    break;
                case R.id.btn_input:
                    String phoneNumber = binding.waitingEtNumberBox.getText().toString();
                    boolean isCheck = binding.waitingCheckBoxAgree.isChecked();
                    if (isCheck){
                        if(phoneNumber.length() != 13){
                            printToast("잘 못된 휴대폰 번호입니다. 다시 입력해주세요.");
                        }else{
                            binding.keyPadLayout.setVisibility(View.GONE);
                            mTimerTask.cancel(); //다이어로그 출력전 타이머 정지
                            showTotalDialog();
                        }
                    }else{
                        printToast("서비스 이용약관에 동의해주셔야 합니다!");
                    }
                    break;
            }
        }
    };

    @Override
    public void validateSuccess(String message, int ticket) {
        hideProgressDialog();
        if (mChildDialog != null  && mChildDialog.isShowing()){
                mChildDialog.dismissDialog();
        }
        if (mTotalDialog != null  && mTotalDialog.isShowing()){
            mTotalDialog.dismissDialog();
        }
        if (mTableDialog != null  && mTableDialog.isShowing()){
            mTableDialog.cbTable4.setChecked(false);
            mTableDialog.cbTable6.setChecked(false);
            mTableDialog.dismissDialog();
        }
        showTicketDialog(ticket);
    }

    @Override
    public void validateFailure(String message) {
        hideProgressDialog();
        printToast(message);
        printLog(TAG, message);
    }

    @Override
    public void modified(long size, int table4, int table6) {
        binding.waitingCountText.setText(size + "팀");
        this.table4 = table4;
        this.table6 = table6;
    }


    @Override
    public void speak(String ticket) {
        if (tts != null){
            String message = String.format(getString(R.string.tts_message),ticket);
            Log.d(TAG, message);

            tts.setSpeechRate(1.0f); //읽는 속도 설정 : 기본
            tts.setPitch((float) 1);      // 음량
//        tts.setSpeechRate(""); //톤 조절
            tts.speak(message , TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mTTsService.removeTTS();
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }

    private TimerTask timerTaskMaker(){
        return new TimerTask() {
            public void run() {
                mHandler.post(new Runnable() {
                    public void run() {
                        mCounter++;
                        Log.d("mCounter", String.valueOf(mCounter));
                        //you can do stuffs here say  like if (mCounter==15) { do something}
                        if (mCounter == 2){
                            binding.waitingCountLayout.setVisibility(View.VISIBLE);
                            binding.keyPadLayout.setVisibility(View.GONE);
                        }
                    }
                });
            }};
    }
    @Override
    protected void onPause() {
        super.onPause();
        printToast("pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        printToast("resume");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch(action) {
            case MotionEvent.ACTION_DOWN :    //화면을 터치했을때
                mCounter = 0;
                binding.waitingCountLayout.setVisibility(View.GONE);
                binding.keyPadLayout.setVisibility(View.VISIBLE);

                break;
        }
        return super.onTouchEvent(event);
    }
}
