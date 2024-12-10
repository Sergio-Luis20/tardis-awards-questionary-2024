package br.sergio.tardis_awards_questionary.discord;

import org.springframework.util.MultiValueMap;

@FunctionalInterface
public interface DiscordTokenParams {

    MultiValueMap<String, String> getMap();

}
