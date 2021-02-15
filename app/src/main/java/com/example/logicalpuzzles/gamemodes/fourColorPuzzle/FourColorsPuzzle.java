package com.example.logicalpuzzles.gamemodes.fourColorPuzzle;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.logicalpuzzles.R;
import com.example.logicalpuzzles.GameController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FourColorsPuzzle extends GameController {
    RelativeLayout rr;
    LinearLayout ll;
    DrawView drawView;
    View activeColorView = null;
    float px;
    int numberOfColors;
    List<Integer> colors = new ArrayList<>(Arrays.asList(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW));
    List<List<List<Integer>>> map = new ArrayList<>();
    ArrayList<ArrayList<Integer>> neighbors = new ArrayList<>();
    private float[] lastTouchDownXY = new float[2];
    private List<DrawView> drawViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_four_colors_puzzle);
        super.onCreate(savedInstanceState);
        setUp();
        setLevelInfo();
        setMap();
    }
    @Override
    public void resetMap(View view){
        if (pc.popupIsNull()) {
            for (DrawView dv : drawViews) {
                dv.setColor(dv.getColor());
            }
            if (activeColorView != null) setColor(activeColorView);
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private void setUp(){
        ll = findViewById(R.id.linear_layout);
        rr = findViewById(R.id.relative_layout);
        rr.setOnTouchListener((v, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                lastTouchDownXY[0] = event.getX();
                lastTouchDownXY[1] = event.getY();
            }
            return false;
        });
        px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                20f,
                getResources().getDisplayMetrics()
        );
    }

    @Override
    protected void setLevelInfo() {
        map = parser.getDoubleNestedArrayList("level", levelID);
        neighbors = parser.getNestedArrayList("neighbors", levelID);
        numberOfColors = parser.getInt("colors", levelID);
        super.setParams(1/((float)numberOfColors+1)-0.04,0.12,10);
    }

    @Override
    protected void setMap() {
        rr.removeAllViews();
        drawViews = new ArrayList<>();
        for(List<List<Integer>> l : map){
            drawView = new DrawView(this,  l);
            rr.addView(drawView);
            drawViews.add(drawView);
        }
        for(int i=0; i<neighbors.size();i++){
            for(Integer j : neighbors.get(i)){
                drawViews.get(i+1).addNeighbor(drawViews.get(j));
            }
        }
        ll.removeAllViews();
        int i=0;
        ImageView iv;
        while(i<numberOfColors){
            iv = new ImageView(this);
            iv.setBackgroundColor(colors.get(i));
            iv.setTag(colors.get(i));
            iv.setLayoutParams(params);
            iv.setOnClickListener(this::setColor);
            ll.addView(iv);
            i++;

        }
        iv = new ImageView(this);
        iv.setImageResource(R.drawable.ic_enter);
        iv.setOnClickListener(this::checkAnswer);
        iv.setLayoutParams(params);
        ll.addView(iv);
    }

    @Override
    protected boolean isCorrect() {
        for(DrawView dv : drawViews){
            if (!dv.isValid()){return false;}
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
         DrawView dv;
         for (int i = 0; i < ans.length(); i++) {
             dv = drawViews.get(i);
             switch (ans.charAt(i)) {
                 case 'r':
                     dv.setColor(Color.RED);
                     break;
                 case 'g':
                     dv.setColor(Color.GREEN);
                     break;
                 case 'b':
                     dv.setColor(Color.BLUE);
                     break;
                 case 'y':
                     dv.setColor(Color.YELLOW);
                     break;
             }
         }
    }

    @SuppressLint("ResourceAsColor")
    public void setColor(View v){
        if(!pc.popupIsNull()){return;}
        if (activeColorView!=null){
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(Integer.parseInt(activeColorView.getTag().toString()));
            activeColorView.setBackground(gd);
        }
        if (activeColorView==v){
            activeColorView = null;
        }
        else {
            activeColorView = v;
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(Integer.parseInt(activeColorView.getTag().toString()));
            gd.setCornerRadius(5);
            gd.setStroke(6, 0xFF000000);
            v.setBackground(gd);
        }
    }
    public void click(View v){
        if(activeColorView==null || !pc.popupIsNull()){return;}
        float x = lastTouchDownXY[0]-px;
        float y = lastTouchDownXY[1]-px;
        for(DrawView d : drawViews){
            if(d.region.contains(Math.round(x), Math.round(y))){
                d.setColor(Integer.parseInt(activeColorView.getTag().toString()));
                break;
            }
        }
    }
}

