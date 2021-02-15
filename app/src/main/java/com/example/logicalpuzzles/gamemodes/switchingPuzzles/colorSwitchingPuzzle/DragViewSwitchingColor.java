package com.example.logicalpuzzles.gamemodes.switchingPuzzles.colorSwitchingPuzzle;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.widget.TextView;

import com.example.logicalpuzzles.gamemodes.switchingPuzzles.DragView;

public class DragViewSwitchingColor extends DragView {

    private int color;
    private ColorSwitchingPuzzle csp;
    @SuppressLint("ClickableViewAccessibility")
    DragViewSwitchingColor(int x, int y, ColorSwitchingPuzzle csp) {
        super( x, y, csp, csp);
    }
    public int getXValue(){
        return x;
    }
    public int getYValue(){
        return y;
    }
    int getColor(){
        return color;
    }
    @Override
    public void setValue(int value){
        this.value = value;
        if (value!=-1) {
            this.setBackgroundColor(value);
        }else this.setBackgroundColor(Color.TRANSPARENT);
    }
}
