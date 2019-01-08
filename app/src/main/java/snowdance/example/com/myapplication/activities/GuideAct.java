package snowdance.example.com.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import snowdance.example.com.myapplication.MainActivity;
import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.utils.MLog;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.activities
 * FILE    Name : GuideAct
 * Creator Name : Snow_Dance
 * Creator Time : 2018/9/30/030 11:27
 * Description  : NULL
 */

public class GuideAct extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    //  容器存储引导页
    private View v1, v2, v3;
    private List<View> mList = new ArrayList<>();
    //  引导页小圆点
    private ImageView p1, p2, p3;
    //  跳过
    private ImageView skip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
    }

    //  初始化View
    private void initView() {
        mViewPager = findViewById(R.id.mViewPager);
        v1 = View.inflate(this, R.layout.page_item_one, null);
        v2 = View.inflate(this, R.layout.page_item_two, null);
        v3 = View.inflate(this, R.layout.page_item_three, null);
        //  装载进容器，使用是设置适配器即可
        mList.add(v1); mList.add(v2); mList.add(v3);
        //  设置适配器
        mViewPager.setAdapter( new GuideAdapter() );

        //  设置小圆点ImageView
        p1 = findViewById(R.id.point1);
        p2 = findViewById(R.id.point2);
        p3 = findViewById(R.id.point3);
        //  默认选中第一个
        setPointImage(true, false, false);

        //  跳过图片ImageView
        skip = findViewById(R.id.skip);

        //  skip点击事件，直接进入主页
        skip.setOnClickListener(this);
        //  监听v3的点击事件(进入主页)
        v3.findViewById(R.id.goto_main).setOnClickListener(this);

        //  监听ViewPager滑动
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            //  page切换
            @Override
            public void onPageSelected(int i) {
                MLog.i("Position " + i);
                switch (i){
                    case 0:
                        skip.setVisibility(View.VISIBLE);
                        setPointImage(true, false, false);
                        break;
                    case 1:
                        skip.setVisibility(View.VISIBLE);
                        setPointImage(false, true, false);
                        break;
                    case 2:
                        skip.setVisibility(View.GONE);
                        setPointImage(false, false, true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //  跳过、进入，均为进入主页事件
            case R.id.skip:
                ;
            case R.id.goto_main:
                startActivity(new Intent(this, LoginAct.class));
                finish();
                break;
            default:
                break;
        }
    }

    //  内部类，适配器
    class GuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mList.get(position));
//          super.destroyItem(container, position, object);
        }
    }

    private void setPointImage(boolean isCheck1, boolean isCheck2, boolean isCheck3){
        if( isCheck1 ){
            p1.setBackgroundResource(R.drawable.green_point);
        }else{
            p1.setBackgroundResource(R.drawable.white_point);
        }

        if( isCheck2 ){
            p2.setBackgroundResource(R.drawable.green_point);
        }else{
            p2.setBackgroundResource(R.drawable.white_point);
        }

        if( isCheck3 ){
            p3.setBackgroundResource(R.drawable.green_point);
        }else{
            p3.setBackgroundResource(R.drawable.white_point);
        }
    }
}
