package co.com.jestma.api.helper;

import co.com.jestma.model.credential.gateways.CredentialRepository;
import co.com.jestma.model.usersession.gateways.UserSessionToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@AllArgsConstructor
public class SecurityJwt implements CredentialRepository, UserSessionToken {
    public static final String PREFIX = "Bearer ";
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

        return PREFIX + token;
    }
    
    private DefaultClaims getClaimFromToken(String token){
        return (DefaultClaims) Jwts
                .parser()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .requireId(jwtId)
                .parse(token.replace(PREFIX, ""))
                .getBody()
                ;
    }
    
    public String getUsername(String token){
        return getClaimFromToken(token).getSubject();
    }

    public Boolean isTokenValid(String token){
        return getClaimFromToken(token).getExpiration().after(new Date(System.currentTimeMillis()));
    }

    @Override
    public String getPasswordEncoded(String password){
        return encoder.encode(password);
    }

    @Override
    public Boolean validatePasswords(String password, String encodedPassword){
        return encoder.matches(password, encodedPassword);
    }

//    public
}
