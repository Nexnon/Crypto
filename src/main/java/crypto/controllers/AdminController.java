package crypto.controllers;

import crypto.database.OperationDAO;
import crypto.database.RateDAO;
import crypto.database.UserDAO;
import crypto.database.WalletDAO;
import crypto.models.Operation;
import crypto.models.Rate;
import crypto.models.Wallet;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AdminController {

    private UserDAO userDao = new UserDAO();
    private WalletDAO walletDao = new WalletDAO();
    private RateDAO rateDao = new RateDAO();
    private OperationDAO operationDAO = new OperationDAO();

    @PostMapping("/change-rate")
    public @ResponseBody Map<String, String> changeRate(@RequestBody Map<String, String> changeRequest){
        Map<String, String> response = new HashMap<>();

        String base_currency = changeRequest.get("base_currency");
        String secret_key = changeRequest.get("secret_key");
        List<Rate> rates = rateDao.findByCurrency(base_currency);

        if(!userDao.findByKey(secret_key).isAdmin()){
            response.put("error", "you_dont_have_enough_permissions");
            return response;
        }

        for(Rate rate : rates){
            double value = Double.parseDouble(changeRequest.get(rate.getOtherCurrency(base_currency)));
            rate.setRateToCurrency(base_currency, value);
            response.put(rate.getOtherCurrency(base_currency), ""+value);
            rateDao.updateRate(rate);
        }

        return response;
    }

    @GetMapping("/get-all")
    public @ResponseBody Map<String, String> getAll(@RequestBody Map<String, String> getAllRequest){
        Map<String, String> response = new HashMap<>();

        String secret_key = getAllRequest.get("secret_key");
        String currency = getAllRequest.get("currency");

        if(!userDao.findByKey(secret_key).isAdmin()){
            response.put("error", "you_dont_have_enough_permissions");
            return response;
        }

        List<Wallet> wallets = walletDao.findByCurrency(currency);
        if(wallets == null){
            response.put("error", "wallets_not_found");
            return response;
        }
        double sum = 0;
        for(Wallet wallet: wallets){
            sum += wallet.getValue();
        }

        response.put(currency, ""+sum);

        return response;
    }

    @GetMapping("/oper-count")
    public @ResponseBody Map<String, String> getOperationsCount(@RequestBody Map<String, String> request){
        Map<String, String> response = new HashMap<>();

        if(!userDao.findByKey(request.get("secret_key")).isAdmin()){
            response.put("error", "you_dont_have_enough_permissions");
            return response;
        }
        try{
        SimpleDateFormat parser = new SimpleDateFormat("dd.MM.yyyy");
        Date dateFrom = parser.parse(request.get("date_from"));
        Date dateTo = parser.parse(request.get("date_to"));

        List operations = operationDAO.findByDates(dateFrom, dateTo);
        response.put("transaction_count", ""+operations.size());

        return response;
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return response;
    }
}
