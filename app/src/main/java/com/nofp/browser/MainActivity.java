package com.nofp.browser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import org.mozilla.geckoview.*;

public class MainActivity extends Activity {
    private static GeckoRuntime runtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (runtime == null) {
            String prefs = PrivacyConfig.buildPrefsFile(this);
            try {
                java.io.File f = new java.io.File(getFilesDir(), "gecko-prefs.txt");
                java.io.FileWriter fw = new java.io.FileWriter(f);
                fw.write(prefs);
                fw.close();
            } catch (Exception ignored) {}

            GeckoRuntimeSettings.Builder b = new GeckoRuntimeSettings.Builder()
                .configFilePath(new java.io.File(getFilesDir(), "gecko-prefs.txt").getAbsolutePath())
                .contentBlocking(new ContentBlocking.Settings.Builder()
                    .enhancedTrackingProtectionLevel(ContentBlocking.EtpLevel.STRICT)
                    .build());
            runtime = GeckoRuntime.create(this, b.build());
        }

        GeckoSession session = new GeckoSession();
        session.open(runtime);

        GeckoView view = new GeckoView(this);
        view.setSession(session);

        EditText urlBar = new EditText(this);
        urlBar.setHint("输入网址");
        urlBar.setSingleLine(true);

        Button go = new Button(this);
        go.setText("→");
        Button set = new Button(this);
        set.setText("设置");

        go.setOnClickListener(v -> {
            String u = urlBar.getText().toString().trim();
            if (!u.isEmpty())
                session.loadUri(u.startsWith("http") ? u : "https://" + u);
        });
        set.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.addView(urlBar, new LinearLayout.LayoutParams(0, -2, 1));
        bar.addView(go);
        bar.addView(set);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.addView(bar);
        root.addView(view, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(root);
    }
}
