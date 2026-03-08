package screens;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainFeedScreen {
    private final SelenideElement onboardingCloseButton =
            $(AppiumBy.id("com.vk.vkvideo:id/close_btn_left"));

    private final SelenideElement authSkipButton =
            $(AppiumBy.xpath("//*[@text='Skip']"));

    private final SelenideElement authContinueButton =
            $(AppiumBy.xpath("//*[@text='Continue']"));

    private final SelenideElement headerTitle =
            $(AppiumBy.xpath("//*[@text='VK Video']"));

    private final SelenideElement forYouTab =
            $(AppiumBy.xpath("//*[@text='For you']"));

    private final SelenideElement mainTab =
            $(AppiumBy.id("com.vk.vkvideo:id/tab_main"));

    private final ElementsCollection videoTitles =
            $$(AppiumBy.id("com.vk.vkvideo:id/title"));

    @Step("Обрабатываем стартовые экраны")
    public void handleStartupScreens() {
        for (int i = 0; i < 8; i++) {

            if (isOnboardingVisible()) {
                closeOnboarding();
                sleep(1200);
                continue;
            }

            if (isAuthVisible()) {
                skipAuth();
                sleep(1200);
                continue;
            }

            if (isMainFeedVisible()) {
                return;
            }

            sleep(1000);
        }

        waitFeedLoaded();
    }

    @Step("Если отображается попап — нажимаем крестик")
    public void closeOnboarding() {
        onboardingCloseButton.shouldBe(Condition.visible, Duration.ofSeconds(10)).click();
    }

    @Step("Если отображается окно авторизации — нажимаем Skip")
    public void skipAuth() {
        authSkipButton.shouldBe(Condition.visible, Duration.ofSeconds(10)).click();
    }

    @Step("Ждём загрузки главной ленты")
    public void waitFeedLoaded() {
        headerTitle.shouldBe(Condition.visible, Duration.ofSeconds(20));
        forYouTab.shouldBe(Condition.visible, Duration.ofSeconds(20));
        mainTab.shouldBe(Condition.visible, Duration.ofSeconds(20));
    }

    @Step("Открываем первое видео из ленты")
    public void openFirstVideoFromFeed() {
        waitFeedLoaded();
        sleep(2000);

        for (SelenideElement title : videoTitles) {
            try {
                if (!title.exists() || !title.isDisplayed()) {
                    continue;
                }

                String text = title.getText();
                if (text == null || text.isBlank()) {
                    continue;
                }

                if (isServiceText(text)) {
                    continue;
                }

                title.click();
                return;
            } catch (Exception ignored) {
            }
        }

        throw new AssertionError("Не удалось найти и открыть первое видео из ленты");
    }

    private boolean isOnboardingVisible() {
        try {
            return onboardingCloseButton.exists() && onboardingCloseButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isAuthVisible() {
        try {
            return authSkipButton.exists() && authSkipButton.isDisplayed()
                    || authContinueButton.exists() && authContinueButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isMainFeedVisible() {
        try {
            return headerTitle.exists() && headerTitle.isDisplayed()
                    && forYouTab.exists() && forYouTab.isDisplayed()
                    && mainTab.exists() && mainTab.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isServiceText(String text) {
        return text.equals("VK Video")
                || text.equals("For you")
                || text.equals("Trending")
                || text.equals("Main")
                || text.equals("Clips")
                || text.equals("Create")
                || text.equals("Following")
                || text.equals("Profile")
                || text.equals("Skip")
                || text.equals("Continue");
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}