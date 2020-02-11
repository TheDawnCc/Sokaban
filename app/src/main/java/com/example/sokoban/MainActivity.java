package com.example.sokoban;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    int[] list_music={R.raw.chinap,R.raw.pyear};
    int music_flag=0;
    public static MediaPlayer mediaPlayer;


    Button Start;
    Button Exit;
    Button ScoreBoard;

    private static List<Activity> activities = new ArrayList<Activity>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_game);

        Start=(Button)findViewById(R.id.button);
        Exit=(Button)findViewById(R.id.button2);
        ScoreBoard=(Button)findViewById(R.id.button3);

        Start.setOnClickListener(new mClick());
        Exit.setOnClickListener(new mClick());
        ScoreBoard.setOnClickListener(new mClick());

        if(mediaPlayer==null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer = MediaPlayer.create(this, list_music[music_flag]);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
        addActivity(this);
    }
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void onTerminate() {

        for (Activity activity : activities) {
            activity.finish();
        }

        onDestroy();
        System.exit(0);
    }

    class mClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v==Start)
            {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,StartGames.class);
                startActivity(intent);
                //设置切换动画
                overridePendingTransition(R.layout.in_from_down,R.layout.out_to_up);
            }
            else if(v==Exit)
            {
                onTerminate();
            }
            else if(v==ScoreBoard)
            {
                SharedPreferences sharedPreferences=getSharedPreferences("ScoreBoard",0);
                Map<String, ?> allContent = sharedPreferences.getAll();
                String[] strings=new String[allContent.size()];
                int i=0;
                for(Map.Entry<String, ?>  entry : allContent.entrySet())
                {
                    strings[i++]=(String)entry.getKey()+" 的用户得分为: "+ (String)entry.getValue();
                    System.out.println(entry.getKey()+" "+entry.getValue());
                }
                AlertDialog.Builder builder =  new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("积分榜");
                builder.setItems(strings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("关闭", null);
                builder.show();
            }
        }
    }


}
