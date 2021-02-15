package com.example.logicalpuzzles.gamemodes.hashiPuzzle;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.logicalpuzzles.R;
import com.example.logicalpuzzles.GameController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HashiPuzzle extends GameController {

    TableLayout tl;
    int columns=0;
    HashMap<TextView, HashiBox> map = new HashMap<>();
    HashiBox activeBox = null;
    List<List<DrawViewHashiPuzzle>> drawViewHashiPuzzles = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_hashi_puzzle);
        super.onCreate(savedInstanceState);
        tl = findViewById(R.id.main_table_layout);
        setMap();


    }

    @Override
    protected void setLevelInfo() {
    }

    @Override
    protected void setMap() {
        columns = parser.getInt("columns",levelID);
        String level = parser.getString("level", levelID);
        map = new HashMap<>();
        activeBox = null;
        drawViewHashiPuzzles = new ArrayList<>();
        tl.removeAllViews();
        double marg = 0.08;
        if(columns==9) marg =0.07;
        if(columns==11) marg =0.06;
        if(columns==13) marg =0.05;
        if(columns==15) marg =0.04;
        setParams(0,marg,0);
        TableRow tr = null;
        List<DrawViewHashiPuzzle> filler = null;
        for(int i=0;i<level.length();i++){
            if (i%columns==0){
                tr = new TableRow(this);
                filler = new ArrayList<>();
                drawViewHashiPuzzles.add(filler);
                tl.addView(tr);
            }
            if(level.charAt(i)=='0'){
                DrawViewHashiPuzzle dp = new DrawViewHashiPuzzle(this,i%columns,i/columns);
                dp.setLayoutParams(this.params);
                dp.setOnClickListener(this::drawViewHashiClick);
                filler.add(dp);
                tr.addView(dp);
            }
            else{
                TextView tv = (TextView) pc.getInflater().inflate(R.layout.equation_puzzle_box,null);
                if(columns==13)tv.setTextSize(19);
                if(columns==15)tv.setTextSize(16);
                tv.setText(String.valueOf(level.charAt(i)));
                tv.setLayoutParams(this.params);
                HashiBox hb = new HashiBox(i%columns,i/columns,tv,Integer.parseInt(String.valueOf(level.charAt(i))));
                tv.setOnClickListener(this::hashiBoxClicked);
                map.put(tv,hb);
                filler.add(null);
                tr.addView(tv);
            }
        }
    }
    @Override
    public void resetMap(View view){
        if(pc.popupIsNull()) {
            deSetActiveInput();
            for (List<DrawViewHashiPuzzle> list : drawViewHashiPuzzles) {
                for (DrawViewHashiPuzzle dp : list) {
                    if (dp != null) {
                        while (dp.removeLine()) {
                        }
                    }
                }
            }
            map.entrySet().forEach(v -> v.getValue().reset());
        }
    }
    @Override
    protected boolean isCorrect() {
        StringBuilder ans= new StringBuilder();
        for(List<DrawViewHashiPuzzle> list : drawViewHashiPuzzles){
            for (DrawViewHashiPuzzle dp : list){
                if (dp!=null){
                    ans.append(dp.status);
                }
                else {ans.append("9");}
            }
        }
        for(HashiBox hb : map.values()){
            if(!hb.isCorrect())return false;
        }
        return true;
    }

    @Override
    protected View addHints(View v) {
        pc.setHintCost(v, 5);
        return v;
    }

    @Override
    public void completeLevel() {
        String ans = parser.getString("ans", levelID);
        int pos = 0;
        for (List<DrawViewHashiPuzzle> list : drawViewHashiPuzzles) {
            for (DrawViewHashiPuzzle dp : list) {
                if (dp != null) {
                    dp.setStatus(Integer.parseInt(String.valueOf(ans.charAt(pos))));
                    pos++;
                }
            }
        }
        for (HashiBox hb : map.values()) {
            hb.autocomplete();
        }
    }

    public void drawViewHashiClick(View v){
        deSetActiveInput();
        int status= ((DrawViewHashiPuzzle)v).status;
        if(((DrawViewHashiPuzzle)v).removeLine()){
            int x= ((DrawViewHashiPuzzle)v).i;
            int y= ((DrawViewHashiPuzzle)v).j;
            if (status<3){
                while(drawViewHashiPuzzles.get(y).get(x-1)!=null){
                    drawViewHashiPuzzles.get(y).get(x-1).removeLine();
                    x--;
                }
                removeLine(x-1,y);
                x= ((DrawViewHashiPuzzle)v).i;
                while(drawViewHashiPuzzles.get(y).get(x+1)!=null){
                    drawViewHashiPuzzles.get(y).get(x+1).removeLine();
                    x++;
                }
                removeLine(x+1,y);
            }
            else{
                while(drawViewHashiPuzzles.get(y-1).get(x)!=null){
                    drawViewHashiPuzzles.get(y-1).get(x).removeLine();
                    y--;
                }
                removeLine(x,y-1);
                y= ((DrawViewHashiPuzzle)v).j;
                while(drawViewHashiPuzzles.get(y+1).get(x)!=null){
                    drawViewHashiPuzzles.get(y+1).get(x).removeLine();
                    y++;
                }
                removeLine(x,y+1);
            }
        }
    }
    private void removeLine(int x, int y){
        for (HashiBox value : map.values()) {
            if(x==value.i&&y==value.j){
                value.lineRemoved();
                return;
            }
        }
        Log.i("noe", "feil har skjedd");
    }
    private void deSetActiveInput(){
        if(activeBox!=null){
            activeBox.highLight();
            activeBox = null;
        }
    }
    public void hashiBoxClicked(View v){
        HashiBox clicked = map.get(v);
        if(activeBox==null){
            clicked.highLight();
            activeBox = clicked;
            return;
        }
        else if(clicked==activeBox){
            deSetActiveInput();
            return;
        }
        if(addLines(clicked.i,clicked.j, activeBox.i,activeBox.j)){
            clicked.lineAdded();
            activeBox.lineAdded();
        }
        else{
            activeBox.highLight();
            activeBox = map.get(v);
            activeBox.highLight();
        }
    }
    private boolean addLines(int startX, int startY, int stopX, int stopY){
        if(startX==stopX){
            int max = Math.max(startY, stopY);
            int min = Math.min(startY, stopY);
            for (int i = min+1;i<max;i++){
                if(drawViewHashiPuzzles.get(i).get(startX)==null || !drawViewHashiPuzzles.get(i).get(startX).canDrawVertical()){
                    return false;
                }
            }
            for (int i = min+1;i<max;i++){
                drawViewHashiPuzzles.get(i).get(startX).drawVertical();
            }
            return true;
        }
        else if (startY == stopY){
            int max = Math.max(startX, stopX);
            int min = Math.min(startX, stopX);
            for (int i = min+1;i<max;i++){
                if(drawViewHashiPuzzles.get(startY).get(i)==null||!drawViewHashiPuzzles.get(startY).get(i).canDrawHorizontal()){
                    return false;
                }
            }
            for (int i = min+1;i<max;i++){
                drawViewHashiPuzzles.get(startY).get(i).drawHorizontal();
            }
            return true;
        }
        else{return false;}
    }
}
