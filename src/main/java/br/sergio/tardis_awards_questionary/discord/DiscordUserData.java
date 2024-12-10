package br.sergio.tardis_awards_questionary.discord;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Locale;

public record DiscordUserData(
        String id,
        String username,
        String avatar,
        @JsonProperty("avatar_decoration") String avatarDecoration,
        String discriminator,
        @JsonProperty("public_flags") int publicFlags,
        int flags,
        String banner,
        @JsonProperty("banner_color") String bannerColor,
        @JsonProperty("accent_color") String accentColor,
        Locale locale,
        @JsonProperty("nfa_enabled") boolean nfaEnabled,
        @JsonProperty("premium_type") int premiumType
) {
}
