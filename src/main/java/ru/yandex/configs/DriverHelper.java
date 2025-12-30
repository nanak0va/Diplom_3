package ru.yandex.configs;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

@Slf4j
public class DriverHelper {

    public WebDriver createDriver() {

        log.debug("Инициализация WebDriver...");

        BrowserType browserType =
                BrowserType.fromString(System.getProperty("runIn", ConfigReader.asString("/browser/runIn")));

        log.debug("Запуск браузера: " + browserType);

        switch (browserType) {
            case CHROME:
                return createChromeDriver();
            case YANDEX:
                return createYandexDriver();
            default:
                throw new IllegalArgumentException(
                        "Unsupported browser: " + browserType.name().toLowerCase());
        }
    }

    private WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        setCommonOptions(options);
        setBrowserSpecificOptions(options, BrowserType.CHROME);
        setChromeBinaryOptions(options);
        return new ChromeDriver(options);
    }

    private WebDriver createYandexDriver() {
        ChromeOptions options = new ChromeOptions();
        setCommonOptions(options);
        setBrowserSpecificOptions(options, BrowserType.YANDEX);
        setYandexBinaryOptions(options);
        setYandexPrefsOptions(options);

        var service = createYandexDriverService(ConfigReader.asString("/browser/yandex/driver-path"));
        return new ChromeDriver(service, options);
    }

    private void setCommonOptions(ChromeOptions options) {
        var commonOptions = ConfigReader.asList("/browser/options/common");
        if (!commonOptions.isEmpty()) {
            options.addArguments(commonOptions);
            log.debug("Добавлены общие опции: " + commonOptions);
        }
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
    }

    private void setBrowserSpecificOptions(ChromeOptions options, BrowserType browserType) {
        var browserSpecificOptions =
                ConfigReader.asList("/browser/options/" + browserType.name().toLowerCase());
        if (!browserSpecificOptions.isEmpty()) {
            options.addArguments(browserSpecificOptions);
            log.info("Добавлены специфичные опции для Browser: " + browserSpecificOptions);
        }
    }

    private void setYandexBinaryOptions(ChromeOptions options) {
        var yandexBinaryPath = ConfigReader.asString("/browser/yandex/binary-path");
        if (yandexBinaryPath != null && !yandexBinaryPath.isEmpty()) {
            options.setBinary(yandexBinaryPath);
            log.debug("Путь к бинарнику Yandex Browser: " + yandexBinaryPath);
        }
    }

    private void setChromeBinaryOptions(ChromeOptions options) {
        var chromeBinaryPath = ConfigReader.asString("/browser/chrome/binary-path");

        if (chromeBinaryPath != null && !chromeBinaryPath.isEmpty()) {
            options.setBinary(chromeBinaryPath);
            log.debug("Путь к бинарнику Chrome: " + chromeBinaryPath);
        }
    }

    private void setYandexPrefsOptions(ChromeOptions options) {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("translate.enabled", false);
        prefs.put("profile.managed_default_content_settings.notifications", 2);
        options.setExperimentalOption("prefs", prefs);
    }

    public ChromeDriverService createYandexDriverService(String pathname) {
        File driverFile = new File(pathname);
        if (!driverFile.exists()) {
            throw new IllegalStateException("Исполняемый файл yandexdriver не найден по пути: " + pathname);
        }

        return new ChromeDriverService.Builder()
                .usingDriverExecutable(driverFile)
                .build();
    }
}
