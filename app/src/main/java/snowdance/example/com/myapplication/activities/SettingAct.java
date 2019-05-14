package snowdance.example.com.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.utils.MLog;
import snowdance.example.com.myapplication.utils.StaticClass;
import snowdance.example.com.myapplication.utils.UtilTools;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.activities
 * FILE    Name : SettingAct
 * Creator Name : Snow_Dance
 * Creator Time : 2018/9/23/023 16:14
 * Description  : NULL
 */

public class SettingAct extends BaseAct implements View.OnClickListener {

    private Button news_Setting;
    private Button photo_Setting;
    private Button else_Setting;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        news_Setting  = findViewById(R.id.news_Setting);
        photo_Setting = findViewById(R.id.photo_Setting);
        else_Setting  = findViewById(R.id.else_Setting);

        news_Setting.setOnClickListener(this);
        photo_Setting.setOnClickListener(this);
        else_Setting.setOnClickListener(this);

        Intent intent = new Intent();
        intent.putExtra("123", "321");
        setResult(RESULT_OK, intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.news_Setting:
                startActivity(new Intent(this, NewsSettingAct.class));
                break;
            case R.id.photo_Setting:
                startActivity(new Intent(this, PhotoSettingAct.class));
                break;
            case R.id.else_Setting:
                startActivity(new Intent(this, ElseSettingAct.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        MLog.d("------OnDestroy------");
        MLog.d("Group = " + StaticClass.GROUP + ", Child = " + StaticClass.CHILD);
        UtilTools.showSth(this, "Group = " + StaticClass.GROUP + ", Child = " + StaticClass.CHILD);
        super.onDestroy();
    }
}

