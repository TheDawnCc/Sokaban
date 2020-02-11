package com.example.sokoban;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Stack;

public class GameView extends AppCompatImageView {
    int width=20,height=20;
    int[][] mymap = new int[21][21]; //存放地图文件中图片位置坐标
    int[][] map=new int[21][21];           //存放地图文件中的图片编号
    int[] Rmap=new int[]{R.raw.m1,R.raw.m2,R.raw.m3,R.raw.m4,R.raw.m5,R.raw.m6,R.raw.m7,R.raw.m8,R.raw.m9,R.raw.m10
            ,R.raw.m11,R.raw.m12,R.raw.m13,R.raw.m14,R.raw.m15,R.raw.m16,R.raw.m17,R.raw.m18,R.raw.m19,R.raw.m20
            ,R.raw.m21,R.raw.m22,R.raw.m23,R.raw.m24,R.raw.m25,R.raw.m26,R.raw.m27,R.raw.m28,R.raw.m29,R.raw.m30
            ,R.raw.m31,R.raw.m32,R.raw.m33};
    Bitmap[] bitmaps=new Bitmap[10];    //图片数组
    int[] Resources=new int[]{R.drawable.a0,R.drawable.a1,R.drawable.a2,R.drawable.a3,
            R.drawable.a4,R.drawable.a5,R.drawable.a6,R.drawable.a7,R.drawable.a8,R.drawable.a9};
    Bitmap men;   //工人
    Bitmap greenBox;  //草地
    Bitmap box;    //箱子
    Paint paint;
    int tx = 120;  //箱子的坐标
    int ty = 120;
    public int mi,mj,mTurn=1;  //工人坐标 mTurn 上下左右->1234  mi->i mj->j
    int level=1;    //关卡
    int max_level=1;
    int level_success=0;
    int success_sum;
    HashMap<Integer,Integer> Transform;
    public Stack<StartGames.RunBack> step;     //悔棋
    int Paint_orin_x,Paint_orin_y;
    int Scale=1;

