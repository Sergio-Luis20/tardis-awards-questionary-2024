package br.sergio.tardis_awards_questionary.user;

import br.sergio.tardis_awards_questionary.questionary.Answer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class AppUser implements UserDetails {

    @Id
    @Column(name = "discord_id", nullable = false, updatable = false)
    private String discordId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "answer_id", referencedColumnName = "discord_id")
    private Answer answer;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> authorities;

    public AppUser(String discordId) {
        this.discordId = Objects.requireNonNull(discordId, "discordId");
        authorities = new HashSet<>();
        authorities.add("USER");
    }

    public boolean hasVoted() {
        return getAnswer() != null;
    }

    public Set<String> getAuthoritiesSet() {
        return authorities;
    }

    public boolean isAdmin() {
        return authorities.contains("ADMIN");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream().map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return discordId;
    }

}
