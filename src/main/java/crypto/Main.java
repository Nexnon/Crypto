package crypto;

import crypto.database.OperationDAO;
import crypto.models.Operation;
import crypto.models.Rate;
import crypto.models.Wallet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        Operation.setMax_id();
        Wallet.setMax_id();
        Rate.setMax_id();
        SpringApplication.run(Main.class, args);
    }
}
