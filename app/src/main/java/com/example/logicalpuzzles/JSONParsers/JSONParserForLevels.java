package com.example.logicalpuzzles.JSONParsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParserForLevels extends JSONParser{

    public JSONParserForLevels(String json){
        super(json);
        super.setJSONArray();
    }
    public String getString(String objectName, int levelID){
        try{
            JSONObject jsonLevel = super.array.getJSONObject(levelID-1);
            return (String) jsonLevel.get(objectName);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
    public int getInt(String objectName, int levelID){
        try{
            JSONObject jsonLevel = super.array.getJSONObject(levelID-1);
            return (int) jsonLevel.get(objectName);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return 0;
    }
    public ArrayList<Integer> getObjectInfo(String obejctName, int levelID){
        try{
            JSONObject jsonLevel = super.array.getJSONObject(levelID-1);
            return convertJSONArrayToArray((JSONArray) jsonLevel.get(obejctName));
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<ArrayList<Integer>> getNestedArrayList(String obejctName, int levelID){
        ArrayList<ArrayList<Integer>> array = new ArrayList<>();
        try{
            JSONObject jsonLevel = super.array.getJSONObject(levelID-1);
            JSONArray level = (JSONArray)jsonLevel.get(obejctName);
            for(int i=0;i<level.length();i++){
                array.add(convertJSONArrayToArray((JSONArray) level.get(i)));
            }
            return array;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
    public List<List<List<Integer>>> getDoubleNestedArrayList(String objectName, int levelID){
        List<List<List<Integer>>> array = new ArrayList<>();
        try{
            JSONObject jsonLevel = super.array.getJSONObject(levelID-1);
            JSONArray level = (JSONArray)jsonLevel.get(objectName);
            for(int i=0;i<level.length();i++){
                List<List<Integer>> inner = new ArrayList<>();
                for (int j=0;j<((JSONArray)level.get(i)).length();j++){
                    inner.add(convertJSONArrayToArray((JSONArray) ((JSONArray) level.get(i)).get(j)));
                }
                array.add(inner);
            }
            return array;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
