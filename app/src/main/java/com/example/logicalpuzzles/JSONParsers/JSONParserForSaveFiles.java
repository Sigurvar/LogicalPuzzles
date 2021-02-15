package com.example.logicalpuzzles.JSONParsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParserForSaveFiles extends JSONParser{
    public JSONParserForSaveFiles(String json) {
        super(json);
    }
    public JSONObject getSudokuAsJSONObject(String level, List<Integer> hintPos){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("level",level);
            jsonObject.put("hintPos", new JSONArray(hintPos));
        }catch (JSONException ignored){
        }
        return jsonObject;
    }
    public String getString(String objectName){
        try{
            return (String) object.get(objectName);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<Integer> getList(String obejctName){
        try{
            return convertJSONArrayToArray((JSONArray) object.get(obejctName));
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
