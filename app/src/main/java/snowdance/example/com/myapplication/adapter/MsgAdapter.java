package snowdance.example.com.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.entity.Message;

//  聊天信息
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<Message> msgList;


    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;

        public ViewHolder(View view){
            super(view);
            leftLayout  = view.findViewById(R.id.left_layout);
            rightLayout = view.findViewById(R.id.right_layout);
            leftMsg  = view.findViewById(R.id.left_msg);
            rightMsg = view.findViewById(R.id.right_msg);
        }
    }

    public MsgAdapter(List<Message> msgList){
        this.msgList = msgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item
                , parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos){
        Message msg = msgList.get(pos);
        if( msg.getType() == Message.TYPE_RECEIVED ){
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
        }else if( msg.getType() == Message.TYPE_SENT ){
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setText(msg.getContent());
        }
    }

    @Override
    public int getItemCount(){
        return msgList.size();
    }

    public void removeAll(){
        msgList.clear();

    }
}
