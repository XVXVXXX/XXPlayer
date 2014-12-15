package com.example.xvxvxxx.xxplayer;

import java.io.Serializable;

/**
 * Created by xvxvxxx on 12/13/14.
 */
public class MusicInfo implements Serializable {
    private int id;
    private String title;
    private String album;
    private String artist;
    private String url;
    private String display_Name;
    private String year;
    private long duration;
    private long size;

    //    private LoadedImage image;
    public MusicInfo() {
        super();
    }

    public MusicInfo(int id, String title, String album, String artist, String url,
                     String display_Name, String year, long size,long duration) {
        super();
        this.id = id;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.url = url;
        this.display_Name = display_Name;
        this.year = year;
        this.duration = duration;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplay_Name() {
        return display_Name;
    }

    public void setDisplay_Name(String display_Name) {
        this.display_Name = display_Name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

//    public LoadedImage getImage(){
//        return image;
//    }

//    public void setImage(LoadedImage image){
//        this.image = image;
//    }
}