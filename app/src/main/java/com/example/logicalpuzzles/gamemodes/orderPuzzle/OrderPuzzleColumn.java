package com.example.logicalpuzzles.gamemodes.orderPuzzle;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.logicalpuzzles.R;

import java.util.ArrayList;

class OrderPuzzleColumn {
    private static LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0,
            1.0f
    );
    private float starty;
    private LinearLayout main;
    private int size;
    private ArrayList<TextView> textViews = new ArrayList<>();
    private int ans;
    boolean changeable = true;
    private OrderPuzzle op;
    private int pos=0;
    private int startPos = 0;
    @SuppressLint("ClickableViewAccessibility")
    OrderPuzzleColumn(LinearLayout cl, int size, OrderPuzzle op){
        main = cl;
        this.size = size;
        this.op = op;
        main.setOnTouchListener((v, event) -> {
            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN: starty = event.getY();break;
                case MotionEvent.ACTION_MOVE: this.moved(event.getY());break;
                default: break;
            }
            return true;
        });
        generateMap();
    }
    private void generateMap(){
        for (int i=0;i<size*2-2;i++){
            textViews.add(generateTextView());
        }
    }
    void newLevel(int ans, String level){
        this.ans = ans;
        setMap(level.split(","));
    }
    private void setMap(String[] level){
        changeable=true;
        setPos(0);
        for(int i=0;i<size;i++){
            textViews.get(i).setText(String.valueOf(level[i]));
        }
        for(int i=0;i<size-2;i++){
            textViews.get(size+i).setText("");
        }
        for (TextView tv : textViews){tv.setTextColor(op.getColor(R.color.black));}
        startPos = (int)(Math.random()*(size-2));
        setPos(startPos);
        updateRow();
    }
    void resetMap(){
        setPos(startPos);
    }
    void highlightTextView(int row, int color){
        textViews.get(row).setTextColor(color);
    }
    private void setPos(int atPos){
        if(!changeable){return;}
        while(pos!=0){
            clickUp();
        }
        while (pos!=atPos){
            clickDown();
        }
    }
    void setCorrect(){
        setPos(ans);
        for(TextView tv: textViews){
            tv.setTextColor(op.getColor(R.color.green));
        }
        changeable = false;
    }
    private void updateRow(){
        main.setWeightSum(size*2-2);
        main.removeAllViews();
        for (TextView tv : textViews){
            main.addView(tv);
        }
    }
    private void clickUp(){
        if(changeable && pos>0){
            TextView tv = textViews.remove(0);
            textViews.add(tv);
            updateRow();
            pos-=1;
        }
    }
    private void clickDown(){
        if(changeable && pos<size-2) {
            TextView tv = textViews.remove(textViews.size() - 1);
            textViews.add(0, tv);
            updateRow();
            pos +=1;
        }
    }
    int getValueAtRow(int row){
        String text = String.valueOf(textViews.get(row).getText());
        return (text.equals("")) ? -1 :  Integer.parseInt(text);
    }
    private TextView generateTextView(){
        TextView tv = new TextView(op, null, 0, R.style.order_puzzle_text_view);
        if(size==8){
            tv.setTextSize(18);
        } else if(size==7){
            tv.setTextSize(22);
        }
        tv.setPadding(30,5,30,5);
        tv.setLayoutParams(param);
        return tv;
    }
    private void moved(float currY){
        op.unHighlight();
        float movedY = currY - starty;
            if (Math.abs(movedY)>140-size*10){
                if (movedY>0){
                    clickDown();
                } else{
                    clickUp();
                }
                starty = currY;
            }
    }
}
