package ru.yandex.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.yandex.services.JsExecutorService;
import ru.yandex.services.WaitService;

public class LoginPage {
    private static final String PAGE_URL = "/login";

    private static final By PAGE_SELECTOR = By.xpath("//main[contains(@class, 'App_componentContainer')]");
    private static final By MAIN_FORM_SELECTOR = By.xpath("//div[contains(@class, 'Auth_login')]");

    private static final By FORMS_HEADER_SELECTOR = By.xpath("//h2[text()='Вход']");

    private static final By EMAIL_INPUT_SELECTOR = By.xpath("//label[text()='Email']/following::input[1]");
    private static final By PASSWORD_INPUT_SELECTOR = By.xpath("//label[text()='Пароль']/following::input[1]");

    private static final By LOGIN_BUTTON_SELECTOR =
            By.xpath("//button[contains(@class, 'button_button') and text()='Войти']");
    private static final By REGISTRATION_LINK_SELECTOR =
            By.xpath("//a[contains(@class, 'Auth_link') and text()='Зарегистрироваться']");
    private static final By RECOVERY_PASSWORD_LINK_SELECTOR =
            By.xpath("//a[contains(@class, 'Auth_link') and text()='Восстановить пароль']");

    private static final By PASSWORD_ERROR_SELECTOR =
            By.xpath("//parent::div[contains(@class, 'input__container')]/p[contains(@class, 'input__error')]");

    private final WaitService waits;
    private final JsExecutorService jsExecutor;

    private WebElement formHeader;
    private WebElement emailInput;
    private WebElement passwordInput;
    private WebElement loginButton;

    public LoginPage(WaitService waits, JsExecutorService jsExecutor) {
        this.waits = waits;
        this.jsExecutor = jsExecutor;
    }

    @Step("Вводим в поле 'Email': {email}")
    private void setEmail(String email) {
        waits.waitForElementBeClickable(emailInput).sendKeys(email);
        jsExecutor.disableFocus(emailInput);
    }

    @Step("Вводим в поле 'Password': {password}")
    private void setPassword(String password) {
        waits.waitForElementBeClickable(passwordInput).sendKeys(password);
        jsExecutor.disableFocus(passwordInput);
    }

    private void initElements() {
        WebElement page = waits.waitForElement(PAGE_SELECTOR);
        WebElement authForm = waits.waitForChildElement(page, MAIN_FORM_SELECTOR);
        this.formHeader = waits.waitForChildElement(authForm, FORMS_HEADER_SELECTOR);
        this.emailInput = waits.waitForChildElement(authForm, EMAIL_INPUT_SELECTOR);
        this.passwordInput = waits.waitForChildElement(authForm, PASSWORD_INPUT_SELECTOR);
        this.loginButton = waits.waitForChildElement(authForm, LOGIN_BUTTON_SELECTOR);
    }

    @Step("Ожидаем загрузки страницы входа")
    public LoginPage waitToLoadLoginPage() {
        waits.isPageLoaded();
        initElements();
        return this;
    }

    @Step("Выполняем быстрый переход на страницу входа по URI")
    public LoginPage openLoginPage() {
        jsExecutor.goToUrl(PAGE_URL);
        return this;
    }

    @Step("Заполняем форму входа")
    public LoginPage fillLoginForm(String email, String password) {
        setEmail(email);
        setPassword(password);
        return this;
    }

    @Step("Нажимаем на кнопку 'Войти'")
    public LoginPage clickOnLoginButton() {
        waits.waitForElementBeClickable(loginButton).click();
        return this;
    }

    @Step("Нажимаем на кнопку 'Зарегистрироваться'")
    public LoginPage clickOnRegistrationLink() {
        waits.waitForElementBeClickable(REGISTRATION_LINK_SELECTOR).click();
        return this;
    }

    @Step("Нажимаем на кнопку 'Восстановить пароль'")
    public LoginPage clickOnRecoveryPasswordLink() {
        waits.waitForElementBeClickable(RECOVERY_PASSWORD_LINK_SELECTOR).click();
        return this;
    }

    @Step("Проверяем отобразилась ли ошибка при вводе некорректного поля 'Пароль'")
    public boolean isPasswordValidationErrorDisplayed() {
        return waits.waitForTextToBePresentInChildElement(
                passwordInput, PASSWORD_ERROR_SELECTOR, "Некорректный пароль");
    }

    @Step("Проверяем отобразилась ли форма входа")
    public boolean isLoginFormDisplayed() {
        return formHeader.isDisplayed()
                && emailInput.isDisplayed()
                && loginButton.isDisplayed()
                && waits.isPageLoaded();
    }
}
