package com.example.logicalpuzzles.gamemodes.makeThemGreenPuzzle;

import android.widget.ImageView;
import androidx.core.content.ContextCompat;

import com.example.logicalpuzzles.R;

public class MakeThemGreenCircle {


    public int posX;
    public int posY;
    private int value;
    private ImageView iv;

    public MakeThemGreenCircle(int posX, int posY, ImageView iv){
        this.posX=posX;
        this.posY=posY;
        this.iv=iv;
        value=0;
    }
    public int getValue(){return value;}
    public void setValue(int value){
        this.value = value;
        changeColor();
    }
    public void increaseValue(){
        this.value = (this.value+1)%2;
        changeColor();
    }
    public void setCorrect(){
        if (this.value==0){
            this.value=1;
            changeColor();
        }
    }
    private void changeColor(){
        if (this.value==1){
            iv.setColorFilter(ContextCompat.getColor(iv.getContext(), R.color.green));
        }
        else {
            iv.setColorFilter(ContextCompat.getColor(iv.getContext(), R.color.red));
        }
    }
}
