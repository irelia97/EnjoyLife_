package snowdance.example.com.myapplication.fragment;

import android.os.Bundle;
import android.os.Parcelable;
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

import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.adapter.GridAdapter;
import snowdance.example.com.myapplication.entity.GirlData;
import snowdance.example.com.myapplication.utils.MLog;
import snowdance.example.com.myapplication.utils.PicassoUtils;
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
    private ImageView iv_img;
    //图片地址的数据
    private List<String> mListUrl = new ArrayList<>();
    //PhotoView
    private PhotoViewAttacher mAttacher;
    //加载图片第几页
    private static int page = 1;
    //存储滑动位置状态
    private Parcelable state;

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

    //初始化View
    private void findView(View view) {

        mGridView = view.findViewById(R.id.mGridView);

        //初始化提示框
        dialog = new CustomDialog(getActivity(), LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, R.layout.dialog_girl,
                R.style.pop_anim_style, Gravity.CENTER);
        iv_img = dialog.findViewById(R.id.iv_img);

        /*原地址已经失效！重新使用新的URL*/
//        String welfare = null;
//        try {
//            gank升級 需要转码
//            welfare = URLEncoder.encode(getString(R.string.text_welfare), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        //解析
        String url = "https://gank.io/api/data/福利/20/" + page;
        page++;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                MLog.i("Girl Json:" + t);
                parsingJson(t);
            }
        });

        //监听点击事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //解析图片
                PicassoUtils.loadImgView(getActivity(), mListUrl.get(position), iv_img);
                //缩放
                mAttacher = new PhotoViewAttacher(iv_img);
                //刷新
                mAttacher.update();
                dialog.show();
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
                        v.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
                        int y = location[1];

                        MLog.d("x" + location[0] + " y" + location[1]);
                        if (view.getLastVisiblePosition() != getLastVisiblePosition && lastVisiblePositionY != y) {
                            //第一次拖至底部
                            UtilTools.showSth(getContext(), "已经拖动至底部，再次拖动即可刷新");
                            getLastVisiblePosition = view.getLastVisiblePosition();
                            lastVisiblePositionY = y;
                            return;
                        } else if (view.getLastVisiblePosition() == getLastVisiblePosition && lastVisiblePositionY == y) {
                            //第二次拖至底部
                            //保存当前浏览位置
//                            state = mGridView.onSaveInstanceState();
//                            int pos = mList.size() - 1;
                            String url = "https://gank.io/api/data/福利/20/" + page;
                            page++;
                            RxVolley.get(url, new HttpCallback() {
                                @Override
                                public void onSuccess(String t) {
                                    MLog.i("Girl Json:" + t);
                                    parsingJson(t);
                                }
                            });
                            UtilTools.showSth(getContext(), "已为您加载更多福利！");
                            //恢复至刷新位置处而非顶部
//                            mGridView.onRestoreInstanceState(state);
//                            mGridView.smoothScrollToPosition(pos);
//                            mGridView.setSelection(y);
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

    //解析Json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            mList.clear();
            mListUrl.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                String url = json.getString("url");
                mListUrl.add(url);

                GirlData data = new GirlData();
                data.setImgUrl(url);
                mList.add(data);
            }
//            List<GirlData> tmp = new ArrayList<>();
//            tmp.addAll(mList);
//            Collections.reverse(tmp);

            //设置适配器
            mAdapter = new GridAdapter(getActivity(), mList);
            mGridView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}