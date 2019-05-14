package snowdance.example.com.myapplication.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import snowdance.example.com.myapplication.utils.MLog;
import snowdance.example.com.myapplication.utils.UtilTools;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.service
 * FILE    Name : SmsService
 * Creator Name : Snow_Dance
 * Creator Time : 2019/5/7/007 1:13
 * Description  : NULL
 */

public class SmsService extends Service {

    private SmsReceiver  mSmsService;
    private final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MLog.i("Msg Service open!");
        initSms();
    }

    @Override
    public void onDestroy() {
        MLog.i("Msg Service close!");
        //  关闭广播
        unregisterReceiver(mSmsService);
        super.onDestroy();
    }

    private void initSms(){
        //  动态注册广播
        mSmsService = new SmsReceiver();
        IntentFilter intent = new IntentFilter();
        //  短信到达即会添加action
        intent.addAction(SMS_ACTION);
        //  设置权限
        intent.setPriority(Integer.MAX_VALUE);
        //  注册
        registerReceiver(mSmsService, intent);
    }

    //  短信广播
    public class SmsReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //  接收到达的action
            String action = intent.getAction();
            if( UtilTools.isEquals(action, SMS_ACTION) ){
                //  若匹配，说明有短信到达
                MLog.d("短信到达！");
            }
        }
    }
}
