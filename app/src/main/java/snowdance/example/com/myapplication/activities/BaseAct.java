package snowdance.example.com.myapplication.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.activities
 * FILE    Name : BaseAct
 * Creator Name : Snow_Dance
 * Creator Time : 2018/9/23 12:46
 * Description  : Base of activities
 */


//  继承统一的属性、接口、方法
public class BaseAct extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  显示返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //  菜单栏操作，响应返回键
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch( item.getItemId() ){
            case android.R.id.home: //  Android自带的返回键Home的ID
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

