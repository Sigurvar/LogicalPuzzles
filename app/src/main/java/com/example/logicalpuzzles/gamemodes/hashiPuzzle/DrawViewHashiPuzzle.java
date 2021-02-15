package com.example.logicalpuzzles.gamemodes.hashiPuzzle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawViewHashiPuzzle extends View {
    Paint paint = new Paint();
    int status=0; //0: no lines, 1: one horizontal, 2: two horizontal, 3: one vertical, 4:two vertical
    int i;
    int j;
    private void init() {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3f);
    }

    public DrawViewHashiPuzzle(Context context, int i, int j) {
        super(context);
        this.i =i;
        this.j=j;
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {
        int x = canvas.getWidth();
        int y = canvas.getHeight();
        if(status==1){
            canvas.drawLine(0, y/2, x, y/2, paint);
        }
        else if(status==2){
            canvas.drawLine(0, y/2-7, x, y/2-7, paint);
            canvas.drawLine(0, y/2+7, x, y/2+7, paint);
        }
        else if(status==3){
            canvas.drawLine(x/2, 0, x/2, y, paint);
        }
        else if(status==4){
            canvas.drawLine(x/2-7, 0, x/2-7, y, paint);
            canvas.drawLine(x/2+7, 0, x/2+7, y, paint);
        }
    }
    boolean canDrawHorizontal(){
        return status<2;
    }
    void drawHorizontal(){
        status++;
        invalidate();
    }
    boolean canDrawVertical(){
        return status != 1 && status != 2 && status != 4;
    }
    void drawVertical(){
        if(status==0)status=3;
        else if(status==3)status=4;
        invalidate();
    }
    boolean removeLine(){
        if (status==0)return false;
        if(status==3)status=0;
        else status-=1;
        invalidate();
        return true;
    }
    void setStatus(int value){
        status = value;
        invalidate();
    }
}
