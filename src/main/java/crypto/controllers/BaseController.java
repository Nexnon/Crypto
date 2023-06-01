package crypto.controllers;

import crypto.database.RateDAO;
import crypto.database.UserDAO;
import crypto.database.WalletDAO;
import crypto.models.data.UserDB;
import crypto.models.data.WalletDB;
import crypto.models.requests.SingUpRequest;
import crypto.models.requests.getWalletsRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BaseController {

    private UserDAO userDao = new UserDAO();
    private WalletDAO walletDao = new WalletDAO();
    private RateDAO rateDao = new RateDAO();

    @PostMapping("/sing-up")
    public @ResponseBody Map<String, String> registration(@RequestBody SingUpRequest singUpModel){
        HashMap<String, String> response = new HashMap<>();
        if(userDao.findByEmailUsername(singUpModel.getUsername(), singUpModel.getEmail()) == null){
            UserDB newUser = new UserDB(singUpModel.getUsername(), singUpModel.getEmail());

            userDao.saveUser(newUser);
            walletDao.saveWallet(new WalletDB(newUser,"RUB"));
            walletDao.saveWallet(new WalletDB(newUser,"BTC"));

            response.put("secret_key", newUser.getSecret_key());
        } else{
            response.put("error", "email_already_singup");
        }
        return response;
    }

    @GetMapping("/get-wallets")
    public @ResponseBody Map<String, String> getWallets(@RequestBody getWalletsRequest getWalletsRequest){
        //!!!!
        UserDB user = userDao.findByKey(getWalletsRequest.getSecret_key());
        //!!!!
        HashMap<String, String> response = new HashMap<>();
        if(user == null){
            response.put("error", "user_not_found");
            return response;
        }
        List<WalletDB> walletList = walletDao.findByUser(user.getSecret_key());
        for (WalletDB wallet : walletList){
            response.put(wallet.getCurrency() + "_wallet", "" + wallet.getValue());
        }
        return response;
    }
}
