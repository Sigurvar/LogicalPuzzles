package com.example.logicalpuzzles.gamemodes;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.logicalpuzzles.R;

import java.util.ArrayList;

public class NextNumberPuzzle extends GameWithAnswerBox {

    private ArrayList<Integer> level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_next_number_puzzle);
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void setLevelInfo() {
        level = parser.getObjectInfo("level",levelID);
        super.setLevelInfo();
    }

    @Override
    protected View addHints(View v) {
        pc.setHintCost(v, 2);
        return v;
    }

    @Override
    protected void setMap() {
        pc.closePopup();
        StringBuilder text= new StringBuilder();
        for(int i : level){
            text.append(i).append(", ");
        }
        text.append("?");
        ((TextView)findViewById(R.id.levelDisplay)).setText(text);
    }
}
