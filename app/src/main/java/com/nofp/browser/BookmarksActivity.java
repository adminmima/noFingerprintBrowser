package com.nofp.browser;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import org.json.JSONObject;
import java.util.*;

public class BookmarksActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.apply(this);
        super.onCreate(savedInstanceState);

        List<JSONObject> list = BookmarksStorage.getAll(this);
        List<String> items = new ArrayList<>();
        for (JSONObject o : list) {
            try {
                items.add(o.getString("title") + "\n" + o.getString("url"));
            } catch (Exception ignored) {}
        }

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        ListView lv = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_list_item_1, items);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((p, v, pos, id) -> {
            try {
                String url = list.get(pos).getString("url");
                android.content.Intent i = new android.content.Intent(this, MainActivity.class);
                i.setFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("load_url", url);
                startActivity(i);
            } catch (Exception ignored) {}
        });
        lv.setOnItemLongClickListener((p, v, pos, id) -> {
            try {
                String url = list.get(pos).getString("url");
                new android.app.AlertDialog.Builder(this)
                    .setTitle("删除收藏？")
                    .setPositiveButton("删除", (d, w) -> {
                        BookmarksStorage.remove(this, url);
                        finish();
                    })
                    .setNegativeButton("取消", null)
                    .show();
            } catch (Exception ignored) {}
            return true;
        });
        root.addView(lv, new LinearLayout.LayoutParams(-1, 0, 1));

        setContentView(root);
    }
}