    public GameView(Context context) {
        super(context);
        paint = new Paint();
        men = BitmapFactory.decodeResource(getResources(), R.drawable.men);  //工人
        greenBox = BitmapFactory.decodeResource(getResources(), R.drawable.greenbox); //草地
        box = BitmapFactory.decodeResource(getResources(), R.drawable.box); //箱子

        for(int i=0;i<10;i++)
        {
            bitmaps[i]=BitmapFactory.decodeResource(getResources(),Resources[i]);
        }
        Transform=new HashMap<Integer, Integer>();
        Transform.put(1,8);
        Transform.put(2,5);
        Transform.put(3,6);
        Transform.put(4,7);
        level=1;



    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        men = BitmapFactory.decodeResource(getResources(), R.drawable.men);  //工人
        greenBox = BitmapFactory.decodeResource(getResources(), R.drawable.greenbox); //草地
        box = BitmapFactory.decodeResource(getResources(), R.drawable.box); //箱子

        for(int i=0;i<10;i++)
        {
            bitmaps[i]=BitmapFactory.decodeResource(getResources(),Resources[i]);
        }
        Transform=new HashMap<Integer, Integer>();
        Transform.put(1,8);
        Transform.put(2,5);
        Transform.put(3,6);
        Transform.put(4,7);
        level=1;
        setScaleX((float)1.5);
        setScaleY((float)1.5);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint();
        men = BitmapFactory.decodeResource(getResources(), R.drawable.men);  //工人
        greenBox = BitmapFactory.decodeResource(getResources(), R.drawable.greenbox); //草地
        box = BitmapFactory.decodeResource(getResources(), R.drawable.box); //箱子

        for(int i=0;i<10;i++)
        {
            bitmaps[i]=BitmapFactory.decodeResource(getResources(),Resources[i]);
        }
        Transform=new HashMap<Integer, Integer>();
        Transform.put(1,8);
        Transform.put(2,5);
        Transform.put(3,6);
        Transform.put(4,7);
        level=1;
     //   Readmap();
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.argb(255, 189, 189, 189));//设置背景颜色
        paint.setColor(Color.argb(255, 220, 220, 220)); //灰白色
        this.myDraw(canvas);
    }

    public void myDraw(Canvas canvas)  //绘制图形
    {
        success_sum=0;
        for (int i = 0; i < height; i++)  //将地图中的数据与图片编号对应
        {
            for (int j = 0; j < width; j++) {
                tx = Paint_orin_x + 90 * j;
                ty = Paint_orin_y + 90 * i;
                if(mymap[i][j]<5&&mymap[i][j]!=3) {
                    canvas.drawBitmap(bitmaps[mymap[i][j]], tx, ty, paint);
                }
                if(mymap[i][j]==5||mymap[i][j]==3)
                {
                    canvas.drawBitmap(greenBox,tx,ty,paint);
                }
                if(map[i][j]==1)                    //箱子paint覆盖草
                {
                    canvas.drawBitmap(bitmaps[3],tx,ty,paint);
                }
                if(mymap[i][j]==4&&map[i][j]==1)        //成功的箱子,覆盖箱子
                {
                    canvas.drawBitmap(bitmaps[9],tx,ty,paint);
                    success_sum++;
                }
                if (i==mi&&j==mj)  //工人
                {
                    tx = Paint_orin_x + mj * 90;
                    ty = Paint_orin_y + mi * 90;

                    canvas.drawBitmap(bitmaps[Transform.get(mTurn)], tx, ty, paint);  //绘制图形
                }
            }
        }
        if(level_success==success_sum)
        {
            startGames.game_win();
        }
    }

    StartGames startGames;
    public void send(StartGames startGames)
    {
        this.startGames=startGames;
        Readmap();
    }
    void Readmap()  //读取地图文件
    {

        startGames.setLevel(level);
        String mapdata = "";
        try {
            InputStream in_file = getResources().openRawResource(Rmap[level-1]);  //输入流对象读取地图文件
            int length = in_file.available();    //获取数据流的字节数
            byte[] buffer = new byte[length];
            int bytes = in_file.read(buffer);   //产生读取数据的动作
            mapdata = new String(buffer, 0, bytes);  //把地图文件数据转换为字符串存放到mapdata中
            mapdata=mapdata.replaceAll("\\s*|\\t|\\r|\\n","");      //替换字符串中不可见字符

        } catch (IOException e) {}

        level_success=0;

        for(int i=0;i<height;i++)
        {
            for(int j=0;j<width;j++)
            {
                mymap[i][j]=mapdata.charAt(i*height+j)-48;
                if(mymap[i][j]==5)
                {
                    mi=i;
                    mj=j;
                    mTurn=2;
                }
                if(mymap[i][j]==3||mymap[i][j]==9)
                {
                    map[i][j]=1;
                }
                if(mymap[i][j]==9)
                {
                    mymap[i][j]=4;
                }
                if(mymap[i][j]==4)
                {
                    level_success++;
                }
            }
        }

        step = new Stack<>();
    }
    void levelup()
    {
        if(level+1<=Rmap.length)
        {
            level++;
            if(max_level<level)
                max_level=level;

            for(int i=0;i<height;i++)
                for(int j=0;j<width;j++)
                    map[i][j]=0;


            Readmap();
            invalidate();
        }
    }
    void setLevel(int slevel)
    {
        if(slevel>0&&slevel<=Rmap.length)
        {
            level=slevel;
            for(int i=0;i<height;i++)
                for(int j=0;j<width;j++)
                    map[i][j]=0;
            Readmap();
            invalidate();
        }
    }

    public int[] GetPos()
    {
        int[] v=new int[]{mi,mj,mTurn};
        return v;
    }
    public void setMen(StartGames.Position position)
    {
        this.mi=position.x;
        this.mj=position.y;
        this.mTurn=position.turn;

    }
    public void set_box_pos(int px1,int py1,int px2,int py2) {
        map[px1][py1]=0;
        map[px2][py2]=1;
    }
    private double spacing(MotionEvent event) {
        double x = event.getX(0) - event.getX(1);
        double y = event.getY(0) - event.getY(1);
        return Math.sqrt(x * x + y * y);
    }

    private void zoom(float scale)
    {
        if(scale>1&&Scale<6)
            Scale++;
        if(scale<1&&Scale>1)
            Scale--;
        this.setScaleX(Scale);
        this.setScaleY(Scale);
        invalidate();

    }

    int x1,y1,x2,y2,mode,oldDist;
    public boolean onTouchEvent(MotionEvent event)          //触摸事件
    {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {              //多点触摸
            case MotionEvent.ACTION_DOWN:
                mode = 1;
                break;
            case MotionEvent.ACTION_UP:

                mode = 0;
                break;
            case MotionEvent.ACTION_POINTER_UP:

                if (mode >= 2) {
                    int newDist = (int) spacing(event);
                    float scale = newDist / oldDist;
                    System.out.println(oldDist+"old   new::: "+newDist);
                    zoom(scale);
                }
                mode -= 1;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = (int) spacing(event);//两点按下时的距离
                mode += 1;
                break;
        }

        if(event.getAction()==MotionEvent.ACTION_DOWN )
        {
            x1=(int)event.getX();
            y1=(int)event.getY();
            return true;
        }
        if(event.getAction()==MotionEvent.ACTION_MOVE)
        {
            x2=(int)event.getX();
            y2=(int)event.getY();

            Paint_orin_x=Paint_orin_x+(x2-x1)/20;
            Paint_orin_y=Paint_orin_y+(y2-y1)/20;
            invalidate();
            return true;
        }

        return super.onTouchEvent(event);
    }

}
