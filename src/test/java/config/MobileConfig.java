package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/${config}.properties",
        "classpath:config/local.properties"
})
public interface MobileConfig extends Config {

    @Key("appium.url")
    @DefaultValue("http://127.0.0.1:4723")
    String appiumUrl();

    @Key("platformName")
    @DefaultValue("Android")
    String platformName();

    @Key("automationName")
    @DefaultValue("UiAutomator2")
    String automationName();

    @Key("deviceName")
    @DefaultValue("Android Emulator")
    String deviceName();

    @Key("udid")
    @DefaultValue("emulator-5554")
    String udid();

    @Key("platformVersion")
    @DefaultValue("11")
    String platformVersion();

    @Key("appPackage")
    @DefaultValue("com.vk.vkvideo")
    String appPackage();

    @Key("appActivity")
    @DefaultValue("")
    String appActivity();

    @Key("appWaitActivity")
    @DefaultValue("*")
    String appWaitActivity();

    @Key("app")
    @DefaultValue("apps/vkvideo.apk")
    String app();

    @Key("reinstallApp")
    @DefaultValue("true")
    boolean reinstallApp();

    @Key("timeoutMs")
    @DefaultValue("30000")
    long timeoutMs();

    @Key("video.start.timeout.ms")
    @DefaultValue("15000")
    long videoStartTimeoutMs();

    @Key("video.progress.check.window.ms")
    @DefaultValue("3000")
    long videoProgressCheckWindowMs();
}
