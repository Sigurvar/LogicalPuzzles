package com.example.logicalpuzzles.gamemodes.makeThemGreenPuzzle;



import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.core.content.ContextCompat;

import com.example.logicalpuzzles.R;
import com.example.logicalpuzzles.GameController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class dummytest extends GameController {

    private List<Integer> currentMap;
    private List<List<Integer>> pattern;
    private List<ImageView> buttons;
    private int levelSize=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_make_them_green_puzzle);
        super.onCreate(savedInstanceState);
        setLevelInfo();
        setMap();
    }

    @Override
    protected void setLevelInfo(){
        setPattern(new ArrayList<Integer>(Arrays.asList(0,1,0,1,1,1,0,1,0)));
    }
    @Override
    public void wrongAns(View view) { }
    void setPattern(List<Integer> pattern){
        this.pattern = new ArrayList<>();
        int size = (int) Math.sqrt(pattern.size());
        int middle = (pattern.size()-1)/2;
        setParams(50, 50, 10);
        TableLayout table = findViewById(R.id.tableLayoutPattern);
        table.removeAllViews();
        TableRow tr = null;
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
                    this.pattern.add(new ArrayList<Integer>(Arrays.asList(i-(size-1)/2,j-(size-1)/2)));
                }
                button.setLayoutParams(this.params);
                tr.addView(button);
            }
        }
        Log.i("Click", this.pattern.toString());
    }
    @Override
    protected void setMap(){
        List<Integer> level = new ArrayList<>(Arrays.asList(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1));
        levelSize = (int)Math.sqrt(level.size());
        buttons = new ArrayList<>();
        currentMap = new ArrayList<>(level);
        setParams(120,120,10);
        TableLayout table = findViewById(R.id.tableLayoutMap);
        table.removeAllViews();
        TableRow tr = null;
        for (int i = 0; i< level.size(); i++){
            if (i%levelSize==0){
                tr = new TableRow(this);
                table.addView(tr);
            }
            ImageView button =  new ImageView(this);
            button.setImageResource(R.drawable.make_them_green_circle);
            button.setTag(((int)Math.floor(i/levelSize))+","+i%levelSize);
            if (level.get(i)==1){
                button.setColorFilter(ContextCompat.getColor(this, R.color.green));
            }
            else {
                button.setColorFilter(ContextCompat.getColor(this, R.color.red));
            }
            button.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View v) {
                    mapClick(v);
                }
            });
            button.setLayoutParams(this.params);
            buttons.add(button);
            tr.addView(button);
        }
    }
    void mapClick(View view){
        if (pc.popupIsNull())return;
        //Log.i("TAG",(String) view.getTag());
        int x = Integer.parseInt(((String) view.getTag()).split(",")[0]);
        int y = Integer.parseInt(((String) view.getTag()).split(",")[1]);
        for (int i = 0; i< pattern.size(); i++){
            int posx = x+ pattern.get(i).get(0);
            int posy = y+ pattern.get(i).get(1);
            if(posx>=0 && posx<levelSize && posy>=0 && posy<levelSize ){
                int pos = posx*levelSize+posy;
                currentMap.set(pos, (currentMap.get(pos)+1)%2);
                buttons.get(pos).setColorFilter(
                        ContextCompat.getColor(this, (currentMap.get(pos)==1 ? R.color.green : R.color.red)));
            }
        }
        Log.i("Map: " , currentMap.toString());
        //checkAnswer(view);
    }
    @Override
    protected boolean isCorrect(){
        for (int a : currentMap){
            if (a==0)return false;
        }
        return true;
    }

    @Override
    protected View addHints(View v) {
        return v;
    }

    @Override
    public void completeLevel() {

    }

}

