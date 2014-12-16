package com.example.xvxvxxx.xxplayer;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends ActionBarActivity  {
    //搜索音乐类
    private SearchMusic searchMusic;
    //返回的音乐列表，用来形成listView
    List<Map<String,Object>> musicList;
    private String musicURL;
    private int musicPosition;
    private Stack<Integer> musicPlayStack;
    //播放列表视图
    private ListView listView;
    private List<String> playList;
    //Header条
    private ImageView listButton;
    private ImageView logoButton;
    private ImageView doubanFMButton;
    //播放栏Button 包括开始/暂停、上一曲、随机、下一曲
    private ImageView backButton;
    private ImageView playButton;
    private ImageView nextButton;
    private ImageView shuffleButton;
    //音乐信息View
    private TextView trackTitle;
    private TextView trackArtist;
    private TextView trackAlbum;
    //音乐进度view
    private TextView trackDuration;
    private TextView trackCurrPostion;
    private TextView trackPostionSeparator;
    private Timer timer;
    private TimerTask timerTask;
    private SeekBar progressBar;
    //webView DoubanFM
    private WebView webView;
    //Slides页面
    private RelativeLayout nowPlayingSlide;
    private RelativeLayout playlistSlide;
    private RelativeLayout doubanFMSlide;
    // Music mediaPlayer
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lookupViewElements();
        init();

    }

    private void lookupViewElements(){
        listButton = (ImageView) findViewById( R.id.listButton);
        logoButton = (ImageView) findViewById( R.id.logoButton);
        logoButton.setImageDrawable(getResources().getDrawable(R.drawable.logo128_active));
        doubanFMButton = (ImageView) findViewById( R.id.doubanFMButton);
        //播放button
        backButton = (ImageView) findViewById( R.id.backButton);
        playButton = (ImageView) findViewById( R.id.playButton);
        shuffleButton = (ImageView) findViewById( R.id.shuffleButton);
        nextButton = (ImageView) findViewById( R.id.nextButton);
        //
        listView = (ListView) findViewById(R.id.filterlist);
        nowPlayingSlide = (RelativeLayout) findViewById(R.id.now_playing_slide);
        playlistSlide = (RelativeLayout) findViewById(R.id.playlist_slide);
        doubanFMSlide = (RelativeLayout) findViewById(R.id.doubanfm);
        //音乐信息view
        trackTitle = (TextView) findViewById( R.id.trackTitle);
        trackTitle.setTextColor(Color.WHITE);
        trackArtist = (TextView) findViewById( R.id.trackArtist);
        trackArtist.setTextColor(Color.WHITE);
        trackAlbum = (TextView) findViewById( R.id.trackAlbum);
        trackAlbum.setTextColor(Color.WHITE);
        //
        trackDuration = (TextView) findViewById( R.id.trackDuration);
        trackDuration.setTextColor(Color.WHITE);
        trackCurrPostion = (TextView) findViewById( R.id.trackCurrPostion);
        trackCurrPostion.setTextColor(Color.WHITE);
        trackPostionSeparator = (TextView) findViewById( R.id.trackPostionSeparator);
        trackPostionSeparator.setTextColor(Color.WHITE);
        progressBar = (SeekBar) findViewById( R.id.progressBar);
        //
        webView = (WebView) findViewById( R.id.webView);
    }

    //点击监控
    private void setupListeners()
    {
        //播放列表点击监控
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                musicPosition = position;
                playMusic();
                SwitchToNowPlayingSlide();
            }
        });
        //三个头部按钮监控
        listButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                SwitchToPlaylistSlide();
            }
        });

        logoButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                SwitchToNowPlayingSlide();
            }
        });

        doubanFMButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {SwitchToDoubanFMSlide();
            }
        });
        //开始播放、暂停播放
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    playButton.setImageDrawable(getResources().getDrawable(R.drawable.pause96));
                }
                else{
                    mediaPlayer.start();
                    playButton.setImageDrawable(getResources().getDrawable(R.drawable.play96));
                }
            }
        });
        //上一曲
        backButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (!musicPlayStack.isEmpty()){
                    musicPosition = musicPlayStack.pop();
                }
                else{
                    musicPosition = (int)(Math.random()*10000)%musicList.size();
                    musicPlayStack.push(musicPosition);
                }
                playMusic();
            }
        });
        //随机下一曲
        shuffleButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                musicPosition = (int)(Math.random()*10000)%musicList.size();
                musicPlayStack.push(musicPosition);
                playMusic();
            }
        });
        //下一曲
        nextButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                musicPosition = (++musicPosition)%musicList.size();
                musicPlayStack.push(musicPosition);
                playMusic();
            }
        });
        //播放完一首歌之后，下一曲
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                musicPosition = (++musicPosition)%musicList.size();
                musicPlayStack.push(musicPosition);
                playMusic();
            }
        });
        //进度条监控
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int tempSeconds = mediaPlayer.getCurrentPosition();
                int minute = (tempSeconds/1000)/60;
                int second = (tempSeconds/1000)%60;
                if (second<10){
                    trackCurrPostion.setText(minute +":0" +second);
                    }
                else{
                    trackCurrPostion.setText(minute +":" +second);
                    }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(progressBar.getProgress());
            }
        });
    }
    //初始化工作
    private void init() {
        musicPlayStack = new Stack<Integer>();
        mediaPlayer = new MediaPlayer();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                progressBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        };
        timer.schedule(timerTask, 0, 10);

        setupListeners();
        //得到音乐列表
        searchMusic = new SearchMusic(this);
        musicList = searchMusic.getList();
        //给listView填写数据
        SimpleAdapter adapter = new SimpleAdapter( this, musicList, R.layout.playlistitem, new String[] {"title", "artist"}, new int[]{R.id.title, R.id.artist});
        listView.setAdapter(adapter);
        //播放歌曲
        //随机播放的URL
        musicPosition = (int)(Math.random()*10000)%musicList.size();
        musicURL = (String)musicList.get(musicPosition).get("url");
        musicPlayStack.push(musicPosition);
        playMusic();
        loadWebView();
    }
    //重置HeaderBar的image
    private void ResetHeaderButtons()
    {
        listButton.setImageDrawable(getResources().getDrawable(R.drawable.list64));
        logoButton.setImageDrawable(getResources().getDrawable(R.drawable.logo128));
        doubanFMButton.setImageDrawable(getResources().getDrawable(R.drawable.settings48));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    //
    private void SwitchToNowPlayingSlide()
    {
        ResetHeaderButtons();
        logoButton.setImageDrawable(getResources().getDrawable(R.drawable.logo128_active));
        nowPlayingSlide.setVisibility(LinearLayout.VISIBLE);
        playlistSlide.setVisibility(LinearLayout.INVISIBLE);
        doubanFMSlide.setVisibility(LinearLayout.INVISIBLE);
    }
    //
    private void SwitchToPlaylistSlide()
    {
        ResetHeaderButtons();
        listButton.setImageDrawable(getResources().getDrawable(R.drawable.list64_active));
        logoButton.requestLayout();
        nowPlayingSlide.setVisibility(LinearLayout.INVISIBLE);
        playlistSlide.setVisibility(LinearLayout.VISIBLE);
        doubanFMSlide.setVisibility(LinearLayout.INVISIBLE);
    }
    //
    private void SwitchToDoubanFMSlide()
    {
        ResetHeaderButtons();
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        doubanFMButton.setImageDrawable(getResources().getDrawable(R.drawable.settings48_active));
        nowPlayingSlide.setVisibility(LinearLayout.INVISIBLE);
        playlistSlide.setVisibility(LinearLayout.INVISIBLE);
        doubanFMSlide.setVisibility(LinearLayout.VISIBLE);
        webView.scrollTo(0,280);
    }
    //播放音乐
    private void playMusic(){
        musicURL = (String)musicList.get(musicPosition).get("url");
        trackTitle.setText((String)musicList.get(musicPosition).get("title"));
        trackArtist.setText((String)musicList.get(musicPosition).get("artist"));
        trackAlbum.setText((String)musicList.get(musicPosition).get("album"));
        //设置时间、进度条
        int tempSeconds = (int)musicList.get(musicPosition).get("duration");
        progressBar.setMax(tempSeconds);
        int minute = (tempSeconds/1000)/60;
        int second = (tempSeconds/1000)%60;
        if (second<10){
            trackDuration.setText(minute +":0" +second);
        }
        else{
            trackDuration.setText(minute +":" +second);
        }
        mediaPlayer.reset();
        Log.i("Playing Music",musicURL);
        try {
            mediaPlayer.setDataSource(musicURL);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //载入webView
    private void loadWebView(){
        webView.setWebViewClient(new WebViewClient() {
            // Load opened URL in the application instead of standard browser
            // application
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            // Set progress bar during loading
            public void onProgressChanged(WebView view, int progress) {
                //BrowserActivity.this.setProgress(progress * 100);
            }
        });
        WebSettings websettings = webView.getSettings();
        websettings.setJavaScriptEnabled(true);     // Warning! You can have XSS vulnerabilities!
        //载入豆瓣电台，作为默认电台
        webView.loadUrl("http://www.douban.fm");
    }
}
