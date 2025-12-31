package ru.yandex;

import static org.junit.Assert.*;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import ru.yandex.model.AccessTokens;
import ru.yandex.model.User;
import ru.yandex.pages.LoginPage;
import ru.yandex.pages.RegistrationPage;
import ru.yandex.utils.DataPreparationHelper;

public class RegistrationTest extends BaseTest {

    private AccessTokens accessTokensRegisteredUser;
    public User registeredUser;

    @Test
    @DisplayName("Регистрация пользователя корректными данными")
    @Description(
            "Проверяет успешную регистрацию пользователя с валидными данными. После регистрации ожидается переход на страницу входа и возможность авторизации (получение токена).")
    public void shouldRegisterUser() {

        registeredUser = DataPreparationHelper.generateUniqueUser();

        new RegistrationPage(waits, jsExecutor)
                .openRegistrationPage()
                .waitForLoadRegistrationPage()
                .fillRegistrationForm(registeredUser.getName(), registeredUser.getEmail(), registeredUser.getPassword())
                .clickOnRegisterButton()
                .waitForRegistrationSuccess();

        assertTrue(
                "Не отобразилась форма входа",
                new LoginPage(waits, jsExecutor).waitToLoadLoginPage().isLoginFormDisplayed());

        accessTokensRegisteredUser = loginService.signInAndGetAccessTokens(registeredUser);

        assertNotNull(
                "Не удалось получить токен авторизации пользователя после регистрации",
                accessTokensRegisteredUser.getAccessToken());
    }

    @Test
    @DisplayName("Регистрация пользователя с уже существующим email")
    @Description(
            "Проверяет поведение формы регистрации при попытке использовать email, уже зарегистрированный в системе. Ожидается отображение ошибки и отсутствие успешной регистрации (токен не должен быть получен).")
    public void shouldNotRegisterUserIfEmailAlreadyExists() {

        createDefaultUser();

        registeredUser = User.builder()
                .email(defaultUser.getEmail())
                .password(DataPreparationHelper.generatePassword())
                .name(DataPreparationHelper.generateName())
                .build();

        var registrationPage = new RegistrationPage(waits, jsExecutor)
                .openRegistrationPage()
                .waitForLoadRegistrationPage()
                .fillRegistrationForm(registeredUser.getName(), registeredUser.getEmail(), registeredUser.getPassword())
                .clickOnRegisterButton();

        assertTrue(
                "Не отображена ошибка о том что пользователь с таким email уже зарегистрирован",
                registrationPage.isUserAlreadyExistsValidationErrorDisplayed());

        accessTokensRegisteredUser = loginService.signInAndGetAccessTokensWithoutValidation(registeredUser);

        assertTrue(
                "Был получен токен авторизации пользователя после попытки регистрации с уже существующим email",
                accessTokensRegisteredUser == null || accessTokensRegisteredUser.getAccessToken() == null);
    }

    @Override
    @After
    @Step("Очищаем зарегистрированного пользователя, если создали")
    public void tearDown() {
        loginService.deleteUserAfterTest(registeredUser, accessTokensRegisteredUser);
        super.tearDown();
    }
}
