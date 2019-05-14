package snowdance.example.com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import snowdance.example.com.myapplication.activities.SettingAct;
import snowdance.example.com.myapplication.fragment.BeautyFragment;
import snowdance.example.com.myapplication.fragment.ManagerFragment;
import snowdance.example.com.myapplication.fragment.PersonFragment;
import snowdance.example.com.myapplication.fragment.WechatFragment;
import snowdance.example.com.myapplication.utils.MLog;
import snowdance.example.com.myapplication.utils.SharedUtils;
import snowdance.example.com.myapplication.utils.StaticClass;
import snowdance.example.com.myapplication.utils.UtilTools;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //  TabLayout
    private TabLayout mTablayout;
    //  ViewPager
    private ViewPager mViewPager;
    //  Title
    private List<String> mTitle;
    //  Fragment
    private List<Fragment> mFragment;
    //  Float Button
    private FloatingActionButton mFabSetting;

    private ManagerFragment managerFragment;
    private WechatFragment  wechatFragment;
    private BeautyFragment  beautyFragment;
    private PersonFragment  personFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  去除顶部阴影
        getSupportActionBar().setElevation(0);

        initData();
        initView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        MLog.DEBUG = true;
//        SharedUtils.putString(this, "lyy", "LiuYongYang");
//        String name, def = "Not found";
//        name = SharedUtils.getString(this, "asd", def);
//        MLog.d(name);
//        name = SharedUtils.getString(this, "lyy", def);
//        MLog.d(name);

//        CrashReport.testJavaCrash();
    }

    //  Init Data
    private void initData() {
        //  顺序添加标题名
        mTitle = new ArrayList<>();
        mTitle.add( this.getString(R.string.server_manager) );
        mTitle.add( this.getString(R.string.weixin_article) );
        mTitle.add( this.getString(R.string.beauty_graph) );
        mTitle.add( this.getString(R.string.person_info) );

        //  顺序添加Fragment
        managerFragment = new ManagerFragment();
        wechatFragment  = new WechatFragment();
        beautyFragment  = new BeautyFragment();
        personFragment  = new PersonFragment();
        mFragment = new ArrayList<>();
        mFragment.add( managerFragment );
        mFragment.add( wechatFragment );
        mFragment.add( beautyFragment );
        mFragment.add( personFragment );
    }
    //  Init View
    private void initView() {
        //  悬浮按钮
        mFabSetting = findViewById(R.id.mFab_Setting);
        mFabSetting.setOnClickListener(this);
        mFabSetting.setVisibility(View.GONE);

        //  预加载Fragment，以免滑动时加载，影响用户体验
        mViewPager = findViewById(R.id.mViewPager);
        mViewPager.setOffscreenPageLimit(mFragment.size());
        //  ViewPager滑动监听，在聊天界面隐藏浮动按钮
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
//                UtilTools.hideKeyBoard();
                if( i == 0 ){
                    mFabSetting.setVisibility(View.GONE);
                }else{
                    mFabSetting.setVisibility(View.VISIBLE);
                }

                if( i == 1 ){
                    UtilTools.showSth(MainActivity.this, "Tips:微信文章下拉可刷新");
                }else if( i == 2 ){
                    UtilTools.showSth(MainActivity.this, "Tips:精选图片上滑可刷新");
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        //  设置适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //  选中的item
            @Override
            public Fragment getItem(int i) {
                return mFragment.get(i);
            }
            //  获取item个数
            @Override
            public int getCount() {
                return mFragment.size();
            }
            //  设置标题
            @Override
            public CharSequence getPageTitle(int i) {
                return mTitle.get(i);
            }
        });
        //  绑定TabLayout和ViewPag
        mTablayout = findViewById(R.id.mTabLayout);
        mTablayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mFab_Setting:
                startActivityForResult(new Intent(this, SettingAct.class), 123);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //  必须使用super调用父类方法，否则fragment中的onActivityResult
        //  会调用此活动中的ActivityResult回调方法
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 123:
                if( resultCode == RESULT_OK ){
                    MLog.d("------Return to MainFunction------");
                    MLog.d("Group = " + StaticClass.GROUP + ", Child = " + StaticClass.CHILD);
                    UtilTools.showSth(this, "Group = " + StaticClass.GROUP + ", Child = " + StaticClass.CHILD);
                }
                //  若检测到图片内容选项改变，在这里拿到fragment的引用
                //  调用fragment内的public方法去刷新界面
                beautyFragment.changeImageType();
                wechatFragment.changeNewsType();

                break;
            default:
                break;
        }
    }

    //  点击输入框外隐藏软键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if( event.getAction() == MotionEvent.ACTION_DOWN ){
            View v = getCurrentFocus();
            if( v!=null && (!(v instanceof EditText) ||
                            !(v instanceof AppCompatEditText)) ){
                InputMethodManager im =
                        (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
            //弹出提示，可以有多种方式
                UtilTools.showSth(this, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
