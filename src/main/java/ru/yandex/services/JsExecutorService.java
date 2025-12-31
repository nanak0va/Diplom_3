package ru.yandex.services;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JsExecutorService {

    WebDriver driver;

    public JsExecutorService(WebDriver driver) {
        this.driver = driver;
    }

    public void goToUrl(String url) {
        ((JavascriptExecutor) driver).executeScript("window.location.pathname = arguments[0];", url);
    }

    public void disableFocus(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].blur();", element);
    }

    public String getValueFromLocalStorage(String key) {
        return (String)
                ((JavascriptExecutor) driver).executeScript("return window.localStorage.getItem(arguments[0]);", key);
    }

    public boolean isPageLoaded() {
        var status = (String) ((JavascriptExecutor) driver).executeScript("return document.readyState");

        return status != null && status.equals("complete");
    }

    public void scrollIntoElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
    }

    public void clickOnElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void moveToElementAndClick(WebElement element) {
        scrollIntoElement(element);
        clickOnElement(element);
    }
}
