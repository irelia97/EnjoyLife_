package snowdance.example.com.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.show.api.ShowApiRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.activities.WebViewAct;
import snowdance.example.com.myapplication.adapter.WeChatAdapter;
import snowdance.example.com.myapplication.entity.WeChatData;
import snowdance.example.com.myapplication.utils.MLog;
import snowdance.example.com.myapplication.utils.StaticClass;
import snowdance.example.com.myapplication.utils.UtilTools;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.fragment
 * FILE    Name : WechatFragment
 * Creator Name : Snow_Dance
 * Creator Time : 2018/9/23/023 14:14
 * Description  : NULL
 */

public class WechatFragment extends Fragment {

    private ListView mListView;
    private List<WeChatData> mList = new ArrayList<>();

    //  容器存储拿到的标题、URL
    private List<String> mListTitle = new ArrayList<>();
    private List<String> mListUrl = new ArrayList<>();
    private List<String> mListImg = new ArrayList<>();
    private int ps = 15;
    private int preState = 0;
    private boolean tag = false;
    private static int mPosition = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weixin, null);
        initView(view);
        return view;
    }

    private void initView(View view){
        mListView = view.findViewById(R.id.mListView);
//        String url = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WEIXIN_APPKEY
//                + "&ps=20&pno=" + pno;

        setData();
//        MLog.longLog("mList.size() = " + mList.size());
//        MLog.longLog("mListUrl.size() = " + mListUrl.size());
//        MLog.longLog("mListTitle.size() = " + mListTitle.size());
//        MLog.longLog("mListImg.size() = " + mListImg.size());

//        pno++;
//        RxVolley.get(url, new HttpCallback() {
//            @Override
//            public void onSuccess(String t) {
////                UtilTools.showSth(getActivity(), "微信接口返回成功！");
////                parseJson(t);
//            }
//        });

        //  点击事件，跳转至微信文章
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MLog.i(position + "th article!");
                //  传值title和url
                Intent intent = new Intent(getActivity(), WebViewAct.class);
                //  因为我将mList逆置了，故这里位置需要修改
                intent.putExtra("title", mListTitle.get(mListTitle.size()-1-position));
                intent.putExtra("url", mListUrl.get(mListUrl.size()-1-position));
//                intent.putExtra("img", mListImg.get(mListImg.size()-1-position));
                //  跳转
                startActivity(intent);
            }
        });

        //  上滑至顶端刷新
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = mListView.getChildAt(0);
                    //  tag表示用户的上一个动作的滑动
                    if (firstVisibleItemView!=null && firstVisibleItemView.getTop()==0) {
                        MLog.d("##### 滚动到顶部 #####");
                        tag = true;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = mListView.getChildAt(mListView.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mListView.getHeight()) {
                        MLog.d("##### 滚动到底部 ######");
                        tag = false;
                    }
                } else{
                    tag = false;
                }
