package snowdance.example.com.myapplication.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.entity.MyUser;
import snowdance.example.com.myapplication.utils.UtilTools;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.activities
 * FILE    Name : RegisterAct
 * Creator Name : Snow_Dance
 * Creator Time : 2018/10/7/007 19:58
 * Description  : Register
 */

public class RegisterAct extends BaseAct implements View.OnClickListener {

    private EditText et_user;
    private EditText et_age;
    private EditText et_desc;
    private RadioGroup mRadioGroup;
    private EditText et_password;
    private EditText et_confirm_password;
    private EditText et_email;
    private EditText et_tel;
    private Button registered;

    private boolean isBoy = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        et_user = findViewById(R.id.edit_username);
        et_age  = findViewById(R.id.edit_age);
        et_desc = findViewById(R.id.edit_description);
        mRadioGroup = findViewById(R.id.mRadioGroup);
        et_password = findViewById(R.id.edit_password);
        et_confirm_password = findViewById(R.id.edit_confirm_password);
        et_email = findViewById(R.id.edit_email);
        et_tel = findViewById(R.id.edit_tel);
        registered = findViewById(R.id.bt_registered);
        registered.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_registered:
                //  获取输入框的值, trim()去除空格
                //  姓名
                String name = et_user.getText().toString().trim();
                if( UtilTools.isEmpty(name) || name.length()<6 ) {
                    UtilTools.showSth(this, this.getString(R.string.user_null_error));
                    break;
                }

                //  密码
                String password = et_password.getText().toString().trim();
                if( UtilTools.isEmpty(password) || UtilTools.isSimplePassword(password) ) {
                    UtilTools.showSth(this, this.getString(R.string.simple_password));
                    break;
                }
                //  确认密码
                String confirm_password = et_confirm_password.getText().toString().trim();
                if( UtilTools.isEmpty(confirm_password) ||
                   !UtilTools.isEquals(password, confirm_password) ) {
                    UtilTools.showSth(this, this.getString(R.string.differ_password));
                    break;
                }

                //  年龄
                String age = et_age.getText().toString().trim();
                if( UtilTools.isEmpty(age) ) {
                    //  用户不设置年龄，则默认为18
                    //  否则字符串为空Integer.parseInt()方法会出错
                    age = "18";
                }
                if( !UtilTools.isNormalAge(Integer.parseInt(age)) ){
                    UtilTools.showSth(this, this.getString(R.string.abnormal_age));
                    break;
                }

                //  个人简介
                String desc = et_desc.getText().toString().trim();
                if( UtilTools.isEmpty(desc) ){
                    //  默认个人简介
                    desc = this.getString(R.string.default_Desc);
                }

                //  邮箱
                String email = et_email.getText().toString().trim();
                if( !UtilTools.isEmail(email) ){
                    UtilTools.showSth(this, this.getString(R.string.error_email));
                    break;
                }

                //  手机
                String tel = et_tel.getText().toString().trim();
                if( !UtilTools.isMobileNO(tel) ){
                    UtilTools.showSth(this, this.getString(R.string.error_tel));
                    break;
                }

                //  选择性别
                mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if( checkedId == R.id.rb_boy ){
                            isBoy = true;
                        }else{
                            isBoy = false;
                        }
                    }
                });
                //  创建新用户
                MyUser user = new MyUser();
                user.setNickname(this.getString(R.string.nameless));
                user.setUsername(name);
                user.setPassword(password);
                user.setEmail(email);
//                user.setEmailVerified(true);
                user.setTelephone(tel);
                user.setAge(Integer.parseInt(age));
                user.setSex(isBoy);
                user.setDescription(desc);
                //  注册
                user.signUp(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        if( e == null ){
                            UtilTools.showSth(RegisterAct.this, "注册成功！");
                            finish();
                        }else{
                            UtilTools.showSth(RegisterAct.this, e.toString());
                        }
                    }
                });

                break;
        }
    }
}
