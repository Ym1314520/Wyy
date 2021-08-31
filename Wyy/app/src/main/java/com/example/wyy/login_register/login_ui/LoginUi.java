package com.example.wyy.login_register.login_ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyy.R;
import com.example.wyy.internetUtil.InternetUtil;
import com.example.wyy.json.JsonUtil;
import com.example.wyy.login_register.Login;
import com.example.wyy.main.MainActivity;

import org.json.JSONException;

import java.util.HashMap;

public class LoginUi extends AppCompatActivity implements View.OnClickListener{

    private String TAG="YY";
    private Button login;
    private DrawerLayout drawerLayout;
    private TextView register;
    private HashMap<String,String> counter;
    private Handler handler;
    private InternetUtil internetUtil;
    private JsonUtil jsonData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        counter=new HashMap<>();
        internetUtil=new InternetUtil();
        jsonData=new JsonUtil();
        register=findViewById(R.id.register);
        drawerLayout=findViewById(R.id.drawer_layout);

        login=findViewById(R.id.login);
        login.setOnClickListener(this);
        register.setOnClickListener(this);

        handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String data=msg.obj.toString();
                switch (msg.what){
                    case 1:
                        try {
                            String code=jsonData.jsonCode(data);
                            if(code.equals("200")){
                                Toast.makeText(getApplicationContext(),"发送成功",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"发送失败",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    case 2:
                        try {
                            String code =jsonData.jsonCode(data);
                            if(code.equals("200")){
                                Toast.makeText(getApplicationContext(),"成功注册,返回进行登录",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"验证码不匹配",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                break;

                }
                return false;
            }
        });

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener(){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                Button send_captcha =findViewById(R.id.send_captcha);
                Button login=findViewById(R.id.login_go);
                EditText password,phone_number,captcha,nickname;
                nickname=findViewById(R.id.nickname);
                password=findViewById(R.id.editText);
                phone_number=findViewById(R.id.phones);
                captcha=findViewById(R.id.captcha);
                send_captcha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counter.put("phone", phone_number.getText().toString());
                        if(!counter.isEmpty()) {
                            internetUtil.sendPostNetRequest("http://49.234.117.142:3000/captcha/sent", counter, handler);
                        }else{
                            Toast.makeText(v.getContext(),"请填写正确的手机号",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counter.clear();
                        counter.put("phone",phone_number.getText().toString());
                        counter.put("password",password.getText().toString());
                        counter.put("captcha",captcha.getText().toString());
                        counter.put("nickname",nickname.getText().toString());
                        Log.d(TAG, counter.values().toString());
                        if(!counter.values().contains("")){
                            internetUtil.sendPostNetRequest("http://49.234.117.142:3000/register/cellphone",counter,handler);
                        }else{
                            Toast.makeText(v.getContext(),"请填写正确完整的信息!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                Intent intent=new Intent(LoginUi.this, Login.class);
                startActivity(intent);
                break;
            case R.id.register:
                drawerLayout.openDrawer(Gravity.RIGHT);
                break;
        }
    }
}
