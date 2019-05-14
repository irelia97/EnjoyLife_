package snowdance.example.com.myapplication.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import snowdance.example.com.myapplication.R;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.view
 * FILE    Name : CustomDialog
 * Creator Name : Snow_Dance
 * Creator Time : 2018/10/14/014 2:00
 * Description  : 自定义Dialog
 */

public class CustomDialog extends Dialog {

    //  定义模板
    public CustomDialog(Context context, int layout, int style){
        this(context, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, layout, style, Gravity.CENTER);
    }

    //  定义属性
    public CustomDialog(Context context, int width, int height, int layout
            , int style, int gravity, int anim) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width  = width;
        layoutParams.height = height;
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);
        window.setWindowAnimations(anim);
    }

    //  实例化构造方法
    public CustomDialog(Context context, int width, int height, int layout
            , int style, int gravity){
        this(context, width, height, layout, style, gravity, R.style.pop_anim_style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
