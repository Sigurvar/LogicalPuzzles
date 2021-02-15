package com.example.logicalpuzzles.gamemodes.hashiPuzzle;

import android.widget.TextView;

import com.example.logicalpuzzles.R;

class HashiBox {

    final int j;
    final int i;
    private final TextView tv;
    private int wantedLines;
    private int currentLines=0;
    private boolean isActive = false;

    HashiBox(int i, int j, TextView tv, int num){
        this.i = i;
        this.j = j;
        this.tv = tv;
        this.wantedLines = num;
    }

    boolean highLight(){
        if(isActive){
            tv.setBackgroundResource(R.drawable.box_with_border);
            isActive = false;
            return false;
        }
        tv.setBackgroundResource(R.drawable.box_with_border_active);
        isActive = true;
        return true;
    }
    void lineAdded(){
        currentLines++;
    }
    void lineRemoved(){
        currentLines--;
    }
    boolean isCorrect(){
        return currentLines==wantedLines;
    }
    void reset(){
        currentLines=0;
    }
    void autocomplete(){
        currentLines = wantedLines;
    }
}
