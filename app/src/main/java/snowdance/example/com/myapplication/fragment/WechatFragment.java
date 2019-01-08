package snowdance.example.com.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    private static int pno = 1;
    private int preState = 0;
    private boolean tag = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weixin, null);
        initView(view);
        return view;
    }

    //  http://v.juhe.cn/weixin/query?key=您申请的KEY
    private void initView(View view) {
        mListView = view.findViewById(R.id.mListView);
        String url = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WEIXIN_APPKEY
                + "&ps=15&pno=" + pno;
        pno++;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                UtilTools.showSth(getActivity(), "微信接口返回成功！");
                parseJson(t);
            }
        });
        UtilTools.showSth(getContext(), "Tips:微信文章下滑再上拉可刷新");

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
                if( tag && preState==SCROLL_STATE_FLING && scrollState==SCROLL_STATE_IDLE ){
                    //  接口解析
                    //  ps=10表示一页显示15篇文章，pno为页数
                    String url = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WEIXIN_APPKEY
                            + "&ps=15&pno=" + pno;
                    MLog.d("pno = " + pno);
                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            parseJson(t);
                        }
                    });
                    //  下一次滑动加载下一页
                    pno++;
                    UtilTools.showSth(getContext(), "已为您新加载15篇精选文章！上滑可刷新！");
                }
                preState = scrollState;
            }
        });
    }

    private void parseJson(String JSON){
        try {
            JSONObject jsonObj = new JSONObject(JSON);
            JSONObject jsonRes = jsonObj.getJSONObject("result");
            JSONArray jsonList = jsonRes.getJSONArray("list");

            if( mList.size() >= 100 ){
                mList.clear();
                mListUrl.clear();
                mListTitle.clear();
            }
            for(int i = 0; i < jsonList.length(); ++i){
                JSONObject json = (JSONObject) jsonList.get(i);
                WeChatData data = new WeChatData();

                String title = json.getString("title");
                data.setTitle(title);
                mListTitle.add(title);

                String url = json.getString("url");
                mListUrl.add(url);

                data.setSource(json.getString("source"));
                data.setImgUrl(json.getString("firstImg"));
                mList.add(data);
            }
            //  临时容器逆置，为了新添加的数据在前面显示，这样需要注意URL和position问题
            //  如果使用插入的话效率可能会低些
            List<WeChatData> tmp = new ArrayList<>();
            tmp.addAll(mList);
            Collections.reverse(tmp);

            WeChatAdapter adapter = new WeChatAdapter(getActivity(), tmp);
            mListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

