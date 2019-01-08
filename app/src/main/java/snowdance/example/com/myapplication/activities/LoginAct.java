package snowdance.example.com.myapplication.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import snowdance.example.com.myapplication.MainActivity;
import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.entity.MyUser;
import snowdance.example.com.myapplication.utils.MLog;
import snowdance.example.com.myapplication.utils.SharedUtils;
import snowdance.example.com.myapplication.utils.StaticClass;
import snowdance.example.com.myapplication.utils.UtilTools;
import snowdance.example.com.myapplication.view.CustomDialog;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.activities
 * FILE    Name : LoginAct
 * Creator Name : Snow_Dance
 * Creator Time : 2018/10/7/007 19:24
 * Description  : Login
 */

public class LoginAct extends AppCompatActivity implements View.OnClickListener {

    //  注册按钮
    private Button bt_register;
    private EditText et_username;
    private EditText et_password;
    private Button bt_login;

    private CheckBox remember_password;
    private TextView tv_forget;

    //  自定义登入提示框
    private CustomDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        bt_register = findViewById(R.id.bt_register);
        bt_register.setOnClickListener(this);

        et_username = findViewById(R.id.login_username);
        et_password = findViewById(R.id.login_password);
        bt_login = findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);

        //  是否记住密码，在onDestroy()方法中操作
        remember_password = findViewById(R.id.remember_password);
        //  缺省值为false，不记住
        boolean def_select = SharedUtils.getBoolean(this
                , "remember_password", false);
        remember_password.setChecked(def_select);

        if( def_select ){
            et_username.setText(SharedUtils.getString(this
                    , "username", ""));
            et_password.setText(SharedUtils.getString(this
                    , "password", ""));
        }

        //  忘记密码
        tv_forget = findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);

        //  登录进度提示框
        dialog = new CustomDialog(this, 300, 300,
                R.layout.dialog_loading, R.style.Theme_dialog,
                Gravity.CENTER);
        //  设置屏幕外点击无效
        dialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                String userName = et_username.getText().toString().trim();
                String passWord = et_password.getText().toString().trim();
                //  用户名或密码不能为空
                if( UtilTools.isEmpty(userName) || UtilTools.isEmpty(passWord) ){
                    UtilTools.showSth(this, this.getString(R.string.textnull));
                    break;
                }else{
                    //  显示Dialog，"正在登录..."
                    dialog.show();
                    //  因为接下里内部类需要访问user，所以加上final关键字
                    //  final关键字的目的就是为了保证内部类和外部函数对变量“认识”的一致性
                    final MyUser user = new MyUser();
                    user.setUsername(userName);
                    user.setPassword(passWord);
                    user.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            //  消除Dialog
                            dialog.dismiss();
                            //  异常为空，说明匹配成功
                            //  否则失败
                            if( e == null ){
                                //  判断邮箱是否验证
                                if( user.getEmailVerified() ){
                                    startActivity(new Intent(LoginAct.this
                                            , MainActivity.class));
                                    finish();
                                }else{
                                    UtilTools.showSth(LoginAct.this,
                                            "邮箱尚未验证！请前往邮箱进行验证！");
                                }
                            }else{
                                UtilTools.showSth(LoginAct.this,
                                        "登录失败！\n" + e.getMessage());
                            }
                        }
                    });
                }
                break;
            case R.id.bt_register:
                startActivity(new Intent(this, RegisterAct.class));
                break;
            case R.id.tv_forget:
                Intent intent = new Intent(this, ForgetPasswordAct.class);
                intent.putExtra("act_name", StaticClass.login_className);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedUtils.putBoolean(this, "remember_password"
                , remember_password.isChecked());
        //  记住密码
        if( remember_password.isChecked() ){
            SharedUtils.putString(this, "username"
                    , et_username.getText().toString().trim());
            SharedUtils.putString(this, "password"
                    , et_password.getText().toString().trim());
        }else{
            SharedUtils.delShare(this, "username");
            SharedUtils.delShare(this, "password");
        }
    }
}
