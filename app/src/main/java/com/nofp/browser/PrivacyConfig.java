package com.nofp.browser;

import android.content.Context;
import android.content.SharedPreferences;
import org.mozilla.geckoview.ContentBlocking;
import org.mozilla.geckoview.GeckoRuntimeSettings;
import java.util.*;

public class PrivacyConfig {

    // 20 个核心开关
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

    public static GeckoRuntimeSettings buildSettings(Context ctx) {
        GeckoRuntimeSettings.Builder b = new GeckoRuntimeSettings.Builder()
            .contentBlocking(new ContentBlocking.Settings.Builder()
                .enhancedTrackingProtectionLevel(ContentBlocking.EtpLevel.STRICT)
                .build());
        return b.build();
    }

    public static String buildPrefsFile(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences("privacy", Context.MODE_PRIVATE);
        StringBuilder sb = new StringBuilder();

        // ===== 反指纹 =====
        if (sp.getBoolean("fp_total", true)) {
            sb.append("privacy.fingerprintingProtection=true\n");
            sb.append("privacy.fingerprintingProtection.pbmode=true\n");
            sb.append("privacy.fingerprintingProtection.overrides=+AllTargets\n");
            sb.append("privacy.resistFingerprinting=true\n");
            sb.append("privacy.resistFingerprinting.pbmode=true\n");
        }
        if (sp.getBoolean("canvas_random", true)) {
            sb.append("privacy.resistFingerprinting.randomDataOnCanvasExtract=true\n");
            sb.append("privacy.resistFingerprinting.autoDeclineNoUserInputCanvasPrompts=true\n");
            sb.append("privacy.resistFingerprinting.randomization.daily_reset.enabled=true\n");
            sb.append("privacy.resistFingerprinting.randomization.canvas.use_siphash=true\n");
        }
        if (sp.getBoolean("webgl_limit", true)) {
            sb.append("webgl.disabled=true\n");
            sb.append("webgl.enable-debug-renderer-info=false\n");
        }
        if (sp.getBoolean("timezone_utc", true)) {
            sb.append("privacy.resistFingerprinting.reduceTimerPrecision=true\n");
            sb.append("privacy.resistFingerprinting.reduceTimerPrecision.microseconds=100000\n");
        }
        if (sp.getBoolean("letterboxing", true)) {
            sb.append("privacy.resistFingerprinting.letterboxing=true\n");
            sb.append("privacy.resistFingerprinting.letterboxing.dimensions=1000x1000,800x600,600x800,400x800,800x400\n");
        }
        if (sp.getBoolean("font_limit", true)) {
            sb.append("privacy.resistFingerprinting.target_video_res=480\n");
        }
        if (sp.getBoolean("ua_standard", true)) {
            sb.append("general.useragent.override=\n");
        }
        if (sp.getBoolean("lang_standard", true)) {
            sb.append("privacy.spoof_english=2\n");
        }

        // ===== 追踪保护 =====
        if (sp.getBoolean("etp_strict", true)) {
            sb.append("privacy.trackingprotection.enabled=true\n");
            sb.append("privacy.trackingprotection.pbmode.enabled=true\n");
            sb.append("privacy.trackingprotection.annotate_channels=true\n");
            sb.append("privacy.annotate_channels.strict_list.enabled=true\n");
            sb.append("privacy.trackingprotection.lower_network_priority=true\n");
        }
        if (sp.getBoolean("crypto_block", true))
            sb.append("privacy.trackingprotection.cryptomining.enabled=true\n");
        if (sp.getBoolean("fp_script_block", true))
            sb.append("privacy.trackingprotection.fingerprinting.enabled=true\n");
        if (sp.getBoolean("social_block", true))
            sb.append("privacy.trackingprotection.socialtracking.enabled=true\n");
        sb.append("privacy.trackingprotection.emailtracking.enabled=true\n");
        if (sp.getBoolean("cookie_restrict", true)) {
            sb.append("network.cookie.cookieBehavior=5\n");
            sb.append("network.cookie.cookieBehavior.pbmode=5\n");
            sb.append("privacy.partition.network_state=true\n");
            sb.append("privacy.partition.serviceWorkers=true\n");
            sb.append("privacy.partition.bloburl_per_agent_cluster=true\n");
            sb.append("privacy.restrict3rdpartystorage.enabled=true\n");
            sb.append("privacy.restrict3rdpartystorage.expiration=2592000\n");
            sb.append("privacy.firstparty.isolate=true\n");
            sb.append("privacy.firstparty.isolate.restrict_opener_access=true\n");
            sb.append("privacy.firstparty.isolate.use_site=true\n");
            sb.append("network.cookie.lifetimePolicy=2\n");
            sb.append("network.cookie.sameSite.laxByDefault=true\n");
            sb.append("network.cookie.sameSite.noneRequiresSecure=true\n");
            sb.append("network.cookie.sameSite.schemeful=true\n");
        }

        // ===== API 限制 =====
        if (sp.getBoolean("webrtc_off", true)) {
            sb.append("media.peerconnection.enabled=false\n");
            sb.append("media.peerconnection.ice.default_address_only=true\n");
            sb.append("media.peerconnection.ice.no_host=true\n");
            sb.append("media.peerconnection.ice.proxy_only_if_behind_proxy=true\n");
            sb.append("media.navigator.enabled=false\n");
            sb.append("media.navigator.video.enabled=false\n");
            sb.append("media.devices.insecure.enabled=false\n");
            sb.append("media.getusermedia.insecure.enabled=false\n");
            sb.append("media.getusermedia.screensharing.enabled=false\n");
        }
        if (sp.getBoolean("geo_off", true)) {
            sb.append("dom.geolocation.enabled=false\n");
            sb.append("geo.enabled=false\n");
            sb.append("geo.provider.network.url=\n");
        }
        if (sp.getBoolean("sensor_off", true)) {
            sb.append("dom.sensors.enabled=false\n");
            sb.append("device.sensors.enabled=false\n");
            sb.append("dom.gamepad.enabled=false\n");
            sb.append("dom.vibrator.enabled=false\n");
            sb.append("dom.battery.enabled=false\n");
            sb.append("dom.bluetooth.enabled=false\n");
            sb.append("dom.webusb.enabled=false\n");
            sb.append("dom.webmidi.enabled=false\n");
            sb.append("dom.webxr.enabled=false\n");
            sb.append("dom.vr.enabled=false\n");
            sb.append("dom.presentation.enabled=false\n");
            sb.append("dom.payments.request.enabled=false\n");
        }
        if (sp.getBoolean("notification_off", true)) {
            sb.append("dom.webnotifications.enabled=false\n");
            sb.append("dom.webnotifications.serviceworker.enabled=false\n");
            sb.append("dom.push.enabled=false\n");
            sb.append("dom.push.connection.enabled=false\n");
            sb.append("dom.beacon.enabled=false\n");
        }
        sb.append("media.audio_data.enabled=false\n");
        sb.append("dom.audiocontext.enabled=false\n");
        sb.append("media.webspeech.recognition.enable=false\n");
        sb.append("media.webspeech.synth.enabled=false\n");
        sb.append("dom.enable_performance_observer=false\n");
        sb.append("dom.performance.enable_user_timing_logging=false\n");
        sb.append("dom.netinfo.enabled=false\n");
        sb.append("dom.storage_access.enabled=false\n");
        sb.append("dom.storageManager.enabled=false\n");
        sb.append("dom.indexedDB.enabled=false\n");
        sb.append("dom.quotaManager.enabled=false\n");
        sb.append("dom.caches.enabled=false\n");
        sb.append("dom.cache.enabled=false\n");
        sb.append("dom.clipboard.enabled=false\n");
        sb.append("dom.clipboard.autocopy.enabled=false\n");
        sb.append("dom.mozTCPSocket.enabled=false\n");
        sb.append("dom.udpsocket.enabled=false\n");

        // ===== 网络隐私 =====
        if (sp.getBoolean("doh_on", true)) {
            sb.append("network.trr.mode=3\n");
            sb.append("network.trr.uri=https://dns.mozilla.org/dns/query\n");
            sb.append("network.trr.default_provider_uri=https://dns.mozilla.org/dns/query\n");
            sb.append("network.dns.echconfig.enabled=true\n");
            sb.append("network.dns.use_https_rr_as_altsvc=true\n");
            sb.append("network.dns.http3_echconfig.enabled=true\n");
        }
        sb.append("network.dns.disablePrefetch=true\n");
        sb.append("network.dns.disablePrefetchFromHTTPS=true\n");
        sb.append("network.prefetch-next=false\n");
        sb.append("network.predictor.enabled=false\n");
        sb.append("network.predictor.enable-prefetch=false\n");
        sb.append("network.preconnect=false\n");
        sb.append("network.http.speculative-parallel-limit=0\n");
        sb.append("browser.urlbar.speculativeConnect.enabled=false\n");
        sb.append("dom.speculation_rules.enabled=false\n");
        sb.append("browser.places.speculativeConnect.enabled=false\n");
        sb.append("network.http.sendRefererHeader=2\n");
        sb.append("network.http.referer.XOriginPolicy=2\n");
        sb.append("network.http.referer.XOriginTrimmingPolicy=2\n");
        sb.append("network.http.referer.trimmingPolicy=2\n");
        sb.append("network.http.referer.spoofSource=false\n");
        sb.append("network.http.referer.defaultPolicy=2\n");
        sb.append("network.http.referer.defaultPolicy.trackers=2\n");
        sb.append("network.http.altsvc.enabled=false\n");
        sb.append("network.http.altsvc.oe=false\n");
        sb.append("network.http.windows-sso.enabled=false\n");
        sb.append("network.negotiate-auth.allow-proxies=false\n");
        sb.append("network.automatic-ntlm-auth.allow-proxies=false\n");
        sb.append("network.auth.subresource-http-auth-allow=1\n");
        sb.append("network.websocket.allowInsecureFromHTTPS=false\n");

        // ===== 遥测全关 =====
        if (sp.getBoolean("telemetry_off", true)) {
            sb.append("toolkit.telemetry.enabled=false\n");
            sb.append("toolkit.telemetry.unified=false\n");
            sb.append("toolkit.telemetry.archive.enabled=false\n");
            sb.append("toolkit.telemetry.server=data:,\n");
            sb.append("toolkit.telemetry.shutdownPingSender.enabled=false\n");
            sb.append("toolkit.telemetry.updatePing.enabled=false\n");
            sb.append("toolkit.telemetry.bhrPing.enabled=false\n");
            sb.append("toolkit.telemetry.newProfilePing.enabled=false\n");
            sb.append("toolkit.telemetry.firstShutdownPing.enabled=false\n");
            sb.append("toolkit.telemetry.eventping.enabled=false\n");
            sb.append("toolkit.telemetry.ecosystemtelemetry.enabled=false\n");
            sb.append("toolkit.telemetry.coverage.opt-out=true\n");
            sb.append("datareporting.healthreport.uploadEnabled=false\n");
            sb.append("datareporting.policy.dataSubmissionEnabled=false\n");
            sb.append("datareporting.policy.dataSubmissionPolicyBypassNotification=true\n");
            sb.append("browser.ping-centre.telemetry=false\n");
            sb.append("browser.newtabpage.activity-stream.feeds.telemetry=false\n");
            sb.append("browser.newtabpage.activity-stream.telemetry=false\n");
        }

        // ===== 崩溃报告 =====
        sb.append("browser.crashReports.unsubmittedCheck.enabled=false\n");
        sb.append("browser.crashReports.unsubmittedCheck.autoSubmit2=false\n");
        sb.append("breakpad.reportURL=\n");
        sb.append("browser.tabs.crashReporting.sendReport=false\n");
        sb.append("browser.tabs.crashReporting.includeURL=false\n");

        // ===== Safe Browsing =====
        sb.append("browser.safebrowsing.malware.enabled=false\n");
        sb.append("browser.safebrowsing.phishing.enabled=false\n");
        sb.append("browser.safebrowsing.downloads.enabled=false\n");
        sb.append("browser.safebrowsing.downloads.remote.enabled=false\n");
        sb.append("browser.safebrowsing.downloads.remote.url=\n");
        sb.append("browser.safebrowsing.globalCache.enabled=false\n");

        // ===== 表单与密码 =====
        if (sp.getBoolean("autofill_off", true)) {
            sb.append("signon.autofillForms=false\n");
            sb.append("signon.autofillForms.autocompleteOff=false\n");
            sb.append("signon.autofillForms.http=false\n");
            sb.append("signon.rememberSignons=false\n");
            sb.append("signon.formlessCapture.enabled=false\n");
            sb.append("signon.generation.enabled=false\n");
            sb.append("signon.management.page.breach-alerts.enabled=false\n");
            sb.append("signon.management.page.vulnerable-passwords.enabled=false\n");
            sb.append("extensions.formautofill.addresses.enabled=false\n");
            sb.append("extensions.formautofill.creditCards.enabled=false\n");
            sb.append("extensions.formautofill.heuristics.enabled=false\n");
        }
        sb.append("services.sync.enabled=false\n");
        sb.append("identity.fxaccounts.enabled=false\n");

        // ===== 权限默认拒绝 =====
        sb.append("permissions.default.image=1\n");
        sb.append("permissions.default.camera=2\n");
        sb.append("permissions.default.microphone=2\n");
        sb.append("permissions.default.geo=2\n");
        sb.append("permissions.default.desktop-notification=2\n");
        sb.append("permissions.default.xr=2\n");
        sb.append("permissions.delegation.enabled=false\n");
        sb.append("permissions.isolateBy.userContext=true\n");
        sb.append("permissions.isolateBy.privateBrowsing=true\n");
        sb.append("media.autoplay.default=5\n");
        sb.append("media.autoplay.blocking_policy=2\n");
        sb.append("media.autoplay.allow-muted=false\n");
        sb.append("media.autoplay.block-webaudio=true\n");
        sb.append("media.autoplay.block-event.enabled=true\n");

        // ===== 地址栏建议全关 =====
        sb.append("browser.urlbar.autocomplete.enabled=false\n");
        sb.append("browser.urlbar.suggest.history=false\n");
        sb.append("browser.urlbar.suggest.bookmark=false\n");
        sb.append("browser.urlbar.suggest.openpage=false\n");
        sb.append("browser.urlbar.suggest.searches=false\n");
        sb.append("browser.urlbar.suggest.topsites=false\n");
        sb.append("browser.urlbar.suggest.engines=false\n");
        sb.append("browser.urlbar.suggest.calculator=false\n");
        sb.append("browser.urlbar.suggest.quicksuggest.all=false\n");
        sb.append("browser.urlbar.suggest.quicksuggest.nonsponsored=false\n");
        sb.append("browser.urlbar.suggest.quicksuggest.sponsored=false\n");
        sb.append("browser.urlbar.suggest.trending=false\n");
        sb.append("browser.urlbar.suggest.recentsearches=false\n");
        sb.append("browser.urlbar.quicksuggest.enabled=false\n");
        sb.append("browser.urlbar.merino.enabled=false\n");
        sb.append("browser.urlbar.quicksuggest.dataCollection.enabled=false\n");

        // ===== 其他扩展 =====
        sb.append("extensions.pocket.enabled=false\n");
        sb.append("extensions.screenshots.disabled=true\n");
        sb.append("extensions.screenshots.upload-disabled=true\n");
        sb.append("extensions.webcompat-reporter.enabled=false\n");
        sb.append("devtools.debugger.remote-enabled=false\n");
        sb.append("devtools.debugger.force-local=true\n");
        sb.append("devtools.chrome.enabled=false\n");
        sb.append("extensions.getAddons.cache.enabled=false\n");
        sb.append("browser.newtabpage.activity-stream.showSponsored=false\n");
        sb.append("browser.newtabpage.activity-stream.showSponsoredTopSites=false\n");
        sb.append("browser.newtabpage.activity-stream.feeds.snippets=false\n");
        sb.append("browser.sessionstore.privacy_level=2\n");
        sb.append("browser.sessionstore.resume_from_crash=false\n");
        sb.append("privacy.donottrackheader.enabled=true\n");
        sb.append("privacy.globalprivacycontrol.enabled=true\n");
        sb.append("privacy.globalprivacycontrol.functionality.enabled=true\n");
        sb.append("privacy.cleanup.cache=true\n");
        sb.append("privacy.clearOnShutdown.cookies=true\n");

        return sb.toString();
    }

