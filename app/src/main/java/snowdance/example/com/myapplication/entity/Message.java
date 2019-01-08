package snowdance.example.com.myapplication.entity;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.entity
 * FILE    Name : Message
 * Creator Name : Snow_Dance
 * Creator Time : 2018/10/27/027 20:09
 * Description  : NULL
 */

public class Message {
    public static final int TYPE_RECEIVED = 0;//    接收信息
    public static final int TYPE_SENT = 1;    //    发送信息
    private String content;                   //    文本内容
    private int type;

    public Message(String content, int type){
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
