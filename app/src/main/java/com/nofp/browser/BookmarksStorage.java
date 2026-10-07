package com.nofp.browser;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

public class BookmarksStorage {
    private static final String KEY = "bookmarks_json";

    public static void add(Context ctx, String url, String title) {
        if (url == null || url.isEmpty()) return;
        SharedPreferences sp = ctx.getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
        try {
            JSONArray arr = new JSONArray(sp.getString(KEY, "[]"));
            for (int i = 0; i < arr.length(); i++)
                if (arr.getJSONObject(i).getString("url").equals(url)) return;
            JSONObject obj = new JSONObject();
            obj.put("url", url);
            obj.put("title", title == null ? url : title);
            arr.put(obj);
            sp.edit().putString(KEY, arr.toString()).apply();
        } catch (Exception ignored) {}
    }

    public static void remove(Context ctx, String url) {
        SharedPreferences sp = ctx.getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
        try {
            JSONArray arr = new JSONArray(sp.getString(KEY, "[]"));
            JSONArray next = new JSONArray();
            for (int i = 0; i < arr.length(); i++)
                if (!arr.getJSONObject(i).getString("url").equals(url))
                    next.put(arr.get(i));
            sp.edit().putString(KEY, next.toString()).apply();
        } catch (Exception ignored) {}
    }

    public static boolean isBookmarked(Context ctx, String url) {
        if (url == null) return false;
        SharedPreferences sp = ctx.getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
        try {
            JSONArray arr = new JSONArray(sp.getString(KEY, "[]"));
            for (int i = 0; i < arr.length(); i++)
                if (arr.getJSONObject(i).getString("url").equals(url)) return true;
        } catch (Exception ignored) {}
        return false;
    }

    public static List<JSONObject> getAll(Context ctx) {
        List<JSONObject> list = new ArrayList<>();
        SharedPreferences sp = ctx.getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
        try {
            JSONArray arr = new JSONArray(sp.getString(KEY, "[]"));
            for (int i = 0; i < arr.length(); i++) list.add(arr.getJSONObject(i));
        } catch (Exception ignored) {}
        return list;
    }
}
