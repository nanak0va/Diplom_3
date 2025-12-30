package ru.yandex.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.yandex.services.JsExecutorService;
import ru.yandex.services.WaitService;

public class MainPage {

    private static final By PAGE_SELECTOR = By.xpath("//div[contains(@class, 'App_App')]");
    private static final By HEADER_SELECTOR = By.xpath("//header[contains(@class, 'AppHeader_header')]");
    private static final By HEADER_PERSONAL_ACCOUNT_LINK_SELECTOR = By.xpath("//p[text()='Личный Кабинет']/parent::a");
    private static final By HEADER_LOGO_SELECTOR = By.xpath("//div[contains(@class, 'AppHeader_header__logo')]");
    private static final By HEADER_CONSTRUCTOR_SELECTOR = By.xpath("//p[text()='Конструктор']/parent::a");

    private static final By COMPONENT_CONTAINER_SELECTOR =
            By.xpath("//main[contains(@class, 'App_componentContainer')]");
    private static final By BUNS_SELECTOR = By.xpath("//span[text()='Булки']/parent::div");
    private static final By SAUCES_SELECTOR = By.xpath("//span[text()='Соусы']/parent::div");
    private static final By FILLINGS_SELECTOR = By.xpath("//span[text()='Начинки']/parent::div");

    private static final By LOGIN_BUTTON_SELECTOR =
            By.xpath("//button[contains(@class, 'button_button') and text()='Войти в аккаунт']");

    private final WaitService waits;
    private final JsExecutorService jsExecutor;

    private WebElement page;
    private WebElement header;
    private WebElement accountLink;
    private WebElement constructorLink;
    private WebElement logo;

    private WebElement buns;
    private WebElement fillings;
    private WebElement sauces;

    public MainPage(WaitService waits, JsExecutorService jsExecutor) {
        this.waits = waits;
        this.jsExecutor = jsExecutor;
    }

    private boolean isTabSelected(WebElement element) {
        try {
            String classAttribute = element.getAttribute("class");
            if (classAttribute != null) {
                return classAttribute.contains("tab_tab_type_current");
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private void initElements() {
        this.page = waits.waitForElement(PAGE_SELECTOR);
        this.header = waits.waitForChildElement(page, HEADER_SELECTOR);
        this.accountLink = waits.waitForChildElement(header, HEADER_PERSONAL_ACCOUNT_LINK_SELECTOR);
        this.constructorLink = waits.waitForChildElement(header, HEADER_CONSTRUCTOR_SELECTOR);
        this.logo = waits.waitForChildElement(header, HEADER_LOGO_SELECTOR);

        WebElement componentContainer = waits.waitForChildElement(page, COMPONENT_CONTAINER_SELECTOR);
        this.buns = waits.waitForChildElement(componentContainer, BUNS_SELECTOR);
        this.fillings = waits.waitForChildElement(componentContainer, FILLINGS_SELECTOR);
        this.sauces = waits.waitForChildElement(componentContainer, SAUCES_SELECTOR);
    }

    @Step("Ожидаем загрузки главной страницы")
    public MainPage waitToLoadMainPage() {
        waits.isPageLoaded();
        initElements();
        return this;
    }

    @Step("Проверяем, что главная страница загружена")
    public boolean isMainPageDisplayed() {
        return waits.isPageLoaded() && buns.isDisplayed();
    }

    @Step("Кликаем на кнопку 'Войти в аккаунт'")
    public MainPage clickOnLoginButton() {
        WebElement loginButton = waits.waitForChildElement(page, LOGIN_BUTTON_SELECTOR);
        waits.waitForElementBeClickable(loginButton).click();
        return this;
    }

    @Step("Кликаем на логотип")
    public MainPage clickOnLogo() {
        jsExecutor.scrollIntoElement(header);
        waits.waitForElementBeClickable(logo).click();
        return this;
    }

    @Step("Кликаем на ссылку 'Личный кабинет'")
    public MainPage clickOnPersonalAccountLink() {
        waits.waitForElementBeClickable(accountLink).click();
        return this;
    }

    @Step("Кликаем на ссылку 'Конструктор'")
    public MainPage clickOnConstructorLink() {
        jsExecutor.scrollIntoElement(header);
        waits.waitForElementBeClickable(constructorLink).click();
        return this;
    }

    @Step("Кликаем на вкладку 'Булки'")
    public MainPage clickOnBunsTab() {
        jsExecutor.moveToElementAndClick(buns);
        return this;
    }

    @Step("Кликаем на вкладку 'Соусы'")
    public MainPage clickOnSaucesTab() {
        jsExecutor.moveToElementAndClick(sauces);
        return this;
    }

    @Step("Кликаем на вкладку 'Начинки'")
    public MainPage clickOnFillingsTab() {
        jsExecutor.moveToElementAndClick(fillings);
        return this;
    }

    @Step("Проверяем, что вкладка 'Булки' активна")
    public boolean isBunsTabActive() {
        return isTabSelected(buns);
    }

    @Step("Проверяем, что вкладка 'Начинки' активна")
    public boolean isFillingsTabActive() {
        return isTabSelected(fillings);
    }

    @Step("Проверяем, что вкладка 'Соусы' активна")
    public boolean isSaucesTabActive() {
        return isTabSelected(sauces);
    }
}
