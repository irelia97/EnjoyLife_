package snowdance.example.com.myapplication.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.utils
 * FILE    Name : UtilTools
 * Creator Name : Snow_Dance
 * Creator Time : 2018/9/23/023 13:02
 * Description  : Normal tools encapsulation
 */

public class UtilTools {

    //  设置字体
    public static void setFont(Context context, TextView textView, String path){
        Typeface fontType = Typeface.createFromAsset(context.getAssets(), path);
        textView.setTypeface(fontType);
    }

    //  判断是否为空
    public static boolean isEmpty(String value){
        return TextUtils.isEmpty(value);
    }

    //  正则判断手机号码
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    //  正则判断邮编
    public static boolean isZip(String zipString){
        String str = "^[1-9][0-9]{5}$";
        return Pattern.compile(str).matcher(zipString).matches();
    }

    //  正则判断邮箱
    public static boolean isEmail(String email){
        if (null==email || email.isEmpty() ) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    //  判断是否为简单密码
    public static boolean isSimplePassword(String password){
        String reg1 = "^[0-9]*";
        String reg2 = "^[a-zA-Z]*";
        return password.length()<6 || password.matches(reg1) || password.matches(reg2);
    }

    //  判断密码是否相等
    public static boolean isEquals(String s1, String s2){
        return TextUtils.equals(s1, s2);
    }

    //  简单判断年龄
    public static boolean isNormalAge(int age){
        return age>=8 && age<=120;
    }

    //  打印Toast
    public static void showSth(Context context, String text){
        Toast.makeText(context, text,
                Toast.LENGTH_SHORT).show();
    }

    public static void hideKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive() ) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    //  将剪裁的头像存储至ShareUtils中
    public static void putImageToShare(Context context, Bitmap bitmap){
        if( bitmap != null ){
            //  Bitmap压缩成字节数组byte[]输出流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
            //  输出流转换为String
            byte[] byteArr = outputStream.toByteArray();
            String imgString = new String(Base64.encodeToString(byteArr, Base64.DEFAULT));
            //  String保存至ShareUtil
            SharedUtils.putString(context, "image_title", imgString);
            MLog.d("头像存储至ShareUtil成功！");
        }else{
            MLog.d("头像存储至ShareUtil失败，bitmap == null！");
        }
    }

    //  将ShareUtils头像设置
    //  String -> Byte[] -> bitmap
    public static void setImageFromShare(Context context, ImageView imageView){
        String imgString = SharedUtils.getString(context, "image_title", "");
        if( !UtilTools.isEquals(imgString, "") ){
            //  String -> byte[]
            byte[] byteArr = Base64.decode(imgString, Base64.DEFAULT);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArr);
            //  byte[] -> Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
            MLog.d("从ShareUtil取出头像成功！");
        }
    }
}

