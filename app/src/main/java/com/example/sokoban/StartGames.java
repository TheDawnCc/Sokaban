package com.example.sokoban;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class StartGames extends Activity{

    ImageButton left,right,up,down,runback;
    Button return_To_Menu;
    TextView level;
    Switch Music;
    MediaPlayer mediaPlayer;
    int Score=0;
    GameView gameView;
    HashMap<ImageButton,Position[]> hashMap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playing);
        //绑定
        this.mediaPlayer=MainActivity.mediaPlayer;

        left=(ImageButton)findViewById(R.id.imageButton10);
        right=(ImageButton)findViewById(R.id.imageButton11);
        up=(ImageButton)findViewById(R.id.imageButton8);
        down=(ImageButton)findViewById(R.id.imageButton9);
        runback=(ImageButton)findViewById(R.id.imageButton);

        return_To_Menu=(Button)findViewById(R.id.button4);

        level=(TextView)findViewById(R.id.textView2);

        Music=(Switch)findViewById(R.id.switch1);
        Music.setChecked(mediaPlayer.isPlaying());

        gameView=(GameView)findViewById(R.id.gameview);
        //Button添加事件
        left.setOnClickListener(new gClick());
        right.setOnClickListener(new gClick());
        up.setOnClickListener(new gClick());
        down.setOnClickListener(new gClick());

        return_To_Menu.setOnClickListener(new mClick());
        Music.setOnClickListener(new mClick());
        runback.setOnClickListener(new mClick());

        level.setOnClickListener(new mClick());

        hashMap=new HashMap<ImageButton, Position[]>();

        Position[] temp=new Position[]{new Position(gameView.GetPos()[0]-1,gameView.GetPos()[1],1),new Position(gameView.GetPos()[0]-2,gameView.GetPos()[1],1)};
        hashMap.put(up,temp);
        temp=new Position[]{new Position(gameView.GetPos()[0]+1,gameView.GetPos()[1],2),new Position(gameView.GetPos()[0]+2,gameView.GetPos()[1],2)};
        hashMap.put(down,temp);
        temp=new Position[]{new Position(gameView.GetPos()[0],gameView.GetPos()[1]-1,3),new Position(gameView.GetPos()[0],gameView.GetPos()[1]-2,3)};
        hashMap.put(left,temp);
        temp=new Position[]{new Position(gameView.GetPos()[0],gameView.GetPos()[1]+1,4),new Position(gameView.GetPos()[0],gameView.GetPos()[1]+2,4)};
        hashMap.put(right,temp);
        gameView.send(this);

        MainActivity.addActivity(this);
        SharedPreferences Max_level=getSharedPreferences("Max_level",0);
        gameView.max_level=Max_level.getInt("Max_level",0);
    }
    public class Position
    {
        public int x;
        public int y;
        public int turn;
        public Position(){}
        public Position(int x,int y,int turn)
        {
            this.x=x;
            this.y=y;
            this.turn=turn;         //上下左右->1234
        }
    }
    class RunBack
    {
        Position pos;
        int[][] Box=new int[21][21];
        public RunBack(Position pos,int[][] Box)
        {
            this.pos=pos;
            this.Box=Box;
        }
        public Position Getpos()
        {
            return this.pos;
        }
        public int[][] GetBox()
        {
            return this.Box;
        }
    }
    class gClick implements View.OnClickListener{
        public void onClick(View v){

            Position p1=new Position();
            Position p2=new Position();
            int[] pos=gameView.GetPos();

            switch (v.getId())
            {
                case R.id.imageButton8:         //上
                    if(pos[1]-1>=0)
                        p1.x=pos[0]-1;
                    else {
                        gameView.setMen(new Position(pos[0],pos[1],1));
                        gameView.invalidate();
                        return;
                    }
                    p1.y=pos[1];
                    p1.turn=1;

                    p2.x=pos[0]-2;
                    p2.y=pos[1];
                    p2.turn=1;

                    break;
                case R.id.imageButton9:         //下

                    if(pos[1]+1<gameView.height)
                        p1.x=pos[0]+1;
                    else {
                        gameView.setMen(new Position(pos[0],pos[1],2));
                        gameView.invalidate();
                        return;
                    }
                    p1.y=pos[1];
                    p1.turn=2;

                    p2.x=pos[0]+2;
                    p2.y=pos[1];
                    p2.turn=2;

                    break;
                case R.id.imageButton10:        //左

                    p1.x=pos[0];
                    if(pos[0]-1>=0)
                        p1.y=pos[1]-1;
                    else{
                        gameView.setMen(new Position(pos[0],pos[1],3));
                        gameView.invalidate();
                        return;
                    }
                    p1.turn=3;

                    p2.x=pos[0];
                    p2.y=pos[1]-2;
                    p2.turn=3;
                    break;
                case R.id.imageButton11:        //右
                    p1.x=pos[0];
                    if(pos[0]+1<gameView.width)
                        p1.y=pos[1]+1;
                    else{
                        gameView.setMen(new Position(pos[0],pos[1],4));
                        gameView.invalidate();
                        return;
                    }

                    p1.turn=4;

                    p2.x=pos[0];
                    p2.y=pos[1]+2;
                    p2.turn=4;
                    break;
            }
            System.out.println("p1:"+p1.x+" "+p1.y);
            int isbox=gameView.map[p1.x][p1.y];
            System.out.println("isbox:"+isbox);
            if(isbox==1) {
                System.out.println("if isbox");
                if (p2.x < gameView.height && p2.y < gameView.width && p2.x >= 0 && p2.y >= 0) {
                    int cango = gameView.mymap[p2.x][p2.y];
                    System.out.println("cango:"+cango);
                    if (cango!=1&&gameView.map[p2.x][p2.y]!=1)
                    {
                        int[][] temp=new int[21][21];
                        for(int i=0;i<gameView.height;i++)
                        {
                            for(int j=0;j<gameView.width;j++)
                            {
                                temp[i][j]=gameView.map[i][j];
                            }
                        }
                        gameView.step.push(new RunBack(new Position(pos[0],pos[1],pos[2]),temp));
                        gameView.set_box_pos(p1.x,p1.y,p2.x,p2.y);
                        gameView.setMen(p1);
                    }
                    else
                    {
                        System.out.println("isbox_else:pos[0]:"+pos[0]+" pos[1]:"+pos[1]);
                        gameView.setMen(new Position(pos[0],pos[1],p1.turn));
                    }
                }
            }
            else
            {
                int cango=gameView.mymap[p1.x][p1.y];
                System.out.println("cango:"+cango);
                if(cango!=1)
                {
                    int[][] temp=new int[21][21];
                    for(int i=0;i<gameView.height;i++)
                    {
                        for(int j=0;j<gameView.width;j++)
                        {
                            temp[i][j]=gameView.map[i][j];
                        }
                    }
                    gameView.step.push(new RunBack(new Position(pos[0],pos[1],pos[2]),temp));
                    gameView.setMen(p1);
                }
            }

            gameView.invalidate();


        }
    }
    public void game_win()
    {
        MediaPlayer mediaPlayer=MediaPlayer.create(gameView.getContext(),R.raw.upgrade);
        mediaPlayer.start();

        AlertDialog.Builder builder  = new AlertDialog.Builder(gameView.getContext());
        builder.setTitle("Success!" ) ;

        builder.setCancelable(false);
        builder.setMessage("恭喜你完成这一局的挑战!要继续吗?!" ) ;
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int temp=gameView.level*100-gameView.step.size();
                if(temp>0)
                {
                    Score+=temp;
                }

                gameView.levelup();

                Toast.makeText(StartGames.this,
                        "当前你的分数为:"+Score,
                        Toast.LENGTH_SHORT).show();
                SharedPreferences Max_level=getSharedPreferences("Max_level",0);
                final SharedPreferences.Editor ML = Max_level.edit();
                ML.putInt("Max_level",gameView.max_level);
                ML.commit();

            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences=getSharedPreferences("ScoreBoard",0);
                final SharedPreferences.Editor mEditor = sharedPreferences.edit();
                int temp=gameView.level*100-gameView.step.size();
                if(temp>0)
                {
                    Score+=temp;
                }

                final EditText inputServer = new EditText(gameView.getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(gameView.getContext());
                builder.setTitle("请输入你的名字:").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                gameView.max_level+=1;
                                SharedPreferences Max_level=getSharedPreferences("Max_level",0);
                                final SharedPreferences.Editor ML = Max_level.edit();
                                ML.putInt("Max_level",gameView.max_level);
                                ML.commit();
                                return_To_Menu.performClick();
                            }
                        }).setCancelable(false);
                builder.setPositiveButton("记录", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mEditor.putString(inputServer.getText().toString(),String.valueOf(Score));
                        mEditor.commit();
                        gameView.max_level+=1;
                        SharedPreferences Max_level=getSharedPreferences("Max_level",0);
                        final SharedPreferences.Editor ML = Max_level.edit();
                        ML.putInt("Max_level",gameView.max_level);
                        ML.commit();
                        return_To_Menu.performClick();
                    }
                });
                builder.show();


            }
        });
        builder.show();

    }
    public void setLevel(int level)
    {
        this.level.setText("当前关卡:"+level);
    }

    class mClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v==Music)
            {
                if(mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                else
                    mediaPlayer.start();
            }
            if(v==return_To_Menu)
            {
                Intent intent=new Intent();
                intent.setClass(StartGames.this,MainActivity.class);
                startActivity(intent);
                //设置切换动画
                overridePendingTransition(R.layout.revert_out_to_up,R.layout.revert_in_from_down);
            }
            if(v==runback)
            {
                if(gameView.step.size()>0) {
                    RunBack r = gameView.step.pop();
                    for(int i=0;i<20;i++)
                    {
                        for(int j=0;j<20;j++)
                        {
                            System.out.print(r.GetBox()[i][j]);
                        }
                        System.out.println();
                    }
                    gameView.setMen(r.Getpos());
                    gameView.map=r.GetBox().clone();
                    gameView.invalidate();

                }
            }
            if(v==level) {
                final String[] strings = new String[gameView.max_level];

                for (int i = 0; i < gameView.max_level; i++) {
                    strings[i] = new String("第" + (i + 1) + "关");
                    System.out.println(strings[i]);
                }

                Score=0;
                AlertDialog.Builder listDialog = new AlertDialog.Builder(StartGames.this);
                listDialog.setTitle("选择关卡");
                listDialog.setItems(strings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        gameView.setLevel(which+1);
                        Toast.makeText(StartGames.this,
                                "你选择了:"+strings[which],
                                Toast.LENGTH_SHORT).show();
                    }
                });
                listDialog.show();
            }

        }
    }
}
