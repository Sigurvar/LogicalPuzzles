package com.example.logicalpuzzles;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logicalpuzzles.gamemodes.pyramidPuzzle.PyramidPuzzle;
import com.example.logicalpuzzles.sideActivities.SettingsScreen;

import com.example.logicalpuzzles.selectors.GameModeSelector;


public class MainActivity extends AppCompatActivity {

    StatsController sc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sc=StatsController.getInstance(this);
    }

    public void playLevel(View view){
        Button btn = (Button) view;
        int levelID = Integer.parseInt(btn.getText().toString().split(" ")[1]);

        Intent intent = new Intent(this, PyramidPuzzle.class);
        Bundle b = new Bundle();
        b.putInt("level", levelID); //Your id

        intent.putExtras(b);
        startActivity(intent);

    }
    public void openGameModeSelector(View view){
        Intent intent = new Intent(this, GameModeSelector.class);
        startActivity(intent);
    }
    public void play(View view){

        /*Intent intent = new Intent(this, PathSwitchingPuzzle.class);
        Bundle b = new Bundle();
        b.putInt("level", 0);
        b.putString("subGameMode",  "color_switching_puzzle_1.json");

        intent.putExtras(b);
        startActivity(intent);*/
    }

    public void settings(View v){
        Intent intent = new Intent(this, SettingsScreen.class);
        startActivity(intent);
    }
    // TODO: generate hint skal kunne endre på antall hint som kreves for å bli kjøpt
    // TODO: auto complete level skal kunne endre på antall coins som kreves
    // TODO: endre på pyramid puzzle til å være bare bokser oppå hverandre
        // Kan da få større figurer
    // TODO: Legg til flere nivåer

}


