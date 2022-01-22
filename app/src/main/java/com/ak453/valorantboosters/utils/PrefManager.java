package com.ak453.valorantboosters.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    public PrefManager(Context context) {
        pref = context.getSharedPreferences("internal_data",Context.MODE_PRIVATE);
    }

//Discord URL Setup
    public void setDiscordCheck(boolean value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("DCheckFlag",value);
        editor.apply();
    }

    public boolean getDiscordCheck() {
        return pref.getBoolean("DCheckFlag",true);
    }

    public void setDiscordUrl(String url) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("discordUrl",url);
        editor.apply();
    }

    public String getDisordUrl() {
        return pref.getString("discordUrl","https://discord.gg/t5k8Acvbus");
    }

//Others

    public void setPassword(String password) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("entry",password);
        editor.apply();
    }

    public String getPassword() {
        return pref.getString("entry","");
    }

    public String getRateArray() {
        return pref.getString("ratearray","[]");
    }

    public void setRateArray(String sKey) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ratearray",sKey);
        editor.apply();
    }

    public void setServiceCharges(String serviceChargeA, String serviceChargeB, String serviceChargeC, String serviceChargeD) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("serviceChargeA",Integer.parseInt(serviceChargeA));
        editor.putInt("serviceChargeB",Integer.parseInt(serviceChargeB));
        editor.putInt("serviceChargeC",Integer.parseInt(serviceChargeC));
        editor.putInt("serviceChargeD",Integer.parseInt(serviceChargeD));
        editor.apply();
    }

    public int getServiceChargeA() {
        return pref.getInt("serviceChargeA",15);
    }

    public int getServiceChargeB() {
        return pref.getInt("serviceChargeB",25);
    }

    public int getServiceChargeC() {
        return pref.getInt("serviceChargeC",50);
    }

    public int getServiceChargeD() {
        return pref.getInt("serviceChargeD",100);
    }

}
