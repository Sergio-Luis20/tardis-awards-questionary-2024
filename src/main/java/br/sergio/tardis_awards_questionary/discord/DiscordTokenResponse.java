package br.sergio.tardis_awards_questionary.discord;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;

import java.util.Collections;
import java.util.Set;

public record DiscordTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") long expiresIn,
        @JsonProperty("refresh_token") String refreshToken,
        String scope
) {

    public TokenType getTokenType() {
        return TokenType.BEARER.getValue().equalsIgnoreCase(tokenType) ? TokenType.BEARER : null;
    }

    public Set<String> getScopes() {
        return scope == null || scope.isEmpty() ? Collections.emptySet() : Set.of(scope.split("\\s+"));
    }

}
