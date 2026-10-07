package com.nofp.browser;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class AdvancedSettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.apply(this);
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("privacy_advanced", MODE_PRIVATE);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(30, 30, 30, 30);

        TextView tip = new TextView(this);
        tip.setText("修改后需重启浏览器生效。英文原文以小字显示在下方。");
        tip.setTextColor(0xFFCC0000);
        tip.setPadding(0, 0, 0, 20);
        root.addView(tip);

        String lastCat = "";
        for (String[] item : PrivacyConfig.ADVANCED) {
            if (item.length < 6) continue;
            String cat = item[0];
            String key = item[1];
            String nameZh = item[2];
            String descZh = item[3];
            String def = item[4];
            String type = item[5];

            if (!cat.equals(lastCat)) {
                lastCat = cat;
                TextView catTitle = new TextView(this);
                catTitle.setText("▸ " + cat);
                catTitle.setTextSize(18);
                catTitle.setPadding(0, 40, 0, 10);
                root.addView(catTitle);
            }

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.VERTICAL);
            row.setPadding(20, 15, 20, 15);

            TextView name = new TextView(this);
            name.setText(nameZh);
            name.setTextSize(15);
            row.addView(name);

            TextView desc = new TextView(this);
            desc.setText(descZh);
            desc.setTextSize(12);
            desc.setTextColor(0xFF999999);
            row.addView(desc);

            TextView keyView = new TextView(this);
            keyView.setText(key);
            keyView.setTextSize(10);
            keyView.setTextColor(0xFF666666);
            keyView.setTypeface(null, Typeface.ITALIC);
            row.addView(keyView);

            if (type.equals("bool")) {
                Switch sw = new Switch(this);
                sw.setChecked(sp.getBoolean(key, Boolean.parseBoolean(def)));
                sw.setOnCheckedChangeListener((b, c) ->
                    sp.edit().putBoolean(key, c).apply());
                LinearLayout swRow = new LinearLayout(this);
                swRow.setGravity(android.view.Gravity.END);
                swRow.addView(sw);
                row.addView(swRow);
            } else {
                EditText et = new EditText(this);
                et.setText(sp.getString(key, def));
                et.setTextSize(13);
                et.setOnFocusChangeListener((v, hasFocus) -> {
                    if (!hasFocus)
                        sp.edit().putString(key, et.getText().toString()).apply();
                });
                row.addView(et);
            }

            root.addView(row);

            View divider = new View(this);
            divider.setLayoutParams(new LinearLayout.LayoutParams(-1, 1));
            divider.setBackgroundColor(0xFF333333);
            root.addView(divider);
        }

        ScrollView sv = new ScrollView(this);
        sv.addView(root);
        setContentView(sv);
    }
}
