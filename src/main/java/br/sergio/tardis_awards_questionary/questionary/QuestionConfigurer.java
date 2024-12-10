package br.sergio.tardis_awards_questionary.questionary;

import br.sergio.tardis_awards_questionary.discord.DiscordService;
import br.sergio.tardis_awards_questionary.discord.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Component
@AllArgsConstructor
@Slf4j
public class QuestionConfigurer implements CommandLineRunner {

    private DiscordService discordService;

    @Override
    public void run(String... args) throws Exception {
        try (InputStream stream = getClass().getResourceAsStream("/simple-questions.csv")) {
            if (stream == null) {
                throw new NullPointerException("File \"simple-questions.csv\" not found in classpath");
            }
            List<Question> questions = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            Map<String, String> notes = new HashMap<>();
            for (String line; (line = reader.readLine()) != null;) {
                notes.clear();
                String[] tokens = line.split(",");
                int questionId = Integer.parseInt(tokens[0]);
                String questionText = tokens[1];
                String[] idsTokens = Arrays.copyOfRange(tokens, 2, tokens.length);
                String[] discordIds = new String[idsTokens.length];
                for (int i = 0; i < discordIds.length; i++) {
                    String token = idsTokens[i];
                    int spaceIndex = token.indexOf(' ');
                    if (spaceIndex < 0) {
                        discordIds[i] = token;
                        continue;
                    }
                    String id = token.substring(0, spaceIndex);
                    discordIds[i] = id;
                    String note = token.substring(spaceIndex + 1);
                    if (note.startsWith("(") && note.endsWith(")")) {
                        note = note.substring(1, note.length() - 1);
                    }
                    notes.put(id, note);
                }
                Question question = simpleQuestion(questionId, questionText, discordIds);
                Set<MemberResponse> members = question.options();
                for (MemberResponse member: members) {
                    String discordId = member.getDiscordId();
                    if (notes.containsKey(discordId)) {
                        member.setNote(notes.get(discordId));
                    }
                }
                questions.add(question);
            }
            questions.add(new Question(25, "Qual é o melhor shipp do servidor?", 2, null));
            questions.add(new Question(26, "Quem é o mais amado do servidor?", 1, null));
            questions.add(new Question(27, "Quem é a mais gasosa (não necessariamente mulher) do servidor?", 1, null));
            questions.add(new Question(28, "Quem é o louco internado do ano?", 1, null));
            Question.setQuestions(questions);
        }
    }

    private Question simpleQuestion(int id, String question, String[] discordIds) {
        Set<MemberResponse> members = new HashSet<>(discordIds.length);
        for (String discordId : discordIds) {
            if (discordId.indexOf('_') >= 0) {
                String[] ids = discordId.split("_");
                Member first = discordService.getMember(ids[0]);
                Member second = discordService.getMember(ids[1]);
                members.add(new MemberResponse(first, second));
            } else {
                members.add(new MemberResponse(discordService.getMember(discordId)));
            }
        }
        return new Question(id, question, 1, members);
    }

}
