package snowdance.example.com.myapplication.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.chrisbanes.photoview.PhotoView;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.utils
 * FILE    Name : PhotoViewPager
 * Creator Name : Snow_Dance
 * Creator Time : 2019/1/11/011 0:31
 * Description  : 自定义ViewPager解决ViewPager下PhotoView缩放IllegalArgumentException问题
 */

public class PhotoViewPager extends ViewPager {
    public PhotoViewPager(Context context){
        super(context);
    }

    public PhotoViewPager(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return false;
    }
}
