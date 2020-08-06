package com.haeseong.izobonga_custom.views.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.haeseong.izobonga_custom.R;

import java.util.Objects;

public class LoadingDialog extends BaseDialog {
    private Context context;

    public LoadingDialog(Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //로딩바의 불필요한 기능들을 없앤다.
        if (getWindow() != null){
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); //로고를 제외한 나머지 반투명처리
        }

        setCanceledOnTouchOutside(false); // 로딩바이미지 바깥부분 터치해도 취소안되게 하기

        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        setFullscreen(true);
        ImageView ivLoading = findViewById(R.id.loading_image);
        ivLoading.setImageResource(R.drawable.ic_launcher_background); //로딩 이미지 설정
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.loading_anim_rotation); //회전 애니메이션
        ivLoading.setAnimation(anim); //로딩이미지에 회전 애니메이션 설정
    }
    @Override
    public void show() {
        super.show();
    }
    @Override
    public void dismiss() {
        super.dismiss();
    }
}
