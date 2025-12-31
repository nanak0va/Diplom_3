package ru.yandex.services;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitService {

    private final Duration defaultWaitDuration;

    WebDriver driver;
    JsExecutorService jsExecutor;

    public WaitService(WebDriver driver, int defaultWaitDuration) {
        this.driver = driver;
        jsExecutor = new JsExecutorService(driver);
        this.defaultWaitDuration = Duration.ofSeconds(defaultWaitDuration);
    }

    public WebElement waitForElement(By selector) {
        return new WebDriverWait(driver, defaultWaitDuration)
                .until(ExpectedConditions.presenceOfElementLocated(selector));
    }

    public WebElement waitForElementBeClickable(final By locator) {
        return new WebDriverWait(driver, defaultWaitDuration).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForElementBeClickable(WebElement element) {
        return new WebDriverWait(driver, defaultWaitDuration).until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebElement waitForChildElement(WebElement parent, By childBy) {
        return new WebDriverWait(driver, defaultWaitDuration)
                .until(ExpectedConditions.presenceOfNestedElementLocatedBy(parent, childBy));
    }

    public boolean isPageLoaded() {
        try {
            new WebDriverWait(driver, defaultWaitDuration).until(d -> jsExecutor.isPageLoaded());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitForTextToBePresentInChildElement(WebElement parent, By childBy, String text) {
        try {
            WebElement element = waitForChildElement(parent, childBy);

            new WebDriverWait(driver, defaultWaitDuration)
                    .until(ExpectedConditions.textToBePresentInElement(element, text));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitForTextToBePresentInElement(final By locator, String text) {
        try {
            new WebDriverWait(driver, defaultWaitDuration)
                    .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String waitForValueInLocalStorage(String key) {
        try {
            return new WebDriverWait(driver, defaultWaitDuration).until(d -> {
                String result = jsExecutor.getValueFromLocalStorage(key);
                return (result != null && !result.isEmpty()) ? result : null;
            });
        } catch (Exception e) {
            return null;
        }
    }

    public void waitUntilUrlNotContains(String urlPart) {
        new WebDriverWait(driver, defaultWaitDuration)
                .until(ExpectedConditions.not(ExpectedConditions.urlContains(urlPart)));
    }
}
