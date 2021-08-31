package com.example.wyy.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyy.R;
import com.example.wyy.data.Song_inner;
import com.example.wyy.data.UserCreateSong;
import com.example.wyy.service.MyService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicPlayer <T> extends AppCompatActivity implements View.OnClickListener{
    String TAG="TIME";
    TextView song_name,song_author;
    ImageView start,cd,type,goback,previous,next;
    static SeekBar bar;
    MyService.MusicControl control;
    static TextView duration_text,progress_text;
    int click_number=0;//奇数暂停，偶数开始
    int count=0;//1随机，2单曲，3循环
    ObjectAnimator animator;
    List<Song_inner> song_innerList;
    List<UserCreateSong> song_list;
    List<String> idList;

    List<T> list;
    String id;
    static int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        Intent intent2=getIntent();
        if(intent2!=null){
            position=intent2.getIntExtra("position",0);
            Log.d(TAG, "position:"+String.valueOf(position));
            song_innerList=(ArrayList<Song_inner>)intent2.getSerializableExtra("songList");
            song_list= (List<UserCreateSong>) intent2.getSerializableExtra("song_list");
            idList=new ArrayList<>();
            //判断一下应该把哪一个传入idList
                for (Song_inner song : song_innerList) {
                    idList.add(song.getId());
                }

        }

        init();
    }

    ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            control=(MyService.MusicControl) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void init() {
        next=findViewById(R.id.next);
        previous=findViewById(R.id.previous);
        song_name=findViewById(R.id.song_name);
        song_author=findViewById(R.id.song_author);
        goback=findViewById(R.id.go_back);
        type=findViewById(R.id.type);
        type.setImageResource(R.drawable.recycle);
        cd=findViewById(R.id.song_player);
        start = findViewById(R.id.start_song);
        bar = findViewById(R.id.bar);
        duration_text=findViewById(R.id.duration);
        progress_text=findViewById(R.id.progress);

        //设定界面内容
        song_name.setText(song_innerList.get(position).getTitle());
        song_author.setText(song_innerList.get(position).getAuthor());

        //该活动绑定服务
        Intent intent=new Intent(this,MyService.class);
        intent.putExtra("idList",(Serializable)idList);
        intent.putExtra("position",position);
        bindService(intent,connection,BIND_AUTO_CREATE);

        animator=ObjectAnimator.ofFloat(cd,"rotation",0.0F,360.0F);
        animator.setDuration(12000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        start.setOnClickListener(this);
        type.setOnClickListener(this);//选择听歌模式
        goback.setOnClickListener(this);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {//seekBar的监听器
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, String.valueOf(seekBar.getMax()));
                if(progress==seekBar.getMax()){
                    if(count%3==2){
                        control.play();
                    }else if(count%3==1){
                        Random ran=new Random();
                        int number=ran.nextInt(20);
                        control.random(number);
                        song_name.setText(song_innerList.get(number).getTitle());
                        song_author.setText(song_innerList.get(number).getAuthor());
                    }else{//列表循环
                        position+=1;
                        if(position==idList.size()){
                            control.recycle();
                            position=0;
                        }else {
                                control.next();
                        }
                            song_name.setText(song_innerList.get(position).getTitle());
                            song_author.setText(song_innerList.get(position).getAuthor());
                    }

                }
                if(fromUser){
                    control.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//拖动bar条的时候
                control.pause();
                animator.pause();
                click_number=1;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                animator.resume();
                control.restart();
            }
        });
    }

    public static Handler handler=new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle=msg.getData();
            int duration=bundle.getInt("duration");
            int current=bundle.getInt("currentPos");
            bar.setMax(duration);
            bar.setProgress(current);
            //更新UI
            int sec=duration/1000;
            int min=sec/60;
            duration_text.setText(String.format("%02d:%02d",min,sec-min*60));
            int sec2=current/1000;
            int min2=sec2/60;
            progress_text.setText(String.format("%02d:%02d",min2,sec2-min2*60));

        }
    };


    //所有控件的监听器
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_song:
                if(click_number==0) {
                    start.setSelected(true);
                    animator.start();
                    control.play();
                    click_number++;
                }else if(click_number%2==1){
                    start.setSelected(false);
                    animator.pause();
                    control.pause();
                    click_number++;
                }else{
                    start.setSelected(true);
                    animator.resume();
                    control.restart();
                    click_number++;
                }
                break;

            case R.id.type:
                count++;
                if(count%3==1){
                    type.setImageResource(R.drawable.random);
                    Toast.makeText(v.getContext(),"随机播放",Toast.LENGTH_SHORT).show();
                }else if(count%3==2){
                    type.setImageResource(R.drawable.single);
                    Toast.makeText(v.getContext(),"单曲循环",Toast.LENGTH_SHORT).show();
                }else{
                    type.setImageResource(R.drawable.recycle);
                    Toast.makeText(v.getContext(),"列表循环",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.go_back:
                finish();
                break;

            case R.id.previous:
                position-=1;
                song_name.setText(song_innerList.get(position).getTitle());
                song_author.setText(song_innerList.get(position).getAuthor());
                control.previous();
                break;

            case R.id.next:
                position+=1;
                if(position==idList.size()){
                    control.recycle();
                    position=0;
                }else {
                    control.next();
                }
                song_name.setText(song_innerList.get(position).getTitle());
                song_author.setText(song_innerList.get(position).getAuthor());
                break;
                
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        control.stop();
        unbindService(connection);
        super.onDestroy();

    }
}
