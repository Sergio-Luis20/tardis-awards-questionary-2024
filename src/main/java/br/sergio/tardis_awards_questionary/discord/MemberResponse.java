package br.sergio.tardis_awards_questionary.discord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse implements Comparable<MemberResponse> {

    private String discordId, name, avatarUrl, note;
    private MemberResponse second;

    public MemberResponse(Member member) {
        this.discordId = member.getId();
        this.name = member.getEffectiveName();
        this.avatarUrl = member.getEffectiveAvatarUrl();
    }

    public MemberResponse(Member first, Member second) {
        this(first);
        this.second = new MemberResponse(second);
    }

    @Override
    public int compareTo(@NotNull MemberResponse o) {
        return discordId.compareTo(o.discordId);
    }

}
