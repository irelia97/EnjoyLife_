package snowdance.example.com.myapplication.utils;

import android.util.Log;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.utils
 * FILE    Name : MLog
 * Creator Name : Snow_Dance
 * Creator Time : 2018/9/30/30 9:56
 * Description  : My Debug class
 */

public class MLog {
    //  布尔值DEBUG，是否开启开关
    //  不需要调试时，令DEBUG为false，即可关闭调试信息
    //  不必自己手动注释或删除
    public static boolean DEBUG = true;
    //  TAG
    public static final String TAG = "EnjoyLife";

    //  等级 debug, info, warning, error, fault
    public static void d(String text){
        if( DEBUG ){
            Log.d(TAG, text);
        }
    }

    public static void i(String text){
        if( DEBUG ){
            Log.i(TAG, text);
        }
    }

    public static void w(String text){
        if( DEBUG ){
            Log.w(TAG, text);
        }
    }

    public static void e(String text){
        if( DEBUG ){
            Log.e(TAG, text);
        }
    }

    public static void f(String text){
        if( DEBUG ){
            Log.wtf(TAG, text);
        }
    }
}
