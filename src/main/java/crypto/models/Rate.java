package crypto.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "currency_rates")
public class Rate {

    private Long id;
    private static long max_id = 0; /////!!!!!!
    private String first_currency;
    private String second_currency;
    private double rate;

    public Rate(){
        id = max_id++;
    }

    public String getFirst_currency() {
        return first_currency;
    }

    public void setFirst_currency(String first_currency) {
        this.first_currency = first_currency;
    }

    public String getOtherCurrency(String baseCurrency){
        if(baseCurrency.equals(first_currency)){
            return second_currency;
        } else{ ///!!!!!!
            return first_currency;
        }
    }
    public void setRateToCurrency(String currency, double rate){
        if(currency.equals(first_currency)){
            this.rate = rate;
        } else{
            this.rate = 1/rate;
        }
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
