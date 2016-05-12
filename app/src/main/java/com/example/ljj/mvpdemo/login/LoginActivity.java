package com.example.ljj.mvpdemo.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.ljj.mvpdemo.MainActivity;
import com.example.ljj.mvpdemo.R;
import com.example.ljj.mvpdemo.login.presenter.LoginPresenter;
import com.example.ljj.mvpdemo.login.presenter.LoginPresenterImpl;
import com.example.ljj.mvpdemo.login.view.LoginView;
import com.example.ljj.mvpdemo.utils.Constants;
import com.example.ljj.mvpdemo.utils.EncryptionUtil;
import com.example.ljj.mvpdemo.utils.VollyNotWorkUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginView {

    @Bind(R.id.tv_password)
    AutoCompleteTextView pswTv;
    @Bind(R.id.tv_phone)
    AutoCompleteTextView phoneTv;
    private ProgressDialog dialog;

    private LoginPresenter loginPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginPresenter = new LoginPresenterImpl(this);
        phoneTv.setError(null);
        pswTv.setError(null);
    }

    @OnClick(R.id.iv_login)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_login://登录
                String phone = phoneTv.getText().toString();
                String pswmd5 = EncryptionUtil.md5(pswTv.getText().toString());
                Map<String, String> params = new HashMap<>();
                params.put("mobilePhone", phone);
                params.put("passwd", pswmd5);
                loginPresenter.LoginService(phone, pswmd5, Constants.URL.LOGIN_URL, 0, params);
                break;
        }
    }

    @Override
    public void loginSuccess(String response, int requestID) {
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    @Override
    public void phoneError(String msg) {
        phoneTv.setError(msg);
    }

    @Override
    public void pswError(String msg) {
        pswTv.setError(msg);
    }

    @Override
    public void loginFails(VolleyError error, VollyNotWorkUtils.RequestStatus requeststatus, int requestID) {
        if (requeststatus != null) {
            int[] errorcodes = getResources().getIntArray(R.array.login_error_code);
            String[] errormessages = getResources().getStringArray(R.array.login_error_message);
            errorParse(requeststatus, errorcodes, errormessages);
        }

    }

    @Override
    public void showProgress() {
        dialog = ProgressDialog.show(LoginActivity.this, "正在登录", null);
    }

    @Override
    public void hintProgress() {
        hindProgressDialog();
    }


    private void hindProgressDialog() {
        if (dialog != null) {
            dialog.hide();
        }
    }

    public String errorParse(VollyNotWorkUtils.RequestStatus status, @NonNull int[] errorcodes, @NonNull String[] errormessages) {
        if (errorcodes.length != errormessages.length) {
            throw new IllegalArgumentException("Silly force!!!!!Length is not the same!!!!!!!!!");
        }
        for (int i = 0; i < errorcodes.length; i++) {
            if (status.getErrorCode() == errorcodes[i]) {
                Toast.makeText(LoginActivity.this, errormessages[i].toString(), Toast.LENGTH_SHORT).show();
            }
        }
        return "";
    }
}
