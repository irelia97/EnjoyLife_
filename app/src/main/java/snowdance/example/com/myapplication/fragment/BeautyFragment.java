package snowdance.example.com.myapplication.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.show.api.ShowApiRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.adapter.GridAdapter;
import snowdance.example.com.myapplication.entity.GirlData;
import snowdance.example.com.myapplication.utils.MLog;
import snowdance.example.com.myapplication.utils.PicassoUtils;
import snowdance.example.com.myapplication.utils.StaticClass;
import snowdance.example.com.myapplication.utils.UtilFile;
import snowdance.example.com.myapplication.utils.UtilTools;
import snowdance.example.com.myapplication.view.CustomDialog;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.fragment
 * FILE    Name : BeautyFragment
 * Creator Name : Snow_Dance
 * Creator Time : 2018/9/23/023 14:15
 * Description  : 美女图片
 */

public class BeautyFragment extends Fragment {

    //列表
    private GridView mGridView;
    //数据
    private List<GirlData> mList = new ArrayList<>();
    //适配器
    private GridAdapter mAdapter;
    //提示框
    private CustomDialog dialog;
    //预览图片
    private PhotoView iv_img;
    //大图
    private ImageView mImg;
    //图片地址的数据
    private List<String> mListUrl = new ArrayList<>();
    //PhotoView
    private PhotoViewAttacher mAttacher;
    //该页的第几组数据
    static int startArr = 0;

