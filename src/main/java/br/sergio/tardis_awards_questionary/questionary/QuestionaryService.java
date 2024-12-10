package br.sergio.tardis_awards_questionary.questionary;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QuestionaryService {

    private AnswerRepository repository;

    public boolean exists(String discordId) {
        return repository.existsById(discordId);
    }

    public Answer post(Answer answer) {
        return repository.save(answer);
    }

    public List<Answer> getAll() {
        return repository.findAll();
    }

    public Optional<Answer> getAnswer(String discordId) {
        return repository.findById(discordId);
    }

}
