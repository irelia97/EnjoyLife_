package snowdance.example.com.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.entity.CourierData;

//  快递信息
public class CourierAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private List<CourierData> traceList = new ArrayList<>(1);
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL= 0x0001;

    public CourierAdapter(Context context, List<CourierData> traceList) {
        inflater = LayoutInflater.from(context);
        this.traceList = traceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.courier_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder itemHolder = (ViewHolder) holder;
        if (getItemViewType(position) == TYPE_TOP) {
            // 第一行头的竖线不显示
            itemHolder.tvTopLine.setVisibility(View.INVISIBLE);
            itemHolder.tv_datetime.setTextColor(0xff555555);
            itemHolder.tv_zone.setTextColor(0xff555555);
            itemHolder.tv_remark.setTextColor(0xff555555);
            // 字体颜色加深
            itemHolder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
        } else if (getItemViewType(position) == TYPE_NORMAL) {
            itemHolder.tvTopLine.setVisibility(View.VISIBLE);
            itemHolder.tvTopLine.setVisibility(View.INVISIBLE);
            itemHolder.tv_datetime.setTextColor(0xff999999);
            itemHolder.tv_zone.setTextColor(0xff999999);
            itemHolder.tv_remark.setTextColor(0xff999999);
            itemHolder.tvDot.setBackgroundResource(R.drawable.timelline_dot_normal);
        }

        itemHolder.bindHolder(traceList.get(position));
    }

    @Override
    public int getItemCount() {
        return traceList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TOP;
        }
        return TYPE_NORMAL;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_datetime, tv_remark, tv_zone;
        private TextView tvTopLine, tvDot;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_remark = itemView.findViewById(R.id.tv_remark);
            tv_zone = itemView.findViewById(R.id.tv_zone);
            tv_datetime = itemView.findViewById(R.id.tv_datetime);
            tvTopLine = itemView.findViewById(R.id.tvTopLine);
            tvDot = itemView.findViewById(R.id.tvDot);
        }

        public void bindHolder(CourierData data) {
            tv_remark.setText(data.getRemark());
            tv_zone.setText(data.getZone());
            tv_datetime.setText(data.getDatetime());
        }
    }
}

