package com.example.ljj.mvpdemo.login.model;

import android.content.Context;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ljj.mvpdemo.utils.VollyNotWorkUtils;

import java.util.Map;

/**
 * Created by mgsd on 2016/4/21.
 */
public class LoginModelImpl implements LoginModel {

    private CallBackListener callBackListener;

    private VollyNotWorkUtils vollyNotWorkUtils;
    private StringRequest stringRequest;

    public LoginModelImpl(CallBackListener callBackListener, Context context) {
        this.callBackListener = callBackListener;
        vollyNotWorkUtils = new VollyNotWorkUtils(context);
    }

    @Override
    public void LoginIn(String url, int requestID, Map<String, String> parmrs, final CallBackListener callBackListener) {
        stringRequest = vollyNotWorkUtils.getPostRequest(requestID, url, parmrs, new VollyNotWorkUtils.NetWorkListener() {
            @Override
            public void onResponse(String response, int requestID) {
                callBackListener.onSuccess(response,requestID);
            }
            @Override
            public void onErrorResponse(VolleyError error, VollyNotWorkUtils.RequestStatus requeststatus, int requestID) {
                callBackListener.onFailure(error,requeststatus,requestID);
            }
        });

        vollyNotWorkUtils.add(stringRequest);
    }

    public  interface CallBackListener {
        void onSuccess(String response, int requestID);
        void onFailure(VolleyError error, VollyNotWorkUtils.RequestStatus requeststatus, int requestID);
    }
}
