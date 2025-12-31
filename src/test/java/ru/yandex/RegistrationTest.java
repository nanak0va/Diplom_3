package ru.yandex;

import static org.junit.Assert.*;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.api.services.LoginService;
import ru.yandex.model.AccessTokens;
import ru.yandex.model.User;
import ru.yandex.pages.LoginPage;
import ru.yandex.pages.RegistrationPage;

public class RegistrationTest extends BaseTest {

    private AccessTokens accessTokens;
    private LoginService loginService;

    @Override
    @Before
    public void initTest() {
        super.initTest();
        this.loginService = new LoginService();
    }

    @Test
    @DisplayName("Регистрация пользователя корректными данными")
    @Description(
            "Проверяет успешную регистрацию пользователя с валидными данными. После регистрации ожидается переход на страницу входа и возможность авторизации (получение токена).")
    public void shouldRegisterUser() {

        var user = User.builder()
                .email(String.format("user-%s@stellar-burgers.com", System.currentTimeMillis()))
                .password("P123456")
                .name("Duck")
                .build();

        new RegistrationPage(waits, jsExecutor)
                .openRegistrationPage()
                .waitForLoadRegistrationPage()
                .fillRegistrationForm(user.getName(), user.getEmail(), user.getPassword())
                .clickOnRegisterButton()
                .waitForRegistrationSuccess();

        assertTrue(
                "Не отобразилась форма входа",
                new LoginPage(waits, jsExecutor).waitToLoadLoginPage().isLoginFormDisplayed());

        accessTokens = loginService.signInAndGetAccessTokens(user);

        assertNotNull(
                "Не удалось получить токен авторизации пользователя после регистрации", accessTokens.getAccessToken());
    }

    @Test
    @DisplayName("Регистрация пользователя с уже существующим email")
    @Description(
            "Проверяет поведение формы регистрации при попытке использовать email, уже зарегистрированный в системе. Ожидается отображение ошибки и отсутствие успешной регистрации (токен не должен быть получен).")
    public void shouldNotRegisterUserIfEmailAlreadyExists() {

        createDefaultUser();

        var user = User.builder()
                .email(DEFAULT_USER_EMAIL)
                .password("SUPPER_SECRET_PASSWORD123")
                .name("Duck")
                .build();

        var registrationPage = new RegistrationPage(waits, jsExecutor)
                .openRegistrationPage()
                .waitForLoadRegistrationPage()
                .fillRegistrationForm(user.getName(), user.getEmail(), user.getPassword())
                .clickOnRegisterButton();

        assertTrue(
                "Не отображена ошибка о том что пользователь с таким email уже зарегистрирован",
                registrationPage.isUserAlreadyExistsValidationErrorDisplayed());

        accessTokens = loginService.signInAndGetAccessTokensWithoutValidation(user);

        assertTrue(
                "Был получен токен авторизации пользователя после попытки регистрации с уже существующим email",
                accessTokens == null || accessTokens.getAccessToken() == null);
    }

    @Override
    @After
    @Step("Очищаем зарегистрированного пользователя, если создали")
    public void tearDown() {
        if (accessTokens != null) {
            loginService.logoutUser(accessTokens);
        }
        super.tearDown();
    }
}
