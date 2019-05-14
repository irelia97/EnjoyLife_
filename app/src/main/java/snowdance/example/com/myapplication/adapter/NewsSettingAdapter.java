package snowdance.example.com.myapplication.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.utils.MLog;
import snowdance.example.com.myapplication.utils.StaticClass;
import snowdance.example.com.myapplication.utils.UtilTools;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.adapter
 * FILE    Name : NewsSettingAdapter
 * Creator Name : Snow_Dance
 * Creator Time : 2019/4/23/023 12:18
 * Description  : NULL
 */




public class NewsSettingAdapter extends BaseExpandableListAdapter {
    public String[] group = { "社会百态", "明星写真", "娱乐八卦", "美女图片", "时尚伊人", "生活趣味", "猎奇图片" };
    public String[][] gridViewChild = {
            { "社会新闻", "国内新闻", "环球动态", "军事新闻 " },
            { "中国明星", "欧美明星", "中国女明星", "中国男明星", "韩国明星", "欧美女明星", "欧美男明星" },
            { "娱乐组图", "八卦爆料", "电影海报", "影视剧照", "活动现场" },
            { "清纯", "气质", "萌妹", "校花", "婚纱", "街拍", "非主流", "美腿", "性感", "车模", "男模", "女模", "魅女", "日韩美女" },
            { "秀场", "霓裳", "鞋包", "婚嫁", "妆容", "广告大片" },
            { "居家", "萌宠", "美食图片", "美丽风景", "奇趣自然", "植物花卉" },
            { "猎奇图片", "溶洞奇观" } };
    String[][] child = { { "" }, { "" }, { "" }, { "" }, { "" }, { "" },
            { "" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" } };
    LayoutInflater mInflater;
    Context context;
    ViewChild mViewChild;

    public NewsSettingAdapter(Context context) {
        // TODO Auto-generated constructor stub
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return child[groupPosition][childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            mViewChild = new ViewChild();
            convertView = mInflater.inflate(
                    R.layout.channel_expandablelistview_item, null);
            mViewChild.gridView = convertView
                    .findViewById(R.id.channel_item_child_gridView);
            convertView.setTag(mViewChild);
        } else {
            mViewChild = (ViewChild) convertView.getTag();
        }

        SimpleAdapter mSimpleAdapter = new SimpleAdapter(context,
                setGridViewData(gridViewChild[groupPosition]),
                R.layout.channel_gridview_item,
                new String[] { "channel_gridview_item" },
                new int[] { R.id.channel_gridview_item });
        mViewChild.gridView.setAdapter(mSimpleAdapter);
        setGridViewListener(mViewChild.gridView);
        mViewChild.gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        return convertView;
    }

    /**
     * 设置gridview点击事件监听
     *
     * @param gridView
     */
    private void setGridViewListener(final GridView gridView) {
        gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if (view instanceof TextView) {
                    // 如果想要获取到哪一行，则自定义gridview的adapter
                    // item设置2个textview一个隐藏设置id，显示哪一行
                    TextView tv = (TextView) view;

                    StaticClass.CHILD = position;
                    UtilTools.showSth(context,
                            "position=" + position + "||" + tv.getText());
                    MLog.d("gridView listaner position=" + position
                            + "||text=" + tv.getText());
                }
            }
        });
    }

    /**
     * 设置gridview数据
     *
     * @param data
     * @return
     */
    private ArrayList<HashMap<String, Object>> setGridViewData(String[] data) {
        ArrayList<HashMap<String, Object>> gridItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < data.length; i++) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("channel_gridview_item", data[i]);
            gridItem.add(hashMap);
        }
        return gridItem;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return child[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return group[groupPosition];
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return group.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            mViewChild = new ViewChild();
            convertView = mInflater.inflate(
                    R.layout.channel_expandablelistview, null);
            mViewChild.textView = convertView
                    .findViewById(R.id.channel_group_name);
            mViewChild.imageView = convertView
                    .findViewById(R.id.channel_imageview_orientation);
            convertView.setTag(mViewChild);
        } else {
            mViewChild = (ViewChild) convertView.getTag();
        }

        if (isExpanded) {
            mViewChild.imageView
                    .setImageResource(R.drawable.channel_expandablelistview_top_icon);
        } else {
            mViewChild.imageView
                    .setImageResource(R.drawable.channel_expandablelistview_bottom_icon);
        }
        mViewChild.textView.setText(getGroup(groupPosition).toString());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

    static class ViewChild {
        ImageView imageView;
        TextView textView;
        GridView gridView;
    }
}
