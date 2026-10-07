package com.nofp.browser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import org.mozilla.geckoview.*;
import java.util.*;

public class MainActivity extends Activity {
    private static GeckoRuntime runtime;
    private final List<GeckoSession> sessions = new ArrayList<>();
    private final List<String> tabTitles = new ArrayList<>();
    private final List<String> tabUrls = new ArrayList<>();
    private GeckoView geckoView;
    private LinearLayout tabBar;
    private EditText urlBar;
    private int currentTab = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.apply(this);
        super.onCreate(savedInstanceState);

        if (runtime == null) {
            runtime = GeckoRuntime.create(this, PrivacyConfig.buildSettings(this));
        }
        try {
            java.io.File f = new java.io.File(getFilesDir(), "gecko-prefs.txt");
            java.io.FileWriter fw = new java.io.FileWriter(f);
            fw.write(PrivacyConfig.buildPrefsFile(this));
            fw.close();
        } catch (Exception ignored) {}

        geckoView = new GeckoView(this);
        tabBar = new LinearLayout(this);
        tabBar.setOrientation(LinearLayout.HORIZONTAL);
        tabBar.setBackgroundColor(0xFF222222);

        urlBar = new EditText(this);
        urlBar.setHint("输入网址");
        urlBar.setSingleLine(true);

        Button back = btn("←", v -> { GeckoSession s = cur(); if (s != null) s.goBack(); });
        Button fwd  = btn("→", v -> { GeckoSession s = cur(); if (s != null) s.goForward(); });
        Button reload = btn("⟳", v -> { GeckoSession s = cur(); if (s != null) s.reload(); });
        Button go = btn("Go", v -> loadUrl(urlBar.getText().toString()));
        Button star = btn("★", v -> toggleBookmark());
        Button menu = btn("⋮", v -> showMenu());
        Button newTab = btn("+", v -> addTab(null));

        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setOrientation(LinearLayout.HORIZONTAL);
        toolbar.setGravity(Gravity.CENTER_VERTICAL);
        toolbar.setBackgroundColor(0xFF333333);
        toolbar.addView(back); toolbar.addView(fwd); toolbar.addView(reload);
        toolbar.addView(urlBar, new LinearLayout.LayoutParams(0, -2, 1));
        toolbar.addView(go); toolbar.addView(star); toolbar.addView(newTab); toolbar.addView(menu);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.addView(tabBar);
        root.addView(toolbar);
        root.addView(geckoView, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(root);

        addTab(null);

        // 从历史/收藏跳过来时带上 load_url
        String extraUrl = getIntent().getStringExtra("load_url");
        if (extraUrl != null && !extraUrl.isEmpty()) loadUrl(extraUrl);
    }

    private Button btn(String text, View.OnClickListener l) {
        Button b = new Button(this);
        b.setText(text);
        b.setOnClickListener(l);
        return b;
    }

    private GeckoSession cur() {
        return (currentTab >= 0 && currentTab < sessions.size())
            ? sessions.get(currentTab) : null;
    }

    private String curUrl() {
        return (currentTab >= 0 && currentTab < tabUrls.size())
            ? tabUrls.get(currentTab) : "";
    }

    private void addTab(String url) {
        GeckoSession session = new GeckoSession();
        session.open(runtime);

        session.setNavigationDelegate(new GeckoSession.NavigationDelegate() {
            @Override
            public GeckoResult<GeckoSession> onNewSession(GeckoSession s, String uri) {
                runOnUiThread(() -> addTab(uri));
                return null;
            }
            @Override
            public void onLocationChange(GeckoSession s, String u) {
                int idx = sessions.indexOf(s);
                if (idx >= 0) {
                    tabUrls.set(idx, u == null ? "" : u);
                    if (idx == currentTab) runOnUiThread(() -> urlBar.setText(u));
                    // 用 URL 变化来记历史
                    if (ThemeHelper.isHistoryEnabled(MainActivity.this)
                        && u != null && !u.isEmpty()
                        && (u.startsWith("http://") || u.startsWith("https://"))) {
                        String title = idx < tabTitles.size() ? tabTitles.get(idx) : u;
                        HistoryStorage.add(MainActivity.this, u, title, System.currentTimeMillis());
                    }
                }
            }
        });

        session.setContentDelegate(new GeckoSession.ContentDelegate() {
            @Override
            public void onTitleChange(GeckoSession s, String title) {
                int idx = sessions.indexOf(s);
                if (idx >= 0) {
                    tabTitles.set(idx, title == null ? "标签" : title);
                    runOnUiThread(() -> refreshTabBar());
                }
            }
        });

        sessions.add(session);
        tabTitles.add("新标签");
        tabUrls.add(url == null ? "" : url);
        if (url != null) session.loadUri(url);
        switchTab(sessions.size() - 1);
    }

    private void switchTab(int index) {
        currentTab = index;
        geckoView.setSession(sessions.get(index));
        urlBar.setText(tabUrls.get(index));
        refreshTabBar();
    }

    private void closeTab(int index) {
        if (sessions.size() <= 1) return;
        sessions.get(index).close();
        sessions.remove(index);
        tabTitles.remove(index);
        tabUrls.remove(index);
        if (currentTab >= sessions.size()) currentTab = sessions.size() - 1;
        switchTab(currentTab);
    }

    private void refreshTabBar() {
        tabBar.removeAllViews();
        for (int i = 0; i < sessions.size(); i++) {
            final int idx = i;
            Button t = new Button(this);
            String title = tabTitles.get(i);
            if (title.length() > 8) title = title.substring(0, 8) + "…";
            t.setText(title + " ×");
            t.setTextSize(11);
            t.setPadding(20, 0, 20, 0);
            if (i == currentTab) t.setBackgroundColor(0xFF555555);
            t.setOnClickListener(v -> switchTab(idx));
            t.setOnLongClickListener(v -> { closeTab(idx); return true; });
            tabBar.addView(t);
        }
    }

    private void loadUrl(String u) {
        if (u == null || u.trim().isEmpty()) return;
        if (!u.startsWith("http")) u = "https://" + u;
        GeckoSession s = cur();
        if (s != null) s.loadUri(u);
    }

    private void toggleBookmark() {
        String u = curUrl();
        if (u == null || u.isEmpty()) return;
        if (BookmarksStorage.isBookmarked(this, u)) {
            BookmarksStorage.remove(this, u);
            toast("已取消收藏");
        } else {
            BookmarksStorage.add(this, u, u);
            toast("已收藏");
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showMenu() {
        String[] items = {"新建标签", "关闭当前标签", "设置", "高级设置", "历史记录", "收藏夹", "退出"};
        new AlertDialog.Builder(this)
            .setTitle("菜单")
            .setItems(items, (d, which) -> {
                switch (which) {
                    case 0: addTab(null); break;
                    case 1: closeTab(currentTab); break;
                    case 2: startActivity(new Intent(this, SettingsActivity.class)); break;
                    case 3: startActivity(new Intent(this, AdvancedSettingsActivity.class)); break;
                    case 4: startActivity(new Intent(this, HistoryActivity.class)); break;
                    case 5: startActivity(new Intent(this, BookmarksActivity.class)); break;
                    case 6: finish(); break;
                }
            })
            .show();
    }
}
