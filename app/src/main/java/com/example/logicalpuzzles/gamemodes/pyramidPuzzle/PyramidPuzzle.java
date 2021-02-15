package com.example.logicalpuzzles.gamemodes.pyramidPuzzle;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.example.logicalpuzzles.R;
import com.example.logicalpuzzles.GameController;

import java.util.ArrayList;

public class PyramidPuzzle extends GameController {

    private PyramidPuzzleMap ppm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pyramid_puzzle);
        this.ppm = new PyramidPuzzleMap(this);
        super.onCreate(savedInstanceState);

        String mode = subGameMode.substring(0,subGameMode.indexOf('.'));
        int layoutID = this.getResources().getIdentifier(mode, "layout", this.getPackageName());
        ViewStub stub = findViewById(R.id.layout_stub);
        stub.setLayoutResource(layoutID);
        stub.inflate();
        setLevelInfo();
        setMap();
    }
    @Override
    protected void setLevelInfo(){
        String operator = parser.getString("operator", levelID);
        ArrayList<Integer> level =parser.getObjectInfo("level", levelID);
        ArrayList<Integer> ans =parser.getObjectInfo("answer", levelID);
        ppm.setLevelInfo(level, ans, operator);
    }

    @Override
    protected void setMap() {
        ppm.setMap();
        ((TextView)findViewById(R.id.operator)).setText("");
    }

    @Override
    protected boolean isCorrect() {
        return ppm.checkAnswer();
    }

    @Override
    protected View addHints(View v) {
        pc.setHintCost(v, 5);
        View hint = pc.createHintButton(1, getString(R.string.pyramid_puzzle_show_operator_hint),2);
        hint.setOnClickListener(this::showOperator);
        ((ViewGroup)v).addView(hint);
        hint = pc.createHintButton(2, getString(R.string.fill_one_number), 1);
        hint.setOnClickListener(this::fillInOneNumber);
        ((ViewGroup)v).addView(hint);
        return v;
    }
    @Override
    public void closePopup(View view) {
        super.closePopup(view);
        ppm.unmarkField();
    }
    @Override
    public void completeLevel(){
        ppm.autoCompleteLevel();
    }

    public void input(View view) {if(pc.popupIsNull()){ ppm.input(view); } }
    public void number_click(View view) {
        if (pc.popupIsNull()) { ppm.number_click(view); }
    }
    public void backgroundClick(View view){
        if (pc.popupIsNull()){ ppm.unmarkField(); }
    }
    public void clearBox(View view){
        if (pc.popupIsNull()){ ppm.clearBox(); }
    }

    public void showOperator(View v){
        if(canAffordHint(v)) {
            closePopup(v);
            pc.createHintPopup(v, getText(R.string.pyramid_puzzle_show_operator) + " " + ppm.op);
            ((TextView) findViewById(R.id.operator)).setText(ppm.op);
        }else displayNotEnoughCoinsErrorMessage(v);
    }
    public void fillInOneNumber(View v){
        if(canAffordHint(v)) {
            ppm.fillInNumbers(v);
        }else displayNotEnoughCoinsErrorMessage(v);
    }

}


