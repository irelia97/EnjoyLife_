package snowdance.example.com.myapplication.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.utils.StaticClass;
import snowdance.example.com.myapplication.utils.UtilTools;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.activities
 * FILE    Name : NewsSettingAct
 * Creator Name : Snow_Dance
 * Creator Time : 2019/4/23/023 12:15
 * Description  : NULL
 */

public class NewsSettingAct extends BaseAct implements View.OnClickListener {

    private EditText et_info;
    private TextView tv_Type;
    private Spinner  sp_Type;
    private Button   bt_confirm;
    static private int pos = 0;

    private final String type  = StaticClass.NEWS_TYPE;
    private final String title = StaticClass.NEWS_TITLE;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newssetting);

        initView();
        sp_Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                if( position == 0 ){
                    StaticClass.NEWS_TYPE = "";
                }else{
                    StaticClass.NEWS_TYPE  = (String)sp_Type.getItemAtPosition(position);
                }

                if( !UtilTools.isEquals(type, StaticClass.NEWS_TYPE) ){
                    StaticClass.NEWS_TYPE_CHANGE = true;
                }else{
                    StaticClass.NEWS_TYPE_CHANGE = false;
                }
                UtilTools.showSth(NewsSettingAct.this, StaticClass.NEWS_TYPE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView(){
        et_info = findViewById(R.id.et_inputInfo);
        tv_Type = findViewById(R.id.tv_newsType);
        sp_Type = findViewById(R.id.sp_newsType);
        bt_confirm = findViewById(R.id.bt_confirm);
        bt_confirm.setOnClickListener(this);

        et_info.setText(StaticClass.NEWS_TITLE);
        sp_Type.setSelection(pos);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_confirm:
                StaticClass.NEWS_TITLE = et_info.getText().toString().trim();
                UtilTools.showSth(NewsSettingAct.this, StaticClass.NEWS_TITLE);
                if( !UtilTools.isEquals(title, StaticClass.NEWS_TITLE) ){
                    StaticClass.NEWS_TYPE_CHANGE = true;
                }else{
                    StaticClass.NEWS_TYPE_CHANGE = false;
                }
                //finish();
        }
    }
}
