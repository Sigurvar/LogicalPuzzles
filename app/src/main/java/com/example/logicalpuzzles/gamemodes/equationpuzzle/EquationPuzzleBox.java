package com.example.logicalpuzzles.gamemodes.equationpuzzle;

import android.widget.TextView;

import com.example.logicalpuzzles.R;


public class EquationPuzzleBox {
    private TextView textView;
    private EquationPuzzle equationPuzzle;
    int value = -1;
    public EquationPuzzleBox(EquationPuzzle ep, TextView v){
        this.equationPuzzle = ep;
        this.textView = v;

    }

    void unMark(){
        textView.setBackgroundResource(R.drawable.box_with_border);
    }
    void mark(){
        textView.setBackgroundResource(R.drawable.box_with_border_active);
    }
    void setValue(int value){
        this.value = value;
        textView.setText(String.valueOf(value));
    }
}
