package drivers;

import com.codeborne.selenide.WebDriverProvider;
import config.ConfigFactoryProvider;
import config.MobileConfig;
import helpers.ApkInstaller;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class AndroidMobileDriver implements WebDriverProvider {

    @Override
    public WebDriver createDriver(Capabilities capabilities) {
        MobileConfig cfg = ConfigFactoryProvider.mobile();

        if (cfg.reinstallApp()) {
            ApkInstaller.reinstall(cfg.udid(), cfg.appPackage(), cfg.app());
        }

        String appActivity = resolveAppActivity(cfg);
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(cfg.platformName())
                .setAutomationName(cfg.automationName())
                .setDeviceName(cfg.deviceName())
                .setUdid(cfg.udid())
                .setPlatformVersion(cfg.platformVersion())
                .setAppPackage(cfg.appPackage())
                .setAppActivity(appActivity)
                .setAppWaitActivity(cfg.appWaitActivity())
                .amend("newCommandTimeout", 180)
                .amend("autoGrantPermissions", true)
                .amend("disableWindowAnimation", true)
                .amend("noReset", true)
                .amend("fullReset", false);

        try {
            return new AndroidDriver(new URL(cfg.appiumUrl()), options);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to create AndroidDriver. " +
                            "appiumUrl=" + cfg.appiumUrl() +
                            ", appPackage=" + cfg.appPackage() +
                            ", appActivity=" + appActivity,
                    e
            );
        }
    }

    private static String resolveAppActivity(MobileConfig cfg) {
        String fromConfig = cfg.appActivity();
        if (fromConfig != null && !fromConfig.isBlank()) {
            return fromConfig.trim();
        }

        String resolved = resolveLaunchableActivity(cfg.udid(), cfg.appPackage());
        if (resolved != null && !resolved.isBlank()) {
            return resolved;
        }

        throw new IllegalStateException("Cannot resolve launchable activity for package: " + cfg.appPackage());
    }

    private static String resolveLaunchableActivity(String udid, String appPackage) {
        String out = adbOutLastLine(
                udid,
                "shell", "cmd", "package", "resolve-activity", "--brief",
                "-c", "android.intent.category.LAUNCHER",
                appPackage
        );

        if (out == null || out.isBlank() || !out.contains("/")) {
            return null;
        }

        String activity = out.substring(out.indexOf('/') + 1).trim();

        if (activity.startsWith(".")) {
            return appPackage + activity;
        }
        if (activity.contains(".")) {
            return activity;
        }
        return appPackage + "." + activity;
    }

    private static String adbOutLastLine(String udid, String... args) {
        try {
            String[] cmd = new String[args.length + 3];
            cmd[0] = "adb";
            cmd[1] = "-s";
            cmd[2] = udid;
            System.arraycopy(args, 0, cmd, 3, args.length);

            Process p = new ProcessBuilder(cmd)
                    .redirectErrorStream(true)
                    .start();

            String last = null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.isBlank()) {
                        last = line.trim();
                    }
                }
            }

            p.waitFor();
            return last;
        } catch (Exception e) {
            return null;
        }
    }
}