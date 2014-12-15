package com.example.xvxvxxx.xxplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xvxvxxx on 12/13/14.
 */
public class MusicListAdapter extends BaseAdapter {
    List<MusicInfo> listVideos;
    int local_postion = 0;
    boolean imageChage = false;
    private LayoutInflater mLayoutInflater;
    //private ArrayList<LoadedImage> photos = new ArrayList<LoadedImage>();
    public MusicListAdapter(Context context, List<MusicInfo> listVideos){
        mLayoutInflater = LayoutInflater.from(context);
        this.listVideos = listVideos;
    }
//    @Override
    public int getCount() {
        return listVideos.size();
    }
//    public void addPhoto(LoadedImage image){
//        photos.add(image);
//    }
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            //view = mLayoutInflater.inflate(R.layout.listview, null);
            //holder.img = (ImageView)view.findViewById(R.id.video_img);
            //holder.title = (TextView)view.findViewById(R.id.video_title);
            //holder.time = (TextView)view.findViewById(R.id.video_time);
            //holder.local=(TextView) view.findViewById(R.id.video_local);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }
        holder.title.setText(listVideos.get(position).getTitle());
        long min = listVideos.get(position).getDuration() /1000 / 60;
        long sec = listVideos.get(position).getDuration() /1000 % 60;
        holder.time.setText("时长"+min+":"+sec);
        holder.local.setText("位置："+listVideos.get(position).getUrl());
        //holder.img.setImageBitmap(photos.get(position).getBitmap());

        return view;
    }

    public final class ViewHolder{
        public ImageView img;
        public TextView title;
        public TextView time;
        public TextView local;
    }
}
