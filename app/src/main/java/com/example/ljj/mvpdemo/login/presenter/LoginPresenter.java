package com.example.ljj.mvpdemo.login.presenter;

import java.util.Map;

/**
 * Created by mgsd on 2016/4/21.
 */
public  interface LoginPresenter {
    void LoginService(String phone ,String psw,String url, int requestID, Map<String,String> parmrs);//请求网络，登录
}
