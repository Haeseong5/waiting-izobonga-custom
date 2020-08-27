package com.haeseong.izobonga_custom.views.activities;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.haeseong.izobonga_custom.BaseActivity;
import com.haeseong.izobonga_custom.R;
import com.haeseong.izobonga_custom.interfaces.CallActivityView;
import com.haeseong.izobonga_custom.models.Customer;
import com.haeseong.izobonga_custom.services.CallService;
import com.haeseong.izobonga_custom.views.adapters.CallAdapter;
import com.gun0912.tedpermission.TedPermission;
import com.haeseong.izobonga_custom.views.dialogs.CheckDialog;
import com.haeseong.izobonga_custom.views.dialogs.PersonnelDialog;

import java.util.ArrayList;

//로컬영역 추가
public class CallActivity extends BaseActivity implements CallActivityView{
    private final String TAG = CallActivity.class.getName();
    MediaPlayer mMediaPlayer;
    RecyclerView recyclerView;
    TextView tvNoCustomerText;
    Toolbar mToolbar;
    CallAdapter adapter;
    ArrayList<Customer> customers;
    CheckDialog mCheckDialog;
    int mSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //상태바 없애기
        setContentView(R.layout.activity_call);
        initView();
        tryInitWaitingCustomer();
        setWaitingDataListener();
        if (mMediaPlayer == null){
            mMediaPlayer = MediaPlayer.create(this, R.raw.sound_alarm_file);
        }
        checkPermission();
    }

    private void initView(){
        tvNoCustomerText = findViewById(R.id.call_tv_message);
        mToolbar = findViewById(R.id.base_toolbar);
        setSupportActionBar(mToolbar);

        // Get the ActionBar here to configure the way it behaves.
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
            actionBar.setTitle(getString(R.string.call_title));
        }
    }

    public void initRecyclerView() {
        recyclerView = findViewById(R.id.call_recyclerView);
        // layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // adapter
        adapter = new CallAdapter(getApplicationContext(), customers);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CallAdapter.OnItemClickListener() {

            @Override
            public void onItemClickCall(View v, int position) {
//                showProgressDialog();
//                tryCallCustomer(customers.get(position).getDocID(), position); //docID, index
                mSelectedPosition = position;
                showCheckDialog();
            }

            @Override
            public void onItemClickDelete(View v, int position) {
                showProgressDialog();
                tryDelete(customers.get(position).getDocID(), position); //docID, index
            }
        });
    }

    private void showCheckDialog(){
        if (mCheckDialog == null) {
            Log.d("showTotalDialog", "null");
            mCheckDialog = new CheckDialog(CallActivity.this, nextOnClickListener, preOnClickListener);
            mCheckDialog.setCancelable(false);
            mCheckDialog.setCanceledOnTouchOutside(false);
        }
        mCheckDialog.show();
        formattingMessage();
    }

    private View.OnClickListener preOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (mCheckDialog != null && mCheckDialog.isShowing()){
                mCheckDialog.dismiss();
                mCheckDialog = null;
            }
        }
    };

    private View.OnClickListener nextOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
                showProgressDialog();
                tryCallCustomer(customers.get(mSelectedPosition).getDocID(), mSelectedPosition); //docID, index
        }
    };

    public void setZeroDataMessage(){
        if (customers.size()==0){
            tvNoCustomerText.setVisibility(View.VISIBLE);
        }else{
            tvNoCustomerText.setVisibility(View.GONE);
        }
        if (getSupportActionBar()!=null)
            getSupportActionBar().setTitle(getString(R.string.call_title)+" "+customers.size()+"명");
    }
    //웨이팅 고객 초기화. onCreate()에서 호출됨.
    public void tryInitWaitingCustomer() {
        showProgressDialog();
        CallService callService = new CallService(this);
        callService.initWaitingCustomer();
    }

    //데이터 변경 시 호출되는 리스너 등록
    private void setWaitingDataListener() {
        CallService callService = new CallService(this);
        callService.setWaitingEventListener();
    }

    //대기고객 호출하기 버튼 누르면 DB에서 아이템 제거 후 리스트 갱신.
    private void tryCallCustomer(final String docID, final int position) {
        showProgressDialog();
        CallService callService = new CallService(this);
        callService.callWaitingCustomer(docID, position);
    }
    //대기고객 호출하기 버튼 누르면 DB에서 아이템 제거 후 리스트 갱신.
    private void tryDelete(final String docID, final int position) {
        showProgressDialog();
        CallService callService = new CallService(this);
        if(docID != null){
            callService.deleteCustomer(docID, position);
        }else{
            printToast("오류가 발생했습니다.");
        }
    }
    //대기고객 호출하기 버튼 누르면 DB에서 아이템 제거 후 리스트 갱신.
    private void tryResetTicket() {
        showProgressDialog();
        CallService callService = new CallService(this);
        callService.resetTicket();
    }

    private void tryCallSMS(String phoneNumber, String message, int ticket, int position){
        CallService callService = new CallService(this);
        callService.sendSMS(phoneNumber, message, ticket, position);
    }

    private void tryAlarm(){
        if (mMediaPlayer != null){
            mMediaPlayer.start();
        }
    }

    private void formattingMessage(){
        boolean isTable6 =  customers.get(mSelectedPosition).isTable6();
        int table;
        if (isTable6){
            table = 6;
        }else{
            table = 4;
        }
        String message = String.format(getString(R.string.check_dialog_message),
                mSelectedPosition+1,
                customers.get(mSelectedPosition).getTicket(),
                customers.get(mSelectedPosition).getPersonnel(),
                customers.get(mSelectedPosition).getChild(),
                table);
        mCheckDialog.tvContent.setText(message);
    }

    @Override
    public void initCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
        initRecyclerView();
        adapter.notifyDataSetChanged();
        setZeroDataMessage();
        hideProgressDialog();
    }

    //고객이 추가되었을 때 호출 됨.
    @Override
    public void added(Customer customer) {
        printLog("modified", "modify");
        tryAlarm();
        customers.add(customer);
        setZeroDataMessage();
        adapter.notifyDataSetChanged();
    }

    //고객이 삭제되었을 때 호출됨.
    @Override
    public void called(int position) {
        tryCallSMS(customers.get(position).getPhone(), getString(R.string.sms_message), customers.get(position).getTicket(), position); //문자메세지 전송
        //몇 분 내 입장 안하면 취소
        adapter.removeItem(position);
        setZeroDataMessage();
        adapter.notifyDataSetChanged();
        if (mCheckDialog != null && mCheckDialog.isShowing()){
            mCheckDialog.dismiss();
            mCheckDialog = null;
        }
        hideProgressDialog();
    }

    @Override
    public void deleted(int position) {
        adapter.removeItem(position);
        setZeroDataMessage();
        adapter.notifyDataSetChanged();
        hideProgressDialog();
    }

    @Override
    public void validateSuccessSMS(String message) {
        printToast(message);
    }

    @Override
    public void validateFailureSMS(String message) {
        printToast(message);
    }

    @Override
    public void validateSuccessResetTicket(String message) {
        hideProgressDialog();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_manage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_item_reset:
                tryResetTicket();
                break;
            case android.R.id.home: //toolbar의 back버튼 눌렀을 때 동작
                finish();
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
//        mMediaPlayerService.remove();
        if (mMediaPlayer!=null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if (mCheckDialog != null && mCheckDialog.isShowing()) {
            mCheckDialog.dismiss();
            mCheckDialog = null;
        }
        hideProgressDialog();
        super.onStop();
    }
}
