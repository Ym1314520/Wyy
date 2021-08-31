package com.example.wyy.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.wyy.R;
import com.example.wyy.activity.MusicPlayer;
import com.example.wyy.internetUtil.InternetUtil;
import com.example.wyy.json.JsonUtil;

import org.json.JSONException;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    String TAG="SONG";
    MediaPlayer mediaPlayer;
    Timer timer;//时间对象
    Handler handler;
    List<String> idList;
    HashMap<String,String> id_url;
    int position;
    String current_id;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        idList=new ArrayList<>();

        handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String data=msg.obj.toString();;
                JsonUtil jsonUtil=new JsonUtil();
                try {
                    id_url=jsonUtil.jsonSongUrl(data);
                    Log.d(TAG, "ID:"+id_url.values());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        mediaPlayer=new MediaPlayer();
    }

    @Override
    public IBinder onBind(Intent intent) {//老通讯员了
        idList= (ArrayList<String>) intent.getSerializableExtra("idList");
        position=intent.getIntExtra("position",0);
        current_id=idList.get(position);
        getSongUrl();
        return new MusicControl();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void addTimer(){
        if(timer==null){
            timer= new Timer();
            TimerTask task=new TimerTask() {
                @Override
                public void run() {
                    int duration=mediaPlayer.getDuration();
                    int currentPos=mediaPlayer.getCurrentPosition();
                    Message message= MusicPlayer.handler.obtainMessage();
                    Bundle bundle=new Bundle();
                    bundle.putInt("duration",duration);
                    bundle.putInt("currentPos",currentPos);
                    message.setData(bundle);
                    MusicPlayer.handler.sendMessage(message);
                }
            };
            timer.schedule(task,5,500);
        }
    }


    public class MusicControl extends Binder{

        public void next(){
            position+=1;
            current_id=idList.get(position);
            play();
        }

        public void random(int number){
            position=number;
            current_id=idList.get(position);
            play();
        }

        public void recycle(){
            position=0;
            current_id=idList.get(position);
            play();
        }

        public void previous(){
            position-=1;
            current_id=idList.get(position);
            play();
        }

        public void play(){
            mediaPlayer.reset();
            if(id_url.get(current_id).equals("null")) {
                Toast.makeText(getApplicationContext(),"该资源暂无，正在努力请求中...",Toast.LENGTH_SHORT).show();
            }else{
                mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(id_url.get(current_id)));//加载歌曲资源uri进来
                mediaPlayer.start();
                addTimer();
            }
        }

        public void pause(){
            mediaPlayer.pause();
        }

        public void seekTo(int ms){
            mediaPlayer.seekTo(ms);
        }

        public void restart(){
            mediaPlayer.start();
        }

        public void stop(){
            try {
                timer.cancel();
                mediaPlayer.stop();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public void getSongUrl(){
        InternetUtil internetUtil=new InternetUtil();
        HashMap<String,String> map=new HashMap<>();
        StringBuilder sb=new StringBuilder();
        for(String id:idList){
            sb.append(id).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        map.put("id",sb.toString());
        internetUtil.sendPostNetRequest("http://49.234.117.142:3000/song/url",map,handler);
    }
}
