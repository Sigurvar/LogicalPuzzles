package com.example.logicalpuzzles.selectors;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.example.logicalpuzzles.R;
import com.example.logicalpuzzles.statistics.GameModeStats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameModeSelector extends Selector {
    public static Map<String, Integer> modefigures;
    static {
        modefigures = new HashMap<>();
        modefigures.put("pyramid_puzzle", R.drawable.logo_pyramid_puzzle);
        modefigures.put("make_them_green_puzzle", R.drawable.logo_make_them_green_puzzle);
        modefigures.put("next_number_puzzle", R.drawable.logo_next_number);
        modefigures.put("pattern_puzzle", R.drawable.logo_pattern_puzzle);
        modefigures.put("sudoku_puzzle", R.drawable.logo_sudoku);
        modefigures.put("four_colors_puzzle", R.drawable.logo_four_colors_puzzle);
        modefigures.put("equation_puzzle", R.drawable.logo_equation_puzzle);
        modefigures.put("hashi_puzzle", R.drawable.logo_hashi_puzzle);
        modefigures.put("order_puzzle", R.drawable.logo_order_puzzle);
        modefigures.put("color_switching_puzzle", R.drawable.logo_color_switching_puzzle);
        modefigures.put("path_switching_puzzle", R.drawable.logo_path_switching_puzzle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRowParams( 0.33, 15);
        super.setDisplayText("Select Gamemode");
    }
    public void gameModeClick(View view ){
        Intent intent = new Intent(this, SubGameModeSelector.class);
        super.gameModeClick(view, intent);
    }
    @Override
    void generateCards(){
        List<String> gameModes = sc.getAllGameModeNames();

        TableRow tr = null;
        for (int i=0; i<gameModes.size();i++) {
            if (i%2==0){
                tr = new TableRow(this);
                table.addView(tr);
            }
            GameModeStats gms = sc.getGameModeStats(gameModes.get(i));
            LinearLayout v = super.setProgress(gms, sc.getTotalLevelsCompleted(),-1);
            tr.addView(v);
        }
    }
    @Override
    void handleUnLocked(LinearLayout ll, String name) {
        ll.findViewById(R.id.iv).setBackgroundResource(modefigures.get(name));
    }
}

