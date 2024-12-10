package br.sergio.tardis_awards_questionary.user;

import net.dv8tion.jda.api.entities.Member;

public record BasicUserResponse(String discordId, String name, String avatarUrl) {

    public BasicUserResponse(Member member) {
        this(member.getId(), member.getEffectiveName(), member.getEffectiveAvatarUrl());
    }

}
