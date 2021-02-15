package com.example.logicalpuzzles.gamemodes.pyramidPuzzle;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.logicalpuzzles.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.function.IntBinaryOperator;

class PyramidPuzzleMap {
    private PyramidPuzzle pyramidPuzzleClass;
    private String nums;
    private TextView currActiveInput;
    private Context context;
    private ArrayList<Integer> level;
    private ArrayList<Integer> ans;
    private IntBinaryOperator operator;
    String op;

    PyramidPuzzleMap(PyramidPuzzle use){
        this.pyramidPuzzleClass = use;
        this.context = pyramidPuzzleClass.getApplicationContext();
    }
    void setLevelInfo(ArrayList<Integer> level, ArrayList<Integer> ans, String operator){
        this.unmarkField();
        this.ans = ans;
        this.level = level;
        this.op = operator;
        switch (operator){
            case "+":
                this.operator = Integer::sum;
                break;
            case "-":
                this.operator = (a,b)->a-b;
                break;
            case "*":
                this.operator = (a,b)->a*b;
                break;
        }
        currActiveInput = null;
        nums="";
    }

    void setMap(){
        unmarkField();
        for (int i=0;i<level.size();i++){
            TextView tv = getTextView(i);
            if (level.get(i)==null){
                tv.setTextColor(context.getColor(R.color.input_text_color));
                tv.setText("");
            }
            else {
                tv.setText(String.valueOf(level.get(i)));
                tv.setTextColor(context.getColor(R.color.white));
            }
        }
    }
    private TextView getTextView(int i){
        String textViewID = "tv"+(i+1);
        return pyramidPuzzleClass.findViewById(context.getResources().getIdentifier(textViewID, "id", context.getPackageName()));
    }
    private void markField(View view){
        if (((TextView)view).getCurrentTextColor()==Color.GREEN)return;
        currActiveInput = pyramidPuzzleClass.findViewById(view.getId());
        currActiveInput.setBackgroundResource(R.drawable.pyramid_box_active);
    }
    void unmarkField(){
        if (currActiveInput ==null) return;
        currActiveInput.setBackgroundResource(R.drawable.pyramid_box);
        currActiveInput = null;
    }
    void input(View view){
        this.unmarkField();
        nums="";
        String id = view.getResources().getResourceName(view.getId());
        int viewID = Integer.parseInt(id.split("/tv")[1]);
        if (level.get(viewID-1)==null) this.markField(view);
    }
    void clearBox(){
        if (currActiveInput ==null) return;
        currActiveInput.setText("");
        nums="";
    }
    boolean checkAnswer(){
        ArrayList<Integer> answer = new ArrayList<>();
        // Get the user answer
        for (int i=0;i<level.size();i++) {
            if(level.get(i)!=null){answer.add(level.get(i));}
            else {
                TextView tv = getTextView(i);
                String text = tv.getText().toString();
                if (text.equals("")) return false;
                answer.add(Integer.parseInt(text));
            }
        }
        int s = answer.size();
        int increase = 1;
        int split = 1;
        int a = 1;
        // Check if user answer is correct
        while (split<s){
            for (int i=0;i<increase;i++){
                if (answer.get(s-a) != operator.applyAsInt( answer.get(s-split-i-2),answer.get(s-split-i-1))){
                    return false;
                }
                a++;
            }
            increase++;
            split+=increase;
        }
        return true;
    }
    void number_click(View view){
        int maxLength = 3;
        if (currActiveInput !=null && nums.length()< maxLength && currActiveInput.getCurrentTextColor()!=Color.GREEN){
            nums += (((Button)view).getText().toString());
            currActiveInput.setText(nums);
        }
    }

    void fillInNumbers(View v){
        int nullCounter = 0;
        for (int i=0;i<level.size();i++) {
            if(level.get(i)==null) {
                TextView tv = getTextView(i);
                if (tv.getText().toString().equals("")){
                    input(tv);
                    tv.setTextColor(Color.GREEN);
                    currActiveInput.setText(String.valueOf(ans.get(nullCounter)));
                    pyramidPuzzleClass.closePopup(v);
                    return;
                }
                nullCounter++;
            }
        }
        Snackbar.make(v, R.string.pyramid_all_boxes_filled, Snackbar.LENGTH_LONG).show();
    }
    void autoCompleteLevel(){
        int nullCounter = 0;
        for (int i=0;i<level.size();i++) {
            if(level.get(i)==null) {
                TextView tv = getTextView(i);
                input(tv);
                tv.setTextColor(Color.GREEN);
                currActiveInput.setText(String.valueOf(ans.get(nullCounter)));
                nullCounter++;
            }
        }
    }
}
