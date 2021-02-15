package com.example.logicalpuzzles.JSONParsers;

import android.util.Log;
import android.util.Pair;

import com.example.logicalpuzzles.statistics.GameModeStats;
import com.example.logicalpuzzles.statistics.SubGameModeStats;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSONParserForStats extends JSONParser{

    private JSONObject gameMode = new JSONObject();
    public JSONParserForStats(String json) {
        super(json);
    }

    public JSONObject getGameMode(String gameModeName) {
        try {
            this.gameMode = object.getJSONObject(gameModeName);
            return gameMode;
        }catch (JSONException ignored){
        }
        return null;
    }

    public void createSubGameModeStatsFromStatsFile(String name, GameModeStats parent){
        try {
            JSONObject sub = gameMode.getJSONObject(name);
            parent.addSubGameMode(name,
                    (Integer)sub.get("total"),
                    (Integer)sub.get("completed"),
                    (Integer)sub.get("unlock_criteria"),
                    (Integer)sub.get("hint_interval"));
        }catch (JSONException ignored){
        }
    }
    public void createSubGameModeStatsFromAssetFile(String json, String name, GameModeStats parent){
        setJSONObject(json);
        setJSONArray();
        parent.addSubGameMode(name,
                getNumberOfLevels(),
                0,
                getJSONInt("unlock_criteria"),
                getJSONInt("hint_interval"));
    }
    public void updateNumberOfSubGameModeLevels(SubGameModeStats subGameModeStats, String json){
        setJSONObject(json);
        setJSONArray();
        if (subGameModeStats.getNumberOfLevels()!=getNumberOfLevels()){
            subGameModeStats.setTotalNumberOfLevels(getNumberOfLevels());
        }
    }
    public List<Pair<String, Integer>> getObjectAsPair(String json){
        super.setJSONObject(json);
        List<Pair<String, Integer>> value = new ArrayList<>();
        try{
            Iterator x = object.keys();
            while (x.hasNext()) {
                String mode = (String) x.next();
                Log.i(mode, String.valueOf(object.getInt(mode)));
                value.add(new Pair<>(mode, object.getInt(mode)));
            }
        }catch(JSONException ignored){ }
        return value;
    }
    public JSONObject getSubGameModeStatsAsJsonObject(SubGameModeStats subGameModeStats){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("total",subGameModeStats.getNumberOfLevels());
            jsonObject.put("completed",subGameModeStats.getNumberOfCompletedLevels());
            jsonObject.put("unlock_criteria",subGameModeStats.getUnlockCriteria());
            jsonObject.put("hint_interval",subGameModeStats.getHintInterval());
        }catch (JSONException ignored){ }
        return jsonObject;
    }
}
