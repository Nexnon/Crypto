package crypto.models.requests;

import org.springframework.lang.NonNull;

public class SingUpRequest {

    @NonNull
    private String username;
    @NonNull
    private String email;

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }
}
