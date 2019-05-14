
package com.show.api.test;

import com.show.api.ShowApiRequest;

public class ShowapiTest {
    public ShowapiTest() {

    }

    /***
     * 测试接口猜一猜
     */
    public static void main(String[] args) {
        String s = (new ShowApiRequest("http://route.showapi.com/632-1", "appid", "appSecret")).post();
        System.out.println(s);
    }

}
