package co.com.jestma.api.helper;

import co.com.jestma.model.credential.gateways.CredentialRepository;
import co.com.jestma.model.usersession.gateways.UserSessionToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@AllArgsConstructor
public class SecurityJwt implements CredentialRepository, UserSessionToken {
    @Value("${security.secretKey}")
    private String secretKey;
    @Value("${security.jwtId}")
    private String jwtId;
    @Value("${security.tokenExpirationTimeMillis}")
    private Long tokenExpirationTimeMillis;

    private final PasswordEncoder encoder;

    @Override
    public String getToken(String email){
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");
        String token = Jwts
                .builder()
                .setId(jwtId)
                .setSubject(email)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationTimeMillis))
                .signWith(SignatureAlgorithm.HS512,secretKey.getBytes())
                .compact();

        return "Bearer " + token;
    }

    @Override
    public String getPasswordEncoded(String password){
        return encoder.encode(password);
    }

    @Override
    public Boolean validatePasswords(String password, String encodedPassword){
        return encoder.matches(password, encodedPassword);
    }
}
