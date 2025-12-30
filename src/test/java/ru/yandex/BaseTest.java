package ru.yandex;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import ru.yandex.api.services.LoginService;
import ru.yandex.configs.ConfigReader;
import ru.yandex.configs.DriverHelper;
import ru.yandex.model.User;
import ru.yandex.services.AuthStorageService;
import ru.yandex.services.JsExecutorService;
import ru.yandex.services.WaitService;

public abstract class BaseTest {
    public static final String DEFAULT_USER_EMAIL = "burger-edu-default-test-user-34@stellar-burgers.com";
    public static final String DEFAULT_USER_NAME = "test";
    public static final String DEFAULT_USER_PASSWORD = "123456";
    public final String uriResourceUnderTest = "https://stellarburgers.education-services.ru/";
    public WebDriver driver;
    public User defaultUser;
    protected WaitService waits;
    protected JsExecutorService jsExecutor;
    protected AuthStorageService authStorageService;

    @Before
    public void initTest() {
        this.driver = new DriverHelper().createDriver();
        this.waits = new WaitService(driver, ConfigReader.asInt("/wait/default"));
        this.jsExecutor = new JsExecutorService(driver);
        this.authStorageService = new AuthStorageService(waits);
        openResource();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void openResource() {
        driver.get(uriResourceUnderTest);
    }

    public void createDefaultUser() {
        defaultUser = new LoginService()
                .createDefaultUser(new User(DEFAULT_USER_EMAIL, DEFAULT_USER_NAME, DEFAULT_USER_PASSWORD));
    }
}
