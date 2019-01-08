package snowdance.example.com.myapplication.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.adapter.CourierAdapter;
import snowdance.example.com.myapplication.entity.CourierData;
import snowdance.example.com.myapplication.utils.MLog;
import snowdance.example.com.myapplication.utils.StaticClass;
import snowdance.example.com.myapplication.utils.UtilTools;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.activities
 * FILE    Name : CourierAct
 * Creator Name : Snow_Dance
 * Creator Time : 2018/10/21/021 20:53
 * Description  : 快递查询
 */

public class CourierAct extends BaseAct implements View.OnClickListener {

    private EditText et_company;
    private EditText et_sno;
    private Button bt_search;

    private RecyclerView rv_Trace;
    //  存储从JSON中解析的数据
    private List<CourierData> courierList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        initView();
    }

    private void initView() {
        rv_Trace = findViewById(R.id.rvTrace);

        et_company = findViewById(R.id.et_company);
        et_sno = findViewById(R.id.et_sno);
        bt_search = findViewById(R.id.bt_search);
        bt_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_search:
                //  获取输入框的数据
                String name = et_company.getText().toString().trim();
                String sno  = et_sno.getText().toString().trim();
                String url  = "http://v.juhe.cn/exp/index?key=" + StaticClass.Express_APPKEY
                        + "&com=" + name + "&no=" + sno;
                if( UtilTools.isEmpty(name) || UtilTools.isEmpty(sno) ){
                    UtilTools.showSth(this, this.getString(R.string.textnull));
                    break;
                }
                //  请求JSON数据
                RxVolley.get(url, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
//                        UtilTools.showSth(this, t);
                        MLog.e("JSON:" + t);
                        parseJSON(t);
                    }

//                    @Override
//                    public void onFailure(VolleyError error) {
//                        super.onFailure(error);
//                    }
                });

                break;
        }
    }

    private void parseJSON(String t){
        try {
            JSONObject jsonObject = new JSONObject(t);
            //  拿到返回结果
            JSONObject res = jsonObject.getJSONObject("result");
            //  获取List及其中每一个Item
            JSONArray arr  = res.getJSONArray("list");
            //  清空上一次的数据
            courierList.clear();
            for(int i = arr.length()-1; i >= 0; --i){
                JSONObject obj = (JSONObject) arr.get(i);

                CourierData data = new CourierData();
                data.setRemark(obj.getString("remark"));
                data.setZone(obj.getString("zone"));
                data.setDatetime(obj.getString("datetime"));

                courierList.add(data);
            }
            //  设置适配器
            CourierAdapter adapter = new CourierAdapter(this, courierList);
            rv_Trace.setLayoutManager(new LinearLayoutManager(this));
            rv_Trace.setAdapter(adapter);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
