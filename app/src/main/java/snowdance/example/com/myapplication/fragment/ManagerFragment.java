package snowdance.example.com.myapplication.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import org.apache.http.client.params.HttpClientParams;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.adapter.MsgAdapter;
import snowdance.example.com.myapplication.entity.Message;
import snowdance.example.com.myapplication.utils.MLog;
import snowdance.example.com.myapplication.utils.StaticClass;
import snowdance.example.com.myapplication.utils.UtilTools;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.fragment
 * FILE    Name : ManagerFragment
 * Creator Name : Snow_Dance
 * Creator Time : 2018/9/23/023 14:13
 * Description  : 机器人聊天
 */

public class ManagerFragment extends Fragment {

    private List<Message> msgList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
//    private Random rand;
//    private String[] data = {"今天天气不错啊！", "嗯", "不知道", "你说神马？", "我是机器人啊!"
//            ,"我听不懂你在说什么", "还行吧", "我还没吃饭呢！你呢？", "好吧", "行"};

    private LinearLayout linearLayout;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_manager, null);
        MLog.DEBUG = true;
        initView(view);
        initMsg();
        //  键盘弹出时布局上移，以免压缩背景图片
//          controlKeyboardLayout(linearLayout, inputText);
        /*
            通过更改控件的方法有效，但是没法改回去......
            于是使用其它方法，先在onCreateView中设置背景图片而非在xml中设置
            再在activity里设置
            android:windowSoftInputMode="adjustPan"
            在RecyclerView中设置
            android:transcriptMode="normal"即可
         */

        //  设置延迟，更符合实际体验
