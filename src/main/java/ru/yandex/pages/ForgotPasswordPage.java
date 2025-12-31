package ru.yandex.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.yandex.services.WaitService;

public class ForgotPasswordPage {

    private static final By PAGE_SELECTOR = By.className("App_componentContainer__2JC2W");
    private static final By MAIN_FORM_SELECTOR = By.className("Auth_login__3hAey");

    private static final By FORMS_HEADER_SELECTOR = By.xpath("//following::h2[text()='Восстановление пароля']");

    private static final By BUTTON_SELECTOR = By.xpath("//button[text()='Восстановить']");
    private static final By LOGIN_LINK_SELECTOR = By.xpath("//a[contains(@class, 'Auth_link') and text()='Войти']");

    private final WaitService waits;

    private WebElement formHeader;
    private WebElement recoveryButton;

    public ForgotPasswordPage(WaitService waits) {
        this.waits = waits;
    }

    private void initElements() {
        WebElement page = waits.waitForElement(PAGE_SELECTOR);
        WebElement authForm = waits.waitForChildElement(page, MAIN_FORM_SELECTOR);
        this.formHeader = waits.waitForChildElement(authForm, FORMS_HEADER_SELECTOR);
        this.recoveryButton = waits.waitForChildElement(authForm, BUTTON_SELECTOR);
    }

    @Step("Ожидаем загрузки страницы восстановления пароля")
    public ForgotPasswordPage waitToLoadForgotPasswordPage() {
        waits.isPageLoaded();
        initElements();
        return this;
    }

    @Step("Нажимаем на ссылку 'Войти'")
    public ForgotPasswordPage clickOnLoginLink() {
        waits.waitForElementBeClickable(LOGIN_LINK_SELECTOR).click();
        return this;
    }

    @Step("Проверяем отобразилась ли форма регистрации")
    public boolean isForgotPasswordFormDisplayed() {
        return formHeader.isDisplayed() && recoveryButton.isDisplayed() && waits.isPageLoaded();
    }
}
