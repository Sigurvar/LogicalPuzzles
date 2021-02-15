package com.example.logicalpuzzles.gamemodes.makeThemGreenPuzzle;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import com.example.logicalpuzzles.R;
import com.example.logicalpuzzles.GameController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MakeThemGreenPuzzle extends GameController {

    private List<List<Integer>> pattern;
    private List<MakeThemGreenCircle> buttons;
    private int levelSize=0;
    private HashMap<View, MakeThemGreenCircle> ids;
    private TextView clicksLeftView;
    private int maxClicks;
    private int currentClicks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_make_them_green_puzzle);
        super.onCreate(savedInstanceState);
        clicksLeftView = findViewById(R.id.clicksLeftView);
        setLevelInfo();
        setMap();
    }

    @Override
    protected void setLevelInfo(){
        maxClicks = parser.getInt("maxClicks", levelID);
        List<Integer> newPattern = parser.getObjectInfo("pattern", levelID);
        setPattern(newPattern);
    }
    @Override
    public void wrongAns(View view) {
        if (maxClicks==currentClicks){
            pc.createAnswerPopup(view, R.string.out_of_clicks, R.string.that_was_close, R.string.try_again);
        }
    }
    @Override
    public void closePopup(View v){
        super.closePopup(v);
        setMap();
    }

    void setPattern(List<Integer> pattern){
        this.pattern = new ArrayList<>();
        int size = (int) Math.sqrt(pattern.size());
        int middle = (pattern.size()-1)/2;
        setParams(0.07, 0.04, 5);
        TableLayout table = findViewById(R.id.tableLayoutPattern);
        table.removeAllViews();
        TableRow tr;
        for (int i=0; i<size;i++){
            tr = new TableRow(this);
            table.addView(tr);
            for (int j=0;j<size;j++){
                ImageView button =  new ImageView(this);
                button.setImageResource(R.drawable.make_them_green_circle);

                if (i*3+j==middle){
                    if (pattern.get(i*3+j)==1){
                        button.setImageResource(R.drawable.make_them_green_circle_middle_on);
                        this.pattern.add(new ArrayList<Integer>(Arrays.asList(i-(size-1)/2,j-(size-1)/2)));
                    }
                    else {
                        button.setImageResource(R.drawable.make_them_green_circle_middle);
                    }
                }
                else if (pattern.get(i*3+j)==1){
                    button.setColorFilter(ContextCompat.getColor(this, R.color.white));
                    this.pattern.add(new ArrayList<Integer>(Arrays.asList(j-(size-1)/2, i-(size-1)/2)));
                }
                button.setLayoutParams(this.params);
                tr.addView(button);
            }
        }
    }

    @Override
    protected void setMap(){
        List<Integer> level = parser.getObjectInfo("level", levelID);
        if (levelSize!=(int)Math.sqrt(level.size())){
            generateMap(level.size());
            levelSize=(int)Math.sqrt(level.size());
        }
        for (int i=0;i<level.size();i++){
            buttons.get(i).setValue(level.get(i));
        }
        clicksLeftView.setText(String.valueOf(maxClicks));
        currentClicks = 0;
    }
    void generateMap(int size){
        buttons = new ArrayList<>();
        ids = new HashMap<>();
        setParams(0.12, 0.07, 10);
        TableLayout table = findViewById(R.id.tableLayoutMap);
        table.removeAllViews();
        TableRow tr = null;
        int sqrt=(int)Math.sqrt(size);
        for (int i = 0; i< size; i++){
            if (i%sqrt==0){
                tr = new TableRow(this);
                table.addView(tr);
            }
            ImageView button =  new ImageView(new ContextThemeWrapper(this, R.style.make_them_green_image_view_circle), null, 0);
            MakeThemGreenCircle a = new MakeThemGreenCircle(i%sqrt, i/sqrt, button);

            button.setLayoutParams(params);
            ids.put(button, a);
            buttons.add(a);
            tr.addView(button);
        }
    }
    public void mapClick(View view){
        if (!pc.popupIsNull())return;
        currentClicks++;
        MakeThemGreenCircle a = ids.get(view);
        int x = a.posX;
        int y = a.posY;
        for (int i = 0; i< pattern.size(); i++){
            int posx = x+ pattern.get(i).get(0);
            int posy = y+ pattern.get(i).get(1);
            if(posx>=0 && posx<levelSize && posy>=0 && posy<levelSize ){
                int pos = posy*levelSize+posx;
                buttons.get(pos).increaseValue();
            }
        }
        clicksLeftView.setText(String.valueOf(maxClicks-currentClicks));
        checkAnswer(view);
    }
    @Override
    protected boolean isCorrect(){
        for (MakeThemGreenCircle a : buttons){
            if (a.getValue()==0)return false;
        }
        return true;
    }

    @Override
    protected View addHints(View v) {
        pc.setHintCost(v, levelSize);
        return v;
    }
    @Override
    public void autoCompleteLevel(View v){
        if(canAffordHint(v)) {
            completeLevel();
            super.closePopup(v);
            super.checkAnswer(v);
        }else displayNotEnoughCoinsErrorMessage(v);
    }
    @Override
    public void completeLevel(){
        for(MakeThemGreenCircle m : buttons){
            m.setCorrect();
        }
    }
}

