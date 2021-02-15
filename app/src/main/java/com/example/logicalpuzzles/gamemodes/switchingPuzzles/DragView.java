package com.example.logicalpuzzles.gamemodes.switchingPuzzles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

public class DragView extends View {

    public int value;
    public float startx;public float starty; public float stopx;public float stopy;
    public int x;
    public int y;
    SwitchingController sc;
    @SuppressLint("ClickableViewAccessibility")
    public DragView(int x, int y, SwitchingController sc, Context context) {
        super(context);
        this.setOnTouchListener((v, event) -> {
            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN: startx = event.getX(); starty = event.getY();break;
                case MotionEvent.ACTION_UP: stopx = event.getX(); stopy = event.getY(); this.moved();break;
                default: break;
            }
            return true;
        });
        this.x = x;
        this.y= y;
        this.sc = sc;
    }
    public int getXPos(){
        return x;
    }
    public int getYPos(){
        return y;
    }
    public int getValue(){
        return value;
    }
    public void setValue(int color){
        this.value = color;
        if (color!=-1) {
            this.setBackgroundColor(color);
        }else this.setBackgroundColor(Color.TRANSPARENT);
    }
    protected void moved(){
        float movedX = stopx - startx;
        float movedY = stopy - starty;
        if (Math.abs(movedX)>=Math.abs(movedY)){
            if (Math.abs(movedX)>50){
                if (movedX>0){// Right
                    sc.move(0, this);
                }
                else{ // Left
                    sc.move(1, this);
                }
            }
        }
        else {
            if (Math.abs(movedY)>50){
                if (movedY>0){ // down
                    sc.move(2, this);
                }
                else{ // up
                    sc.move(3, this);
                }
            }
        }
    }



}

