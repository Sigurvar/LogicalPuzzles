package com.example.logicalpuzzles.gamemodes.sudokuPuzzle;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.logicalpuzzles.R;

import java.util.ArrayList;

class SudokuBox {

    private RelativeLayout rl;
    private TextView tv;
    private TableLayout tl;
    private int ans;
    private ArrayList<Integer> numbers_in_box  = new ArrayList<>();
    private boolean isChangeable;
    private Context c;
    private int hintTextColor;
    int row;
    int column;

    SudokuBox(RelativeLayout rl, Context c, LayoutInflater inflater, int height, int row, int column){
        this.rl = rl;
        this.c= c;
        this.row = row;
        this.column = column;
        this.hintTextColor = c.getColor(R.color.dark_green);
        this.tv = (TextView) inflater.inflate( R.layout.sudoku_textview, null);
        this.tl = (TableLayout)inflater.inflate(R.layout.sudoku_box_table, null);

        this.tv.setHeight(height);
        this.tl.setMinimumHeight(height);
        this.rl.setMinimumHeight(height);
    }
    int getValue(){
        return numbers_in_box.size()==1 ? numbers_in_box.get(0) : 0;
    }
    void reset(){
        if(isChangeable && tv.getCurrentTextColor()!=hintTextColor){
            clearBox();
        }
    }
    void newLevel(int value){
        clearBox();
        isChangeable = (value==0);
        if (!isChangeable){
            numbers_in_box.add(value);
            tv.setTextColor(c.getColor(R.color.black));
            tv.setText(String.valueOf(value));
        }
        else{
            tv.setTextColor(c.getColor(R.color.input_text_color));
        }
    }
    void setAns(int ans){
        this.ans = ans;
    }
    boolean isChangeable(){
        return isChangeable;
    }
    boolean isEmpty(){
        return numbers_in_box.size()==0;
    }
    void fillWithAns(){
        clearBox();
        tv.setTextColor(hintTextColor);
        tv.setText(String.valueOf(ans));
        numbers_in_box.add(ans);
    }
    void highlight(int resource){
        rl.setBackgroundResource(resource);
    }
    void setValue(int value){
        if (isChangeable && tv.getCurrentTextColor()!=hintTextColor){
            for(int i=0;i<numbers_in_box.size();i++){
                if (value==numbers_in_box.get(i)){
                    numbers_in_box.remove(i);
                    if (numbers_in_box.size()==1){
                        rl.removeAllViews();
                        rl.addView(tv);
                        tv.setText(String.valueOf(numbers_in_box.get(0)));
                    }
                    else if (numbers_in_box.size()==0){
                        tv.setText("");
                    }
                    getTextViewFromBoxTable(value).setText("");
                    return;
                }
            }
            numbers_in_box.add(value);
            if (numbers_in_box.size()==2){
                rl.removeAllViews();
                rl.addView(tl);

            }
            if (numbers_in_box.size()==1){
                tv.setText(String.valueOf(value));
            }
            getTextViewFromBoxTable(value).setText(String.valueOf(value));
        }
    }
    void clearBox(){
        tv.setText("");
        while(numbers_in_box.size()>0){
            getTextViewFromBoxTable(numbers_in_box.get(0)).setText("");
            numbers_in_box.remove(0);}
        rl.removeAllViews();
        rl.addView(tv);
    }
    private TextView getTextViewFromBoxTable(int id){
        String textViewID = "id"+id;
        return tl.findViewById(c.getResources().getIdentifier(textViewID, "id", c.getPackageName()));
    }
}

