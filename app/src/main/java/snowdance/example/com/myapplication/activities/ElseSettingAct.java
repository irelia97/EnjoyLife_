package snowdance.example.com.myapplication.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Switch;

import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.service.SmsService;
import snowdance.example.com.myapplication.utils.MLog;
import snowdance.example.com.myapplication.utils.SharedUtils;
import snowdance.example.com.myapplication.utils.UtilTools;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.activities
 * FILE    Name : ElseSettingAct
 * Creator Name : Snow_Dance
 * Creator Time : 2019/5/6/006 23:37
 * Description  : 其它设置
 */

public class ElseSettingAct extends BaseAct implements View.OnClickListener {

    private Switch sw_TTS;
    private Switch sw_message;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elsesetting);

        initView();
    }

    private void initView(){
        sw_TTS = findViewById(R.id.sw_TTS);
        sw_TTS.setOnClickListener(this);
        boolean TTS_state = SharedUtils.getBoolean(this, "TTS", false);
        sw_TTS.setChecked(TTS_state);

        sw_message = findViewById(R.id.sw_message);
        sw_message.setOnClickListener(this);
        boolean message_state = SharedUtils.getBoolean(this, "message", false);
        sw_message.setChecked(message_state);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sw_TTS:
                boolean TTS_state = sw_TTS.isSelected();
                //  设置为选中状态相反
                sw_TTS.setSelected(!TTS_state);
                SharedUtils.putBoolean(this, "TTS", sw_TTS.isChecked());
                break;
            case R.id.sw_message:
                boolean message_state = sw_message.isSelected();
                //  设置为选中状态相反
                sw_message.setSelected(!message_state);
                SharedUtils.putBoolean(this, "message", sw_message.isChecked());
                //  选中，开启短信弹窗提醒服务；否则关闭
                if( sw_message.isChecked() ){
                    //  动态获取权限
                    //  是否有该权限
                    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                            != PackageManager.PERMISSION_GRANTED){
                        //  是否拒绝申请过
                        if( !ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.RECEIVE_SMS) ){
                            //  申请权限
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.RECEIVE_SMS}, 123);
                        }
                    }
                    //  开启短信到达弹窗服务
                    startService(new Intent(this, SmsService.class));
                }else{
                    stopService(new Intent(this, SmsService.class));
                }


                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if( requestCode==123 && grantResults.length!=0
                && grantResults[0]==PackageManager.PERMISSION_GRANTED ){
            //  同意该权限
            MLog.d("同意申请接收短信权限！");
        }else{
            //  拒绝该权限
            MLog.d("拒绝申请接收短信权限！");
        }
    }
}
