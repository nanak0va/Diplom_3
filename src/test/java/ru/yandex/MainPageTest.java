package ru.yandex;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Test;
import ru.yandex.pages.MainPage;

public class MainPageTest extends BaseTest {

    @Test
    @DisplayName("Переход к разделу «Булки»: вкладка становится активной")
    public void shouldActivateBunsTabWhenClicked() {
        MainPage mainPage = new MainPage(waits, jsExecutor)
                .waitToLoadMainPage()
                .clickOnFillingsTab()
                .clickOnBunsTab();

        Assert.assertTrue(mainPage.isBunsTabActive());
    }

    @Test
    @DisplayName("Переход к разделу «Начинки»: вкладка становится активной")
    public void shouldActivateFillingsTabWhenClicked() {
        MainPage mainPage = new MainPage(waits, jsExecutor)
                .waitToLoadMainPage()
                .clickOnBunsTab()
                .clickOnFillingsTab();

        Assert.assertTrue(mainPage.isFillingsTabActive());
    }

    @Test
    @DisplayName("Переход к разделу «Соусы»: вкладка становится активной")
    public void shouldActivateSaucesTabWhenClicked() {
        MainPage mainPage = new MainPage(waits, jsExecutor)
                .waitToLoadMainPage()
                .clickOnBunsTab()
                .clickOnSaucesTab();

        Assert.assertTrue(mainPage.isSaucesTabActive());
    }
}
