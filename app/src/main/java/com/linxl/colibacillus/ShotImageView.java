package com.linxl.colibacillus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.linxl.colibacillus.model.ImagePoint;

public class ShotImageView extends View {

    private Bitmap background;
    private ImagePoint dot1 = new ImagePoint();
    private ImagePoint dot2 = new ImagePoint();
    private ImagePoint dot3 = new ImagePoint();
    private ImagePoint dot4 = new ImagePoint();

    //触摸事件的坐标
    private float event_x;
    private float event_y;
    private float begin_x;
    private float begin_y;
    //四点坐标的数组
    private float[] pts;
    //控件的宽和高
    private float v_width;
    private float v_height;
    //手势选中的点 标记
    private int select_index = 0;

    private static float raduis = 25f;

    public ShotImageView(Context context) {
        super(context);
    }

    public ShotImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShotImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        v_width = this.getWidth(); //获取当前View的宽
        v_height = this.getHeight(); //获取当前View的高
        dot1.x = v_width / 2 - 250;  //点1 x轴坐标
        dot1.y = v_height / 2 + 250; //点1 y轴坐标
        dot2.x = dot1.x + 300;
        dot2.y = dot1.y;
        dot3.x = dot1.x;
        dot3.y = dot1.y + 300;
        dot4.x = dot2.x;
        dot4.y = dot2.y + 300;
        //封装坐标点数组
        pts = getPts();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBitmap(canvas);
        pts = getPts();
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.transparent_paint)); //画笔颜色 （点）
        paint.setStrokeWidth(25f);                             //画笔宽度 （点的大小）
        drawCircle(canvas, paint, dot1);                        //画出四个点
        drawCircle(canvas, paint, dot2);                        //画出四个点
        drawCircle(canvas, paint, dot3);                        //画出四个点
        drawCircle(canvas, paint, dot4);                        //画出四个点

        paint.setStrokeWidth(10f);                             //画笔宽度 （线的粗细）
        canvas.drawLines(getLine(), paint);          //画出四条线 (连接四个点)
