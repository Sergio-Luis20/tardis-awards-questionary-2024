package br.sergio.tardis_awards_questionary.user;

import net.dv8tion.jda.api.entities.Member;

public record UserResponse(String discordId, String name, String avatarUrl, boolean voted, boolean admin) {

    public UserResponse(Member member, boolean voted, boolean admin) {
        this(member.getId(), member.getEffectiveName(), member.getEffectiveAvatarUrl(), voted, admin);
    }

}