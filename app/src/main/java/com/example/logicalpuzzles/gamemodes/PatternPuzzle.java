package com.example.logicalpuzzles.gamemodes;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.logicalpuzzles.R;
import com.example.logicalpuzzles.gamemodes.GameWithAnswerBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PatternPuzzle extends GameWithAnswerBox {

    private ArrayList<String> hintLetters = new ArrayList<>(Arrays.asList("a","b","c","d","e","f"));
    ArrayList<ArrayList<Integer>> level;
    private int maxPerRow=0;
    public static Map<Integer, Integer> patternSizeFigures;
    static {
        patternSizeFigures = new HashMap<>();
        patternSizeFigures.put(4, R.layout.pattern_puzzle_triangle);
        patternSizeFigures.put(5, R.layout.pattern_puzzle_square);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pattern_puzzle);
        super.onCreate(savedInstanceState);
        int layoutID = this.getResources().getIdentifier(subGameMode.substring(0,subGameMode.indexOf('.')), "layout", this.getPackageName());
        ViewStub stub = findViewById(R.id.layout_stub);
        stub.setLayoutResource(layoutID);
        stub.inflate();


    }

    @Override
    protected void setLevelInfo() {
        level = parser.getNestedArrayList("level", levelID);
        maxPerRow = parser.getInt("maxPerRow", levelID);
        super.setLevelInfo();
    }

    @Override
    protected View addHints(View v) {
        pc.setHintCost(v, 3);
        View hint = pc.createHintButton(0, getString(R.string.show_pattern),1);
        hint.setOnClickListener(this::showPattern);
        ((ViewGroup)v).addView(hint);
        return v;
    }


    @Override
    protected void setMap() {
        Resources res = getResources();
        for (int i =0;i<level.size();i++){
            String figId = "fig"+(i+1);
            int figViewId = res.getIdentifier(figId, "id", this.getPackageName());
            View cl = findViewById(figViewId);
            for(int j=0;j<level.get(i).size();j++){
                String textViewIdName = "num"+(j+1);
                int textViewId = res.getIdentifier(textViewIdName, "id", this.getPackageName());
                if(level.get(i).get(j)==null){
                    ((TextView)cl.findViewById(textViewId)).setText("?");
                }
                else{
                    ((TextView)cl.findViewById(textViewId)).setText(String.valueOf(level.get(i).get(j)));
                }
            }

        }
    }

    private void showPattern(View v){
        if(canAffordHint(v)) {
            closePopup(v);
            View cellView = pc.getInflater().inflate(patternSizeFigures.get(level.get(0).size()), null);
            for (int j = 0; j < level.get(0).size(); j++) {
                String textViewIdName = "num" + (j + 1);
                int textViewId = getResources().getIdentifier(textViewIdName, "id", this.getPackageName());
                ((TextView) cellView.findViewById(textViewId)).setTextColor(Color.BLACK);
                if (j == 0) {
                    String pattern = parser.getString("pattern", levelID);
                    ((TextView) cellView.findViewById(textViewId)).setText(pattern);
                } else {
                    ((TextView) cellView.findViewById(textViewId)).setText(hintLetters.get(j - 1));

                }
            }
            View popupView = pc.getInflater().inflate(R.layout.popup_hint, null);
            ((TextView) popupView.findViewById(R.id.info)).setText("The pattern used on the figures is:");
            ((RelativeLayout) popupView.findViewById(R.id.imageBox)).addView(cellView);
            pc.createPopup(v, popupView);
        }else displayNotEnoughCoinsErrorMessage(v);
    }
}

