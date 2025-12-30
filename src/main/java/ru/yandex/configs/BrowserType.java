package ru.yandex.configs;

public enum BrowserType {
    CHROME,
    YANDEX;

    public static BrowserType fromString(String browserName) {
        try {
            return BrowserType.valueOf(browserName.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return CHROME;
        }
    }
}
