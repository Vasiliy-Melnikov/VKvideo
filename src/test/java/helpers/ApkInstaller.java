package helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class ApkInstaller {
    private ApkInstaller() {}

    public static void reinstall(String udid, String appPackage, String apkPath) {
        adbIgnoreFail(udid, "uninstall", appPackage);
        int code = adbStrict(udid, "install", "-r", "-g", apkPath);
        if (code != 0) {
            throw new RuntimeException("APK install failed. apk=" + apkPath + ", adb exit=" + code);
        }
    }

    private static void adbIgnoreFail(String udid, String... args) {
        try { adbStrict(udid, args); } catch (Exception ignored) {}
    }

    private static int adbStrict(String udid, String... args) {
        try {
            Process p = new ProcessBuilder(buildCmd(udid, args))
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                while (br.readLine() != null) { /* read all */ }
            }
            return p.waitFor();
        } catch (Exception e) {
            throw new RuntimeException("adb command failed", e);
        }
    }

    private static List<String> buildCmd(String udid, String... args) {
        List<String> cmd = new ArrayList<>();
        cmd.add("adb");
        cmd.add("-s");
        cmd.add(udid);
        for (String a : args) cmd.add(a);
        return cmd;
    }
}