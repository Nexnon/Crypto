package crypto.models.requests;

public class getWalletsRequest {
    private String secret_key;

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }
}