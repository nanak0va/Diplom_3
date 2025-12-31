package ru.yandex.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.yandex.services.JsExecutorService;
import ru.yandex.services.WaitService;

public class RegistrationPage {

    private static final String PAGE_URL = "/register";

    private static final By PAGE_SELECTOR = By.xpath("//main[contains(@class, 'App_componentContainer')]");
    private static final By MAIN_FORM_SELECTOR = By.xpath("//div[contains(@class, 'Auth_login')]");
    private static final By FORMS_HEADER_SELECTOR = By.xpath("//following::h2[text()='Регистрация']");
    private static final By NAME_INPUT_SELECTOR = By.xpath("//label[text()='Имя']/following::input[1]");
    private static final By EMAIL_INPUT_SELECTOR = By.xpath("//label[text()='Email']/following::input[1]");
    private static final By PASSWORD_INPUT_SELECTOR = By.xpath("//label[text()='Пароль']/following::input[1]");
    private static final By REGISTER_BUTTON_SELECTOR =
            By.xpath("//button[contains(@class, 'button_button') and text()='Зарегистрироваться']");
    private static final By PASSWORD_ERROR_SELECTOR =
            By.xpath("//parent::div[contains(@class, 'input__container')]/p[contains(@class, 'input__error')]");
    private static final By USER_ALREADY_EXISTS_ERROR_SELECTOR =
            By.xpath("//p[contains(@class, 'input__error') and text()='Такой пользователь уже существует']");

    private static final By LOGIN_LINK_SELECTOR = By.xpath("//a[contains(@class, 'Auth_link') and text()='Войти']");

    private final WaitService waits;
    private final JsExecutorService jsExecutor;

    private WebElement formHeader;
    private WebElement nameInput;
    private WebElement emailInput;
    private WebElement passwordInput;
    private WebElement registerButton;
    private WebElement loginLink;

    public RegistrationPage(WaitService waits, JsExecutorService jsExecutor) {
        this.waits = waits;
        this.jsExecutor = jsExecutor;
    }

    @Step("Вводим в поле 'Имя': {name}")
    private void setName(String name) {
        waits.waitForElementBeClickable(nameInput).sendKeys(name);
        jsExecutor.disableFocus(nameInput);
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
        this.nameInput = waits.waitForChildElement(authForm, NAME_INPUT_SELECTOR);
        this.emailInput = waits.waitForChildElement(authForm, EMAIL_INPUT_SELECTOR);
        this.passwordInput = waits.waitForChildElement(authForm, PASSWORD_INPUT_SELECTOR);
        this.registerButton = waits.waitForChildElement(authForm, REGISTER_BUTTON_SELECTOR);
        this.loginLink = waits.waitForChildElement(authForm, LOGIN_LINK_SELECTOR);
    }

    @Step("Ожидаем загрузки страницы регистрации")
    public RegistrationPage waitForLoadRegistrationPage() {
        waits.isPageLoaded();
        initElements();
        return this;
    }

    @Step("Выполняем быстрый переход на страницу регистрации по URI")
    public RegistrationPage openRegistrationPage() {
        jsExecutor.goToUrl(PAGE_URL);
        waits.waitForElement(REGISTER_BUTTON_SELECTOR);
        return this;
    }

    @Step("Заполняем форму регистрации")
    public RegistrationPage fillRegistrationForm(String name, String email, String password) {
        setName(name);
        setEmail(email);
        setPassword(password);
        return this;
    }

    @Step("Нажимаем на кнопку 'Зарегистрироваться'")
    public RegistrationPage clickOnRegisterButton() {
        waits.waitForElementBeClickable(registerButton).click();
        return this;
    }

    @Step("Проверяем отобразилась ли ошибка при вводе некорректного поля 'Пароль'")
    public boolean isPasswordValidationErrorDisplayed() {
        return waits.waitForTextToBePresentInChildElement(
                passwordInput, PASSWORD_ERROR_SELECTOR, "Некорректный пароль");
    }

    @Step("Проверяем отобразилась ли ошибка при вводе уже существующего пользователя")
    public boolean isUserAlreadyExistsValidationErrorDisplayed() {
        return waits.waitForTextToBePresentInElement(
                USER_ALREADY_EXISTS_ERROR_SELECTOR, "Такой пользователь уже существует");
    }

    @Step("Проверяем отобразилась ли форма регистрации")
    public boolean isRegistrationPageDisplayed() {
        return formHeader.isDisplayed()
                && nameInput.isDisplayed()
                && registerButton.isDisplayed()
                && waits.isPageLoaded();
    }

    @Step("Ожидаем успешной регистрации, долен измениться URL")
    public RegistrationPage waitForRegistrationSuccess() {
        waits.waitUntilUrlNotContains(PAGE_URL);
        return this;
    }

    @Step("Нажимаем на ссылку 'Войти'")
    public RegistrationPage clickOnLoginLink() {
        waits.waitForElementBeClickable(loginLink).click();
        return this;
    }
}
