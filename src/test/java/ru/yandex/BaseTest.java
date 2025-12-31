package ru.yandex;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import ru.yandex.api.services.LoginService;
import ru.yandex.configs.ConfigReader;
import ru.yandex.configs.DriverHelper;
import ru.yandex.model.AccessTokens;
import ru.yandex.model.User;
import ru.yandex.services.AuthStorageService;
import ru.yandex.services.JsExecutorService;
import ru.yandex.services.WaitService;
import ru.yandex.utils.DataPreparationHelper;

public abstract class BaseTest {
    public final String uriResourceUnderTest = "https://stellarburgers.education-services.ru/";
    public WebDriver driver;
    public User defaultUser;
    protected WaitService waits;
    protected JsExecutorService jsExecutor;
    protected AuthStorageService authStorageService;
    protected LoginService loginService;
    public AccessTokens accessTokensDefaultUser;

    @BeforeClass
    public static void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void initTest() {
        this.driver = new DriverHelper().createDriver();
        this.waits = new WaitService(driver, ConfigReader.asInt("/wait/default"));
        this.jsExecutor = new JsExecutorService(driver);
        this.authStorageService = new AuthStorageService(waits);
        this.loginService = new LoginService();
        openResource();
    }

    @After
    public void tearDown() {
        loginService.deleteUserAfterTest(defaultUser, accessTokensDefaultUser);
        if (driver != null) {
            driver.quit();
        }
    }

    public void openResource() {
        driver.get(uriResourceUnderTest);
    }

    public void createDefaultUser() {
        defaultUser = DataPreparationHelper.generateUniqueUser();
        accessTokensDefaultUser = loginService.createUserBeforeTest(defaultUser);
    }
}