//        inputText.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                msgRecyclerView.scrollToPosition(msgList.size()-1);
//            }
//        }, 100);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = inputText.getText().toString();
                if( !"".equals(content) ){
                    //  用户发送的信息
                    setText(content, Message.TYPE_SENT);
                    //  清空输入框
                    inputText.setText("");

                    //  Error code 302解决方案，但是好像没有用
                    BasicHttpParams params = new BasicHttpParams();
                    HttpClientParams.setRedirecting(params, true);
                    //  因此更换了茉莉机器人API
                    //  获取机器人回复语句
                    String url = "http://i.itpk.cn/api.php?question="+content
                            +"&api_key="+StaticClass.ROBOT_APPKEY+"&api_secret="
                            +StaticClass.ROBOT_Secret;
                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            MLog.d("JSON: Robot CallBack success!" + t);
                            switch (content){
                                case "cls":
                                    msgList.clear();

                                    //  清屏后收起键盘，否则清屏效果不会出现(不知道为什么)
                                    UtilTools.hideKeyBoard(view);
                                    msgList.add(new Message(StaticClass.ROBOT_CLS,
                                            Message.TYPE_RECEIVED));
//                                    setText(StaticClass.ROBOT_CLS, Message.TYPE_RECEIVED);
                                    break;
                                case "帮助":
                                    setText(StaticClass.ROBOT_HELP, Message.TYPE_RECEIVED);
                                    break;
                                case "笑话":
                                    Parse_Joke(t);
                                    break;
                                case "观音灵签":
                                    Parse_GuanYin(t);
                                    break;
                                case "月老灵签":
                                    Parse_YueLao(t);
                                    break;
                                case "财神爷灵签":
                                    Parse_CaiShen(t);
                                    break;
                                default:
                                    Parse_Text(t);
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(VolleyError error) {
                            MLog.d("Error url!");
                        }
                    });
                }
            }
        });

        //  点击EditText以外区域收起软键盘
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager manager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(getActivity().getCurrentFocus()!=null &&
                       getActivity().getCurrentFocus().getWindowToken()!=null){
                        manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
//                getActivity().onTouchEvent(event);
                return false;
            }
        });

        return view;
    }

    private void initView(View view) {
//        rand = new Random();
        linearLayout = view.findViewById(R.id.root);
        inputText = view.findViewById(R.id.input_text);
        inputText.setFocusable(true);

        send = view.findViewById(R.id.send);
        msgRecyclerView = view.findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
    }

    private void initMsg(){
        Message msg1 = new Message(StaticClass.ROBOT_WELCOME, Message.TYPE_RECEIVED);
        msgList.add(msg1);
        Message msg2 = new Message(StaticClass.ROBOT_TOAST, Message.TYPE_RECEIVED);
        msgList.add(msg2);
    }

    private void setText(String text, int type){
        msgList.add(new Message(text, type));
        //  插入新消息
        adapter.notifyItemInserted(msgList.size()-1);
        //  定位到最后一行
        msgRecyclerView.scrollToPosition(msgList.size()-1);
        MLog.d("DEBUG for reply: " + text);
    }

    //  解析文本
    private void Parse_Text(String text) {
        setText(text, Message.TYPE_RECEIVED);
    }

    //  解析笑话
    private void Parse_Joke(String JSON){
        try {
            JSONObject jsonObject = new JSONObject(JSON);
            String title = jsonObject.getString("title");
            String content = jsonObject.getString("content");
            setText(title + ":\n" + content, Message.TYPE_RECEIVED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //  解析观音灵签
    private void Parse_GuanYin(String JSON){
        try {
            JSONObject jsonObject = new JSONObject(JSON);
            String num  = jsonObject.getString("number2");
            String type = jsonObject.getString("type");     //观音灵签
            String fate = jsonObject.getString("haohua");   //签运
            String mean = jsonObject.getString("qianyu");   //签语
            String poet = jsonObject.getString("shiyi");    //签诗
            String straight_mean = jsonObject.getString("jieqian"); //白话

            String text = type + " - " + num + "\n\n签运：" + fate + "\n签语：" + mean
                    + "\n签意：" + poet + "\n白话解签：" + straight_mean;
            setText(text, Message.TYPE_RECEIVED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Parse_YueLao(String JSON){
        try {
            JSONObject jsonObject = new JSONObject(JSON);
            String num  = jsonObject.getString("number2");
            String type = jsonObject.getString("type");     //观音灵签
            String fate = jsonObject.getString("haohua");   //签运
            String mean = jsonObject.getString("jieqian");   //签语
            String poet = jsonObject.getString("shiyi");    //签诗
            String note = jsonObject.getString("zhushi");   //注释
            String straight_mean = jsonObject.getString("baihua"); //白话

            String text = type + " - " + num + "\n\n签运：" + fate + "\n签诗：" + poet
                    + "\n解签：" + note + "\n白话解签：" + straight_mean
                    + "\n注释：" + mean;
            setText(text, Message.TYPE_RECEIVED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Parse_CaiShen(String JSON){
        try {
            JSONObject jsonObject = new JSONObject(JSON);
            String num = jsonObject.getString("number2");
            String type = jsonObject.getString("type");     //财神灵签
            String mean = jsonObject.getString("qianyu");   //签语
            String poet = jsonObject.getString("jieqian");  //解签
            String note = jsonObject.getString("zhushi");   //注释
            String straight_mean = jsonObject.getString("jieshuo"); //白话
            //  宜，不宜
            String str = "婚姻：" + jsonObject.getString("hunyin") + "\n事业：" + jsonObject.getString("shiye")
                    + "\n功名：" + jsonObject.getString("gongming") + "\n出外移居" + jsonObject.getString("cwyj")
                    + "\n六甲：" + jsonObject.getString("liujia") + "\n求财：" + jsonObject.getString("qiucai")
                    + "\n交易：" + jsonObject.getString("jiaoyi") + "\n疾病：" + jsonObject.getString("jibin")
                    + "\n诉讼：" + jsonObject.getString("susong") + "\n运途：" + jsonObject.getString("yuntu")
                    + "\n某事：" + jsonObject.getString("moushi") + "\n合伙：" + jsonObject.getString("hhzsy");

            String text = type + " - " + num + "\n\n签语：" + mean + "\n解签：" + poet
                    + "\n白话解签：" + straight_mean + "\n" + note + "\n\n" + str;
            setText(text, Message.TYPE_RECEIVED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //  这个方法会使控件超出边界，弃用
    private void controlKeyboardLayout(final View root, final View scrollToView){
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //  获取root在窗体可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //  当前视图最外层高度-当前视图最底部Y坐标
                int differ = root.getRootView().getHeight() - rect.bottom;
                MLog.i("最外层高度为：" + differ);

                //  若differ > 100，说明键盘弹出
                if( differ > 100 ){
                    //  获取scrollView在窗体的坐标
                    int[] location = new int[2];
                    scrollToView.getLocationInWindow(location);
                    //  计算应当滚动的高度
                    int scrollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    root.scrollTo(0, scrollHeight);
                }else{
                    root.scrollTo(0, 0);
                }
            }
        });
    }

}

