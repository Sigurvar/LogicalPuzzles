package com.example.logicalpuzzles.gamemodes.switchingPuzzles;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;

import com.example.logicalpuzzles.GameController;
import com.example.logicalpuzzles.R;

import java.util.List;

public abstract class SwitchingController extends GameController{

    public String level;
    public String ans;
    public int width = 4;
    public TableLayout tl;
    public List<List<DragView>> dragViews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_color_switching_puzzle);
        super.onCreate(savedInstanceState);
        tl = findViewById(R.id.tableLayout);
        setLevelInfo();
        generateMap();
        setMap();
    }

    protected abstract void generateMap();

    @Override
    protected void setLevelInfo() {
        level = parser.getString("level", levelID);
        ans = parser.getString("ans", levelID);
        width = parser.getInt("width", levelID);
    }
    @Override
    public void completeLevel() {
        setDragViews(ans);
    }
    @Override
    protected void setMap() {
        setDragViews(level);
    }
    protected abstract void setDragViews(String mapString);

    @Override
    protected View addHints(View v) {
        pc.setHintCost(v, 5);
        return v;
    }

    protected DragView switchingWith(int direction, int x, int y){//Direction 0:right, 1:left, 2:down, 3:up
        try {
            if (direction == 0) return dragViews.get(y).get(x+1);
            else if (direction == 1) return dragViews.get(y).get(x-1);
            else if (direction == 2) return dragViews.get(y+1).get(x);
            else if (direction == 3) return dragViews.get(y-1).get(x);
        }catch (Exception ignored){}
        return null;
    }
    void move(int direction, DragView dv){
        DragView switching = switchingWith(direction, dv.getXPos(), dv.getYPos());
        if (switching==null || dv.getValue()==0)return;
        if (switching.getValue()==-1){
            switching.setValue(dv.getValue());
            dv.setValue(-1);
        }
        else {
            Log.i("not switching with blank filed", "ignoring");}
    }
}
