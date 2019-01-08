package snowdance.example.com.myapplication.entity;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.entity
 * FILE    Name : WeChatData
 * Creator Name : Snow_Dance
 * Creator Time : 2018/11/11/011 13:42
 * Description  : 微信文章实体类
 */

public class WeChatData {
    private String title;   //标题
    private String source;  //来源
    private String imgUrl;  //图片URL

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
