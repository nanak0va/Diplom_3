package ru.yandex;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.pages.RegistrationPage;

@RunWith(Parameterized.class)
public class RegistrationFormValidationTest extends BaseTest {

    @Parameterized.Parameter
    public String testName;

    @Parameterized.Parameter(1)
    public String userName;

    @Parameterized.Parameter(2)
    public String email;

    @Parameterized.Parameter(3)
    public String password;

    @Parameterized.Parameter(4)
    public boolean dataIsValid;

    @Parameterized.Parameters(name = "Тест валидции формы регистрации: {0}")
    public static Object[][] getData() {
        return new Object[][] {
            {"Регистрация клиента с валидными данными", "Иван", "ivanov-34@stellar-burgers.com", "P12345", true},
            {"Регистрация клиента с паролем менее 6 символов", "Иван", "ivanov-34@stellar-burgers.com", "P1234", false},
            {"Регистрация клиента с пустым паролем", "Иван", "ivanov-34@stellar-burgers.com", " ", false}
        };
    }

    @Test
    public void registrationFormShouldValidateFieldsAndShowErrorMessages() {
        RegistrationPage registrationPage = new RegistrationPage(waits, jsExecutor)
                .openRegistrationPage()
                .waitForLoadRegistrationPage()
                .fillRegistrationForm(userName, email, password);

        assertNotEquals(
                "Должно отобразиться сообщение об ошибке в поле пароль при некорректных данных",
                dataIsValid,
                registrationPage.isPasswordValidationErrorDisplayed());
    }
}
