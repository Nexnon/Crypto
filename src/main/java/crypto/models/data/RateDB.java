package crypto.models.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "currency_rates")
public class RateDB {

    private Long id;
    private static long max_id = 0; /////!!!!!!
    private String first_currency;
    private String second_currency;
    private double rate;

    public RateDB(){
        id = max_id++;
    }

    public String getFirst_currency() {
        return first_currency;
    }

    public void setFirst_currency(String first_currency) {
        this.first_currency = first_currency;
    }

    public String getSecond_currency() {
        return second_currency;
    }

    public void setSecond_currency(String second_currency) {
        this.second_currency = second_currency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
