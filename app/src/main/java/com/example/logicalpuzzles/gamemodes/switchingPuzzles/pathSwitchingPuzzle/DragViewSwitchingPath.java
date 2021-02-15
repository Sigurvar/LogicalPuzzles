package com.example.logicalpuzzles.gamemodes.switchingPuzzles.pathSwitchingPuzzle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.logicalpuzzles.gamemodes.switchingPuzzles.DragView;

@SuppressLint("ViewConstructor")
public class DragViewSwitchingPath extends DragView {

    private Paint strokePaint = new Paint();
    Path strokePath;
    Drawable border;
    @SuppressLint("ClickableViewAccessibility")
    DragViewSwitchingPath(int x, int y, PathSwitchingPuzzle csp, Context context, Drawable d) {
        super(x, y, csp, context);
        init();
        border = d;
        this.setBackground(border);
    }
    public int getXValue(){
        return x;
    }
    public int getYValue(){
        return y;
    }
    private void init() {
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(10);
    }

    @Override
    public void onDraw(Canvas canvas) {
        int x = canvas.getWidth();
        int y = canvas.getHeight();
        strokePath = new Path();
        final RectF oval = new RectF();
        if(value==1){
            strokePath.moveTo(0, y/2);
            strokePath.lineTo(x, y/2);
        } else if(value==2){
            oval.set(-x/2, -y/2, x/2,y/2);
            strokePath.arcTo(oval, 90, -90, true);
        } else if(value==3){
            oval.set(-x/2, y/2, x/2,3*y/2);
            strokePath.arcTo(oval, 270, 90, true);
        } else if(value==4){
            strokePath.moveTo(x/2, 0);
            strokePath.lineTo(x/2, y);
        } else if(value==5){
            oval.set(x/2, -y/2, 3*x/2,y/2);
            strokePath.arcTo(oval, 90, 90, true);
        } else if(value==6){
            oval.set(x/2, y/2, 3*x/2,3*y/2);
            strokePath.arcTo(oval, 180, 90, true);
        } else if(value==7){
            strokePath.moveTo(x/2, 0);
            strokePath.lineTo(x/2, y);
            strokePath.moveTo(0, y/2);
            strokePath.lineTo(x, y/2);
        }else if(value==8){
            oval.set(-x/2, y/2, x/2,3*y/2);
            strokePath.arcTo(oval, 270, 90, true);
            oval.set(x/2, -y/2, 3*x/2,y/2);
            strokePath.arcTo(oval, 90, 90, true);
        }else if(value==9){
            oval.set(-x/2, -y/2, x/2,y/2);
            strokePath.arcTo(oval, 90, -90, true);
            oval.set(x/2, y/2, 3*x/2,3*y/2);
            strokePath.arcTo(oval, 180, 90, true);
        }
        canvas.drawPath(strokePath, strokePaint);
    }
    @Override
    public void setValue(int value){
        this.value = value;
        if (value == 0){
            this.setBackgroundColor(Color.BLACK);
        }
        invalidate();
    }
    public int enterDirectionToConnectingField(int fromDirection){//Direction 0:right, 1:left, 2:down, 3:up
        if (value==0)return -2;
        switch (fromDirection){
            case 0:
                switch (value){
                    case 1:
                    case 7:
                        return 0;
                    case 5:
                    case 8:
                        return 2;
                    case 6:
                    case 9:
                        return 3;
                }
                break;
            case 1:
                switch (value){
                    case 1:
                    case 7:
                        return 1;
                    case 2:
                    case 9:
                        return 2;
                    case 3:
                    case 8:
                        return 3;
                }
                break;
            case 2:
                switch (value){
                    case 4:
                    case 7:
                        return 2;
                    case 6:
                    case 9:
                        return 1;
                    case 3:
                    case 8:
                        return 0;
                }
                break;
            case 3:
                switch (value){
                    case 4:
                    case 7:
                        return 3;
                    case 2:
                    case 9:
                        return 0;
                    case 5:
                    case 8:
                        return 1;
                }
                break;
        }
        return -1;
    }
}
