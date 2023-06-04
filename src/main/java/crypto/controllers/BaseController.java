package crypto.controllers;

import crypto.database.OperationDAO;
import crypto.database.RateDAO;
import crypto.database.UserDAO;
import crypto.database.WalletDAO;
import crypto.models.*;
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
    private OperationDAO operationDAO = new OperationDAO();

    @PostMapping("/sing-up")
    public @ResponseBody Map<String, String> registration(@RequestBody SingUpRequest singUpModel){
        HashMap<String, String> response = new HashMap<>();
        if(userDao.findByEmailUsername(singUpModel.getUsername(), singUpModel.getEmail()) == null){
            User newUser = new User(singUpModel.getUsername(), singUpModel.getEmail());

            userDao.saveUser(newUser);
            walletDao.saveWallet(new Wallet(newUser,"RUB"));
            walletDao.saveWallet(new Wallet(newUser,"BTC"));

            response.put("secret_key", newUser.getSecret_key());
        } else{
            response.put("error", "email_already_singup");
        }
        return response;
    }

    @GetMapping("/get-wallets")
    public @ResponseBody Map<String, String> getWallets(@RequestBody getWalletsRequest getWalletsRequest){
        //!!!!
        User user = userDao.findByKey(getWalletsRequest.getSecret_key());
        //!!!!
        HashMap<String, String> response = new HashMap<>();
        if(user == null){
            response.put("error", "user_not_found");
            return response;
        }
        List<Wallet> walletList = walletDao.findByUser(user.getSecret_key());
        for (Wallet wallet : walletList){
            response.put(wallet.getCurrency() + "_wallet", "" + wallet.getValue());
        }
        return response;
    }

    @PostMapping("/fill-up")
    public @ResponseBody Map<String, String> fillUpWallet(@RequestBody Map<String, String> fillUpWalletRequest){
        HashMap<String, String> response = new HashMap<>();

        if(userDao.findByKey(fillUpWalletRequest.get("secret_key")) != null){

            List<Wallet> wallets = walletDao.findByUser(fillUpWalletRequest.get("secret_key")); ///!!!!!

            for(Wallet w: wallets){
                if(fillUpWalletRequest.containsKey((w.getCurrency()+"_wallet"))){
                    if(Double.parseDouble(fillUpWalletRequest.get((w.getCurrency()+"_wallet"))) <= 0){
                        response.put("error", "value_must_be_positive");
                        return response;
                    }
                    w.setValue(
                            w.getValue()+
                                    Double.parseDouble(
                                            fillUpWalletRequest.get(w.getCurrency()+"_wallet")));
                    walletDao.updateWallet(w);

                    operationDAO.saveOperation(new Operation(1));

                    response.put(w.getCurrency() + "_wallet", "" + w.getValue());
                }
            }
            if(response.isEmpty()){
                response.put("error", "wallet_not_found");
            }

        } else{
            response.put("error", "user_not_found");
        }
        return response;
    }

    @PostMapping("/withdrawal")
    public @ResponseBody Map<String, String> withdrawal(@RequestBody Map<String, String> withdrawalRequest){
        HashMap<String, String> response = new HashMap<>();

        if(userDao.findByKey(withdrawalRequest.get("secret_key")) != null){

            List<Wallet> wallets = walletDao.findByUser(withdrawalRequest.get("secret_key")); /// !!!!!

            for(Wallet w: wallets){
                if(w.getCurrency().equals(withdrawalRequest.get("currency"))){
                    if(Double.parseDouble(withdrawalRequest.get("count")) <= 0){
                        response.put("error", "count_must_be_positive");
                        return response;
                    }
                    if(Double.parseDouble(withdrawalRequest.get("count")) > w.getValue()){
                        response.put("error", "not_enough_money");
                        return response;
                    }
                    w.setValue(
                            w.getValue()-
                                    Double.parseDouble(
                                            withdrawalRequest.get("count")));
                    walletDao.updateWallet(w);

                    operationDAO.saveOperation(new Operation(2));

                    response.put(w.getCurrency() + "_wallet", "" + w.getValue());
                }
            }
            if(response.isEmpty()){
                response.put("error", "wallet_not_found");
            }

        } else{
            response.put("error", "user_not_found");
        }
        return response;
    }

    @GetMapping("/get-rates")
    public @ResponseBody Map<String, String> getRate(@RequestBody Map<String, String> getRateRequest){
        HashMap<String, String> response = new HashMap<>();

        System.out.println(getRateRequest.get("currency"));

        List<Rate> rates = rateDao.findByCurrency(getRateRequest.get("currency"));

        if(rates == null){
            response.put("error", "rate_not_found");
            return response;
        }

        for (Rate rate : rates){
            if(rate.getFirst_currency().equals(getRateRequest.get("currency"))){
                response.put(rate.getSecond_currency(), "" + rate.getRate()); //1111
            } else{
                response.put(rate.getFirst_currency(), "" + (1/rate.getRate())); //1111
            }
        }
        return response;
    }

    @PostMapping("/exchange")
    public @ResponseBody Map<String, String> exchange(@RequestBody Map<String, String> exchangeRequest){
        Map<String, String> response = new HashMap<>();
        if(userDao.findByKey(exchangeRequest.get("secret_key")) == null){
            response.put("error", "user_not_found");
            return response;
        }
        List<Wallet> wallets = walletDao.findByUser(exchangeRequest.get("secret_key"));
        Wallet wallet_from = null;
        Wallet wallet_to = null;

        for(Wallet wallet : wallets){
            if(wallet.getCurrency().equals(exchangeRequest.get("currency_from"))){
                wallet_from = wallet;
                System.out.println(wallet_from.getCurrency());
            }
            if(wallet.getCurrency().equals(exchangeRequest.get("currency_to"))){
                wallet_to = wallet;
                System.out.println(wallet_to.getCurrency());
            }
        }



        if(wallet_from == null || wallet_to == null){
            response.put("error", "wallet_not_found");
            return response;
        }

        if(wallet_from.getValue() < Double.parseDouble(exchangeRequest.get("amount"))){
            response.put("error", "not_enough_money");
            return response;
        }

        Rate rate = rateDao.findByCurrencies(exchangeRequest.get("currency_from"), exchangeRequest.get("currency_to"));

        wallet_from.setValue(wallet_from.getValue() - Double.parseDouble(exchangeRequest.get("amount")));

        if(rate.getFirst_currency().equals(exchangeRequest.get("currency_from"))){
            wallet_to.setValue(wallet_to.getValue() + rate.getRate() * Double.parseDouble(exchangeRequest.get("amount")));
        } else{
            wallet_to.setValue(wallet_to.getValue() + (1/rate.getRate()) * Double.parseDouble(exchangeRequest.get("amount")));
        }

        walletDao.updateWallet(wallet_from);
        walletDao.updateWallet(wallet_to);

        operationDAO.saveOperation(new Operation(3));

        response.put(wallet_from.getCurrency() + "_wallet", ""+wallet_from.getValue());
        response.put(wallet_to.getCurrency() + "_wallet", ""+wallet_to.getValue());

        return response;
    }
}
