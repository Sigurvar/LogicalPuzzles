package com.example.logicalpuzzles.gamemodes.makeThemGreenPuzzle_v2;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import com.example.logicalpuzzles.R;
import com.example.logicalpuzzles.GameController;
import com.example.logicalpuzzles.gamemodes.makeThemGreenPuzzle.MakeThemGreenCircle;
import com.example.logicalpuzzles.gamemodes.makeThemGreenPuzzle.MakeThemGreenPuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MakeThemGreenPuzzle_v2 extends GameController{
    private List<List<Integer>> pattern;
    private ImageView activeDirection;
    private List<Integer> activeDirectionNumbers; // [x,y]
    private List<MakeThemGreenCircle> buttons;
    private int levelSize=0;
    private HashMap<View, MakeThemGreenCircle> ids;
    private TextView clicksLeftView;
    private int maxClicks;
    private int currentClicks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_make_them_green_puzzle_v2);
        super.onCreate(savedInstanceState);
        clicksLeftView = findViewById(R.id.clicksLeftView);
        setLevelInfo();
        setMap();
    }

    @Override
    protected void setLevelInfo(){
        maxClicks = parser.getInt("maxClicks", levelID);
        activeDirection = findViewById(R.id.row);
        activeDirectionNumbers = Arrays.asList(0,1);
    }
    @Override
    public void wrongAns(View view) {
        if (maxClicks==currentClicks){
            pc.createAnswerPopup(view, R.string.out_of_clicks, R.string.that_was_close, R.string.try_again);
        }
    }
    private boolean isValidButtonLocation(int row, int column){
        return row>=0 && column>=0 && row<levelSize && column<levelSize;
    }
    @Override
    public void closePopup(View v){
        super.closePopup(v);
        setMap();
    }

    @SuppressLint("NonConstantResourceId")
    public void switchDirection(View view){
        activeDirection.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.grey)));
        activeDirection = (ImageView) view;
        activeDirection.setImageTintList(null);
        int id = view.getId();
        switch (id){
            case R.id.row:
                activeDirectionNumbers = Arrays.asList(1,0);
                break;
            case R.id.column:
                activeDirectionNumbers = Arrays.asList(0,1);
                break;
            case R.id.right_up:
                activeDirectionNumbers = Arrays.asList(1,-1);
                break;
            case R.id.right_down:
                activeDirectionNumbers = Arrays.asList(1,1);
                break;
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
        assert a != null;
        int x = a.posX;
        int y = a.posY;
        for (int i = -1; i<=1; i+=2){
            int column = x +activeDirectionNumbers.get(0)*i;
            int row = y+activeDirectionNumbers.get(1)*i;
            while(isValidButtonLocation(row, column)){
                int pos = row*levelSize+column;
                // TODO: Switchvalue???
                buttons.get(pos).increaseValue();
                column  += activeDirectionNumbers.get(0)*i;
                row += activeDirectionNumbers.get(1)*i;
            }
        }
        int pos = y*levelSize+x;
        // TODO: Switchvalue???
        buttons.get(pos).increaseValue();
        clicksLeftView.setText(String.valueOf(maxClicks-currentClicks));
        checkAnswer(view);
    }
    @Override
    protected boolean isCorrect(){
        return buttons.stream().allMatch(a->a.getValue()==1);
        /*for (MakeThemGreenCircle a : buttons){
            if (a.getValue()==0)return false;
        }
        return true;*/
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
        buttons.forEach(MakeThemGreenCircle::setCorrect);
    }
}
