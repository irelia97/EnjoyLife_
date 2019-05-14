package snowdance.example.com.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.entity.WeChatData;
import snowdance.example.com.myapplication.utils.MLog;
import snowdance.example.com.myapplication.utils.PicassoUtils;
import snowdance.example.com.myapplication.utils.UtilTools;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.adapter
 * FILE    Name : WeChatAdapter
 * Creator Name : Snow_Dance
 * Creator Time : 2018/11/11/011 13:40
 * Description  : 微信容器
 */

public class WeChatAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<WeChatData> mList;
    private WeChatData data;
    private int width, height;


    public WeChatAdapter(Context mContext, List<WeChatData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//      WindowManager的getWidth()和getHeight()已过时
/*
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
*/
        //  这里的getResources()需要使用mContext获取
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        width  = dm.widthPixels;
        height = dm.heightPixels;
        MLog.i("Width:" + width + "Height:" + height);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.wechat_item, null);
            viewHolder.dv_img = convertView.findViewById(R.id.dv_img);
            viewHolder.tv_title = convertView.findViewById(R.id.tv_title);
            viewHolder.tv_source = convertView.findViewById(R.id.tv_source);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        data = mList.get(position);
        viewHolder.tv_title.setText(data.getTitle());
        viewHolder.tv_source.setText(data.getSource());

        MLog.i("Set title and Source!");

        //  当前聚合接口暂不支持封面图片
        //  若图片Url非空，则加载
        if(!TextUtils.isEmpty(data.getImgUrl())) {
//            加载图片
            PicassoUtils.loadImgView(mContext, data.getImgUrl(),
                    viewHolder.dv_img, width / 3, 250);
            //  Picasso第一次加载网络图片时，缓存被gc回收
            //  无法解决该问题，更换Fresco、Glide亦无效
            MLog.i("Set image!");
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView dv_img;
        private TextView  tv_title;
        private TextView  tv_source;
    }
}