package com.example.application.services;

import com.example.application.data.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;

import static com.example.application.services.RoundingUtil.round;

@Service
@RequiredArgsConstructor
public class ViewService {

    private final AccountRepo accountRepo;
    private final CurrencyRepo currencyRepo;
    private final AssetRepo assetRepo;
    private final RestTemplate restTemplate;

    public void addAccount(Account account) {
        accountRepo.save(account);
    }

    public void addCurrency(Currency currency) {
        currencyRepo.save(currency);
    }

    public List<Currency> currencies() {
        return currencyRepo.findAll();
    }

    public List<Account> accounts() {
        return accountRepo.findAll();
    }

    public void saveAsset(double amount, Account account, Currency currency) {
        Rate rate;
        if (!currency.getDisplayName().equals("inr")) {
            rate = restTemplate.getForObject("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/%s/inr.json".formatted(currency.getDisplayName()), Rate.class);
        } else {
            rate = new Rate(0);
        }
        var assetEntity = new AssetEntity();
        assetEntity.setAmount(amount);
        assetEntity.setAccount(account);
        assetEntity.setCurrency(currency);
        assetEntity.setRate(round(rate.inr()));
        var save = assetRepo.save(assetEntity);
        System.out.println(save);
    }

    public List<AssetEntity> assets() {
        return assetRepo.findAll();
    }

    public void deleteAsset(Collection<AssetEntity> selectedItems) {
        assetRepo.deleteAll(selectedItems);
    }
}

