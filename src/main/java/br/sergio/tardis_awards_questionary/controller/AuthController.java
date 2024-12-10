package br.sergio.tardis_awards_questionary.controller;

import br.sergio.tardis_awards_questionary.discord.DiscordService;
import br.sergio.tardis_awards_questionary.oauth2.JwtTokenProvider;
import br.sergio.tardis_awards_questionary.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@Slf4j
@AllArgsConstructor
public class AuthController {

    private DiscordService discordService;
    private UserService userService;
    private JwtTokenProvider provider;

    @GetMapping("/discord/callback")
    public ResponseEntity<?> discordCallback(@AuthenticationPrincipal OAuth2User user) {
        String discordId = user.getAttribute("id");
        String username = user.getAttribute("username");

        if (discordId == null || username == null) {
            log.error("OAuth2User with discordId or username null");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            if (userService.createNewUser(discordId) == null) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (NumberFormatException e) {
            log.info("Could not parse id");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String token = provider.createToken(discordId);
        URI page = UriComponentsBuilder.fromUriString(discordService.getAuthenticatedUri())
                .queryParam("token", token)
                .build()
                .toUri();

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(page)
                .build();
    }

//    @PostMapping("/login-admin")
//    public ResponseEntity<String> loginAdmin(@RequestBody AdminUserLoginRequestBody body) {
//        if (!body.isValid()) {
//            return ResponseEntity.badRequest().build();
//        }
//        UserDetails admin;
//        try {
//            admin = admins.loadUserByUsername(body.username());
//        } catch (UsernameNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//        if (!encoder.matches(body.password(), admin.getPassword())) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//
//        Instant now = Instant.now();
//        JwtClaimsSet claims = JwtClaimsSet.builder()
//                .subject(adminSubject)
//                .issuer("TARDIS Awards Questionary")
//                .issuedAt(now)
//                .expiresAt(now.plusSeconds(10 * 60)) // 10 minutes
//                .build();
//
//        Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));
//        String token = jwt.getTokenValue();
//
//        return ResponseEntity.ok()
//                .header("Content-Type", "text/plain")
//                .body(token);
//    }
//
//    public record AdminUserLoginRequestBody(String username, String password) {
//
//        public boolean isValid() {
//            return username != null && password != null;
//        }
//
//    }

}
