package ru.yandex;

import static org.junit.Assert.*;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.model.User;
import ru.yandex.pages.ForgotPasswordPage;
import ru.yandex.pages.LoginPage;
import ru.yandex.pages.MainPage;
import ru.yandex.pages.RegistrationPage;

public class LoginTest extends BaseTest {

    @Override
    @Before
    @Step("Подготавливаем данные для теста")
    public void initTest() {
        super.initTest();
        createDefaultUser();
    }

    @Test
    @DisplayName("Вход с валидными данными: токен сохраняется в localStorage")
    @Description(
            "Проверяет, что пользователь может успешно авторизоваться с корректными учетными данными. Ожидается, что после входа токен аутентификации будет сохранён в localStorage.")
    public void shouldLoginWithValidCredentialsAndSaveAccessToken() {

        new LoginPage(waits, jsExecutor)
                .openLoginPage()
                .waitToLoadLoginPage()
                .fillLoginForm(defaultUser.getEmail(), defaultUser.getPassword())
                .clickOnLoginButton();

        assertNotNull(
                "Токен аутентификации не был найден в localStorage",
                authStorageService.getAccessTokens().getAccessToken());
    }

    @Test
    @DisplayName("Вхожд клиента с несуществующими данными: не должно быть токена в localStorage")
    @Description(
            "Проверяет поведение формы входа при вводе несуществующего email и пароля. Ожидается, что вход не будет выполнен, и токен не появится в localStorage.")
    public void shouldNotLoginWithInvalidCredentials() {

        User user = User.builder()
                .email(String.format("user-%s@stellar-burgers.com", System.currentTimeMillis()))
                .password("123456")
                .name("Duck")
                .build();

        new LoginPage(waits, jsExecutor)
                .openLoginPage()
                .waitToLoadLoginPage()
                .fillLoginForm(user.getEmail(), user.getPassword())
                .clickOnLoginButton();

        assertNull(
                "В localStorage не должно быть аутентификационных токенов",
                authStorageService.getAccessTokens().getAccessToken());
    }

    @Test
    @DisplayName("Вход через кнопку «Войти в аккаунт» на главной, проверка: успешный вход")
    @Description(
            "Проверяет переход на страницу входа по кнопке «Войти в аккаунт» на главной странице. После ввода корректных данных ожидается успешная авторизация.")
    public void shouldLoginFromMainPageViaLoginButton() {

        new MainPage(waits, jsExecutor).waitToLoadMainPage().clickOnLoginButton();

        new LoginPage(waits, jsExecutor)
                .waitToLoadLoginPage()
                .fillLoginForm(defaultUser.getEmail(), defaultUser.getPassword())
                .clickOnLoginButton();

        assertNotNull(
                "Токен аутентификации не был найден в localStorage",
                authStorageService.getAccessTokens().getAccessToken());
    }

    @Test
    @DisplayName("Вход через ссылку «Личный кабинет» на главной, проверка: успешный вход")
    @Description(
            "Проверяет переход на страницу входа через ссылку «Личный кабинет» на главной. После ввода корректных данных ожидается успешная авторизация.")
    public void shouldLoginFromMainPageViaPersonalAccountLink() {

        new MainPage(waits, jsExecutor).waitToLoadMainPage().clickOnPersonalAccountLink();

        new LoginPage(waits, jsExecutor)
                .waitToLoadLoginPage()
                .fillLoginForm(defaultUser.getEmail(), defaultUser.getPassword())
                .clickOnLoginButton();

        assertNotNull(
                "Токен аутентификации не был найден в localStorage",
                authStorageService.getAccessTokens().getAccessToken());
    }

    @Test
    @DisplayName("Вход со страницы регистрации через ссылку «Войти», проверка: успешный вход")
    @Description(
            "Проверяет возможность возврата на форму входа со страницы регистрации через ссылку «Войти». Ожидается корректный переход и успешная авторизация с валидными данными.")
    public void shouldLoginFromRegistrationPage() {

        new MainPage(waits, jsExecutor).waitToLoadMainPage().clickOnPersonalAccountLink();

        LoginPage loginPage =
                new LoginPage(waits, jsExecutor).waitToLoadLoginPage().clickOnRegistrationLink();

        var registrationPage = new RegistrationPage(waits, jsExecutor).waitForLoadRegistrationPage();

        assertTrue("Форма регистрации не отображается", registrationPage.isRegistrationPageDisplayed());

        registrationPage.clickOnLoginLink();

        loginPage
                .waitToLoadLoginPage()
                .fillLoginForm(defaultUser.getEmail(), defaultUser.getPassword())
                .clickOnLoginButton();

        assertNotNull(
                "Токен аутентификации не был найден в localStorage",
                authStorageService.getAccessTokens().getAccessToken());
    }

    @Test
    @DisplayName("Вход со страницы восстановления пароля через ссылку «Войти», проверка: успешный вход")
    @Description(
            "Проверяет возможность возврата на форму входа со страницы восстановления пароля через ссылку «Войти». Ожидается корректный переход и успешная авторизация с валидными данными.")
    public void shouldLoginFromForgotPasswordPage() {

        new MainPage(waits, jsExecutor).waitToLoadMainPage().clickOnPersonalAccountLink();

        LoginPage loginPage =
                new LoginPage(waits, jsExecutor).waitToLoadLoginPage().clickOnRecoveryPasswordLink();

        var forgotPasswordPage = new ForgotPasswordPage(waits).waitToLoadForgotPasswordPage();

        assertTrue("Форма восстановления пароля не отображается", forgotPasswordPage.isForgotPasswordFormDisplayed());

        forgotPasswordPage.clickOnLoginLink();

        loginPage
                .waitToLoadLoginPage()
                .fillLoginForm(defaultUser.getEmail(), defaultUser.getPassword())
                .clickOnLoginButton();

        assertNotNull(
                "Токен аутентификации не был найден в localStorage",
                authStorageService.getAccessTokens().getAccessToken());
    }
}
