package com.nofp.browser;

import android.content.Context;
import android.content.SharedPreferences;

public class PrivacyConfig {

    public static final String[] TOGGLE_KEYS = {
        "fp_total", "canvas_random", "webgl_limit", "timezone_utc",
        "letterboxing", "font_limit", "ua_standard", "lang_standard",
        "etp_strict", "crypto_block", "fp_script_block", "social_block",
        "cookie_restrict", "webrtc_off", "geo_off", "sensor_off",
        "notification_off", "doh_on", "telemetry_off", "autofill_off"
    };

    public static final String[] TOGGLE_LABELS = {
        "反指纹总开关", "Canvas 随机化", "WebGL 限制", "时区标准化 (UTC)",
        "窗口尺寸标准化", "字体限制", "User-Agent 标准化", "语言偏好标准化",
        "严格追踪保护 (ETP)", "加密货币挖矿拦截", "指纹识别脚本拦截", "社交媒体追踪拦截",
        "Cookie 限制", "禁用 WebRTC", "禁用地理位置 API", "禁用传感器 API",
        "禁用 Notifications", "DNS over HTTPS", "禁用遥测", "禁用自动填充与密码保存"
    };

    public static String buildPrefsFile(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences("privacy", Context.MODE_PRIVATE);
        StringBuilder sb = new StringBuilder();

        if (sp.getBoolean("fp_total", true)) {
            sb.append("privacy.fingerprintingProtection=true\n");
            sb.append("privacy.fingerprintingProtection.pbmode=true\n");
            sb.append("privacy.fingerprintingProtection.overrides=+AllTargets\n");
        }
        if (sp.getBoolean("etp_strict", true)) {
            sb.append("privacy.trackingprotection.enabled=true\n");
            sb.append("privacy.trackingprotection.pbmode.enabled=true\n");
        }
        if (sp.getBoolean("crypto_block", true))
            sb.append("privacy.trackingprotection.cryptomining.enabled=true\n");
        if (sp.getBoolean("fp_script_block", true))
            sb.append("privacy.trackingprotection.fingerprinting.enabled=true\n");
        if (sp.getBoolean("social_block", true))
            sb.append("privacy.trackingprotection.socialtracking.enabled=true\n");
        if (sp.getBoolean("cookie_restrict", true)) {
            sb.append("network.cookie.cookieBehavior=5\n");
            sb.append("privacy.partition.network_state=true\n");
        }
        if (sp.getBoolean("webrtc_off", true)) {
            sb.append("media.peerconnection.enabled=false\n");
            sb.append("media.navigator.enabled=false\n");
        }
        if (sp.getBoolean("geo_off", true))
            sb.append("dom.geolocation.enabled=false\n");
        if (sp.getBoolean("sensor_off", true))
            sb.append("dom.sensors.enabled=false\n");
        if (sp.getBoolean("notification_off", true))
            sb.append("dom.webnotifications.enabled=false\n");
        if (sp.getBoolean("doh_on", true)) {
            sb.append("network.trr.mode=3\n");
            sb.append("network.trr.uri=https://dns.mozilla.org/dns/query\n");
        }
        if (sp.getBoolean("telemetry_off", true)) {
            sb.append("toolkit.telemetry.enabled=false\n");
            sb.append("datareporting.healthreport.uploadEnabled=false\n");
        }
        if (sp.getBoolean("autofill_off", true)) {
            sb.append("signon.autofillForms=false\n");
            sb.append("signon.rememberSignons=false\n");
            sb.append("extensions.formautofill.addresses.enabled=false\n");
            sb.append("extensions.formautofill.creditCards.enabled=false\n");
        }

        sb.append("network.dns.disablePrefetch=true\n");
        sb.append("network.prefetch-next=false\n");
        sb.append("network.predictor.enabled=false\n");
        sb.append("network.http.speculative-parallel-limit=0\n");
        sb.append("browser.urlbar.autocomplete.enabled=false\n");
        sb.append("dom.beacon.enabled=false\n");
        sb.append("dom.gamepad.enabled=false\n");
        sb.append("dom.storage_access.enabled=false\n");

        return sb.toString();
    }
}
