package crypto.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="wallets")
public class Wallet {

    private Long id;
    private static long max_id = 0; //!!!!!

    private String secret_key;
    private String currency;
    private double value;

    public Wallet(){}

    public Wallet(User user, String currency){
        this.secret_key = user.getSecret_key();
        this.currency = currency;
        this.value = 0;
        this.id = max_id++;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String security_key) {
        this.secret_key = security_key;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
