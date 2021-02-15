package com.example.logicalpuzzles;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.logicalpuzzles.JSONParsers.JSONParserForStats;
import com.example.logicalpuzzles.statistics.GameModeStats;
import com.example.logicalpuzzles.statistics.SubGameModeStats;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class StatsController extends Application {

    private static StatsController instance = null;
    public static StatsController getInstance(Context context) {
        if (instance == null) {
            instance = new StatsController(context);
        }
        return instance;
    }

    private int amountOfHints;
    private List<GameModeStats> gameModeStats = new ArrayList<>();
    private FileController fc;
    private JSONParserForStats parser;
    private Context context;

    public StatsController(Context context) {
        this.context = context;
        parser = new JSONParserForStats("");
        fc = new FileController(context);
        this.getStats();
    }
    public void save(){
        fc.saveToStatsFile(this.createJSONStringFromStats());
    }

    public void increaseHints(){
        amountOfHints++;
    }
    public int getAmountOfHints(){
        return amountOfHints;
    }
    public boolean spendHints(int spendAmount){
        if (amountOfHints >= spendAmount){
            amountOfHints-= spendAmount;
            return true;
        }
        return false;
    }
    public List<String> getAllGameModeNames(){
        return gameModeStats.stream().map(GameModeStats::getName).collect(Collectors.toList());
    }
    public GameModeStats getGameModeStats(String gameMode){
        return this.gameModeStats.stream().filter(s->s.getName().equals(gameMode)).findFirst().get();
    }
    public SubGameModeStats getSubGameModeStats(String subGameMode){
        return this.getParentGameMode(subGameMode).getSubGameModeStats(subGameMode);
    }
    public int getTotalLevelsCompleted(){
        return gameModeStats.stream().mapToInt(GameModeStats::getNumberOfLevels).sum();
    }
    public GameModeStats getParentGameMode(String subGameMode){
        return this.gameModeStats.stream().filter(s->subGameMode.contains(s.getName())).findFirst().get();
    }
    public void resetAllStats(){
        gameModeStats.forEach(g-> g.getAllSubGameModeStats().forEach(s->s.setCompletedLevels(0)));
        this.save();
    }

    private void getStats(){
        this.initGameModeStats();
        Log.i("StatsController", "generatingStats");
        this.readFromStatsFile();
        this.checkForUpdate();
        this.save();
        Log.i("StatsController", "stats generated successfully");
    }

    private void checkForUpdate() {
        // Check for new sub game mode
        List<String> all = new ArrayList<>();
        gameModeStats.forEach(g-> g.getAllSubGameModeStats().forEach(s->all.add(s.getName())));
        readAllSubGameModesFromAssetFile().stream().
                filter(subGameModeName->!all.contains(subGameModeName)).
                forEach(subGameModeName->parser.
                        createSubGameModeStatsFromAssetFile(fc.readFromAssetsFile(subGameModeName),subGameModeName, getParentGameMode(subGameModeName)));
        // Check for new levels in existing sub game mode
        gameModeStats.forEach(gameMode->gameMode.getAllSubGameModeStats().
                forEach(subGameMode->parser.
                        updateNumberOfSubGameModeLevels(subGameMode,fc.readFromAssetsFile(subGameMode.getName()))));
        if(amountOfHints==-1){ // Not existing
            amountOfHints = 100;
        }
    }

    private void initGameModeStats(){
        String json = fc.readFromAssetsFile("game_mode_unlock_criteria.json");
        parser.getObjectAsPair(json).forEach(a->this.gameModeStats.add(new GameModeStats(a.first, a.second)));
    }
    private void readFromStatsFile(){
        String json = fc.readFromStatsFile();
        parser.setJSONObject(json);
        for (GameModeStats gameModeStats : gameModeStats){
            JSONObject existingStats = parser.getGameMode(gameModeStats.getName());
            if (existingStats==null){
                newGameMode(gameModeStats);
                continue;
            }
            Iterator x = existingStats.keys();
            while (x.hasNext()) {
                parser.createSubGameModeStatsFromStatsFile((String) x.next(), gameModeStats);
            }
        }
        amountOfHints = parser.getJSONInt("amountOfHints");
    }

    private void newGameMode(GameModeStats gameModeStats){
        readAllSubGameModesFromAssetFile().stream().
                filter(name->name.contains(gameModeStats.getName())).collect(Collectors.toList()).
                forEach(name->parser.createSubGameModeStatsFromAssetFile(fc.readFromAssetsFile(name),name, gameModeStats));
    }

    private List<String> readAllSubGameModesFromAssetFile() {
        List<String> modes = new ArrayList<>();
        try {
            modes.addAll(Arrays.asList(context.getAssets().list("data")));
            modes.remove("game_mode_unlock_criteria.json");
        }catch(IOException e){e.printStackTrace();}
        return modes;
    }

    private String createJSONStringFromStats() {
        JSONObject json = new JSONObject();
        for (GameModeStats gameModeStats : gameModeStats){
            JSONObject jsonGameMode = new JSONObject();
            try {
                for (SubGameModeStats subGameModeStats : gameModeStats.getAllSubGameModeStats()){
                    jsonGameMode.put(subGameModeStats.getName(), parser.getSubGameModeStatsAsJsonObject(subGameModeStats));
                }
                json.put(gameModeStats.getName(), jsonGameMode);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        try {// Add amount of hints to the json stats file
            json.put("amountOfHints", amountOfHints);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return json.toString();
    }
}
