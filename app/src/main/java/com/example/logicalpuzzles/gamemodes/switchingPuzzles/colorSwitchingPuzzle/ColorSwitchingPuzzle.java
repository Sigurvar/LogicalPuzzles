package com.example.logicalpuzzles.gamemodes.switchingPuzzles.colorSwitchingPuzzle;

import android.os.Bundle;
import android.widget.TableRow;

import com.example.logicalpuzzles.R;
import com.example.logicalpuzzles.gamemodes.switchingPuzzles.DragView;
import com.example.logicalpuzzles.gamemodes.switchingPuzzles.SwitchingController;

import java.util.ArrayList;
import java.util.List;

public class ColorSwitchingPuzzle extends SwitchingController {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_color_switching_puzzle);
        super.onCreate(savedInstanceState);
        tl = findViewById(R.id.tableLayout);
        setLevelInfo();
        generateMap();
        setMap();
    }


    @Override
    protected void setDragViews(String mapString) {
        int color;
        for(int i=0;i<dragViews.size();i++){
            for(int j=0;j<dragViews.get(i).size();j++) {
                color = getColorFromChar(mapString.charAt(i*dragViews.size()+j));
                dragViews.get(i).get(j).setValue(color);
            }
        }
    }

    private int getColorFromChar(char c){
        switch (c) {
            case 'r':
                return getColor(R.color.red);
            case 'b':
                return getColor(R.color.blue);
            case 'g':
                return getColor(R.color.green);
            case 'y':
                return getColor(R.color.yellow);
            default:
                return  -1;
        }
    }
    @Override
    protected void generateMap(){
        tl.removeAllViews();
        setParams(0.9/((float)width), 0.7/((float)level.length()/width), 0);
        dragViews = new ArrayList<>();
        DragView dv;
        ArrayList<DragView> inner = null;
        TableRow tr = null;
        for (int i =0;i<level.length();i++){
            if(i%width==0){
                tr = new TableRow(this);
                tl.addView(tr);
                inner = new ArrayList();
                dragViews.add(inner);
            }
            dv = new DragView(i%width, i/width, this, this);
            dv.setLayoutParams(params);
            tr.addView(dv);
            inner.add(dv);
        }
    }
    @Override
    protected boolean isCorrect() {
        int totalVisited = 0;
        List<Integer> colorsSeen = new ArrayList<>();
        for(int i=0;i<dragViews.size();i++){
            for(int j=0;j<dragViews.get(i).size();j++) {
                int color = dragViews.get(i).get(j).getValue();
                if (!colorsSeen.contains(color)){
                    colorsSeen.add(color);
                    totalVisited += connectedColors(color, j, i);
                }
            }
        }
        return totalVisited==level.length();
    }
    private int connectedColors(int color, int x, int y){
        int amountVisited = 0;
        List<Integer> visited = new ArrayList<>();
        List<Integer> toVisit = new ArrayList<>();
        toVisit.add(y*width+x);
        while(toVisit.size()>0){
            visited.add(toVisit.get(0));
            int curr = toVisit.remove(0);
            amountVisited++;
            for(int i=0;i<4;i++) {
                DragView adjacent = (DragView) switchingWith(i, curr % width, curr / width);
                if(adjacent!=null) {
                    int pos = adjacent.getYPos() * width + adjacent.getXPos();
                    if (adjacent.getValue() == color && !visited.contains(pos) && !toVisit.contains(pos)) {
                        toVisit.add(pos);
                    }
                }
            }
        }
        return amountVisited;
    }

}
