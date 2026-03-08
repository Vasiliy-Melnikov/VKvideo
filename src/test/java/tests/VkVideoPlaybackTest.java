package tests;

import config.ConfigFactoryProvider;
import config.MobileConfig;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import screens.MainFeedScreen;
import screens.PlayerScreen;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@Epic("Mobile")
@Feature("VK Video")
@Story("Playback")
public class VkVideoPlaybackTest extends BaseTest {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Owner("VSM")
    @DisplayName("VK Video: видео должно воспроизводиться")
    void videoShouldPlay() {
        MobileConfig cfg = ConfigFactoryProvider.mobile();

        MainFeedScreen feed = new MainFeedScreen();
        PlayerScreen player = new PlayerScreen();

        step("Обрабатываем стартовые экраны", feed::handleStartupScreens);

        step("Открываем первое видео из ленты", feed::openFirstVideoFromFeed);

        step("Ждём открытия экрана плеера", () ->
                player.waitUntilOpened(Duration.ofMillis(cfg.videoStartTimeoutMs()))
        );

        boolean playing = step("Видео воспроизводится", () ->
                player.isPlaying(
                        Duration.ofMillis(cfg.videoStartTimeoutMs()),
                        Duration.ofMillis(cfg.videoProgressCheckWindowMs())
                )
        );

        if (playing) {
            step("Видео воспроизводится", () -> assertTrue(true));
        } else {
            fail("Видео НЕ воспроизводится: плеер не открылся или не удержался в активном состоянии");
        }
    }

    @Step("{title}")
    private void step(String title, Runnable action) {
        action.run();
    }

    @Step("{title}")
    private <T> T step(String title, java.util.concurrent.Callable<T> action) {
        try {
            return action.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}