package com.nofp.browser;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("privacy", MODE_PRIVATE);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(40, 40, 40, 40);

        TextView tip = new TextView(this);
        tip.setText("提示：修改后需重启浏览器生效。");
        tip.setTextColor(0xFFCC0000);
        tip.setPadding(0, 0, 0, 30);
        root.addView(tip);

        for (int i = 0; i < PrivacyConfig.TOGGLE_KEYS.length; i++) {
            String key = PrivacyConfig.TOGGLE_KEYS[i];
            String label = PrivacyConfig.TOGGLE_LABELS[i];

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(0, 20, 0, 20);

            TextView tv = new TextView(this);
            tv.setText(label);
            tv.setTextSize(16);
            row.addView(tv, new LinearLayout.LayoutParams(0, -2, 1));

            Switch sw = new Switch(this);
            sw.setChecked(sp.getBoolean(key, true));
            sw.setOnCheckedChangeListener((btn, checked) ->
                sp.edit().putBoolean(key, checked).apply());
            row.addView(sw);

            root.addView(row);
        }

        ScrollView sv = new ScrollView(this);
        sv.addView(root);
        setContentView(sv);
    }
}
