package com.example.logicalpuzzles;

import com.example.logicalpuzzles.selectors.GameModeSelector;
import com.example.logicalpuzzles.JSONParsers.JSONParserForLevels;
import com.example.logicalpuzzles.statistics.SubGameModeStats;
import com.google.android.material.snackbar.Snackbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public abstract class GameController extends AppCompatActivity {

    public TableRow.LayoutParams params;
    public int levelID;
    public String subGameMode;
    public JSONParserForLevels parser;
    StatsController sc = StatsController.getInstance(this);
    public PopupForGames pc;
    SubGameModeStats subGameModeStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            throw new IllegalArgumentException("Need level ID in intent bundle");
        }
        this.levelID = bundle.getInt("level");
        this.subGameMode = bundle.getString("subGameMode");
        ((TextView) findViewById(R.id.levelText)).setText(getString(R.string.level) +" " + levelID);

        String json =  new FileController(this).readFromAssetsFile(subGameMode);
        parser = new JSONParserForLevels(json);
        pc = new PopupForGames(this);

        this.subGameModeStats = sc.getSubGameModeStats(subGameMode);
    }
    @Override
    public void onResume(){
        super.onResume();
        if(levelID==1 && subGameMode.contains("1")){
            findViewById(R.id.info_button).post(() -> showInfo(findViewById(R.id.info_button)));
        }
    }
    @Override
    public void onPause() {
        pc.closePopup();
        sc.save();
        super.onPause();
    }
    protected abstract void setLevelInfo(); // Set info for a new level
    protected abstract void setMap(); // Sets the map with level info
    protected abstract boolean isCorrect();
    protected abstract View addHints(View v);
    public abstract void completeLevel();
    public void autoCompleteLevel(View v){
        if(canAffordHint(v)) {
            completeLevel();
            closePopup(v);
        }else displayNotEnoughCoinsErrorMessage(v);
    }
    public void resetMap(View view){
        if (pc.popupIsNull()){setMap();}
    }
    public void nextLevel(View view) {
        this.levelID+=1;
        ((TextView) findViewById(R.id.levelText)).setText(getString(R.string.level)+ " " + (levelID));
        pc.closePopup();
        setLevelInfo();
        setMap();
    }
    public void showInfo(View v){
        pc.showInfo(v, subGameMode);
    }
    public void showHintScreen(View v){
        pc.showHintScreen(v);
    }
    public void checkAnswer(View view){
        if (!pc.popupIsNull())return;
        if (this.isCorrect()){
            int info = R.string.good_job;
            int buttonName = R.string.next_level;
            if(subGameModeStats.isHintRewardingLevel(levelID)){
                sc.increaseHints();
                info = R.string.good_job_with_hint;
            }
            if (subGameModeStats.isLastLevel(levelID)) {
                info = R.string.all_levels_completed;
                buttonName=R.string.home;
            }
            pc.createAnswerPopup(view, R.string.correct, info, buttonName);
        }
        else {
            wrongAns(view);
        }
    }
    public void wrongAns(View view){
        pc.createAnswerPopup(view, R.string.wrong, R.string.not_correct, R.string.try_again);
    }
    public void popupClick(View view){
        pc.popupClick(view);
    }
    public void closePopup(View v){
        pc.closePopup();
    }

    public void goHome(View view){
        pc.closePopup();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void moreLevels(View view){
        pc.closePopup();
        Intent intent = new Intent(this, GameModeSelector.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void setParams(double relWidth, double relHeight, int margin){
        int width = (int) (pc.size.x*relWidth);
        int height = (int) (pc.size.y*relHeight);
        setParams(width, height, margin);
    }
    public void setParams(int width, int height, int margin){
        params = new TableRow.LayoutParams(width,height);
        params.setMargins(margin, margin,margin,margin);
    }
    protected boolean canAffordHint(View v){
        int cost =Integer.parseInt((String) ((TextView)v.findViewById(R.id.hint_cost)).getText());
        return sc.spendHints(cost);
    }
    protected void displayNotEnoughCoinsErrorMessage(View view){
        Snackbar.make(view, R.string.can_not_afford, Snackbar.LENGTH_LONG).show();
    }
}

