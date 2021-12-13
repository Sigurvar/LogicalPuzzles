package com.example.logicalpuzzles.selectors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.logicalpuzzles.R;
import com.example.logicalpuzzles.gamemodes.PatternPuzzle;
import com.example.logicalpuzzles.gamemodes.makeThemGreenPuzzle_v2.MakeThemGreenPuzzle_v2;
import com.example.logicalpuzzles.gamemodes.switchingPuzzles.colorSwitchingPuzzle.ColorSwitchingPuzzle;
import com.example.logicalpuzzles.gamemodes.fourColorPuzzle.FourColorsPuzzle;
import com.example.logicalpuzzles.gamemodes.NextNumberPuzzle;
import com.example.logicalpuzzles.gamemodes.equationpuzzle.EquationPuzzle;
import com.example.logicalpuzzles.gamemodes.hashiPuzzle.HashiPuzzle;
import com.example.logicalpuzzles.gamemodes.makeThemGreenPuzzle.MakeThemGreenPuzzle;
import com.example.logicalpuzzles.gamemodes.orderPuzzle.OrderPuzzle;
import com.example.logicalpuzzles.gamemodes.pyramidPuzzle.PyramidPuzzle;
import com.example.logicalpuzzles.gamemodes.sudokuPuzzle.SudokuPuzzle;
import com.example.logicalpuzzles.gamemodes.switchingPuzzles.pathSwitchingPuzzle.PathSwitchingPuzzle;
import com.example.logicalpuzzles.statistics.SubGameModeStats;

public class LevelSelector extends Selector {

    private String subGameMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRowParams(0.10, 7);

        Bundle bundle = getIntent().getExtras();
        subGameMode = bundle.getString("mode");
        super.setDisplayText(bundle.getString("displayText"));
    }

    @Override
    void generateCards(){
        SubGameModeStats stats = sc.getSubGameModeStats(subGameMode);
        TableLayout table = findViewById(R.id.tableLayout);
        table.removeAllViews();

        TableRow tr = null;
        View button;
        String level = getString(R.string.level)+" ";
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i=0;i<stats.getNumberOfLevels();i++){

            if (i%3==0){
                tr = new TableRow(this);
                table.addView(tr);
            }
            if (i<=stats.getNumberOfCompletedLevels()){
                button = inflater.inflate(R.layout.level_with_coin, null);
                Button levelText = button.findViewById(R.id.levelText);
                levelText.setText(level+(i+1));
                if (i!=stats.getNumberOfCompletedLevels()) {
                    levelText.setBackgroundResource(R.drawable.level_completed);
                }
            }
            else {
                button = inflater.inflate(R.layout.locked_level_with_coin, null);
            }
            if (i % stats.getHintInterval() == 0 && i>=stats.getNumberOfCompletedLevels() && i!=0) {
                button.findViewById(R.id.coin).setVisibility(View.VISIBLE);
            }
            button.setLayoutParams(super.rowParams);
            tr.addView(button);
        }
        scroll(stats.getNumberOfCompletedLevels());
    }

    private void scroll(int completedLevels){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Handler h = new Handler();
        h.postDelayed(() -> findViewById(R.id.scroll).scrollTo(0, (int)(size.y*0.1*(completedLevels/3))-150), 50);
    }

    @Override
    void handleUnLocked(LinearLayout ll, String name) {
    }

    public void playLevel(View view){
        Button btn = (Button) view;
        int levelID = Integer.parseInt(btn.getText().toString().split(" ")[1]);
        Intent intent;
        if (subGameMode.contains("pyramid_puzzle")){
            intent = new Intent(this, PyramidPuzzle.class);
        }
        else if (subGameMode.contains("next_number_puzzle")){
            intent = new Intent(this, NextNumberPuzzle.class);
        }
        else if (subGameMode.contains("pattern_puzzle")){
            intent = new Intent(this, PatternPuzzle.class);
        }
        else if (subGameMode.contains("sudoku_puzzle")){
            intent = new Intent(this, SudokuPuzzle.class);
        }
        else if (subGameMode.contains("four_colors_puzzle")){
            intent = new Intent(this, FourColorsPuzzle.class);
        }
        else if (subGameMode.contains("equation_puzzle")){
            intent = new Intent(this, EquationPuzzle.class);
        }
        else if (subGameMode.contains("hashi_puzzle")){
            intent = new Intent(this, HashiPuzzle.class);
        }
        else if (subGameMode.contains("order_puzzle")){
            intent = new Intent(this, OrderPuzzle.class);
        }
        else if (subGameMode.contains("color_switching_puzzle")){
            intent = new Intent(this, ColorSwitchingPuzzle.class);
        }
        else if (subGameMode.contains("path_switching_puzzle")){
            intent = new Intent(this, PathSwitchingPuzzle.class);
        }
        else if (subGameMode.contains("v2")){
            intent = new Intent(this, MakeThemGreenPuzzle_v2.class);
        }
        else {
            intent = new Intent(this, MakeThemGreenPuzzle.class);
        }
        Bundle b = new Bundle();
        b.putInt("level", levelID);
        b.putString("subGameMode",  subGameMode);

        intent.putExtras(b);
        startActivity(intent);
    }
}

