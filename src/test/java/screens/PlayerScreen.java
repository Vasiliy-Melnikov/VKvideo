package screens;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class PlayerScreen {

    private final SelenideElement miniPlayerContainer =
            $(AppiumBy.id("com.vk.vkvideo:id/vk_video_minimizable_player_container"));

    private final SelenideElement motionLayout =
            $(AppiumBy.id("com.vk.vkvideo:id/minimizableMotionLayout"));

    @Step("Ждём открытия экрана плеера")
    public void waitUntilOpened(Duration timeout) {
        miniPlayerContainer.shouldBe(Condition.visible, timeout);
        motionLayout.shouldBe(Condition.visible, timeout);
    }

    @Step("Проверяем, что видео воспроизводится")
    public boolean isPlaying(Duration startTimeout, Duration progressWindow) {
        waitUntilOpened(startTimeout);

        sleep(progressWindow);

        return miniPlayerContainer.exists() && miniPlayerContainer.isDisplayed();
    }

    private void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}