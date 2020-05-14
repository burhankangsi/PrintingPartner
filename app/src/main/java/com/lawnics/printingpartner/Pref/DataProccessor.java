package com.lawnics.printingpartner.Pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataProccessor {

    private final static String PREFS_NAME = "Printing_partner";
    private Context context;

    public DataProccessor(Context context) {
        this.context = context;
    }

    public void setInt(String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(key, 0);
    }

    public void setStr(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStr(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key, "DKTV");
    }

    public void setBool(String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBool(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getBoolean(key, false);
    }

    public boolean exist() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.contains("phone_no");
    }

    public boolean keyExist(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.contains(key);
    }

    public void deleteEverything() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        prefs.edit().clear().apply();
    }

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(key, json);
    }

    public void set(String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        if (prefs != null) {
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putString(key, value);
            prefsEditor.apply();
        }
    }

    public List<String> getStringList(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        if (prefs != null) {

            Gson gson = new Gson();
            List<String> list;

            String string = prefs.getString(key, null);
            Type type = new TypeToken<List<String>>() {
            }.getType();
            list = gson.fromJson(string, type);
            return list;
        }
        return null;
    }

}
