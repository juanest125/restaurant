package co.com.jestma.model.credential.gateways;

public interface CredentialRepository {
    String getPasswordEncoded(String password);
    Boolean validatePasswords(String password, String encodedPassword);
}
