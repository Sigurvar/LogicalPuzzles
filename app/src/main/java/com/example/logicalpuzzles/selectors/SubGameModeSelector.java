package com.example.logicalpuzzles.selectors;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.logicalpuzzles.R;
import com.example.logicalpuzzles.statistics.SubGameModeStats;

import java.util.List;


public class SubGameModeSelector extends Selector {

    String gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRowParams(0.25, 25);

        Bundle bundle = getIntent().getExtras();
        this.gameMode = bundle.getString("mode");

        super.setDisplayText(getDisplayText(gameMode));
    }
    public void gameModeClick(View view ){
        Intent intent = new Intent(this, LevelSelector.class);
        intent.putExtra("displayText", getDisplayText(view.getTag().toString()));
        super.gameModeClick(view, intent);
    }
    @Override
    void generateCards(){
        List<SubGameModeStats> subGameModeStats = sc.getGameModeStats(gameMode).getAllSubGameModeStats();
        int total = sc.getGameModeStats(gameMode).getNumberOfLevels();

        TableRow tr;
        for (SubGameModeStats sgm : subGameModeStats){
            tr = new TableRow(this);
            table.addView(tr);
            LinearLayout ll = setProgress(sgm, total, R.drawable.standard_button);
            tr.addView(ll);
        }
    }

    @Override
    void handleUnLocked(LinearLayout ll, String name) {
        ((TextView) ll.findViewById(R.id.tv)).setText(getDisplayText(name));
    }
    private String getDisplayText(String subGameMode){
        String display ="";
        switch (subGameMode) {
            case "pyramid_puzzle":
                display = getString(R.string.pyramid_puzzles);
                break;
            case "pyramid_puzzle_1.json":
                display = getString(R.string.small) + " " + getString(R.string.pyramid_puzzles);
                break;
            case "pyramid_puzzle_2.json":
                display = getString(R.string.medium) + " " + getString(R.string.pyramid_puzzles);
                break;
            case "pyramid_puzzle_3.json":
                display = getString(R.string.large) + " " + getString(R.string.pyramid_puzzles);
                break;
            case "make_them_green_puzzle":
                display = getString(R.string.make_them_green_puzzles);
                break;
            case "make_them_green_puzzle_1.json":
                display = getString(R.string.easy) + " " + getString(R.string.make_them_green_puzzles);
                break;
            case "make_them_green_puzzle_2.json":
                display = getString(R.string.medium) + " " + getString(R.string.make_them_green_puzzles);
                break;
            case "make_them_green_puzzle_3.json":
                display = getString(R.string.hard) + " " + getString(R.string.make_them_green_puzzles);
                break;
            case "make_them_green_puzzle_4.json":
                display = getString(R.string.extreme) + " " + getString(R.string.make_them_green_puzzles);
                break;
            case "make_them_green_puzzle_5.json":
                display = getString(R.string.expert) + " " + getString(R.string.make_them_green_puzzles);
                break;
            case "next_number_puzzle":
                display = getString(R.string.next_number_puzzles);
                break;
            case "next_number_puzzle_1.json":
                display = getString(R.string.simple) + " " + getString(R.string.next_number_puzzles);
                break;
            case "next_number_puzzle_2.json":
                display = getString(R.string.medium) + " " + getString(R.string.next_number_puzzles);
                break;
            case "pattern_puzzle":
                display = getString(R.string.pattern_puzzles);
                break;
            case "pattern_puzzle_1.json":
                display = getString(R.string.simple) + " " + getString(R.string.pattern_puzzles);
                break;
            case "pattern_puzzle_2.json":
                display = getString(R.string.medium) + " " + getString(R.string.pattern_puzzles);
                break;
            case "sudoku_puzzle":
                display = getString(R.string.sudoku_puzzles);
                break;
            case "sudoku_puzzle_1.json":
                display = getString(R.string.simple) + " " +getString(R.string.sudoku_puzzles);
                break;
            case "sudoku_puzzle_2.json":
                display = getString(R.string.medium) + " " +getString(R.string.sudoku_puzzles);
                break;
            case "sudoku_puzzle_3.json":
                display = getString(R.string.hard) + " " +getString(R.string.sudoku_puzzles);
                break;
            case "sudoku_puzzle_4.json":
                display = getString(R.string.extreme) + " " +getString(R.string.sudoku_puzzles);
                break;
            case "sudoku_puzzle_5.json":
                display = getString(R.string.expert) + " " +getString(R.string.sudoku_puzzles);
                break;
            case "four_colors_puzzle":
                display = getString(R.string.four_color_puzzles);
                break;
            case "four_colors_puzzle_1.json":
                display = getString(R.string.easy) + " " +getString(R.string.four_color_puzzles);
                break;
            case "equation_puzzle":
                display = getString(R.string.equation_puzzles);
                break;
            case "equation_puzzle_1.json":
                display = getString(R.string.easy) + " " +getString(R.string.equation_puzzles);
                break;
            case "equation_puzzle_2.json":
                display = getString(R.string.medium) + " " +getString(R.string.equation_puzzles);
                break;
            case "equation_puzzle_3.json":
                display = getString(R.string.hard) + " " +getString(R.string.equation_puzzles);
                break;
            case "equation_puzzle_4.json":
                display = getString(R.string.extreme) + " " +getString(R.string.equation_puzzles);
                break;
            case "equation_puzzle_5.json":
                display = getString(R.string.expert) + " " +getString(R.string.equation_puzzles);
                break;
            case "hashi_puzzle":
                display = getString(R.string.hashi_puzzles);
                break;
            case "hashi_puzzle_1.json":
                display = getString(R.string.small) + " " +getString(R.string.hashi_puzzles);
                break;
            case "hashi_puzzle_2.json":
                display = getString(R.string.medium) + " " +getString(R.string.hashi_puzzles);
                break;
            case "hashi_puzzle_3.json":
                display = getString(R.string.large) + " " +getString(R.string.hashi_puzzles);
                break;
            case "hashi_puzzle_4.json":
                display = getString(R.string.enormous) + " " +getString(R.string.hashi_puzzles);
                break;
            case "hashi_puzzle_5.json":
                display = getString(R.string.gigantic) + " " +getString(R.string.hashi_puzzles);
                break;
            case "order_puzzle":
                display = getString(R.string.order_puzzle);
                break;
            case "order_puzzle_1.json":
                display = getString(R.string.small)+" "+getString(R.string.order_puzzle);
                break;
            case "order_puzzle_2.json":
                display = getString(R.string.medium)+" "+getString(R.string.order_puzzle);
                break;
            case "order_puzzle_3.json":
                display = getString(R.string.large)+" "+getString(R.string.order_puzzle);
                break;
            case "order_puzzle_4.json":
                display = getString(R.string.enormous)+" "+getString(R.string.order_puzzle);
                break;
            case "order_puzzle_5.json":
                display = getString(R.string.gigantic)+" "+getString(R.string.order_puzzle);
                break;
            case "color_switching_puzzle":
                display = getString(R.string.color_switching_puzzle);
                break;
            case "color_switching_puzzle_1.json":
                display = getString(R.string.small)+" "+getString(R.string.color_switching_puzzle);
                break;
            case "color_switching_puzzle_2.json":
                display = getString(R.string.medium)+" "+getString(R.string.color_switching_puzzle);
                break;
            case "color_switching_puzzle_3.json":
                display = getString(R.string.large)+" "+getString(R.string.color_switching_puzzle);
                break;
            case "color_switching_puzzle_4.json":
                display = getString(R.string.enormous)+" "+getString(R.string.color_switching_puzzle);
                break;
            case "color_switching_puzzle_5.json":
                display = getString(R.string.gigantic)+" "+getString(R.string.color_switching_puzzle);
                break;
            case "path_switching_puzzle":
                display = getString(R.string.path_switching_puzzle);
                break;
            case "path_switching_puzzle_1.json":
                display = getString(R.string.small)+" "+getString(R.string.path_switching_puzzle);
                break;
            case "path_switching_puzzle_2.json":
                display = getString(R.string.medium)+" "+getString(R.string.path_switching_puzzle);
                break;
            case "path_switching_puzzle_3.json":
                display = getString(R.string.large)+" "+getString(R.string.path_switching_puzzle);
                break;
            case "path_switching_puzzle_4.json":
                display = getString(R.string.enormous)+" "+getString(R.string.path_switching_puzzle);
                break;
            case "path_switching_puzzle_5.json":
                display = getString(R.string.gigantic)+" "+getString(R.string.path_switching_puzzle);
                break;
        }
        return display;
    }
}
