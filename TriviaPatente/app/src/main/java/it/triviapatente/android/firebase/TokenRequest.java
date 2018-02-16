package it.triviapatente.android.firebase;

import java.util.regex.Pattern;

import it.triviapatente.android.models.auth.User;

/**
 * Created by donadev on 16/02/18.
 */

public class TokenRequest {
    private static final String SEPARATOR = "|||";

    private String deviceId;
    private String token;
    private Long userId;

    public TokenRequest(String deviceId, String token, Long userId) {
        this.deviceId = deviceId;
        this.token = token;
        this.userId = userId;
    }
    public String serialize() {
        return token + SEPARATOR + deviceId + SEPARATOR + userId;
    }
    public Boolean is(String deviceId, String token, User user) {
        return this.deviceId.equals(deviceId) && this.token.equals(token) && this.userId.equals(user.id);
    }
    public static TokenRequest from(String input) {
        try {
            String[] values = input.split(Pattern.quote(SEPARATOR));
            String token = values[0];
            String deviceId = values[1];
            Long userId = Long.parseLong(values[2]);
            return new TokenRequest(deviceId, token, userId);
        } catch (Exception e) {
            return null;
        }
    }
}
