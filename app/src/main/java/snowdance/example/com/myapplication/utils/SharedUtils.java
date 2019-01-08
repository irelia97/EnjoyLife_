package snowdance.example.com.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.utils
 * FILE    Name : SharedUtils
 * Creator Name : Snow_Dance
 * Creator Time : 2018/9/30/030 10:22
 * Description  : My SharedPreferences
 */

//  封装轻量级数据存储方法SharedPreferences
public class SharedUtils {

    public static final String NAME = "Snow_Dance";
    //  存取实现
    //  存取String
    public static void putString(Context mContext, String key, String value){
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }
    public static String getString(Context mContext, String key, String defString){
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defString);
    }

    //  存取int
    public static void putInt(Context mContext, String key, int value){
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }
    public static int getInt(Context mContext, String key, int defInt){
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defInt);
    }

    //  存取Boolean
    public static void putBoolean(Context mContext, String key, Boolean value){
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }
    public static Boolean getBoolean(Context mContext, String key, Boolean defBoolean){
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defBoolean);
    }

    //  删除单个
    public static void delShare(Context mContext, String key){
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }
    //  清空
    public static void delAll(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().clear();
    }
}
