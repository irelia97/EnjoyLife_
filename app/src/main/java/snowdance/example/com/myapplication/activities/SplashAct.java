package snowdance.example.com.myapplication.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import snowdance.example.com.myapplication.MainActivity;
import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.utils.SharedUtils;
import snowdance.example.com.myapplication.utils.StaticClass;
import snowdance.example.com.myapplication.utils.UtilTools;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.activities
 * FILE    Name : SplashAct
 * Creator Name : Snow_Dance
 * Creator Time : 2018/9/30/030 10:53
 * Description  : 闪屏页 Splash
 */

public class SplashAct extends AppCompatActivity {

    /**
     * 延迟2秒
     * 判断是否第一次进入App
     * 自定义字体
     * 自定义主题
     *
     * @param savedInstanceState
     */

    private TextView tv_splash;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticClass.HANDLER_SPLASH:
                    if( isFirst() ) {
                        startActivity(new Intent(SplashAct.this, GuideAct.class));
                    }else{
                        startActivity(new Intent(SplashAct.this, LoginAct.class));
                    }
                    finish();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
    }

    //  初始化View
    private void initView(){
        //  发送延迟消息，延迟2s，之后转到handler的switch中
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH, 2000);
        tv_splash = findViewById(R.id.tv_splash);
        //  设置字体
        UtilTools.setFont(this, tv_splash, "fonts/simkai.ttf");
    }

    private boolean isFirst(){
        //  第一次进入时，SharedUtils为空，tag读取到默认值true
        boolean tag = SharedUtils.getBoolean(this, StaticClass.ISFIRST, true);
        if( tag ){
            //  之后将StaticClass.ISFIRST作为key存储进去，value存储是false
            //  这样之后运行时tag取到的就是false了
            SharedUtils.putBoolean(this, StaticClass.ISFIRST, false);
            return true;
        }else{
            return false;
        }
    }

    //  禁用返回键
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
