package snowdance.example.com.myapplication.entity;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.entity
 * FILE    Name : CourierData
 * Creator Name : Snow_Dance
 * Creator Time : 2018/10/21/021 22:06
 * Description  : 快递查询实体
 */

public class CourierData {

    private String datetime;    //时间
    private String remark;      //信息
    private String zone;        //地区

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "CourierData{" +
                "datetime='" + datetime + '\'' +
                ", remark='" + remark + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }
}
