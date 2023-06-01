package crypto.models.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "currency_rates")
public class RateDB {

    private Long id;
    private String first_currency;
    private String second_currency;
    private double rate;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
