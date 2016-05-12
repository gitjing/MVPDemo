package com.example.ljj.mvpdemo.login.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.example.ljj.mvpdemo.login.model.LoginModel;
import com.example.ljj.mvpdemo.login.model.LoginModelImpl;
import com.example.ljj.mvpdemo.login.view.LoginView;
import com.example.ljj.mvpdemo.utils.VerifyUtil;
import com.example.ljj.mvpdemo.utils.VollyNotWorkUtils;

import java.util.Map;

/**
 * Created by mgsd on 2016/4/21.
 */
public class LoginPresenterImpl implements LoginPresenter, LoginModelImpl.CallBackListener {

    private LoginView loginView;
    private LoginModel loginModel;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        loginModel = new LoginModelImpl(this, (Context) loginView);
    }

    @Override
    public void LoginService(String phone , String psw , String url, int requestID, Map<String,String> parmrs) {
        if (TextUtils.isEmpty(phone)) {//phone number is empty
            loginView.phoneError("phone number can not be empty");
            return;
        }
        if (TextUtils.isEmpty(psw)) {//password is empty
            loginView.pswError("password can not be empty");
            return;
        }

        if (!VerifyUtil.isMobileNO(phone)) {//phone number type error
            loginView.phoneError("phone number type error");
            return;
        }
        if (!VerifyUtil.isVertifyPsw(psw)) {
            loginView.phoneError("Password length is not enough");
            return;
        }
        loginView.showProgress();
        loginModel.LoginIn(url,requestID,parmrs,this);
    }

    @Override
    public void onSuccess(String response, int requestID) {
        loginView.hintProgress();
        loginView.loginSuccess(response,requestID);
    }

    @Override
    public void onFailure(VolleyError error, VollyNotWorkUtils.RequestStatus requeststatus, int requestID) {
        loginView.hintProgress();
        loginView.loginFails(error,requeststatus,requestID);
    }
}
