package br.sergio.tardis_awards_questionary.questionary;

import br.sergio.tardis_awards_questionary.exc.InvalidAnswerException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@ToString
public class QuestionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int questionId;

    @ElementCollection
    private Set<String> membersIds;

    @JsonCreator
    public QuestionAnswer(@JsonProperty("questionId") int questionId, @JsonProperty("membersIds") Set<String> membersIds) {
        try {
            if (questionId < 0 || questionId >= Question.questions().size()) {
                throw new IndexOutOfBoundsException("questionId out of bounds");
            }
            this.questionId = questionId;
            this.membersIds = Objects.requireNonNull(membersIds, "membersIds cannot be null");
            Question question = Question.questions().get(questionId);
            if (membersIds.size() != question.numAnswers()) {
                throw new IllegalArgumentException("Members ids amount is different from required");
            }
            for (String id : membersIds) {
                if (id.indexOf('_') < 0) {
                    if (Long.parseLong(id) <= 0) {
                        throw new IllegalArgumentException("Invalid discord user id: " + id);
                    }
                } else {
                    String[] ids = id.split("_");
                    if (ids.length != 2) {
                        throw new IllegalArgumentException("Illegal id pair");
                    }
                    long id1 = Long.parseLong(ids[0]);
                    long id2 = Long.parseLong(ids[1]);
                    if (id1 == id2) {
                        throw new IllegalArgumentException("Pair ids can't be equal");
                    }
                }
            }
        } catch (Exception e) {
            throw new InvalidAnswerException(e.getMessage(), e);
        }
    }

}
