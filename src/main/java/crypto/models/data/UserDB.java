package crypto.models.data;

import jakarta.persistence.*;
import org.hibernate.type.NumericBooleanConverter;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "users")
public class UserDB {

    @Id
    private String secret_key;
    @Column(name="user_name")
    private String username;
    private String email;
    @Convert(converter = NumericBooleanConverter.class)
    private boolean isAdmin;

    public UserDB(){}

    public UserDB(String username, String email){
        this.username = username;
        this.email = email;
        md();
        isAdmin = false;
    }

    private void md(){
        try {
            String plaintext = email;
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(plaintext.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            secret_key = bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
