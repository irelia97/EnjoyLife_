package snowdance.example.com.myapplication.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.adapter.NewsSettingAdapter;
import snowdance.example.com.myapplication.utils.MLog;
import snowdance.example.com.myapplication.utils.StaticClass;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.activities
 * FILE    Name : PhotoSettingAct
 * Creator Name : Snow_Dance
 * Creator Time : 2019/4/23/023 18:22
 * Description  : NULL
 */

public class PhotoSettingAct extends BaseAct{

    private final int gp = StaticClass.GROUP;
    private final int cp = StaticClass.CHILD;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photosetting);

        expandableListView    = findViewById(R.id.expandableListView);
        expandableListAdapter = new NewsSettingAdapter(this);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.expandGroup(0);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                MLog.d("groupPosition = " + groupPosition);
                if( groupPosition != gp )
                    StaticClass.PHOTO_TYPE_CHANGE = true;
                else
                    StaticClass.PHOTO_TYPE_CHANGE = false;

                StaticClass.GROUP = groupPosition;
                return false;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                MLog.d("groupPosition = " + groupPosition);
                MLog.d("childPosition = " + childPosition);
                StaticClass.GROUP = groupPosition;
                StaticClass.CHILD = childPosition;
                if( groupPosition != gp || childPosition != cp )
                    StaticClass.PHOTO_TYPE_CHANGE = true;
                else
                    StaticClass.PHOTO_TYPE_CHANGE = false;

                return false;
            }
        });
    }
}