    // ============ 300 项高级设置 ============
    // 格式：分类|键名|中文名|中文说明|默认值|类型(bool/int/string)
    public static final String[][] ADVANCED = {
        // ---------- 反指纹 ----------
        {"反指纹","privacy.resistFingerprinting","反指纹总开关","伪装时区、字体、窗口尺寸，可能影响部分网站","true","bool"},
        {"反指纹","privacy.resistFingerprinting.pbmode","隐私模式反指纹","隐私模式下启用反指纹","true","bool"},
        {"反指纹","privacy.resistFingerprinting.letterboxing","窗口尺寸标准化","固定视口尺寸，防止通过窗口大小识别","true","bool"},
        {"反指纹","privacy.resistFingerprinting.letterboxing.dimensions","标准化尺寸列表","允许的窗口尺寸，逗号分隔","1000x1000,800x600,600x800,400x800,800x400","string"},
        {"反指纹","privacy.resistFingerprinting.autoDeclineNoUserInputCanvasPrompts","自动拒绝 Canvas 提取","无用户交互时自动拒绝 Canvas 数据读取","true","bool"},
        {"反指纹","privacy.resistFingerprinting.randomDataOnCanvasExtract","Canvas 随机化","提取 Canvas 数据时加入随机噪声","true","bool"},
        {"反指纹","privacy.resistFingerprinting.randomization.daily_reset.enabled","每日重置随机种子","每天重置随机数种子","true","bool"},
        {"反指纹","privacy.resistFingerprinting.randomization.canvas.use_siphash","Canvas 使用 SipHash","Canvas 随机化算法使用 SipHash","true","bool"},
        {"反指纹","privacy.resistFingerprinting.target_video_res","视频分辨率标准化","伪装视频分辨率为指定值","480","int"},
        {"反指纹","privacy.resistFingerprinting.reduceTimerPrecision","降低时间精度","降低 JS 时间精度，防止计时攻击","true","bool"},
        {"反指纹","privacy.resistFingerprinting.reduceTimerPrecision.microseconds","时间精度微秒","时间精度降低到多少微秒","100000","int"},
        {"反指纹","privacy.spoof_english","伪装英文偏好","2=总是伪装成英文","2","int"},
        {"反指纹","privacy.window.maxInnerWidth","窗口最大宽度","窗口最大内部宽度","1000","int"},
        {"反指纹","privacy.window.maxInnerHeight","窗口最大高度","窗口最大内部高度","1000","int"},
        {"反指纹","privacy.screen.maxWidth","屏幕最大宽度","伪装屏幕最大宽度","1920","int"},
        {"反指纹","privacy.screen.maxHeight","屏幕最大高度","伪装屏幕最大高度","1080","int"},
        {"反指纹","webgl.disabled","禁用 WebGL","关闭 WebGL，防止显卡指纹","true","bool"},
        {"反指纹","webgl.enable-debug-renderer-info","WebGL 调试信息","暴露显卡渲染器信息","false","bool"},
        {"反指纹","media.audio_data.enabled","AudioContext 数据","允许网页读取音频数据","false","bool"},
        {"反指纹","dom.audiocontext.enabled","AudioContext API","启用 AudioContext","false","bool"},
        {"反指纹","media.getusermedia.audiocapture.enabled","音频捕获","启用音频捕获","false","bool"},
        {"反指纹","media.webspeech.recognition.enable","Web Speech 识别","启用语音识别","false","bool"},
        {"反指纹","media.webspeech.synth.enabled","Web Speech 合成","启用语音合成","false","bool"},
        {"反指纹","dom.enable_performance_observer","Performance Observer","启用性能观察 API","false","bool"},
        {"反指纹","dom.performance.enable_user_timing_logging","用户计时日志","允许记录用户计时","false","bool"},
        {"反指纹","dom.battery.enabled","电池 API","允许网页读取电池状态","false","bool"},
        {"反指纹","dom.netinfo.enabled","网络信息 API","允许网页读取网络类型","false","bool"},
        {"反指纹","device.sensors.enabled","设备传感器","启用加速度计、陀螺仪","false","bool"},
        {"反指纹","dom.sensors.enabled","传感器 API","启用传感器 API","false","bool"},
        {"反指纹","dom.gamepad.enabled","Gamepad API","允许网页访问手柄","false","bool"},
        {"反指纹","dom.keyboardevent.dispatch_during_composition","组合键事件","在输入法组合期间派发键盘事件","false","bool"},
        {"反指纹","dom.keyboardevent.keypress.dispatch_non_printable","非打印键事件","派发非打印键的 keypress","false","bool"},

        // ---------- 追踪保护 ----------
        {"追踪保护","privacy.trackingprotection.enabled","追踪保护总开关","拦截已知跟踪器","true","bool"},
        {"追踪保护","privacy.trackingprotection.pbmode.enabled","隐私模式追踪保护","隐私模式下的追踪保护","true","bool"},
        {"追踪保护","privacy.trackingprotection.cryptomining.enabled","加密货币挖矿拦截","拦截挖矿脚本","true","bool"},
        {"追踪保护","privacy.trackingprotection.fingerprinting.enabled","指纹脚本拦截","拦截已知指纹识别脚本","true","bool"},
        {"追踪保护","privacy.trackingprotection.socialtracking.enabled","社交媒体追踪拦截","拦截社交平台追踪器","true","bool"},
        {"追踪保护","privacy.trackingprotection.emailtracking.enabled","邮件追踪拦截","拦截邮件中的追踪像素","true","bool"},
        {"追踪保护","privacy.trackingprotection.annotate_channels","标注追踪通道","标记追踪请求","true","bool"},
        {"追踪保护","privacy.annotate_channels.strict_list.enabled","严格列表标注","使用严格列表标注","true","bool"},
        {"追踪保护","privacy.trackingprotection.lower_network_priority","降低追踪请求优先级","降低追踪请求网络优先级","true","bool"},
        {"追踪保护","privacy.trackingprotection.testing.report_blocked_node","报告拦截节点","用于测试，生产环境关闭","false","bool"},
        {"追踪保护","privacy.restrict3rdpartystorage.enabled","限制第三方存储","限制第三方 Cookie 和存储","true","bool"},
        {"追踪保护","privacy.restrict3rdpartystorage.expiration","第三方存储过期秒数","超过此时间自动清理","2592000","int"},
        {"追踪保护","privacy.storagePrincipal.enabledForTrackers","追踪器存储主元","为追踪器启用存储主元","true","bool"},
        {"追踪保护","privacy.partition.network_state","网络状态分区","按站点隔离网络状态","true","bool"},
        {"追踪保护","privacy.partition.serviceWorkers","Service Worker 分区","按站点隔离 SW","true","bool"},
        {"追踪保护","privacy.partition.bloburl_per_agent_cluster","Blob URL 分区","按代理集群隔离 Blob","true","bool"},
        {"追踪保护","network.cookie.cookieBehavior","Cookie 策略","0=全接受 1=仅第一方 2=全拒绝 5=仅第一方+隔离","5","int"},
        {"追踪保护","network.cookie.cookieBehavior.pbmode","隐私模式 Cookie 策略","隐私模式下的 Cookie 策略","5","int"},
        {"追踪保护","network.cookie.lifetimePolicy","Cookie 生命周期","0=正常 2=仅会话 3=固定天数","2","int"},
        {"追踪保护","network.cookie.thirdparty.sessionOnly","第三方仅会话","第三方 Cookie 仅当前会话","true","bool"},
        {"追踪保护","network.cookie.thirdparty.nonsecureSessionOnly","非安全第三方仅会话","非 HTTPS 第三方 Cookie 仅会话","true","bool"},
        {"追踪保护","privacy.firstparty.isolate","第一方隔离","每个第一方站点独立 Cookie 罐","true","bool"},
        {"追踪保护","privacy.firstparty.isolate.restrict_opener_access","限制 opener 访问","限制 window.opener 访问","true","bool"},
        {"追踪保护","privacy.firstparty.isolate.use_site","使用站点隔离","使用 eTLD+1 隔离","true","bool"},
        {"追踪保护","privacy.cleanup.cache","退出清理缓存","退出浏览器时清理缓存","true","bool"},
        {"追踪保护","privacy.clearOnShutdown.cookies","退出清 Cookie","退出时清除 Cookie","true","bool"},

        // ---------- API 限制 ----------
        {"API 限制","media.peerconnection.enabled","WebRTC 总开关","关闭可防止真实 IP 泄露","false","bool"},
        {"API 限制","media.peerconnection.ice.default_address_only","WebRTC 仅默认地址","只暴露默认地址","true","bool"},
        {"API 限制","media.peerconnection.ice.no_host","WebRTC 排除主机地址","不暴露本地 IP","true","bool"},
        {"API 限制","media.peerconnection.ice.proxy_only_if_behind_proxy","代理后仅走代理","检测到代理时只走代理","true","bool"},
        {"API 限制","media.navigator.enabled","媒体设备枚举","允许网页枚举麦克风/摄像头","false","bool"},
        {"API 限制","media.navigator.video.enabled","视频设备","启用视频设备访问","false","bool"},
        {"API 限制","media.navigator.streams.fake","假媒体流","返回假的媒体流","false","bool"},
        {"API 限制","media.devices.insecure.enabled","非安全设备访问","HTTP 页面访问设备","false","bool"},
        {"API 限制","media.getusermedia.insecure.enabled","非安全 getUserMedia","HTTP 页面获取媒体","false","bool"},
        {"API 限制","media.getusermedia.screensharing.enabled","屏幕共享","允许网页共享屏幕","false","bool"},
        {"API 限制","dom.geolocation.enabled","地理位置 API","允许网页获取位置","false","bool"},
        {"API 限制","geo.enabled","地理定位","启用地理定位","false","bool"},
        {"API 限制","geo.provider.network.url","地理定位服务 URL","网络定位服务地址","","string"},
        {"API 限制","dom.webnotifications.enabled","网页通知","允许网页显示通知","false","bool"},
        {"API 限制","dom.webnotifications.serviceworker.enabled","SW 通知","允许 SW 显示通知","false","bool"},
        {"API 限制","dom.push.enabled","Push API","允许网页推送","false","bool"},
        {"API 限制","dom.push.connection.enabled","Push 连接","启用 Push 长连接","false","bool"},
        {"API 限制","dom.beacon.enabled","Beacon API","允许后台数据上报","false","bool"},
        {"API 限制","beacon.enabled","Beacon","启用 Beacon","false","bool"},
        {"API 限制","dom.vibrator.enabled","震动 API","允许网页震动设备","false","bool"},
        {"API 限制","dom.battery.enabled","电池 API","允许读取电池状态","false","bool"},
        {"API 限制","dom.bluetooth.enabled","蓝牙 API","允许网页访问蓝牙","false","bool"},
        {"API 限制","dom.webusb.enabled","WebUSB","允许网页访问 USB","false","bool"},
        {"API 限制","dom.webmidi.enabled","Web MIDI","允许网页访问 MIDI","false","bool"},
        {"API 限制","dom.webxr.enabled","WebXR","允许 VR/AR 访问","false","bool"},
        {"API 限制","dom.vr.enabled","VR API","启用 VR","false","bool"},
        {"API 限制","dom.presentation.enabled","Presentation API","允许投屏","false","bool"},
        {"API 限制","dom.payments.request.enabled","Payment Request","允许支付请求","false","bool"},
        {"API 限制","dom.serviceWorkers.enabled","Service Worker","允许注册 SW","false","bool"},
        {"API 限制","dom.workers.sharedWorkers.enabled","Shared Worker","允许 Shared Worker","false","bool"},
        {"API 限制","dom.workers.serialized-sab-access","序列化 SAB","允许序列化 SharedArrayBuffer","false","bool"},
        {"API 限制","dom.storage_access.enabled","Storage Access API","允许请求存储访问","false","bool"},
        {"API 限制","dom.storageManager.enabled","Storage Manager","允许查询存储配额","false","bool"},
        {"API 限制","dom.indexedDB.enabled","IndexedDB","允许使用 IndexedDB","false","bool"},
        {"API 限制","dom.quotaManager.enabled","Quota Manager","允许查询配额","false","bool"},
        {"API 限制","dom.caches.enabled","Cache API","允许使用 Cache API","false","bool"},
        {"API 限制","dom.cache.enabled","Cache","启用缓存","false","bool"},
        {"API 限制","dom.clipboard.enabled","剪贴板 API","允许读写剪贴板","false","bool"},
        {"API 限制","dom.clipboard.autocopy.enabled","自动复制","允许网页自动复制","false","bool"},
        {"API 限制","dom.events.testing.asyncClipboard","异步剪贴板测试","测试用途，生产关闭","false","bool"},
        {"API 限制","dom.mozTCPSocket.enabled","TCP Socket","允许原始 TCP 连接","false","bool"},
        {"API 限制","dom.udpsocket.enabled","UDP Socket","允许 UDP 连接","false","bool"},

        // ---------- 网络隐私 ----------
        {"网络隐私","network.trr.mode","DoH 模式","0=禁用 2=优先 3=强制 5=代理模式","3","int"},
        {"网络隐私","network.trr.uri","DoH 服务器","加密 DNS 查询地址","https://dns.mozilla.org/dns/query","string"},
        {"网络隐私","network.trr.default_provider_uri","默认 DoH","默认加密 DNS","https://dns.mozilla.org/dns/query","string"},
        {"网络隐私","network.trr.bootstrapAddr","DoH 引导地址","DoH 服务器 IP","","string"},
        {"网络隐私","network.trr.excluded-domains","DoH 排除域名","不走 DoH 的域名","","string"},
        {"网络隐私","network.dns.echconfig.enabled","ECH 加密","加密 Client Hello，隐藏 SNI","true","bool"},
        {"网络隐私","network.dns.use_https_rr_as_altsvc","HTTPS RR","使用 HTTPS 记录作为 Alt-Svc","true","bool"},
        {"网络隐私","network.dns.http3_echconfig.enabled","HTTP/3 ECH","HTTP/3 下的 ECH","true","bool"},
        {"网络隐私","network.dns.disablePrefetch","禁用 DNS 预取","不提前解析域名","true","bool"},
        {"网络隐私","network.dns.disablePrefetchFromHTTPS","HTTPS 页面禁用预取","HTTPS 下也不预取","true","bool"},
        {"网络隐私","network.prefetch-next","页面预取","不预取下一页","false","bool"},
        {"网络隐私","network.predictor.enabled","网络预测","关闭网络行为预测","false","bool"},
        {"网络隐私","network.predictor.enable-prefetch","预测预取","关闭预测预取","false","bool"},
        {"网络隐私","network.preconnect","预连接","关闭预连接","false","bool"},
        {"网络隐私","network.http.speculative-parallel-limit","并行连接限制","0=不允许推测连接","0","int"},
        {"网络隐私","network.http.max-persistent-connections-per-server","每服务器持久连接","单服务器最大持久连接","6","int"},
        {"网络隐私","network.http.max-connections","最大连接数","全局最大连接数","256","int"},
        {"网络隐私","network.websocket.max-connections","WebSocket 最大连接","单页面最大 WS 连接","200","int"},
        {"网络隐私","network.websocket.allowInsecureFromHTTPS","允许不安全 WS","HTTPS 页面用非加密 WS","false","bool"},
        {"网络隐私","network.http.sendRefererHeader","Referer 头","0=不发 1=仅点击 2=全发","2","int"},
        {"网络隐私","network.http.referer.XOriginPolicy","跨域 Referer 策略","0=总发 1=同源 2=严格","2","int"},
        {"网络隐私","network.http.referer.XOriginTrimmingPolicy","跨域 Referer 截断","0=不截 1=仅主机 2=严格","2","int"},
        {"网络隐私","network.http.referer.trimmingPolicy","Referer 截断策略","0=不截 1=仅主机 2=严格","2","int"},
        {"网络隐私","network.http.referer.spoofSource","伪装 Referer 来源","用目标 URL 伪装来源","false","bool"},
        {"网络隐私","network.http.referer.defaultPolicy","默认 Referer 策略","0=总发 1=同源 2=严格","2","int"},
        {"网络隐私","network.http.referer.defaultPolicy.trackers","追踪器 Referer 策略","针对追踪器的策略","2","int"},
        {"网络隐私","network.http.sendOriginHeader","Origin 头","0=不发 1=跨域发","1","int"},
        {"网络隐私","network.http.altsvc.enabled","Alt-Svc","允许备用服务","false","bool"},
        {"网络隐私","network.http.altsvc.oe","Alt-Svc 机会加密","允许机会加密","false","bool"},
        {"网络隐私","network.http.windows-sso.enabled","Windows SSO","Windows 单点登录","false","bool"},
        {"网络隐私","network.negotiate-auth.allow-proxies","Negotiate 代理","允许代理 Negotiate","false","bool"},
        {"网络隐私","network.negotiate-auth.delegation-uris","Negotiate 委托","委托的 URI 列表","","string"},
        {"网络隐私","network.automatic-ntlm-auth.allow-proxies","NTLM 代理","允许代理 NTLM","false","bool"},
        {"网络隐私","network.auth.subresource-http-auth-allow","子资源认证","0=拒绝 1=同源 2=第三方","1","int"},

        // ---------- 遥测 ----------
        {"遥测","toolkit.telemetry.enabled","遥测总开关","禁用所有遥测","false","bool"},
        {"遥测","toolkit.telemetry.unified","统一遥测","统一遥测开关","false","bool"},
        {"遥测","toolkit.telemetry.archive.enabled","遥测归档","保存遥测数据到本地","false","bool"},
        {"遥测","toolkit.telemetry.server","遥测服务器","设为 data:, 禁用上报","data:,","string"},
        {"遥测","toolkit.telemetry.shutdownPingSender.enabled","关闭 Ping","退出时发送 Ping","false","bool"},
        {"遥测","toolkit.telemetry.updatePing.enabled","更新 Ping","更新时发送 Ping","false","bool"},
        {"遥测","toolkit.telemetry.bhrPing.enabled","BHR Ping","后台卡顿 Ping","false","bool"},
        {"遥测","toolkit.telemetry.newProfilePing.enabled","新配置 Ping","新建配置时 Ping","false","bool"},
        {"遥测","toolkit.telemetry.firstShutdownPing.enabled","首次关闭 Ping","首次关闭时 Ping","false","bool"},
        {"遥测","toolkit.telemetry.eventping.enabled","事件 Ping","事件 Ping","false","bool"},
        {"遥测","toolkit.telemetry.ecosystemtelemetry.enabled","生态遥测","生态遥测","false","bool"},
        {"遥测","toolkit.telemetry.coverage.opt-out","覆盖率遥测退出","退出覆盖率采集","true","bool"},
        {"遥测","datareporting.healthreport.uploadEnabled","健康报告上传","上传健康报告","false","bool"},
        {"遥测","datareporting.policy.dataSubmissionEnabled","数据提交","提交数据","false","bool"},
        {"遥测","datareporting.policy.dataSubmissionPolicyBypassNotification","数据策略通知","跳过策略通知","true","bool"},
        {"遥测","browser.ping-centre.telemetry","Ping Centre","Ping Centre 遥测","false","bool"},
        {"遥测","browser.newtabpage.activity-stream.feeds.telemetry","新标签遥测","新标签页遥测","false","bool"},
        {"遥测","browser.newtabpage.activity-stream.telemetry","新标签遥测 2","新标签页遥测总开关","false","bool"},
        {"遥测","browser.newtabpage.activity-stream.telemetry.structuredIngestion.endpoint","结构化采集端点","遥测上报端点","","string"},
        {"遥测","toolkit.telemetry.dap_enabled","DAP 遥测","隐私聚合遥测","false","bool"},

        // ---------- 崩溃 ----------
        {"崩溃","browser.crashReports.unsubmittedCheck.enabled","崩溃报告","检查未提交的崩溃","false","bool"},
        {"崩溃","browser.crashReports.unsubmittedCheck.autoSubmit2","自动提交崩溃","自动提交崩溃报告","false","bool"},
        {"崩溃","browser.crashReports.unsubmittedCheck.autoSubmit","自动提交（旧）","旧版自动提交","false","bool"},
        {"崩溃","breakpad.reportURL","Breakpad URL","崩溃报告上传地址，留空禁用","","string"},
        {"崩溃","browser.tabs.crashReporting.sendReport","Tab 崩溃报告","发送 Tab 崩溃","false","bool"},
        {"崩溃","browser.tabs.crashReporting.includeURL","包含 URL","崩溃报告包含 URL","false","bool"},
        {"崩溃","devtools.debugger.remote-enabled","远程调试","允许远程调试","false","bool"},
        {"崩溃","devtools.debugger.force-local","强制本地调试","只允许本地调试","true","bool"},
        {"崩溃","devtools.chrome.enabled","Chrome 调试","浏览器 chrome 调试","false","bool"},
        {"崩溃","devtools.console.stdout.content","控制台 stdout","输出到 stdout","false","bool"},
        {"崩溃","devtools.selfxss.count","自 XSS 计数","用于开发，生产关","0","int"},
        {"崩溃","extensions.webcompat-reporter.enabled","兼容性报告","向 Mozilla 报告兼容性问题","false","bool"},

        // ---------- Cookie 与存储 ----------
        {"Cookie","network.cookie.alwaysAcceptSessionCookies","接受会话 Cookie","总是接受会话 Cookie","false","bool"},
        {"Cookie","network.cookie.sameSite.laxByDefault","SameSite Lax 默认","默认 SameSite=Lax","true","bool"},
        {"Cookie","network.cookie.sameSite.noneRequiresSecure","SameSite=None 需 Secure","None 必须配合 Secure","true","bool"},
        {"Cookie","network.cookie.sameSite.schemeful","SameSite 按 Scheme","不同 scheme 视为跨站","true","bool"},
        {"Cookie","network.cookie.sameSite.laxPlusPOST.timeout","Lax+POST 超时","Lax+POST 的兼容窗口","0","int"},
        {"Cookie","privacy.donottrackheader.enabled","DNT 头","发送 Do Not Track","true","bool"},
        {"Cookie","privacy.donottrackheader.value","DNT 值","1=不追踪","1","int"},
        {"Cookie","privacy.globalprivacycontrol.enabled","GPC","发送 Global Privacy Control","true","bool"},
        {"Cookie","privacy.globalprivacycontrol.functionality.enabled","GPC 功能","启用 GPC 功能","true","bool"},
        {"Cookie","dom.storage_access.max_concurrent_auto_grants","并发自动授予上限","同时自动授予的存储访问上限","5","int"},
        {"Cookie","dom.storage_access.auto_grants.delayed","延迟自动授予","延迟授予存储访问","true","bool"},
        {"Cookie","dom.storage_access.auto_grants","自动授予存储访问","自动授予存储访问","true","bool"},
        {"Cookie","network.cookie.maxPerHost","每主机最大 Cookie","单主机 Cookie 上限","180","int"},
        {"Cookie","network.cookie.maxNumber","Cookie 总数上限","全局 Cookie 上限","3000","int"},
        {"Cookie","browser.cache.disk.enable","磁盘缓存","启用磁盘缓存","false","bool"},

        // ---------- 表单与密码 ----------
        {"表单","signon.autofillForms","自动填充","自动填充表单","false","bool"},
        {"表单","signon.autofillForms.autocompleteOff","autocomplete 关闭时填充","即使网页关闭 autocomplete 也填充","false","bool"},
        {"表单","signon.autofillForms.http","HTTP 页面填充","HTTP 页面也填充","false","bool"},
        {"表单","signon.rememberSignons","保存密码","保存网站密码","false","bool"},
        {"表单","signon.formlessCapture.enabled","无表单捕获","捕获无表单的登录","false","bool"},
        {"表单","signon.generation.enabled","密码生成","生成强密码","false","bool"},
        {"表单","signon.management.page.breach-alerts.enabled","泄露告警","显示密码泄露告警","false","bool"},
        {"表单","signon.management.page.vulnerable-passwords.enabled","弱密码提示","提示弱密码","false","bool"},
        {"表单","signon.management.page.fileImport.enabled","文件导入","允许从文件导入密码","false","bool"},
        {"表单","signon.showAutoCompleteFooter","自动完成页脚","显示自动完成页脚","false","bool"},
        {"表单","extensions.formautofill.addresses.enabled","地址填充","自动填充地址","false","bool"},
        {"表单","extensions.formautofill.creditCards.enabled","信用卡填充","自动填充信用卡","false","bool"},
        {"表单","extensions.formautofill.heuristics.enabled","启发式填充","使用启发式识别表单","false","bool"},
        {"表单","extensions.formautofill.creditCards.used","信用卡已用","使用过信用卡填充","0","int"},
        {"表单","services.sync.enabled","同步","启用 Firefox Sync","false","bool"},

        // ---------- 媒体与权限 ----------
        {"媒体","permissions.default.image","图片权限","1=允许 2=阻止","1","int"},
        {"媒体","permissions.default.camera","摄像头权限","0=询问 1=允许 2=拒绝","2","int"},
        {"媒体","permissions.default.microphone","麦克风权限","0=询问 1=允许 2=拒绝","2","int"},
        {"媒体","permissions.default.geo","地理位置权限","0=询问 1=允许 2=拒绝","2","int"},
        {"媒体","permissions.default.desktop-notification","桌面通知权限","0=询问 1=允许 2=拒绝","2","int"},
        {"媒体","permissions.default.xr","XR 权限","0=询问 1=允许 2=拒绝","2","int"},
        {"媒体","permissions.delegation.enabled","权限委托","允许委托权限","false","bool"},
        {"媒体","permissions.isolateBy.userContext","用户上下文隔离","按用户上下文隔离权限","true","bool"},
        {"媒体","permissions.isolateBy.privateBrowsing","隐私浏览隔离","按隐私浏览隔离权限","true","bool"},
        {"媒体","media.autoplay.default","自动播放","0=允许 5=阻止","5","int"},
        {"媒体","media.autoplay.blocking_policy","自动播放阻止策略","0=旧 1=用户交互 2=黏性交互","2","int"},
        {"媒体","media.autoplay.allow-muted","允许静音自动播放","允许静音视频自动播放","false","bool"},
        {"媒体","media.autoplay.block-webaudio","阻止 Web Audio 自动播放","阻止 Web Audio 自动播放","true","bool"},
        {"媒体","media.autoplay.block-event.enabled","阻止事件","发送阻止事件","true","bool"},
        {"媒体","media.block-autoplay-until-in-foreground","前台才自动播放","仅前台标签自动播放","true","bool"},
        {"媒体","media.hardware-video-decoding.enabled","硬件视频解码","启用硬件解码","true","bool"},
        {"媒体","media.mediasource.enabled","Media Source","启用 MSE","true","bool"},
        {"媒体","media.getusermedia.browser.enabled","浏览器媒体访问","浏览器自身访问媒体","false","bool"},
        {"媒体","media.getusermedia.audiocapture.enabled","音频捕获","音频捕获","false","bool"},
        {"媒体","media.webrtc.platformencoder","WebRTC 平台编码","使用平台编码","false","bool"},

        // ---------- 界面与体验 ----------
        {"界面","browser.urlbar.autocomplete.enabled","地址栏自动补全","地址栏自动补全","false","bool"},
        {"界面","browser.urlbar.suggest.history","历史建议","地址栏显示历史","false","bool"},
        {"界面","browser.urlbar.suggest.bookmark","书签建议","地址栏显示书签","false","bool"},
        {"界面","browser.urlbar.suggest.openpage","已打开页面建议","地址栏显示已打开标签","false","bool"},
        {"界面","browser.urlbar.suggest.searches","搜索建议","地址栏显示搜索建议","false","bool"},
        {"界面","browser.urlbar.suggest.topsites","热门站点建议","地址栏显示热门站点","false","bool"},
        {"界面","browser.urlbar.suggest.engines","引擎建议","地址栏显示搜索引擎","false","bool"},
        {"界面","browser.urlbar.suggest.calculator","计算器建议","地址栏显示计算结果","false","bool"},
        {"界面","browser.urlbar.suggest.quicksuggest.all","快速建议总开关","所有快速建议","false","bool"},
        {"界面","browser.urlbar.suggest.quicksuggest.nonsponsored","非赞助建议","非赞助快速建议","false","bool"},
        {"界面","browser.urlbar.suggest.quicksuggest.sponsored","赞助建议","赞助快速建议","false","bool"},
        {"界面","browser.urlbar.suggest.trending","趋势建议","趋势搜索","false","bool"},
        {"界面","browser.urlbar.suggest.recentsearches","最近搜索","最近搜索建议","false","bool"},
        {"界面","browser.urlbar.suggest.yelp","Yelp 建议","Yelp 建议","false","bool"},
        {"界面","browser.urlbar.suggest.mdn","MDN 建议","MDN 建议","false","bool"},
        {"界面","browser.urlbar.suggest.weather","天气建议","天气建议","false","bool"},
        {"界面","browser.urlbar.quicksuggest.enabled","快速建议","快速建议总开关","false","bool"},
        {"界面","browser.urlbar.merino.enabled","Merino 建议","Merino 建议服务","false","bool"},
        {"界面","browser.urlbar.quicksuggest.dataCollection.enabled","快速建议数据采集","采集快速建议数据","false","bool"},
        {"界面","browser.urlbar.suggest.searches.pbmode","隐私模式搜索建议","隐私模式的搜索建议","false","bool"},

        // ---------- 安全浏览 ----------
        {"安全浏览","browser.safebrowsing.malware.enabled","恶意软件检查","向 Google 发送 URL 检查","false","bool"},
        {"安全浏览","browser.safebrowsing.phishing.enabled","钓鱼检查","向 Google 发送钓鱼检查","false","bool"},
        {"安全浏览","browser.safebrowsing.downloads.enabled","下载检查","检查下载文件","false","bool"},
        {"安全浏览","browser.safebrowsing.downloads.remote.enabled","远程下载检查","远程检查下载","false","bool"},
        {"安全浏览","browser.safebrowsing.downloads.remote.url","远程检查 URL","远程检查地址","","string"},
        {"安全浏览","browser.safebrowsing.downloads.remote.block_potentially_unwanted","潜在不需要软件","阻止 PUP","false","bool"},
        {"安全浏览","browser.safebrowsing.downloads.remote.block_uncommon","罕见文件","阻止罕见文件","false","bool"},
        {"安全浏览","browser.safebrowsing.downloads.remote.timeout_ms","超时毫秒","检查超时","5000","int"},
        {"安全浏览","browser.safebrowsing.provider.google.updateURL","Google 更新 URL","Google 列表更新地址","","string"},
        {"安全浏览","browser.safebrowsing.provider.google.gethashURL","Google hash URL","Google hash 查询地址","","string"},
        {"安全浏览","browser.safebrowsing.provider.google4.updateURL","Google4 更新 URL","Google4 更新地址","","string"},
        {"安全浏览","browser.safebrowsing.provider.google4.gethashURL","Google4 hash URL","Google4 hash 地址","","string"},
        {"安全浏览","browser.safebrowsing.provider.mozilla.updateURL","Mozilla 更新 URL","Mozilla 更新地址","","string"},
        {"安全浏览","browser.safebrowsing.provider.mozilla.gethashURL","Mozilla hash URL","Mozilla hash 地址","","string"},
        {"安全浏览","browser.safebrowsing.globalCache.enabled","全局缓存","启用全局缓存","false","bool"},

        // ---------- 缓存与性能 ----------
        {"缓存","browser.cache.disk.capacity","磁盘缓存容量","KB","0","int"},
        {"缓存","browser.cache.memory.enable","内存缓存","启用内存缓存","false","bool"},
        {"缓存","browser.cache.offline.enable","离线缓存","启用离线缓存","false","bool"},
        {"缓存","browser.cache.disk.smart_size.enabled","智能容量","自动调整容量","false","bool"},
        {"缓存","browser.sessionstore.max_tabs_undo","撤销标签上限","可撤销的标签数","0","int"},
        {"缓存","browser.sessionstore.max_windows_undo","撤销窗口上限","可撤销的窗口数","0","int"},
        {"缓存","browser.sessionstore.resume_from_crash","崩溃恢复","崩溃后恢复会话","false","bool"},
        {"缓存","browser.sessionstore.privacy_level","会话隐私级别","0=全存 2=不存表单","2","int"},
        {"缓存","browser.sessionstore.privacy_level_deferred","延迟会话隐私","延迟会话隐私级别","2","int"},
        {"缓存","browser.history_expire_days","历史过期天数","历史保留天数","0","int"},
        {"缓存","browser.history_expire_days_min","最短历史保留","最少保留天数","0","int"},
        {"缓存","browser.privatebrowsing.autostart","自动隐私浏览","启动即隐私模式","false","bool"},

        // ---------- 扩展与其他 ----------
        {"扩展","extensions.enabledScopes","扩展启用范围","位掩码","0","int"},
        {"扩展","extensions.getAddons.cache.enabled","扩展缓存","缓存扩展信息","false","bool"},
        {"扩展","extensions.pocket.enabled","Pocket 集成","启用 Pocket","false","bool"},
        {"扩展","extensions.pocket.api","Pocket API","Pocket API 地址","","string"},
        {"扩展","extensions.screenshots.disabled","禁用截图","禁用截图扩展","true","bool"},
        {"扩展","extensions.screenshots.upload-disabled","禁用截图上传","禁用截图上传","true","bool"},
        {"扩展","extensions.webcompat-reporter.enabled","兼容性报告","向 Mozilla 报告","false","bool"},
        {"扩展","extensions.formautofill.addresses.enabled","地址填充扩展","地址填充","false","bool"},
        {"扩展","browser.newtabpage.enabled","新标签页","启用新标签页","false","bool"},
        {"扩展","browser.newtabpage.activity-stream.feeds.section.topstories","热门故事","新标签页显示热门故事","false","bool"},
        {"扩展","browser.newtabpage.activity-stream.feeds.topsites","热门站点","新标签页显示热门站点","false","bool"},
        {"扩展","browser.newtabpage.activity-stream.showSponsored","赞助内容","显示赞助内容","false","bool"},
        {"扩展","browser.newtabpage.activity-stream.showSponsoredTopSites","赞助热门站点","显示赞助热门站点","false","bool"},
        {"扩展","browser.newtabpage.activity-stream.feeds.snippets","摘要","显示 Mozilla 摘要","false","bool"},
        {"扩展","browser.newtabpage.activity-stream.telemetry","新标签遥测","新标签遥测","false","bool"},
        {"扩展","browser.newtabpage.activity-stream.feeds.system.topsites","系统热门站点","系统热门站点","false","bool"},
        {"扩展","browser.newtabpage.activity-stream.feeds.system.topstories","系统热门故事","系统热门故事","false","bool"},
        {"扩展","browser.newtabpage.activity-stream.discoverystream.enabled","发现流","启用发现流","false","bool"},
        {"扩展","browser.newtabpage.activity-stream.discoverystream.sponsored-collections.enabled","赞助合集","赞助合集","false","bool"},
        {"扩展","browser.newtabpage.activity-stream.feeds.section.highlights","亮点","亮点推荐","false","bool"}
    };

    public static String buildAdvancedOverrides(Context ctx) {
        SharedPreferences adv = ctx.getSharedPreferences("privacy_advanced", Context.MODE_PRIVATE);
        StringBuilder sb = new StringBuilder();
        for (String[] item : ADVANCED) {
            if (item.length < 6) continue;
            String key = item[1];
            String type = item[5];
            if (adv.contains(key)) {
                if (type.equals("bool")) {
                    sb.append(key).append("=").append(adv.getBoolean(key, false)).append("\n");
                } else {
                    sb.append(key).append("=").append(adv.getString(key, "")).append("\n");
                }
            }
        }
        return sb.toString();
    }

}
