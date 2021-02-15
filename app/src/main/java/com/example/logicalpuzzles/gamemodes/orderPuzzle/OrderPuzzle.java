package com.example.logicalpuzzles.gamemodes.orderPuzzle;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.logicalpuzzles.R;
import com.example.logicalpuzzles.GameController;

import java.util.ArrayList;

public class OrderPuzzle extends GameController {


    int[] highlighted = null;
    int size;
    ArrayList<OrderPuzzleColumn> columns = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_puzzle);
        super.onCreate(savedInstanceState);
        generateMap();
        setMap();
    }

    @Override
    protected void setLevelInfo() {

    }

    void generateMap(){
        size = (int) Math.sqrt(parser.getString("level",levelID).split("[,;]").length);
        LinearLayout ll;
        TableLayout tl = findViewById(R.id.tableLayout);
        TableRow tr = new TableRow(this);
        tl.addView(tr);
        for (int i=0;i<size;i++){
            ll= new LinearLayout(this, null, 0, R.style.order_puzzle_column);

            //ll = (LinearLayout) pc.getInflater().inflate(R.layout.order_puzzle_column, null);
            OrderPuzzleColumn opc = new OrderPuzzleColumn(ll, size, this);
            columns.add(opc);
            tr.addView(ll);
        }
    }
    @Override
    protected void setMap() {
        String[] level = parser.getString("level", levelID).split(";");
        String ans = parser.getString("ans", levelID);
        for (int i=0;i<size;i++){
            columns.get(i).newLevel(Integer.parseInt(String.valueOf(ans.charAt(i))), level[i]);
        }
    }

    @Override
    protected boolean isCorrect() {
        for (int i =0;i<size*2-2;i++){
            int value = columns.get(0).getValueAtRow(i);
            for (int j =1;j<size;j++){
                int found = columns.get(j).getValueAtRow(i);
                if(found!=-1){
                    if (found<=value){
                        highlighted = new int[]{j, i};
                        columns.get(j).highlightTextView(i, getColor(R.color.red));
                        return false;
                    }
                    else {value = found;}
                }
            }
        }
        return true;
    }

    @Override
    protected View addHints(View v) {
        pc.setHintCost(v, 5);
        View hint = pc.createHintButton(0, getString(R.string.one_column_in_right_position), 1);
        hint.setOnClickListener(this::setOneInRightPos);
        ((ViewGroup)v).addView(hint);
        return v;
    }

    private void setOneInRightPos(View view) {
        if (canAffordHint(view)) {
            closePopup(view);
            int num = (int) (Math.random() * columns.size() - 1);
            columns.get(num).setCorrect();
        }else displayNotEnoughCoinsErrorMessage(view);
    }

    @Override
    public void completeLevel() {
        for (OrderPuzzleColumn column : columns) {
            column.setCorrect();
        }
    }
    @Override
    public void resetMap(View v){
        if (pc.popupIsNull())columns.forEach(OrderPuzzleColumn::resetMap);
    }
    void unHighlight(){
        if(highlighted!=null){
            columns.get(highlighted[0]).highlightTextView(highlighted[1],columns.get(highlighted[0]).changeable ? getColor(R.color.black) : getColor(R.color.green));
        }
        highlighted=null;
    }
}
