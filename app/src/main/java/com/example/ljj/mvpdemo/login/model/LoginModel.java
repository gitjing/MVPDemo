package com.example.ljj.mvpdemo.login.model;

import java.util.Map;

/**
 * Created by mgsd on 2016/4/21.
 */
public interface LoginModel {
    void LoginIn(String url, int requestID, Map<String,String> parmrs,LoginModelImpl.CallBackListener callBackListener);//登录请求接口
}
