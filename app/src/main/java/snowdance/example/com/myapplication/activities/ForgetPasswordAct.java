package snowdance.example.com.myapplication.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.entity.MyUser;
import snowdance.example.com.myapplication.utils.MLog;
import snowdance.example.com.myapplication.utils.StaticClass;
import snowdance.example.com.myapplication.utils.UtilTools;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.activities
 * FILE    Name : ForgetPasswordAct
 * Creator Name : Snow_Dance
 * Creator Time : 2018/10/14/014 0:32
 * Description  : NULL
 */

public class ForgetPasswordAct extends BaseAct implements View.OnClickListener {

    private EditText et_old_password;
    private EditText et_new_password;
    private EditText et_confrim_password;
    private Button bt_modify;

    private EditText et_email;
    private Button bt_sendemail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initView();
    }

    //  初始化View
    private void initView() {
        String lastAct = getIntent().getStringExtra("act_name");
        et_old_password = findViewById(R.id.old_password);
        et_new_password = findViewById(R.id.new_password);
        et_confrim_password = findViewById(R.id.confirm_password);
        bt_modify = findViewById(R.id.bt_modify);
        bt_modify.setOnClickListener(this);

        //  若是从登录界面进入，只能通过邮箱找回
        if( UtilTools.isEquals(lastAct, StaticClass.login_className) ){
            et_old_password.setEnabled(false);
            et_new_password.setEnabled(false);
            et_confrim_password.setEnabled(false);
            bt_modify.setVisibility(View.GONE);
            UtilTools.showSth(this, this.getString(R.string.only_email));
        }

        et_email = findViewById(R.id.edit_email);
        bt_sendemail = findViewById(R.id.bt_sendemail);
        bt_sendemail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_modify:
                //  此方法修改的是当前(或上一次登录)的用户的密码
                String old_password = et_old_password.getText().toString().trim();
                String new_password = et_new_password.getText().toString().trim();
                if( UtilTools.isSimplePassword(new_password) ) {
                    UtilTools.showSth(this, this.getString(R.string.simple_password));
                    break;
                }
                String confirm_password = et_confrim_password.getText().toString().trim();
                if( UtilTools.isEmpty(old_password) || UtilTools.isEmpty(new_password) ||
                    UtilTools.isEmpty(confirm_password) ){
                    UtilTools.showSth(this, this.getString(R.string.textnull));
                }else if( UtilTools.isEquals(old_password, new_password) ){
                    UtilTools.showSth(this, this.getString(R.string.old_new_equals));
                }else if( !UtilTools.isEquals(new_password, confirm_password) ){
                    UtilTools.showSth(this, this.getString(R.string.differ_password));
                }else{
                    MyUser.updateCurrentUserPassword(old_password, new_password
                            , new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if( e == null ){
                                UtilTools.showSth(ForgetPasswordAct.this,
                                        "重置密码成功！");
                                finish();
                            }else{
                                UtilTools.showSth(ForgetPasswordAct.this,
                                        "重置密码失败！\n" + e.getMessage());
                            }
                        }
                    });
                }
                break;
            case R.id.bt_sendemail:
                final String email = et_email.getText().toString().trim();
                if( UtilTools.isEmpty(email) ){
                    UtilTools.showSth(this, this.getString(R.string.textnull));
                    break;
                }else if( !UtilTools.isEmail(email) ){
                    UtilTools.showSth(this, this.getString(R.string.error_email));
                    break;
                }
                //  调用MyUser的静态方法，发送邮件
                MyUser.resetPasswordByEmail(email, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if( e == null ){
                            UtilTools.showSth(ForgetPasswordAct.this,
                                    "邮件已成功发送至：" + email);
                            finish();
                        }else{
                            UtilTools.showSth(ForgetPasswordAct.this,
                                    "邮件发送失败！\n" + e.getMessage());
                        }
                    }
                });
                break;
        }
    }
}
