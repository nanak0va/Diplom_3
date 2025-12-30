package ru.yandex;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.pages.LoginPage;
import ru.yandex.pages.MainPage;
import ru.yandex.pages.ProfilePage;

public class ProfilePageTest extends BaseTest {

    private MainPage mainPage;

    @Override
    @Before
    public void initTest() {
        super.initTest();

        createDefaultUser();

        mainPage = new MainPage(waits, jsExecutor).waitToLoadMainPage().clickOnLoginButton();

        new LoginPage(waits, jsExecutor)
                .waitToLoadLoginPage()
                .fillLoginForm(defaultUser.getEmail(), defaultUser.getPassword())
                .clickOnLoginButton();

        assertNotNull(
                "Токен аутентификации не был найден в localStorage",
                authStorageService.getAccessTokens().getAccessToken());

        mainPage.waitToLoadMainPage().clickOnPersonalAccountLink();
    }

    @Test
    @DisplayName("Открытие личного кабинета: страница профиля отображается")
    @Step("Пользователь заходит в личный кабинет. Ожидание: отображается форма профиля")
    public void shouldOpenProfilePageWhenUserClicksOnPersonalAccount() {
        ProfilePage profilePage = new ProfilePage(waits, jsExecutor).waitToLoadProfilePage();
        assertTrue("Форма профиля должна быть видимой", profilePage.isProfilePageDisplayed());
    }

    @Test
    @DisplayName("Переход на главную через логотип из личного кабинета")
    @Step("Клик по логотипу в личном кабинете. Ожидание: переход на главную страницу")
    public void shouldOpenMainPageWhenClickOnLogoFromProfile() {
        new ProfilePage(waits, jsExecutor).waitToLoadProfilePage();
        mainPage.clickOnLogo().waitToLoadMainPage();

        assertTrue("Главная страница должна быть видимой", mainPage.isMainPageDisplayed());
    }

    @Test
    @DisplayName("Переход на главную через ссылку «Конструктор» из личного кабинета")
    @Step("Клик по ссылке «Конструктор» в личном кабинете. Ожидание: переход на главную страницу")
    public void clickOnConstructorLinkShouldOpenMainPage() {
        new ProfilePage(waits, jsExecutor).waitToLoadProfilePage();
        mainPage.clickOnConstructorLink().waitToLoadMainPage();

        assertTrue("Главная страница должна быть видимой", mainPage.isMainPageDisplayed());
    }
}
