package ru.yandex.services;

import lombok.AllArgsConstructor;
import ru.yandex.model.AccessTokens;

@AllArgsConstructor
public class AuthStorageService {

    private final WaitService waits;

    public AccessTokens getAccessTokens() {
        String accessToken = waits.waitForValueInLocalStorage("accessToken");
        String refreshToken = waits.waitForValueInLocalStorage("refreshToken");

        AccessTokens accessTokens = new AccessTokens();
        if (accessToken != null) {
            accessTokens.setAccessToken(accessToken);
        }
        if (refreshToken != null) {
            accessTokens.setRefreshToken(refreshToken);
        }

        return accessTokens;
    }
}
