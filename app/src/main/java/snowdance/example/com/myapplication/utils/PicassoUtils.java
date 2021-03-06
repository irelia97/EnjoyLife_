package snowdance.example.com.myapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.logging.Handler;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.utils
 * FILE    Name : PicassoUtils
 * Creator Name : Snow_Dance
 * Creator Time : 2018/11/12/012 11:24
 * Description  : Picasso图片加载封装
 */

public class PicassoUtils {

    //  原尺寸加载图片
    public static void loadImgView(Context mContext, String url, ImageView imageView){
        Picasso.get().load(url).into(imageView);
    }

    //  指定大小加载图片
    public static void loadImgView(Context mContext, String url, ImageView imageView,
                                   int width, int height){
        Picasso.get().load(url).resize(width, height)
                //.memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                .config(Bitmap.Config.RGB_565).centerCrop().noFade().into(imageView);
    }

    //  默认图片、错误图片的加载
    public static void loadImgViewHolder(Context mContext, String url, ImageView imageView,
                                         int unloadImg, int errorImg){
        Picasso.get().load(url).placeholder(unloadImg).error(errorImg).into(imageView);
    }

    //  裁剪方法
    public static void loadImgViewCrop(Context mContext, String url, ImageView imageView){
        Picasso.get().load(url).transform(new CropSquareTransformation()).into(imageView);
    }


    //  图片裁剪至矩形
    public static class CropSquareTransformation implements Transformation{

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight()- size) / 2;
            Bitmap res = Bitmap.createBitmap(source, x, y, size, size);
            if( res != source ){
                //  回收source
                source.recycle();
            }
            return res;
        }

        @Override
        public String key() {
            return "SnowDance()";
        }
    }
}