    /**
     * 1.监听点击事件
     * 2.提示框
     * 3.加载图片
     * 4.PhotoView
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beauty, null);
        findView(view);
        return view;
    }

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            if( bitmap != null ){
                //  外部存储状态
                MLog.d("bitmap!=null！准备存储");
                String state = Environment.getExternalStorageState();
                if( !state.equals(Environment.MEDIA_MOUNTED) ) {
                    MLog.d("外部存储异常！");
                    return;
                }

                //  通过系统时间(精确到秒)得到唯一图片名称
                String fileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                        .format(new Date()) + ".jpg";
                String savePath = UtilFile.saveFile(getContext(), fileName, bitmap);
                MLog.d("savePath = " + savePath);
//                                      尝试保存
                try{
//                                        MLog.d("尝试保存jpg中...");
//                                        MLog.d(dir + fileName);
                    File file = new File(savePath);
//                                        MLog.d("000000");
//                                        FileOutputStream out = new FileOutputStream(file);
//                                        MLog.d("111111");
//                                        //  100%质量输出
//                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                                        MLog.d("222222");
//                                        out.flush();
//                                        MLog.d("333333");
//                                        out.close();
//                                        MLog.d("保存图片成功！");
                    //  保存图片后发送广播更新图库
                    Uri uri = Uri.fromFile(file);
                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    MLog.d("发送广播成功！");
                    UtilTools.showSth(getContext(), "保存成功！");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                MLog.d("bitmap为Null，保存图片至本地失败！");
                UtilTools.showSth(getContext(), "保存失败！");
            }
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            MLog.d("回调bitmap失败！");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    //初始化View
    private void findView(View view) {

        mGridView = view.findViewById(R.id.mGridView);

        //初始化提示框
        dialog = new CustomDialog(getActivity(), LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, R.layout.dialog_girl,
                Gravity.CENTER, R.style.pop_anim_style);
        iv_img = dialog.findViewById(R.id.iv_img);

        setData();


        //监听点击事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //解析图片
                PicassoUtils.loadImgView(getActivity(), mListUrl.get(mList.size()-position-1), iv_img);
                //缩放
                mAttacher = new PhotoViewAttacher(iv_img);
                //刷新
                mAttacher.update();
                dialog.show();
            }
        });
        //监听长按时间
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //  UtilTools.showSth(getContext(), "长按事件！");
                //  弹出dialog，显示是否保存
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("欢乐时光提醒您");
                alertDialog.setMessage("是否保存图片？");
                //  不允许通过返回键取消
                alertDialog.setCancelable(false);

                alertDialog.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String urlPath = mListUrl.get(mList.size()-position-1);
                        //  使用target回调方法
                        Picasso.get().load(urlPath)./*resize(480,320).*/into(target);
                    }
                });
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();

                return true;
            }
        });

        //  监听滑动事件，是否二次下滑到底部，是则刷新
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int getLastVisiblePosition = 0, lastVisiblePositionY = 0;
            private int[] location = new int[2];
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //滚动到底部
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                        View v = view.getChildAt(view.getChildCount() - 1);
                        //获取在整个屏幕内的绝对坐标
                        v.getLocationOnScreen(location);
                        int y = location[1];

                        MLog.d("x" + location[0] + " y" + location[1]);
                        if (view.getLastVisiblePosition() != getLastVisiblePosition &&
                                lastVisiblePositionY != y) {
                            //第一次拖至底部
                            UtilTools.showSth(getContext(), "已经拖动至底部，再次拖动即可刷新");
                            getLastVisiblePosition = view.getLastVisiblePosition();
                            lastVisiblePositionY = y;
                            return;
                        } else if (view.getLastVisiblePosition() == getLastVisiblePosition &&
                                lastVisiblePositionY == y) {
                            setData();

                            UtilTools.showSth(getContext(), "已为您加载更多图片！");
                        }
                    }
                    //未滚动到底部，第二次拖至底部都初始化
                    getLastVisiblePosition = 0;
                    lastVisiblePositionY = 0;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private String getJson(){
        String res = new ShowApiRequest(StaticClass.YIYUAN_PHOTO, StaticClass.YIYUAN_APPID,
                StaticClass.YIYUAN_KEY)
                .addTextPara("type", (StaticClass.GROUP+1) + "00" + (StaticClass.CHILD+1))
                .addTextPara("page", StaticClass.PHOtO_PNO+"")
                .post();
        MLog.longLog("PhotoJson: " + res);

        return res;
    }

    //解析Json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray jsonArray = jsonObject.getJSONObject("showapi_res_body")
                    .getJSONObject("pagebean").getJSONArray("contentlist");
            MLog.d("图片List共有" + jsonArray.length() + "条数据");
            //MLog.longLog(t);
            if( jsonArray.length() == 0 ){
                MLog.d("非常抱歉，暂无该类型图片数据!");
                return;
            }

            //  图片数量过多或检测到用户更换图片类型，清空容器
            if( true ){
                mList.clear();
                mListUrl.clear();
                if( StaticClass.PHOTO_TYPE_CHANGE )
                    StaticClass.PHOTO_TYPE_CHANGE = false;
            }

            if( jsonArray.length() == 0 ){
                UtilTools.showSth(getContext(), "无此类型数据！");
                return;
            }

            int count = 0;
            int i, j;
            for (i = startArr; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                String title = json.getString("title");

                JSONArray photoArr = json.getJSONArray("list");
                for(j = 0; j < photoArr.length(); ++j){
                    String url = "";
                    JSONObject photo = (JSONObject) photoArr.get(j);
                    if( photo.has("big")){
                        url = photo.getString("big");
                    }else if( photo.has("middle") ){
                        url = photo.getString("middle");
                    }else if( photo.has("small") ){
                        url = photo.getString("small");
                    }
                    mListUrl.add(url);

                    GirlData data = new GirlData();
                    data.setTitle(title);
                    data.setImgUrl(url);
                    mList.add(data);

                    ++count;
                }
                if( count >= 50 ){
                    startArr = ++i;
                    break;
                }
//                String url = json.getString("url");
//                mListUrl.add(url);
//
//                GirlData data = new GirlData();
//                data.setImgUrl(url);
//                mList.add(data);
            }
            if( i == jsonArray.length() ){
                ++StaticClass.PHOtO_PNO;
                startArr = 0;
            }

            List<GirlData> tmp = new ArrayList<>();
            tmp.addAll(mList);
            Collections.reverse(tmp);

            //设置适配器
            mAdapter = new GridAdapter(getActivity(), tmp);
            mGridView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData()
    {
        ExecutorService executorService = Executors.newCachedThreadPool();
        MyThread callable = new MyThread();
        Future<String> future = executorService.submit(callable);

        String JSON = "";
        while( true ){
            if( future.isDone() ){
                try{
                    JSON = future.get().toString();
                    //MLog.longLog("图片数据：" + JSON);
                    executorService.shutdown();
                    break;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        parsingJson(JSON);
    }

    public void changeImageType()
    {
        MLog.d("BeautyFragment changeImggeType()");
        if( StaticClass.PHOTO_TYPE_CHANGE ){
            StaticClass.PHOTO_TYPE_CHANGE = false;
            StaticClass.PHOtO_PNO = 1;  //重新变为第一页
            setData();
        }
    }

    public class MyThread implements Callable<String> {
        @Override
        public String call() {
            return getJson();
        }
    }
}