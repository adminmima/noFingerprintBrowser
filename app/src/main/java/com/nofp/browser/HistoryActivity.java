package com.nofp.browser;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import org.json.JSONObject;
import java.util.*;

public class HistoryActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.apply(this);
        super.onCreate(savedInstanceState);

        List<JSONObject> list = HistoryStorage.getAll(this);
        List<String> items = new ArrayList<>();
        for (JSONObject o : list) {
            try {
                items.add(o.getString("title") + "\n" + o.getString("url"));
            } catch (Exception ignored) {}
        }

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        Button clear = new Button(this);
        clear.setText("清空历史");
        clear.setOnClickListener(v -> {
            HistoryStorage.clear(this);
            finish();
        });
        root.addView(clear);

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
        root.addView(lv, new LinearLayout.LayoutParams(-1, 0, 1));

        setContentView(root);
    }
}
