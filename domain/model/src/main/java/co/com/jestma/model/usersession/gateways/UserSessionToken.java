package co.com.jestma.model.usersession.gateways;

public interface UserSessionToken {
    String getToken(String email);
    String getUsername(String token);
    Boolean isTokenValid(String token);
}
