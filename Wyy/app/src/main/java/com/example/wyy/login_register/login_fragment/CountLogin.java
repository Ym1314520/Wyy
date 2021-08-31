package com.example.wyy.login_register.login_fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wyy.Cookie;
import com.example.wyy.R;
import com.example.wyy.internetUtil.InternetUtil;
import com.example.wyy.json.JsonUtil;
import com.example.wyy.main.MainActivity;

import org.json.JSONException;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;


public class CountLogin extends Fragment implements View.OnClickListener{

    String TAG="cookie";
    String data;
    EditText count,password;
    Button goIt;
    InternetUtil internetUtil;
    JsonUtil jsonUtil;
    HashMap<String,String> counter;
    Handler handler;
    Cookie cookieManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        internetUtil=new InternetUtil();
        jsonUtil=new JsonUtil();
        counter=new HashMap<>();
        handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                data=msg.obj.toString();
                try {
                    HashMap<String,String> map=jsonUtil.jsonData(data);
                    cookieManager=new Cookie();
                    cookieManager.setCookie(map.get("cookie"));
                    if(map.get("code").equals("200")){
                        Intent intent =new Intent(getContext(), MainActivity.class);
                        intent.putExtra("sinal",data);
                        Toast.makeText(getContext(),"成功登录",Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }else{
                        Toast.makeText(getContext(),"密码错误",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.count_login, container, false);
        count=view.findViewById(R.id.count);
        password=view.findViewById(R.id.password);
        goIt=view.findViewById(R.id.goIt);
        goIt.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goIt:
                counter.put("phone",count.getText().toString());
                counter.put("password",password.getText().toString());
                internetUtil.sendPostNetRequest("http://49.234.117.142:3000/login/cellphone",counter,handler);
        }
    }
}
