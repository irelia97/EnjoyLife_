package snowdance.example.com.myapplication.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private TextView tv_company;
    private Spinner  sp_company;
    private String   company_no = "zto";
    private Map<String, String> map = new HashMap<String, String>();

    private EditText et_sno;
    private Button bt_search;

    private RecyclerView rv_Trace;
    //  存储从JSON中解析的数据
    private List<CourierData> courierList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        initData();
        sp_company.setSelection(2);
        sp_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String company_name = (String)sp_company.getItemAtPosition(position);
                company_no = map.get(company_name);
                UtilTools.showSth(CourierAct.this, company_no);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initData() {
        rv_Trace = findViewById(R.id.rvTrace);
        tv_company = findViewById(R.id.et_company);
        sp_company = findViewById(R.id.sp_company);
        et_sno = findViewById(R.id.et_sno);
        bt_search = findViewById(R.id.bt_search);
        bt_search.setOnClickListener(this);

        map.put("顺丰", "sf");
        map.put("申通", "sto");
        map.put("圆通", "yt");
        map.put("韵达", "yd");
        map.put("京东", "jd");
        map.put("天天", "tt");
        map.put("EMS", "ems");
        map.put("中通", "zto");
        map.put("汇通", "ht");
        map.put("全峰", "qf");
        map.put("德邦", "db");
        map.put("国通", "gt");
        map.put("如风达", "rfd");
        map.put("宅急送", "zjs");
        map.put("EMS国际", "emsg");
        map.put("Fedex国际", "fedex");
        map.put("邮政国内(挂号信)", "yzgn");
        map.put("UPS国际快递", "ups");
        map.put("中铁", "ztky");
        map.put("佳吉", "jiaji");
        map.put("速尔", "suer");
        map.put("信丰", "xfwl");
        map.put("优速", "yousu");
        map.put("中邮", "zhongyu");
        map.put("天地华宇", "tdhy");
        map.put("安信达", "axd");
        map.put("快捷速递", "kuaijie");
        map.put("AAE全球专递", "aae");
        map.put("DHL", "dhl");
        map.put("DPEX国际快递", "dpex");
        map.put("D速物流", "ds");
        map.put("FEDEX国内", "fedexcn");
        map.put("OCS", "ocs");
        map.put("TNT", "tnt");
        map.put("东方", "coe");
        map.put("传喜", "cxwl");
        map.put("城市100", "cs");
        map.put("城市之星", "cszx");
        map.put("安捷", "aj");
        map.put("百福东方", "bfdf");
        map.put("程光", "chengguang");
        map.put("递四方", "dsf");
        map.put("长通", "ctwl");
        map.put("飞豹", "feibao");
        map.put("马来西亚(大包EMS)", "malaysiaems");
        map.put("安能", "ane66");
        map.put("中通快运", "ztoky");
        map.put("远成物流", "ycgky");
        map.put("远成快运", "ycky");
        map.put("邮政快递", "youzheng");
        map.put("百世快运", "bsky");
        map.put("苏宁快递", "suning");
        map.put("安能物流", "anneng");
        map.put("九曳", "jiuye");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_search:
                //  获取输入框的数据

                String sno  = et_sno.getText().toString().trim();
                String url  = "http://v.juhe.cn/exp/index?key=" + StaticClass.Express_APPKEY
                        + "&com=" + company_no + "&no=" + sno;
                if( UtilTools.isEmpty(company_no) || UtilTools.isEmpty(sno) ){
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

                    @Override
                    public void onFailure(VolleyError error) {

                    }
                });

                break;
        }
    }

    private void parseJSON(String t){
        try {
            JSONObject jsonObject = new JSONObject(t);
            String reason = jsonObject.getString("reason");
            UtilTools.showSth(this, reason);
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
