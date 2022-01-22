package com.ak453.valorantboosters.utils;

import android.app.Activity;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Util {
    public static void goTo(Activity currentClass, Class toClass) {
        try {
            Intent gotoIntent = new Intent(currentClass, toClass);
            currentClass.startActivity(gotoIntent);
            currentClass.finish();
        } catch (Exception e) {
            e.printStackTrace();
       }
    }

    public static ArrayList<Integer> getListFromJSONArray(JSONArray array)
            throws JSONException {
        ArrayList<Integer> rates = new ArrayList<>();
        for(int i = 0;i<array.length();i++) {
            rates.add(array.getInt(i));
        }
        return rates;
    }

    public static String getStringFromJSON(String data, String key) throws JSONException {
        JSONObject dataObj = new JSONObject(data);
        return dataObj.getString(key);
    }
}
