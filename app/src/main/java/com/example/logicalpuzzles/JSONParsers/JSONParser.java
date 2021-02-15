package com.example.logicalpuzzles.JSONParsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

abstract class JSONParser {
    JSONObject object = null;
    JSONArray array = null;


    JSONParser(String json){
        setJSONObject(json);
    }
    public void setJSONObject(String json) {
        try {
            this.object = new JSONObject(json);
        }catch(JSONException ignored){
        }
    }
    void setJSONArray(){
        try {
            this.array = this.object.getJSONArray("levels");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    public int getJSONInt(String name)  {
        try {
            return (int) this.object.get(name);
        }catch(JSONException e){
            e.printStackTrace();
        }
        return -1;
    }
    int getNumberOfLevels(){
        return array.length();
    }
    ArrayList<Integer> convertJSONArrayToArray(JSONArray jArray){
        ArrayList<Integer> listdata = new ArrayList<>();
        try {
            if (jArray != null) {
                for (int i = 0; i < jArray.length(); i++) {
                    if (jArray.get(i).toString().equals("null")){
                        listdata.add(null);
                    }
                    else listdata.add((int)jArray.get(i));
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return listdata;
    }

}
