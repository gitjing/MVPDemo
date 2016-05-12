package com.example.ljj.mvpdemo.utils;

/**
 * Created by ljj on 2016/4/21.
 */
public class Constants {

    private static final String RELEASE_URL = "http://hlf.meifangquan.com/";

    public static double longitude = 0d;//经度
    public static double latitude = 0d;//纬度
    public static final String APP_VERSION = "99";

    public interface URL{
        String LOGIN_URL = RELEASE_URL + "user/commonLogin.action";
    }
}
