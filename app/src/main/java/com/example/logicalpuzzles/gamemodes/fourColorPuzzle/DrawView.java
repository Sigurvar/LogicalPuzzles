package com.example.logicalpuzzles.gamemodes.fourColorPuzzle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DrawView extends View{
    Context context;
    Paint fillPaint = new Paint();
    Paint strokePaint = new Paint();
    Path fillPath;
    Path strokePath;
    Region region = new Region();
    List<List<Integer>> points;
    List<DrawView> neighbors;
    private float[] lastTouchDownXY = new float[2];

    private void init() {
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(10);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(Color.parseColor("#0000ff00"));

    }
    public int getColor(){
        return fillPaint.getColor();
    }
    public DrawView(Context context,List<List<Integer>> list) {
        super(context);
        this.context = context;
        this.neighbors = new ArrayList<>();
        points = list;
        
        init();
    }
    public void addNeighbor(DrawView neighbor){
        this.neighbors.add(neighbor);
    }
    public boolean isValid(){
        if (fillPaint.getColor()==Color.parseColor("#0000ff00")){return false;}
        try{
            for (DrawView dv : neighbors){
                if(this.getColor()==dv.getColor()){return false;}
            }
        }catch (NullPointerException e){
            return true;
        }
        return true;
    }
    @Override
    public void onDraw(Canvas canvas) {

        int x=canvas.getWidth();
        int y=canvas.getHeight();
        if (fillPath==null){
            fillPath = new Path();
            strokePath = new Path();
            fillPath.moveTo(x*points.get(0).get(0)/10, y*points.get(0).get(1)/10);
            strokePath.moveTo(x*points.get(0).get(0)/10, y*points.get(0).get(1)/10);
            for (List<Integer> l : points){
                fillPath.lineTo(x*l.get(0)/10, y*l.get(1)/10);
                strokePath.lineTo(x*l.get(0)/10, y*l.get(1)/10);
            }
            strokePath.lineTo(x*points.get(0).get(0)/10, y*points.get(0).get(1)/10);
        }
        canvas.drawPath(fillPath, fillPaint);
        canvas.drawPath(strokePath, strokePaint);
        Region clip = new Region(0,  0,x, y);

        region.setPath(fillPath, clip);
    }
    public void setColor(int color){
        if (fillPaint.getColor()!=color){
            fillPaint.setColor(color);
        }
        else {fillPaint.setColor(Color.parseColor("#0000ff00"));}
        invalidate();
    }
}