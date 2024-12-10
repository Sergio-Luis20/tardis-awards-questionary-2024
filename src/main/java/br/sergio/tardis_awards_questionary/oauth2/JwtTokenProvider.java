package br.sergio.tardis_awards_questionary.oauth2;

import br.sergio.tardis_awards_questionary.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;

import java.net.URI;
import java.time.Instant;

@Slf4j
public class JwtTokenProvider {

    private UserService userService;
    private JwtEncoder jwtEncoder;
    private JwtDecoder jwtDecoder;
    private URI issuer;

    public JwtTokenProvider(UserService userService, JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        issuer = URI.create("http://tardis-awards.fakedomain.com");

        this.userService = userService;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String resolveToken(HttpServletRequest request) {
        String prefix = "Bearer ";
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith(prefix)) {
            return bearerToken.substring(prefix.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            if (jwt.getExpiresAt().isBefore(Instant.now()))
                return false;
            if (!issuer.equals(jwt.getIssuer().toURI()))
                return false;
            return Long.parseLong(jwt.getSubject()) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails user = userService.loadUserByUsername(getSubject(token));
        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }

    public String createToken(String discordId) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(discordId)
                .issuer(issuer.toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(60 * 60)) // 1 hour
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String getSubject(String token) {
        return jwtDecoder.decode(token).getSubject();
    }

}
