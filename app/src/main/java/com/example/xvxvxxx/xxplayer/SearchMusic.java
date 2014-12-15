package com.example.xvxvxxx.xxplayer;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by xvxvxxx on 12/13/14.
 */
public class SearchMusic {
    private Context context;
    public SearchMusic(Context context) {
        this.context = context;
    }

    public List<Map<String,Object>> getList(){
        //List<MusicInfo> list = null;
        List<Map<String,Object>>listItems =new ArrayList<Map<String, Object>>();


        if (context != null){
            Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            cursor.moveToFirst();
            //list = new ArrayList<MusicInfo>();
            do {
                Map<String, Object> listItem = new HashMap<String, Object>();
                //歌曲ID：MediaStore.Audio.Media._ID
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                listItem.put("id",id);
                //歌曲的名称 ：MediaStore.Audio.Media.TITLE
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                listItem.put("title",title);
                //歌曲的专辑名：MediaStore.Audio.Media.ALBUM
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                listItem.put("album",album);
                //歌曲的歌手名： MediaStore.Audio.Media.ARTIST
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                listItem.put("artist",artist);
                //歌曲文件的全路径 ：MediaStore.Audio.Media.DATA
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                listItem.put("url",url);
                //歌曲文件的名称：MediaStore.Audio.Media.DISPLAY_NAME
                String display_name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                listItem.put("display_name", display_name);
                //歌曲文件的发行日期：MediaStore.Audio.Media.YEAR
                String year = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR));
                listItem.put("year", year);
                //歌曲的总播放时长 ：MediaStore.Audio.Media.DURATION
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                listItem.put("duration", duration);
                //歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                listItem.put("size", size);
                MusicInfo musicInfo = new MusicInfo(id, title, album, artist, url, display_name, year, size, duration);

                listItems.add(listItem);
                Log.i("Cursor", "\n\n@:" + title + "\n@:" + album + "\n" + "\n@:" + artist + "\n" + "\n@:" + url + "\n" + "\n@:" + display_name + "\n" + "\n@:" + year + "\n" + "\n@:" + duration + "\n" + "\n@:" + size);
            }while (cursor.moveToNext());
            cursor.close();
            Log.i("Search", "搜索音乐文件完成");
            System.out.println("搜索音乐文件完成");
        }
        return listItems;
    }

}

