package br.sergio.tardis_awards_questionary.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AdminAdder implements CommandLineRunner {

    private UserService userService;
    private Set<String> adminIds;

    public AdminAdder(UserService userService, @Value("${admin.ids}") String adminIds) {
        this.userService = userService;
        this.adminIds = Arrays.stream(adminIds.split(",")).collect(Collectors.toSet());
    }

    @Override
    public void run(String... args) throws Exception {
        for (String id : adminIds) {
            if (!userService.exists(id)) {
                log.info("Adding admin with id: {}", id);

                AppUser admin = new AppUser(id);
                admin.getAuthoritiesSet().add("ADMIN");
                userService.saveUser(admin);
            }
        }
    }

}
