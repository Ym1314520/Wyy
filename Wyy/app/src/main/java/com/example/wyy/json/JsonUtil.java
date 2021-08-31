package com.example.wyy.json;

import android.util.Log;

import com.example.wyy.data.Banner;
import com.example.wyy.data.Recommend;
import com.example.wyy.data.Song;
import com.example.wyy.data.Song_inner;
import com.example.wyy.data.UserCreateSong;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonUtil {
    String TAG="MENG";
    static int count=10;
    //横幅banner
    Banner banner;
    List<Banner> bannerList;
    //推荐歌曲recommend
    Recommend recommend;
    List<Recommend> recommendList;
    //新歌songs
    Song song;
    List<List<Song>> songList;

    //用户创建歌单
    List<Song_inner> createSongList,collectionSongList;

    //歌单详情
    HashMap<String,String> map;
    List<Song_inner> song_innerList;
    List<Song_inner> temp;
    //搜索详情
    List<Song_inner> searchList;


    public List<Song_inner> getSong_innerList() {
        return song_innerList;
    }


    public HashMap<String, String> getMap() {
        return map;
    }

    public List<Banner> getBannerList() {
        return bannerList;
    }

    public List<Recommend> getRecommendList() {
        return recommendList;
    }

    public List<List<Song>> getSongList() {
        return songList;
    }

    public List<Song_inner> getCreateSongList() {
        return createSongList;
    }

    public List<Song_inner> getCollectionSongList() {
        return collectionSongList;
    }

    public void jsonBanner(String temp_data) throws JSONException {
        bannerList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(temp_data);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray blocks = data.getJSONArray("blocks");
        JSONObject mBlock = blocks.getJSONObject(0);
        JSONObject extInfo = mBlock.getJSONObject("extInfo");
        JSONArray banners = extInfo.getJSONArray("banners");
        for (int i = 0; i < banners.length(); i++) {
            banner = new Banner();
            JSONObject picBlock = banners.getJSONObject(i);
            String pic = picBlock.getString("pic");
            banner.setPic(pic);
            bannerList.add(banner);

        }

    }

    public void jsonRecommend(String temp_data) throws JSONException {
        recommendList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(temp_data);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray blocks = data.getJSONArray("blocks");
        JSONObject mBlock = blocks.getJSONObject(1);
        JSONArray creatives = mBlock.getJSONArray("creatives");
        for (int k = 0; k < creatives.length(); k++) {
            recommend = new Recommend();
            JSONObject mblock = creatives.getJSONObject(k);
            String creativeId = mblock.getString("creativeId");
            JSONObject uiElement = mblock.getJSONObject("uiElement");
            JSONObject mainTitle = uiElement.getJSONObject("mainTitle");
            String title = mainTitle.getString("title");
            JSONObject image = uiElement.getJSONObject("image");
            String imageUrl = image.getString("imageUrl");
            recommend.setId(creativeId);
            recommend.setTitle(title);
            recommend.setImageUrl(imageUrl);
            recommendList.add(recommend);
        }
    }

    //新歌解析
    public void jsonSongs(String temp_data) throws JSONException {
        List<Song> temp_songs = new ArrayList<>();
        songList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(temp_data);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray blocks = data.getJSONArray("blocks");
        JSONObject mBlock = blocks.getJSONObject(2);
        JSONArray creatives = mBlock.getJSONArray("creatives");
        for (int w = 0; w < creatives.length(); w++) {
            JSONObject mmBlock = creatives.getJSONObject(w);//拿到新歌一栏
            JSONArray resources = mmBlock.getJSONArray("resources");
            for (int t = 0; t < resources.length(); t++) {
                song = new Song();
                JSONObject mblock = resources.getJSONObject(t);
                JSONObject uiElement = mblock.getJSONObject("uiElement");
                JSONObject mainTitle = uiElement.getJSONObject("mainTitle");
                String title1 = mainTitle.getString("title");
                JSONObject subTitle = uiElement.getJSONObject("subTitle");
                String title2 = subTitle.getString("title");
                JSONObject image = uiElement.getJSONObject("image");
                String imageUrl = image.getString("imageUrl");
                JSONObject resourceExtInfo = mblock.getJSONObject("resourceExtInfo");
                JSONArray artists = resourceExtInfo.getJSONArray("artists");
                JSONObject mmblock = artists.getJSONObject(0);
                String name = mmblock.getString("name");
                song.setName(name);
                song.setImageUrl(imageUrl);
                song.setMainTitle(title1);
                song.setSubTitle(title2);
                temp_songs.add(song);
                if ((t + 1) % 3 == 0) {
                    songList.add(temp_songs);
                    temp_songs = new ArrayList<>();
                }
            }
        }
    }

    //简单的解析一个数据（比如登录、短信等）
    public HashMap<String,String> jsonData(String datas) throws JSONException {
        HashMap<String,String> map=new HashMap<>();
        JSONObject jsonObject=new JSONObject(datas);
        String code=jsonObject.getString("code");
        String cookie=jsonObject.getString("cookie");
        map.put("code",code);
        map.put("cookie",cookie);
        return map;
    }

    //解析用于判断code的网络请求
    public String jsonCode(String data) throws JSONException{
        JSONObject jsonObject=new JSONObject(data);
        return jsonObject.getString("code");
    }

    //简单的解析用户名
    public HashMap<String,String> jsonUser(String data4) throws JSONException {
        HashMap<String,String> map=new HashMap<>();
        JSONObject jsonObject=new JSONObject(data4);
        JSONObject profile=jsonObject.getJSONObject("profile");
        map.put("nickname",profile.getString("nickname"));
        map.put("userId",profile.getString("userId"));
        return map;
    }

    public String jsonLevel(String data5) throws JSONException{
        JSONObject jsonObject=new JSONObject(data5);
        JSONObject data=jsonObject.getJSONObject("data");
        return data.getString("level");
    }

    public Song_inner jsonIt(String data) throws JSONException {
        Song_inner song=new Song_inner();
        JSONObject jsonObject=new JSONObject(data);
        JSONArray playlist=jsonObject.getJSONArray("playlist");
        for(int i=0;i<playlist.length();i++){
            JSONObject mBlock2=playlist.getJSONObject(i);
            if(mBlock2.getString("subscribed").equals("true")){
                String coverImgUrl2 = mBlock2.getString("coverImgUrl");
                String name = mBlock2.getString("name");
                String id =mBlock2.getString("id");
                String trackCount2=mBlock2.getString("trackCount");
                song.setCoverImgUrl(coverImgUrl2);
                song.setId(id);
                song.setTitle(name);
                song.setTrackCount(trackCount2);
                break;
            }
        }
        return song;
    }
    public HashMap<String,String> jsonCoverPic(String data) throws JSONException {
        createSongList=new ArrayList<>();
        collectionSongList=new ArrayList<>();
        HashMap<String,String> map=new HashMap<>();
        JSONObject jsonObject=new JSONObject(data);
        JSONArray playlist=jsonObject.getJSONArray("playlist");
        JSONObject mBlock=playlist.getJSONObject(0);
        String coverImgUrl=mBlock.getString("coverImgUrl");
        String trackCount=mBlock.getString("trackCount");
        String id_love=mBlock.getString("id");
        map.put("picUrl",coverImgUrl);
        map.put("count",trackCount);
        map.put("id",id_love);
        for(int i=1;i<playlist.length();i++) {
            Song_inner song=new Song_inner();
            JSONObject mBlock2=playlist.getJSONObject(i);
            String coverImgUrl2 = mBlock2.getString("coverImgUrl");
            String name = mBlock2.getString("name");
            String id =mBlock2.getString("id");
            String trackCount2=mBlock2.getString("trackCount");
            song.setCoverImgUrl(coverImgUrl2);
            song.setId(id);
            song.setTitle(name);
            song.setTrackCount(trackCount2);
            if(mBlock2.getString("subscribed").equals("true")){
                collectionSongList.add(song);
            }else{
                createSongList.add(song);
            }

        }
        return map;
    }

    public void jsonSongList(String data) throws JSONException {
        List<String> songs=new ArrayList<>();
        song_innerList=new ArrayList<>();
        map=new HashMap<>();
        JSONObject jsonObject=new JSONObject(data);
        JSONObject playlist=jsonObject.getJSONObject("playlist");
        String name=playlist.getString("name");
        String coverImgUrl=playlist.getString("coverImgUrl");
        map.put("name",name);
        map.put("coverImgUrl",coverImgUrl);
        JSONArray tracks=playlist.getJSONArray("tracks");
        for(int i=0;i<tracks.length();i++){
            Song_inner song=new Song_inner();
            JSONObject mBlock=tracks.getJSONObject(i);
            String song_name=mBlock.getString("name");
            String song_id=mBlock.getString("id");
            JSONArray ar=mBlock.getJSONArray("ar");
            JSONObject mblock=ar.getJSONObject(0);
            String singer=mblock.getString("name");
            JSONObject al=mBlock.getJSONObject("al");
            String subname=al.getString("name");
            song.setTitle(song_name);
            song.setAuthor(singer);
            song.setSubtitle(subname);
            song.setId(song_id);
            song_innerList.add(song);
        }
    }

    public HashMap<String,String> jsonSongUrl(String data1) throws JSONException {
        HashMap<String,String> id_url=new HashMap<>();
        JSONObject jsonObject=new JSONObject(data1);
        JSONArray data=jsonObject.getJSONArray("data");
        for (int i=0;i<data.length();i++){
            JSONObject mBlcok=data.getJSONObject(i);
            String url=mBlcok.getString("url");
            String id=mBlcok.getString("id");
            id_url.put(id,url);
        }
        return id_url;
    }

    public List<Song_inner> jsonSearchList(String data) throws JSONException{
        searchList=new ArrayList<>();
        JSONObject jsonObject=new JSONObject(data);
        JSONObject result=jsonObject.getJSONObject("result");
        JSONArray songs=result.getJSONArray("songs");
        for(int i=0;i<songs.length();i++){
            Song_inner search=new Song_inner();
            JSONObject mBlcok=songs.getJSONObject(i);
            String name=mBlcok.getString("name");
            JSONArray ar=mBlcok.getJSONArray("ar");
            JSONObject mblock=ar.getJSONObject(0);
            String author_name=mblock.getString("name");
            JSONObject al=mBlcok.getJSONObject("al");
            String subname=al.getString("name");
            String id=mBlcok.getString("id");
            search.setTitle(name);
            search.setAuthor(author_name);
            search.setSubtitle(subname);
            search.setId(id);
            searchList.add(search);
        }
        return searchList;
    }


    public void json(String data) throws JSONException {
        JSONObject jsonObject=new JSONObject(data);
        String date=jsonObject.getString("date");
        JSONArray stories=jsonObject.getJSONArray("stories");
        for(int i=0;i<stories.length();i++){
            JSONObject mBlock=stories.getJSONObject(i);
            String item=mBlock.getString("item");//这里就是{},{},{}里面一个一个的item
        }

        JSONArray top_stories=jsonObject.getJSONArray("top_stories");
        for(int j=0;j<top_stories.length();j++){
            JSONObject mBlock2=stories.getJSONObject(j);
            String item2=mBlock2.getString("item");
        }
    }

}

