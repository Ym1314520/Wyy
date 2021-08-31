package com.example.wyy.internetUtil;

import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.wyy.Cookie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class InternetUtil {
    String TAG = "INTERNET";
    String cookie;
//    static String cookie = "MUSIC_U=f32325d98287a7e5756d332cd582161ba93a41302bde13af32ea11eb7d11ed000931c3a9fbfe3df2; Max-Age=1296000; Expires=Tue, 14 Sep 2021 11:35:56 GMT; Path=/;;__csrf=aafe4b860390d4a5e9824d0cc4ed84b5; Max-Age=1296010; Expires=Tue, 14 Sep 2021 11:36:06 GMT; Path=/;;__remember_me=true; Max-Age=1296000; Expires=Tue, 14 Sep 2021 11:35:56 GMT; Path=/;";
    public InternetUtil(){
        Cookie cookies =new Cookie();
        cookie=cookies.getCookie();
    }

    public void sendGetNetRequest(final String mUrl, final Handler handler) {//Handler机制的网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(mUrl);//实例化一个网络对象
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();//网络对象打开网络连接
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    if (cookie != null) {
                        //发送cookie信息上去，以表明自己的身份，否则会被认为没有权限
                        connection.setRequestProperty("Cookie", cookie);
                    }
                    connection.connect();
                    InputStream in = connection.getInputStream();
                    String data = StreamToString(in);//用自定义的StreamToString函数将字节流转换为字符流
                    Log.d(TAG,"onStart:"+ data);
                    //这里用Handler传data回去
                    Message message = new Message();
                    message.what = 0;
                    message.obj = data;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    //POST
    public void sendPostNetRequest(final String mUrl, final HashMap<String, String> params, final Handler handler) {
        Log.d(TAG, "刚进来的cookie:onStart:" + cookie);
        new Thread(
                new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        try {
                            StringBuilder dataToWrite = new StringBuilder();
                            dataToWrite.append(mUrl).append("?");
                            for (String key : params.keySet()) {
                                dataToWrite.append(key).append("=").append(params.get(key)).append("&");
                            }

                            URL url = new URL(dataToWrite.deleteCharAt(dataToWrite.length() - 1).toString());
                            Log.d(TAG, "onStart,这是请求的网址：" + url);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setConnectTimeout(8000);
                            connection.setReadTimeout(8000);
                            connection.setDoOutput(true);
                            connection.setDoInput(true);
                            if (cookie != null) {
                                //发送cookie信息上去，以表明自己的身份，否则会被认为没有权限
                                connection.setRequestProperty("Cookie", cookie);
                            }
                            connection.connect();//正式连接
                            Log.d(TAG, "onStart:cookie:" + cookie);
                            InputStream in = connection.getInputStream();
                            String responseData = StreamToString(in);//这里就是服务器返回的数据
                            Log.d(TAG, "返回的data:" + responseData);
                            if (params.size() == 1) {
                                Message message = new Message();
                                message.what = 1;
                                message.obj = responseData;
                                handler.sendMessage(message);
                            } else {
                                Message message = new Message();
                                message.what = 2;
                                message.obj = responseData;
                                handler.sendMessage(message);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).start();
    }


    private String StreamToString(InputStream in) {
        StringBuilder sb = new StringBuilder();//字符串构造器
        String oneLine;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            while ((oneLine = reader.readLine()) != null) {
                sb.append(oneLine).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
