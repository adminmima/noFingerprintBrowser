package com.nofp.browser;

import android.app.Activity;
import android.content.Context;

public class ThemeHelper {
    public static boolean isDark(Context ctx) {
        return ctx.getSharedPreferences("privacy", Context.MODE_PRIVATE)
            .getBoolean("dark_mode", true); // 默认深色
    }

    public static boolean isHistoryEnabled(Context ctx) {
        return ctx.getSharedPreferences("privacy", Context.MODE_PRIVATE)
            .getBoolean("history_enabled", false); // 默认不存历史
    }

    public static void apply(Activity a) {
        if (isDark(a)) {
            a.setTheme(android.R.style.Theme_Material_NoActionBar);
        } else {
            a.setTheme(android.R.style.Theme_Material_Light_NoActionBar);
        }
    }
}
