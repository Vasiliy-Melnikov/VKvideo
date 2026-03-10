package helpers;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public final class AllureAttachments {
    private AllureAttachments() {}

    public static void screenshot(String name) {
        if (WebDriverRunner.hasWebDriverStarted()) {
            byte[] png = ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(png), "png");
        }
    }

    public static void pageSource(String name) {
        if (WebDriverRunner.hasWebDriverStarted()) {
            String source = WebDriverRunner.source();
            Allure.addAttachment(name, "text/xml", new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)), "xml");
        }
    }
}
