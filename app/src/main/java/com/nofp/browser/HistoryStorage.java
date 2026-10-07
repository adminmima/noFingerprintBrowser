package com.nofp.browser;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

public class HistoryStorage {
    private static final String KEY = "history_json";
    private static final int MAX = 500;

    public static void add(Context ctx, String url, String title, long time) {
        if (url == null || url.isEmpty()) return;
        SharedPreferences sp = ctx.getSharedPreferences("history", Context.MODE_PRIVATE);
        try {
            JSONArray arr = new JSONArray(sp.getString(KEY, "[]"));
            JSONObject obj = new JSONObject();
            obj.put("url", url);
            obj.put("title", title == null ? url : title);
            obj.put("time", time);
            arr.put(obj);
            if (arr.length() > MAX) {
                JSONArray trimmed = new JSONArray();
                for (int i = arr.length() - MAX; i < arr.length(); i++)
                    trimmed.put(arr.get(i));
                arr = trimmed;
            }
            sp.edit().putString(KEY, arr.toString()).apply();
        } catch (Exception ignored) {}
    }

    public static List<JSONObject> getAll(Context ctx) {
        List<JSONObject> list = new ArrayList<>();
        SharedPreferences sp = ctx.getSharedPreferences("history", Context.MODE_PRIVATE);
        try {
            JSONArray arr = new JSONArray(sp.getString(KEY, "[]"));
            for (int i = arr.length() - 1; i >= 0; i--)
                list.add(arr.getJSONObject(i));
        } catch (Exception ignored) {}
        return list;
    }

    public static void clear(Context ctx) {
        ctx.getSharedPreferences("history", Context.MODE_PRIVATE).edit().clear().apply();
    }
}
