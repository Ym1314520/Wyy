package com.example.wyy.login_register.login_fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyy.R;
import com.example.wyy.fragment.MyPage;
import com.example.wyy.internetUtil.InternetUtil;
import com.example.wyy.json.JsonUtil;
import com.example.wyy.main.MainActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class PhoneLogin extends Fragment implements View.OnClickListener{
    String TAG="YY";
    EditText phone,check;
    TextView send;
    Button click;
    Map<String,String> phone_number;
    InternetUtil internetUtil;
    Handler handler;
    JsonUtil jsonData;
    HashMap<String,String> check_number;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        internetUtil=new InternetUtil();
        jsonData=new JsonUtil();
        phone_number=new HashMap<>();
        check_number=new HashMap<>();
        Log.d(TAG, "YYYYY");
        handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String data=msg.obj.toString();
                switch ((msg.what)){
                    case 1:
                        try {
                            HashMap<String,String> map=jsonData.jsonData(data);
                            if(map.get("code").equals("200")){
                                Toast.makeText(getContext(),"发送成功",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getContext(),"发送失败",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        try {
                            HashMap<String,String> map=jsonData.jsonData(data);
                            if(map.get("code").equals("200")){

                                Toast.makeText(getContext(),"成功登录",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getContext(), MainActivity.class);
                                intent.putExtra("sinal",data);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getContext(),"验证码不匹配",Toast.LENGTH_SHORT).show();
                             }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }

                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.phone_login, container, false);
        phone=view.findViewById(R.id.phone);
        check=view.findViewById(R.id.check);
        send=view.findViewById(R.id.send);
        click=view.findViewById(R.id.click);
        send.setOnClickListener(this);
        click.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send:
                phone_number.put("phone", phone.getText().toString());
                if(!phone_number.isEmpty()) {
                    internetUtil.sendPostNetRequest("http://49.234.117.142:3000/captcha/sent", (HashMap<String, String>) phone_number, handler);
                }else{
                    Toast.makeText(v.getContext(),"请填写正确的手机号",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.click:
                check_number.put("phone",phone.getText().toString());
                check_number.put("captcha",check.getText().toString());
                if(!check_number.isEmpty()){
                    internetUtil.sendPostNetRequest("http://49.234.117.142:3000/login/cellphone",check_number,handler);
                }else{
                    Toast.makeText(v.getContext(),"请填写验证码",Toast.LENGTH_SHORT).show();
                }
        }
    }
}
