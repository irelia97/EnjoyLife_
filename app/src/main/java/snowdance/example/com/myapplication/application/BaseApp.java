package snowdance.example.com.myapplication.application;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bmob.v3.Bmob;
import snowdance.example.com.myapplication.utils.StaticClass;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.application
 * FILE    Name : BaseApp
 * Creator Name : Snow_Dance
 * Creator Time : 2018/9/23 12:43
 * Description  : Base of application
 */

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //  初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(),
                StaticClass.BUGLY_APPID, true);
        //  初始化Bomb
        Bmob.initialize(this, StaticClass.Bomb_APPID);
        //  初始化Fresco
//        Fresco.initialize(this);
        // 初始化科大讯飞TTS
        SpeechUtility.createUtility(getApplicationContext(),
                SpeechConstant.APPID + "=" + StaticClass.XunFei_APPID);
    }
}

