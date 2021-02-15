package com.example.logicalpuzzles.gamemodes.switchingPuzzles.pathSwitchingPuzzle;

import android.os.Bundle;
import android.util.Log;
import android.widget.TableRow;
import com.example.logicalpuzzles.R;
import com.example.logicalpuzzles.gamemodes.switchingPuzzles.SwitchingController;

import java.util.ArrayList;

public class PathSwitchingPuzzle extends SwitchingController {

    private int scanXPos=0;
    private int scanYPos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void setDragViews(String mapString) {
        for(int i=0;i<dragViews.size();i++){
            for(int j=0;j<dragViews.get(i).size();j++) {
                String value = String.valueOf(mapString.charAt(i*dragViews.get(i).size()+j));
                if(value.equals(" ")){
                    dragViews.get(i).get(j).setValue(-1);
                }else {
                    dragViews.get(i).get(j).setValue(Integer.parseInt(value));
                }
            }
        }
    }

    @Override
    protected void generateMap(){
        tl.removeAllViews();
        setParams(0.9/((float)width), 0.7/((float)level.length()/width), 0);
        dragViews = new ArrayList<>();
        DragViewSwitchingPath dv;
        ArrayList inner = null;
        TableRow tr = null;
        for (int i =0;i<level.length();i++){
            if(i%width==0){
                tr = new TableRow(this);
                tl.addView(tr);
                inner = new ArrayList();
                dragViews.add(inner);
            }
            dv = new DragViewSwitchingPath(i%width, i/width, this, this,getDrawable(R.drawable.box_with_light_border));
            dv.setLayoutParams(params);
            tr.addView(dv);
            inner.add(dv);
        }
    }
    @Override
    protected boolean isCorrect() {
        scanXPos=0;
        scanYPos=0;
        int value = getNextEnterValue(1);
        while(!(value==-1 || value==-2)){
            value = getNextEnterValue(value);
        }
        if(value==-2){
            return true;
        }
        scanXPos=0;
        scanYPos=0;
        value = getNextEnterValue(3);
        while(!(value==-1 || value==-2)){
            value = getNextEnterValue(value);
        }
        return value == -2;
    }
    private int getNextEnterValue(int fromDirection){//Direction 0:right, 1:left, 2:down, 3:up
        switch (fromDirection){
            case 0: scanXPos-=1;break;
            case 1: scanXPos+=1;break;
            case 2: scanYPos-=1;break;
            case 3: scanYPos+=1;break;
        }
        try{
            return ((DragViewSwitchingPath)dragViews.get(scanYPos).get(scanXPos)).enterDirectionToConnectingField(fromDirection);
        }catch (IndexOutOfBoundsException ignored){}
        return -1;
    }
}
