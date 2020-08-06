package com.haeseong.izobonga_custom.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.haeseong.izobonga_custom.R;

import java.util.ArrayList;
import java.util.Objects;

public class TableDialog extends BaseDialog {
    public CheckBox cbTable6, cbTable4;
    private ArrayList<CheckBox> checkBoxList;
    public TextView mTvTable4, mTvTable6;
    private View.OnClickListener nextButtonListener;
    private View.OnClickListener preButtonListener;
    private int table4, table6, selected_position;

    public TableDialog(@NonNull Context context,
                       View.OnClickListener positiveListener, View.OnClickListener preButtonListener ,int table4, int table6) {
        super(context);
        this.nextButtonListener = positiveListener;
        this.preButtonListener = preButtonListener;
        this.table4=table4;
        this.table6=table6;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_table);
        setCancelable(false);
        setFullscreen(true);
        setTranslucentBlack();
        Button mBtnPrevious = findViewById(R.id.table_bt_previous);
        Button mBtnNext = findViewById(R.id.table_bt_next);
        mTvTable4 = findViewById(R.id.table_tv_4);
        mTvTable6 = findViewById(R.id.table_tv_6);
        cbTable6 = findViewById(R.id.table_cb_6);
        cbTable4 = findViewById(R.id.table_cb_4);

        checkBoxList = new ArrayList<>();
        checkBoxList.add(cbTable4);
        checkBoxList.add(cbTable6);

        cbTable4.setChecked(false);
        cbTable6.setChecked(false);
        mBtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableDialog.this.dismiss();
            }
        });

        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        mBtnNext.setOnClickListener(nextButtonListener);
        mBtnPrevious.setOnClickListener(preButtonListener);

        mTvTable4.setText(String.valueOf(table4));
        mTvTable6.setText(String.valueOf(table6));

        cbTable4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox)view).isChecked()){
                    for (int i=0; i<checkBoxList.size(); i++){
                        if (checkBoxList.get(i)==view){
                            selected_position = i;
                        }else{
                            checkBoxList.get(i).setChecked(false);
                        }
                    }
                }else{
                    selected_position=-1;
                }
            }
        });

        cbTable6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox)view).isChecked()){
                    for (int i=0; i<checkBoxList.size(); i++){
                        if (checkBoxList.get(i)==view){
                            selected_position = i;
                        }else{
                            checkBoxList.get(i).setChecked(false);
                        }
                    }
                }else{
                    selected_position=-1;
                }
            }
        });

    }
    public void dismissDialog(){
        cbTable4.setChecked(false);
        cbTable6.setChecked(false);
        dismiss();
    }
}