//        Path path = new Path();                                 //初始化路径（用于填充颜色）
//        path.moveTo(dot1.x, dot1.y);                           //路径移动到第一个点（从点 1 开始）
//        path.lineTo(dot2.x, dot2.y);                           //路径直线到第二个点
//        path.lineTo(dot3.x, dot3.y);                           //路径直线到第四个点
//        path.lineTo(dot4.x, dot4.y);                           //路径直线到第三个点
//        path.lineTo(dot1.x, dot1.y);                           //路径直线到第一个点（闭合形成四边形）
//        //paint.setColor(getResources().getColor(R.color.transparent_gray));  //画笔颜色 （填充）
//        canvas.drawPath(path, paint);
    }

    private void drawCircle(Canvas canvas, Paint paint, ImagePoint dot) {
        canvas.drawCircle(dot.x, dot.y, raduis, paint);
    }

    /**
     * 设置背景图片
     */
    public void setBackground(Bitmap bitmap) {
        background = bitmap;
        invalidate();
    }

    private void drawBitmap(Canvas canvas) {
        if (background != null) {
            // 指定图片绘制区域(左上角的四分之一)
            Rect src = new Rect(0, 0, background.getWidth(), background.getHeight());
            // 指定图片在屏幕上显示的区域
            Rect dst = new Rect(0, 0, (int) v_width, (int) v_height);
            canvas.drawBitmap(background, src, dst, null);
        }
    }

    /**
     * 判断触摸的点是不是在坐标点上
     */
    private boolean isRangeOfView(ImagePoint dot, float ev_x, float ev_y) {
        float dx = dot.x;
        float dy = dot.y;
        //dx：点的x轴  dy：点的y轴  ev_x：手势触摸到的x轴  ev_y：手势触摸到的y轴
        // 点的 x,y 轴 上下左右各增加40像素，扩大触摸范围，判断触摸手势是否在该范围内
        return (ev_x > dx - 40) && (ev_x < dx + 40) && (ev_y > dy - 40) && (ev_y < dy + 40);
    }

    /**
     * 判断触摸的点是不是在图像中间
     * 如果点的顺序变了怎么办
     */
    private boolean isCenterOfView(float ev_x, float ev_y) {
        //dx：点的x轴  dy：点的y轴  ev_x：手势触摸到的x轴  ev_y：手势触摸到的y轴
        // 点的 x,y 轴 上下左右各增加40像素，扩大触摸范围，判断触摸手势是否在该范围内
//        Log.d("down", ev_x + " " + ev_y);
//        Log.d("down", dot1.x + " " + dot2.x);
//        Log.d("down", dot1.y + " " + dot3.y);
        return (ev_x > dot1.x + 40) && (ev_x < dot2.x - 40) && (ev_y > dot1.y + 40) && (ev_y < dot3.y - 40);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        event_x = event.getX(); //当前手势X轴坐标
        event_y = event.getY(); //当前手势Y轴坐标
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //当手指按下时判断是否选中四个点中的一个
                if (isRangeOfView(dot1, event_x, event_y)) {
                    select_index = 0;
                } else if (isRangeOfView(dot2, event_x, event_y)) {
                    select_index = 1;
                } else if (isRangeOfView(dot3, event_x, event_y)) {
                    select_index = 2;
                } else if (isRangeOfView(dot4, event_x, event_y)) {
                    select_index = 3;
                } else if (isCenterOfView(event_x, event_y)) {
                    select_index = 4;
                } else {
                    select_index = -1;//没选中为 -1
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //当手指滑动的时候，判断移动哪一个点
                switch (select_index) {
                    case 0: //标记=0  选中了第一个点
                        dot1.x = event_x; //点1 x轴赋值=当前手势x轴
                        dot1.y = event_y; //点1 y轴赋值=当前手势y轴
                        break;
                    case 1:
                        dot2.x = event_x;
                        dot2.y = event_y;
                        break;
                    case 2:
                        dot3.x = event_x;
                        dot3.y = event_y;
                        break;
                    case 3:
                        dot4.x = event_x;
                        dot4.y = event_y;
                        break;
                    case 4:
                        float dyX = event_x - begin_x;
                        float dyY = event_y - begin_y;
                        dot1.x = dot1.x + dyX;
                        dot1.y = dot1.y + dyY;
                        dot2.x = dot2.x + dyX;
                        dot2.y = dot2.y + dyY;
                        dot3.x = dot3.x + dyX;
                        dot3.y = dot3.y + dyY;
                        dot4.x = dot4.x + dyX;
                        dot4.y = dot4.y + dyY;
                        break;
                }
                //从新绘制view
                if (select_index != -1) {
                    pts = getPts();
                    invalidate();
                }
                break;
        }
        begin_x = event_x;
        begin_y = event_y;
        return true;

    }

    private float[] getPts() {
        return new float[]{dot1.x, dot1.y, 25f, dot2.x, dot2.y, 25f, dot3.x, dot3.y, 25f, dot4.x, dot4.y, 25f};
    }

    private float[] getLine() {
        return new float[]{dot1.x, dot1.y, dot2.x, dot2.y, dot2.x, dot2.y, dot4.x, dot4.y, dot4.x, dot4.y, dot3.x, dot3.y, dot3.x, dot3.y, dot1.x, dot1.y};
    }

    //获取剪切的图片
    public Bitmap getBitmap() {
        int x = (int) Math.max(dot1.x, dot3.x);
        int y = (int) Math.min(dot1.y, dot2.y);
        int width = (int) Math.min(dot2.x, dot4.x) - (int) Math.max(dot1.x, dot3.x);
        int height = (int) Math.max(dot3.y, dot4.y) - (int) Math.min(dot1.y, dot2.y);
        Log.d("shotView", "getBitmap: " + width + " " + height);
        Log.d("shotView", "getBitmap: " + x + " " + y);
        return Bitmap.createBitmap(background, x, y, width, height);
    }

}
