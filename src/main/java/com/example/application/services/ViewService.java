package com.example.application.services;

import com.example.application.data.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.example.application.services.RoundingUtil.round;

@Service
@RequiredArgsConstructor
public class ViewService {

    private static final String API_URL = "https://open.er-api.com/v6/latest/%s";
    private final AccountRepo accountRepo;
    private final CurrencyRepo currencyRepo;
    private final AssetRepo assetRepo;

    @SneakyThrows
    public static double convertToINR(String selectedCurrencyCode) {
        // Fetch the latest exchange rates from the API
        var formatted = API_URL.formatted(selectedCurrencyCode);
        var url = new URL(formatted);
        var connection = (HttpURLConnection) url.openConnection();
        // Set up a GET request
        connection.setRequestMethod("GET");
        // Get the response and parse it into a JsonObject
        try (var jsonReader = Json.createReader(new InputStreamReader(connection.getInputStream()))) {
            var jsonResponse = jsonReader.readObject();
            // Extract the exchange rate for the selected currency
            var rates = jsonResponse.getJsonObject("rates");
            return rates.getJsonNumber("INR").doubleValue();
        }
    }

    @Transactional
    public void addAccount(Account account) {
        currencyRepo.save(account.getCurrency());
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
        double rate;
        if (!currency.getDisplayName().equals("INR")) {
            rate = convertToINR(currency.getDisplayName());
        } else {
            rate = 0;
        }
        var assetEntity = new AssetEntity();
        assetEntity.setAmount(amount);
        assetEntity.setAccount(account);
        assetEntity.setCurrency(currency);
        assetEntity.setRate(round(rate));
        var save = assetRepo.save(assetEntity);
        System.out.println(save);
    }

    public List<AssetEntity> assets() {
        return assetRepo.findAll();
    }

    public void deleteAsset(Collection<AssetEntity> selectedItems) {
        assetRepo.deleteAll(selectedItems);
    }

    public List<String> fetchCurrencyNames() throws IOException {
        // Replace with a different API URL if needed
        var apiUrl = "https://open.er-api.com/v6/latest";
        var url = new URL(apiUrl);
        var connection = (HttpURLConnection) url.openConnection();
        // Set up a GET request
        connection.setRequestMethod("GET");
        // Get the response and parse it into a JsonObject
        try (var jsonReader = Json.createReader(new InputStreamReader(connection.getInputStream()))) {
            var jsonResponse = jsonReader.readObject();
            // Extract the currencies from the response
            var currencies = jsonResponse.getJsonObject("rates");
            // Get currency names and add them to the list
            List<String> currencyNames = new ArrayList<>();
            for (var currencyCode : currencies.keySet()) {
                currencyNames.add(currencyCode);
            }
            return currencyNames;
        }
    }
}

