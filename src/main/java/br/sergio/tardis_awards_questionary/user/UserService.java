package br.sergio.tardis_awards_questionary.user;

import br.sergio.tardis_awards_questionary.discord.DiscordService;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepository repository;
    private DiscordService discordService;
    private EntityManager entityManager;

    public AppUser createNewUser(String discordId) {
        Optional<AppUser> user = repository.findById(discordId);

        if (user.isPresent()) {
            return user.get();
        } else if (!discordService.containsMemberById(discordId)) {
            return null;
        } else {
            return repository.save(new AppUser(discordId));
        }
    }

    public void saveUser(AppUser user) {
        entityManager.detach(user);
        repository.save(user);
    }

    public List<AppUser> getAll() {
        return repository.findAll();
    }

    public boolean exists(String id) {
        return repository.existsById(id);
    }

    public AppUser getUser(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public AppUser loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            AppUser user = getUser(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found for id: " + username);
            }
            return user;
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Not a discord id: " + username);
        }
    }

}
