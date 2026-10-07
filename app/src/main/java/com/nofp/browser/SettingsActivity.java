package com.nofp.browser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.apply(this);
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

        // 主题开关
        addSwitch(root, sp, "dark_mode", "深色主题（默认开）", true);
        // 历史记录开关（默认关）
        addSwitch(root, sp, "history_enabled", "启用历史记录（默认关）", false);

        // 高级设置入口
        Button advanced = new Button(this);
        advanced.setText("打开高级设置（300+ 项）");
        advanced.setOnClickListener(v ->
            startActivity(new Intent(this, AdvancedSettingsActivity.class)));
        root.addView(advanced);

        // 分隔
        TextView sep = new TextView(this);
        sep.setText("— 核心隐私开关 —");
        sep.setPadding(0, 30, 0, 10);
        root.addView(sep);

        // 20 个核心开关
        for (int i = 0; i < PrivacyConfig.TOGGLE_KEYS.length; i++) {
            addSwitch(root, sp, PrivacyConfig.TOGGLE_KEYS[i],
                PrivacyConfig.TOGGLE_LABELS[i], true);
        }

        ScrollView sv = new ScrollView(this);
        sv.addView(root);
        setContentView(sv);
    }

    private void addSwitch(LinearLayout root, SharedPreferences sp,
                           String key, String label, boolean def) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(0, 20, 0, 20);

        TextView tv = new TextView(this);
        tv.setText(label);
        tv.setTextSize(16);
        row.addView(tv, new LinearLayout.LayoutParams(0, -2, 1));

        Switch sw = new Switch(this);
        sw.setChecked(sp.getBoolean(key, def));
        sw.setOnCheckedChangeListener((btn, checked) ->
            sp.edit().putBoolean(key, checked).apply());
        row.addView(sw);

        root.addView(row);

        View divider = new View(this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(-1, 1));
        divider.setBackgroundColor(0xFF444444);
        root.addView(divider);
    }
}
