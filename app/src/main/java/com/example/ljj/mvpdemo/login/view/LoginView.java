package com.example.ljj.mvpdemo.login.view;

import com.android.volley.VolleyError;
import com.example.ljj.mvpdemo.utils.VollyNotWorkUtils;

/**
 * Created by mgsd on 2016/4/21.
 */

public  interface LoginView {

    void loginSuccess(String response, int requestID);//登录成功

    void phoneError(String msg);//手机格式错误

    void pswError(String msg);//密码错误

    void loginFails(VolleyError error, VollyNotWorkUtils.RequestStatus requeststatus, int requestID);//登录失败

    void showProgress();//显示进度

    void hintProgress();//隐藏进度提示
}

