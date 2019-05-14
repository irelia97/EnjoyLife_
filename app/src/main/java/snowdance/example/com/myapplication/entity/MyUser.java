package snowdance.example.com.myapplication.entity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.entity
 * FILE    Name : MyUser
 * Creator Name : Snow_Dance
 * Creator Time : 2018/10/7/007 20:56
 * Description  : 用户实体类
 */

public class MyUser extends BmobUser {
    private int age;
    private boolean sex;
    private String telephone;
    private String description;
    private String nickname;
    private BmobFile img;

    public MyUser(){

    }

    public MyUser(BmobFile file){
        img = file;
    }

    public MyUser(String objId){
        setObjectId(objId);
    }

    public BmobFile getImg() {
        return img;
    }

    public void setImg(BmobFile img) {
        this.img = img;
    }

    public boolean isSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean getSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getNickname() {
        return nickname;
    }
}
