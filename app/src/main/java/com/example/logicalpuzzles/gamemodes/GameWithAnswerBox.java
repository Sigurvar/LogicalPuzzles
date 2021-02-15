package com.example.logicalpuzzles.gamemodes;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.logicalpuzzles.R;
import com.example.logicalpuzzles.GameController;

public abstract class GameWithAnswerBox extends GameController {

    int ans;
    String numbers="";
    TextView answerBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        answerBox = findViewById(R.id.ans_box);
    }
    @Override
    public void onResume() {
        super.onResume();
        setLevelInfo();
        setMap();
    }
    @Override
    protected void setLevelInfo() {
        ans = parser.getInt("ans", levelID);
        clearBox(null);
    }

    @Override
    protected boolean isCorrect() {
        if (numbers.equals("")) return false;
        return ans == Integer.parseInt(numbers);
    }
    @Override
    public void resetMap(View view) {
        if (pc.popupIsNull()) {
            clearBox(view);
        }
    }
    @Override
    public void completeLevel() {
        answerBox.setTextColor(getColor(R.color.green));
        answerBox.setText(String.valueOf(ans));
        numbers = String.valueOf(ans);
    }
    public void number_click(View view){
        if (pc.popupIsNull()) {
            answerBox.setTextColor(getColor(R.color.input_text_color));
            if (numbers.length()< 3){
                numbers += (((Button)view).getText().toString());
                answerBox.setText(numbers);
            }
        }
    }
    public void clearBox(View view){
        if (pc.popupIsNull()){
            answerBox.setTextColor(Color.parseColor("#3f000000"));
            numbers="";
            answerBox.setText(getString(R.string.answer));
        }
    }
}