//                if( firstVisibleItem > oldVisibleItem ){
//                    //  上滑
//                }else if( firstVisibleItem < oldVisibleItem ){
//                    //  下滑
//                    tag = true; //下滑时tag为true，这样再次上滑至顶端就能刷新了
//                }
//                oldVisibleItem = firstVisibleItem;
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if( scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    mPosition = mListView.getFirstVisiblePosition();
                }

                if( tag && preState==SCROLL_STATE_FLING && scrollState==SCROLL_STATE_IDLE ){
                    //  接口解析
                    //  ps=5表示一页显示5篇文章，pno为页数

//                    String url = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WEIXIN_APPKEY
//                            + "&ps=5&pno=" + pno;
                    setData();
//                    MLog.longLog("mList.size() = " + mList.size());
//                    MLog.longLog("mListUrl.size() = " + mListUrl.size());
//                    MLog.longLog("mListTitle.size() = " + mListTitle.size());
//                    MLog.longLog("mListImg.size() = " + mListImg.size());

                    MLog.d("pno = " + StaticClass.NEWS_PNO);
//                    RxVolley.get(url, new HttpCallback() {
//                        @Override
//                        public void onSuccess(String t) {
//                            parseJson(t);
//                        }
//                    });
                    //  下一次滑动加载下一页
//                    pno++;
                    UtilTools.showSth(getContext(), "已为您新加载" + ps + "篇精选文章！");


                }
                preState = scrollState;
            }
        });
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
                    MLog.longLog("文章数据：" + JSON);
                    executorService.shutdown();
                    break;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        parseJson(JSON);
    }

    private String getJson()
    {
        String res = new ShowApiRequest(StaticClass.YIYUAN_NEWS, StaticClass.YIYUAN_APPID,
                StaticClass.YIYUAN_KEY)
                .addTextPara("channelName", StaticClass.NEWS_TYPE)
                .addTextPara("title", StaticClass.NEWS_TITLE)
                .addTextPara("needHtml", "1")
                .addTextPara("page", StaticClass.NEWS_PNO+"")
                .addTextPara("maxResult", ps+"")
                .post();
        ++StaticClass.NEWS_PNO;

        return res;
    }

    private void parseJson(String JSON){
        try {
            if( mList.size() >= 100 ){
                mList.clear();
                mListUrl.clear();
                mListTitle.clear();
            }

            JSONObject jsonObj = new JSONObject(JSON);
            JSONObject jsonRes = jsonObj.getJSONObject("showapi_res_body").getJSONObject("pagebean");
            JSONArray jsonList = jsonRes.getJSONArray("contentlist");

            MLog.d("文章List有" + jsonList.length() + "条数据");
            MLog.d("MaxResult = " + jsonRes.getString("maxResult"));
            if( jsonList.length() == 0 ){
                UtilTools.showSth(getContext(), "无此类型数据！");
                return;
            }

            /* 聚合数据网已经停止更新，故选择易源数据网接口 */
            for(int i = 0; i < jsonList.length(); ++i)
            {
                JSONObject json = (JSONObject) jsonList.get(i);
                final WeChatData data = new WeChatData();
                //  URL显示不正常，原因未知
                //  折中解决方案是使用webview去加载html数据
                mListUrl.add(json.getString("html"));
                MLog.longLog("url = " + json.getString("link"));
                //  标题
                String title = json.getString("title");
                data.setTitle(title);
                mListTitle.add(title);
                MLog.longLog("title = " + title);
                //  来源
                data.setSource(json.getString("source"));
                MLog.longLog("source = " + json.getString("source"));
                //  预览图(可能不存在)
                String imgUrl = "";
                MLog.d("111111111111111111111111111111111111");

                MLog.d("22222222222222222222222222222222");
                if( json.has("imageurls") ){
                    MLog.d("3333333333333333333333333333333333333333");
                    JSONArray imgList = json.getJSONArray("imageurls");
                    MLog.d("4444444444444444444444444444444444444444");
                    if( imgList.length() != 0 ){
                        imgUrl =  ((JSONObject)imgList.get(0)).getString("url");
                        MLog.d("5555555555555555555555555555555555555555");
                    }else{
                        MLog.d("No imgUrl!");
                        imgUrl = StaticClass.NEWS_NULL_PHOtO[StaticClass.NULL_PHOTO_NO];
                        StaticClass.NULL_PHOTO_NO = (StaticClass.NULL_PHOTO_NO + 1) % 10;
                    }
                }
                data.setImgUrl(imgUrl);
                mListImg.add(imgUrl);
                MLog.longLog("imgUrl = " + imgUrl);

                mList.add(data);
            }
//            MLog.d("---------MListUrl------------");
//            for(int i = 0; i < mListUrl.size(); ++i){
//                MLog.longLog("URL" + (i+1) + ": " + mListUrl.get(i));
//            }


            //  临时容器逆置，为了新添加的数据在前面显示，这样需要注意URL和position问题
            //  如果使用插入的话效率可能会低些
            List<WeChatData> tmp = new ArrayList<>();
            tmp.addAll(mList);
            Collections.reverse(tmp);

            WeChatAdapter adapter = new WeChatAdapter(getActivity(), tmp);
            mListView.setAdapter(adapter);
//            mListView.setSelection(adapter.getCount()-1);
//            mListView.setSelection(5);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void changeNewsType()
    {
        MLog.d("WechatFragment changeImggeType()");
        if( StaticClass.NEWS_TYPE_CHANGE ){
            StaticClass.NEWS_TYPE_CHANGE = false;
            StaticClass.NEWS_PNO = 1;  //重新变为第一页
            setData();
        }
    }

    public class MyThread implements Callable<String>{
        @Override
        public String call() {
            return getJson();
        }
    }
}