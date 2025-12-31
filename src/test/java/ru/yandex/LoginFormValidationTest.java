package ru.yandex;

import static org.junit.Assert.assertNotEquals;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.pages.LoginPage;

@RunWith(Parameterized.class)
public class LoginFormValidationTest extends BaseTest {

    @Parameterized.Parameter
    public String name;

    @Parameterized.Parameter(1)
    public String email;

    @Parameterized.Parameter(2)
    public String password;

    @Parameterized.Parameter(3)
    public boolean dataIsValid;

    @Parameterized.Parameters(name = "Тест валидции формы входа: {0}")
    public static Object[][] getData() {
        return new Object[][] {
            {"Вход клиента с валидными данными", "ivanov-34@stellar-burgers.com", "Password123", true},
            {"Вход клиента с паролем менее 6 символов", "ivanov-34@stellar-burgers.com", "P123", false},
            {"Вход клиента с пустым паролем", "ivanov", " ", false}
        };
    }

    @Test
    @Description(
            "Проверяет валидацию полей на форме входа. При некорректных данных отображается сообщение об ошибке в поле пароля.")
    public void loginFormShouldValidateFieldsAndShowErrorMessages() {

        LoginPage loginPage = new LoginPage(waits, jsExecutor)
                .openLoginPage()
                .waitToLoadLoginPage()
                .fillLoginForm(email, password)
                .clickOnLoginButton();

        assertNotEquals(
                "Должно отобразиться сообщение об ошибке в поле пароль при некорректных данных",
                dataIsValid,
                loginPage.isPasswordValidationErrorDisplayed());
    }
}
