package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.ConfigFactoryProvider;
import config.MobileConfig;
import drivers.AndroidMobileDriver;
import helpers.AllureAttachments;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import screens.MainFeedScreen;

public abstract class BaseTest {

    @BeforeAll
    static void beforeAll() {
        MobileConfig cfg = ConfigFactoryProvider.mobile();

        Configuration.browser = AndroidMobileDriver.class.getName();
        Configuration.timeout = cfg.timeoutMs();
        Configuration.pageLoadTimeout = cfg.timeoutMs();
        Configuration.browserSize = null;

        SelenideLogger.addListener("allure", new AllureSelenide()
                .screenshots(false)
                .savePageSource(false)
        );
    }

    @BeforeEach
    void beforeEach() {
        Selenide.open();

        MainFeedScreen feed = new MainFeedScreen();
        feed.handleStartupScreens();
    }

    @AfterEach
    void afterEach() {
        AllureAttachments.screenshot("Last screenshot");
        AllureAttachments.pageSource("Page source");
        Selenide.closeWebDriver();
    }
}