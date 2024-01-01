package com.example.application.views;

import com.example.application.data.Currency;
import com.example.application.services.ViewService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Route(value = "add-currency", layout = MainLayout.class)
@PageTitle("Add Currency")
public class AddCurrency extends VerticalLayout {
//    private final TextField currencyName = new TextField("Currency");
//    private final Button save = new Button("Save");
//
//    private final ViewService viewService;
//
//    public AddCurrency(ViewService viewService) {
//        this.viewService = viewService;
//        add(currencyName, save);
//        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        save.addClickListener(clickEvent -> {
//            var currency = new Currency().setDisplayName(currencyName.getValue());
//            viewService.addCurrency(currency);
//            updateUI();
//        });
//        updateUI();
//    }
//
//    private void updateUI() {
//        UI.getCurrent()
//                .access(() -> viewService.currencies()
//                        .forEach(currency -> {
//                            var span = new Span(currency.getDisplayName());
//                            span.getElement().getThemeList().add("badge");
//                            add(span);
//                        }));
//    }
public AddCurrency(ViewService viewService) {
    // Fetch and display currencies in a dropdown on view creation
    var currencyDropdown = new ComboBox<String>("Select Currency");
    currencyDropdown.setItemLabelGenerator(String::toString);
    try {
        var currencyNames = viewService.fetchCurrencyNames();
        currencyDropdown.setItems(currencyNames);

    } catch (IOException e) {
        e.printStackTrace();
    }
    add(currencyDropdown);
}
}
