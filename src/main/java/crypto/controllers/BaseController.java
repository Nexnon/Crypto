package crypto.controllers;

import crypto.database.OperationDAO;
import crypto.database.RateDAO;
import crypto.database.UserDAO;
import crypto.database.WalletDAO;
import crypto.models.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BaseController {

    @PostMapping("/sing-up")
    public @ResponseBody Map<String, String> registration(@RequestBody Map<String, String> request){
        HashMap<String, String> response = new HashMap<>();
        if(UserDAO.findByEmailUsername(request.get("username"), request.get("email")) == null){
            User newUser = new User(request.get("username"), request.get("email"));

            UserDAO.saveUser(newUser);
            WalletDAO.saveWallet(new Wallet(newUser,"RUB"));
            WalletDAO.saveWallet(new Wallet(newUser,"BTC"));

            response.put("secret_key", newUser.getSecret_key());
        } else{
            response.put("error", "email_already_singup");
        }
        return response;
    }

    @GetMapping("/get-wallets")
    public @ResponseBody Map<String, String> getWallets(@RequestBody Map<String, String> request){
        HashMap<String, String> response = new HashMap<>();

        User user = UserDAO.findByKey(request.get("secret_key"));
        if(user == null){
            response.put("error", "user_not_found");
            return response;
        }

        List<Wallet> walletList = WalletDAO.findByUser(user.getSecret_key());
        for (Wallet wallet : walletList){
            response.put(wallet.getCurrency() + "_wallet", "" + wallet.getValue());
        }
        return response;
    }

    @PostMapping("/fill-up")
    public @ResponseBody Map<String, String> fillUpWallet(@RequestBody Map<String, String> request){
        HashMap<String, String> response = new HashMap<>();

        if(UserDAO.findByKey(request.get("secret_key")) != null){

            List<Wallet> wallets = WalletDAO.findByUser(request.get("secret_key")); ///!!!!!

            for(Wallet w: wallets){
                if(request.containsKey((w.getCurrency()+"_wallet"))){
                    if(Double.parseDouble(request.get((w.getCurrency()+"_wallet"))) <= 0){
                        response.put("error", "value_must_be_positive");
                        return response;
                    }
                    w.setValue(
                            w.getValue()+
                                    Double.parseDouble(
                                            request.get(w.getCurrency()+"_wallet")));
                    WalletDAO.updateWallet(w);

                    OperationDAO.saveOperation(new Operation(1));

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
    public @ResponseBody Map<String, String> withdrawal(@RequestBody Map<String, String> request){
        HashMap<String, String> response = new HashMap<>();

        if(UserDAO.findByKey(request.get("secret_key")) != null){

            List<Wallet> wallets = WalletDAO.findByUser(request.get("secret_key")); /// !!!!!

            for(Wallet w: wallets){
                if(w.getCurrency().equals(request.get("currency"))){
                    if(Double.parseDouble(request.get("count")) <= 0){
                        response.put("error", "count_must_be_positive");
                        return response;
                    }
                    if(Double.parseDouble(request.get("count")) > w.getValue()){
                        response.put("error", "not_enough_money");
                        return response;
                    }
                    w.setValue(
                            w.getValue()-
                                    Double.parseDouble(
                                            request.get("count")));
                    WalletDAO.updateWallet(w);

                    OperationDAO.saveOperation(new Operation(2));

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
    public @ResponseBody Map<String, String> getRate(@RequestBody Map<String, String> request){
        HashMap<String, String> response = new HashMap<>();

        System.out.println(request.get("currency"));

        List<Rate> rates = RateDAO.findByCurrency(request.get("currency"));

        if(rates == null){
            response.put("error", "rate_not_found");
            return response;
        }

        for (Rate rate : rates){
            response.put(rate.getOtherCurrency(request.get("currency")), "" + rate.getRateToCurrency(request.get("currency")));
        }
        return response;
    }

    @PostMapping("/exchange")
    public @ResponseBody Map<String, String> exchange(@RequestBody Map<String, String> request){
        Map<String, String> response = new HashMap<>();
        if(UserDAO.findByKey(request.get("secret_key")) == null){
            response.put("error", "user_not_found");
            return response;
        }
        List<Wallet> wallets = WalletDAO.findByUser(request.get("secret_key"));
        Wallet wallet_from = null;
        Wallet wallet_to = null;

        for(Wallet wallet : wallets){
            if(wallet.getCurrency().equals(request.get("currency_from"))){
                wallet_from = wallet;
                System.out.println(wallet_from.getCurrency());
            }
            if(wallet.getCurrency().equals(request.get("currency_to"))){
                wallet_to = wallet;
                System.out.println(wallet_to.getCurrency());
            }
        }



        if(wallet_from == null || wallet_to == null){
            response.put("error", "wallet_not_found");
            return response;
        }

        if(wallet_from.getValue() < Double.parseDouble(request.get("amount"))){
            response.put("error", "not_enough_money");
            return response;
        }

        Rate rate = RateDAO.findByCurrencies(request.get("currency_from"), request.get("currency_to"));

        wallet_from.setValue(wallet_from.getValue() - Double.parseDouble(request.get("amount")));

        if(rate.getFirst_currency().equals(request.get("currency_from"))){
            wallet_to.setValue(wallet_to.getValue() + rate.getRate() * Double.parseDouble(request.get("amount")));
        } else{
            wallet_to.setValue(wallet_to.getValue() + (1/rate.getRate()) * Double.parseDouble(request.get("amount")));
        }

        WalletDAO.updateWallet(wallet_from);
        WalletDAO.updateWallet(wallet_to);

        OperationDAO.saveOperation(new Operation(3));

        response.put(wallet_from.getCurrency() + "_wallet", ""+wallet_from.getValue());
        response.put(wallet_to.getCurrency() + "_wallet", ""+wallet_to.getValue());

        return response;
    }
}
