package com.example.ljj.mvpdemo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mgsd on 2016/4/21.
 */
public class VollyNotWorkUtils {

    private RequestQueue mRequestQueue;
    private NetWorkListener mNetWorkListener;
    private Map<String, String> mHearder;
    private Context context;


    private String compId = "1";
    private String commChannel = "1";

    public VollyNotWorkUtils(Context context) {
        this.context = context;
        initHeader();
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public void setMnetWorkListener(NetWorkListener mnetWorkListener) {
        this.mNetWorkListener = mnetWorkListener;
    }

    /**
     * 初始化报文头
     * ps:传说的报文头，个人理解头文件
     */
    private void initHeader() {
        mHearder = new HashMap<>();
        mHearder.put(headerInter.LATLNG, String.valueOf(Constants.latitude) + "###" + String.valueOf(Constants.longitude));
        mHearder.put(headerInter.MARK, getMac());
        mHearder.put(headerInter.IDENT_INFO,  "");
        mHearder.put(headerInter.MOBILE_INFO, getInfo(context));
        mHearder.put(headerInter.COMP_ID, compId + "," + commChannel);
    }

    /**
     * 头文件常量
     */
    private interface headerInter {
        String LATLNG = "latlng";
        String MARK = "mfygMark";
        String IDENT_INFO = "identInfo";
        String MOBILE_INFO = "mobileInfo";
        String COMP_ID = "compId";
    }

    private static String getMac() {
        String time = String.valueOf(System.currentTimeMillis());
        int mac = 0;
        for (int i = 0, len = time.length(); i < len - 2; ) {
            int tmp = Integer.parseInt(time.substring(i, i + 2));
            mac += tmp;
            i = i + 3;
        }
        mac += Integer.parseInt(time.substring(time.length() - 2));
        return time + "," + mac;
    }

    /**
     * 获取相关信息
     *
     * @param context
     * @return
     */
    private static String getInfo(Context context) {
        StringBuilder sb = new StringBuilder();
        String split = "###";
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String id = tm.getDeviceId();
        String type = Build.MODEL;// 手机型号
        String sysVersion = Build.VERSION.RELEASE;// 操作系统版本号 eg: 4.4.4
        // String brand = Build.BRAND;// 手机品牌
        PackageInfo pkg = null;
        try {
            pkg = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);// 版本名称
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String appVersion = pkg.versionName;
        return sb.append("1").append(split).append(id).append(split)
                .append(type).append(split).append(appVersion).append(split)
                .append(Constants.APP_VERSION).append(split).append(sysVersion)
                .toString();
    }

    /**
     *
     * @param requestID 请求ID  用于多次请求区分
     * @param url       请求网络路径
     * @param params    请求参数
     * @return {@link StringRequest}
     */
    public StringRequest getPostRequest(final int requestID, @NonNull final String url, final Map<String, String> params) {

        return getPostRequest(requestID, url, params, null);
    }

    /**
     * 创建一个POST请求,然后调用{@link }
     *
     * @param requestID       请求ID  用于多次请求区分
     * @param url             请求网络路径
     * @param params          请求参数
     * @param netWorkListener 局部监听
     * @return {@link StringRequest}
     */
    public StringRequest getPostRequest(final int requestID, @NonNull final String url, final Map<String, String> params, NetWorkListener netWorkListener) {
        final NetWorkListener _networkListener = netWorkListener == null ? mNetWorkListener : netWorkListener;
        return new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(url, "response =  " + response);
                if (_networkListener != null) {
                    try {
                        RequestStatus requestStatus = JSON.parseObject(response, RequestStatus.class);
                        if (requestStatus.operFlag.equals("1000")) {
                            _networkListener.onResponse(response, requestID);
                        } else {
                            _networkListener.onErrorResponse(null, requestStatus, requestID);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        String errorMessage = ("服务器出错！");
                        _networkListener.onErrorResponse(new VolleyError(errorMessage), null, requestID);
                    }
                } else {
                    throw new NullPointerException(" 网络监听未设置 或 顺序出错 ");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (_networkListener != null) {
                    String errorMessage = ("网络错误");
                    _networkListener.onErrorResponse(new VolleyError(errorMessage, error), null, requestID);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params == null ? super.getParams() : params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String str = null;
                try {
                    str = new String(response.data, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return mHearder;
            }
        };
    }

    /**
     * 把{@link Request}添加到 {@link RequestQueue} 中 note:只有添加才能真正请求网络
     *
     * @param request
     * @param <T>
     * @return
     */
    public <T> Request<T> add(Request<T> request) {
        return mRequestQueue.add(request);
    }




    public static class RequestStatus {
        public static final String OK = "1000";
        /**
         * operFlag : 1001
         * errorCode : 2
         */
        private String operFlag;
        private int errorCode;

        public void setOperFlag(String operFlag) {
            this.operFlag = operFlag;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getOperFlag() {
            return operFlag;
        }

        public int getErrorCode() {
            return errorCode;
        }

        @Override
        public String toString() {
            return "RequestStatus{" +
                    "operFlag='" + operFlag + '\'' +
                    ", errorCode=" + errorCode +
                    '}';
        }
    }

    /**
     * 网络监听
     */
    public interface NetWorkListener {

        /**
         * 网络请求成功
         *
         * @param response  成功返回的值
         * @param requestID 请求ID
         */
        void onResponse(String response, int requestID);

        /**
         * 网络请求失败
         *
         * @param error         失败信息 可能为空
         * @param requeststatus 失败返回状态码 可能为空
         * @param requestID     请求ID
         */
        void onErrorResponse(VolleyError error, RequestStatus requeststatus, int requestID);
    }

}
