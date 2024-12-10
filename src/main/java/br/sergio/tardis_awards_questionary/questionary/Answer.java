package br.sergio.tardis_awards_questionary.questionary;

import br.sergio.tardis_awards_questionary.exc.InvalidAnswerException;
import br.sergio.tardis_awards_questionary.user.AppUser;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "answers")
@NoArgsConstructor
@Getter
@ToString
public class Answer {

    @Id
    @Column(name = "discord_id")
    private String discordId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<QuestionAnswer> answers;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "answer")
    private AppUser user;

    @JsonCreator
    public Answer(@JsonProperty("discordId") String discordId, @JsonProperty("answers") Set<QuestionAnswer> answers) {
        try {
            this.discordId = Objects.requireNonNull(discordId, "discordId cannot be null");
            if (answers.isEmpty()) {
                throw new IllegalArgumentException("Answers cannot be empty");
            }
            this.answers = answers;
        } catch (Exception e) {
            throw new InvalidAnswerException(e.getMessage(), e);
        }
    }

}