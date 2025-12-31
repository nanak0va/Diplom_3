package ru.yandex.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.yandex.services.JsExecutorService;
import ru.yandex.services.WaitService;

public class ProfilePage {

    private static final String PAGE_URL = "/account/profile";

    private static final By PAGE_SELECTOR = By.xpath("//main[contains(@class, 'App_componentContainer')]");
    private static final By FORM_SELECTOR = By.xpath("//div[contains(@class, 'Account_account')]");

    private static final By LOGOUT_BUTTON_SELECTOR =
            By.xpath("//button[contains(@class, 'Account_button') and text()='Выход']");
    private static final By PROFILE_LINK_SELECTOR =
            By.xpath("//a[contains(@class, 'Account_link') and text()='Профиль']");

    private final WaitService waits;
    private final JsExecutorService jsExecutor;

    private WebElement logoutButton;
    private WebElement profileLink;

    public ProfilePage(WaitService waits, JsExecutorService jsExecutor) {
        this.waits = waits;
        this.jsExecutor = jsExecutor;
    }

    private void initElements() {
        WebElement page = waits.waitForElement(PAGE_SELECTOR);
        WebElement form = waits.waitForChildElement(page, FORM_SELECTOR);
        this.logoutButton = waits.waitForChildElement(form, LOGOUT_BUTTON_SELECTOR);
        this.profileLink = waits.waitForChildElement(form, PROFILE_LINK_SELECTOR);
    }

    @Step("Проверяем, что страница профиля загружена")
    public ProfilePage waitToLoadProfilePage() {
        waits.isPageLoaded();
        initElements();
        return this;
    }

    @Step("Открываем страницу профиля")
    public ProfilePage openProfilePage() {
        jsExecutor.goToUrl(PAGE_URL);
        return this;
    }

    @Step("Проверяем, что страница профиля загружена")
    public boolean isProfilePageDisplayed() {
        return profileLink.isDisplayed() && logoutButton.isDisplayed() && waits.isPageLoaded();
    }
}
