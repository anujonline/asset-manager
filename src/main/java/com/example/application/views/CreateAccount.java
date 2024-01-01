package com.example.application.views;

import com.example.application.data.Account;
import com.example.application.data.Currency;
import com.example.application.services.ViewService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.util.Objects;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Route(value = "create-account", layout = MainLayout.class)
public class CreateAccount extends VerticalLayout {
    private final Notification error = new Notification("Required fields are empty", 5000, Notification.Position.TOP_STRETCH);
    private final Notification success = new Notification("Data saved", 5000, Notification.Position.TOP_STRETCH);
    private final Grid<Account> accountGrid = new Grid<>(Account.class, false);
    public CreateAccount(ViewService viewService) {
        var accountName = new TextField("Account name");
        accountName.setHelperText("Account name could be bank name, stock app name etc.");
        var label = new TextField("Label");
        label.setHelperText("label could be, saving, current, stocks etc.");


        var currencyDropdown = getCurrencyDropdown(viewService);

        add(accountName, label);
        var save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(clickEvent -> {
            if (isBlank(accountName.getValue()) || isBlank(label.getValue()) || Objects.isNull(currencyDropdown.getValue())) {
                error.addThemeVariants(NotificationVariant.LUMO_ERROR);
                error.open();
            } else {
                var account = new Account().setDisplayName(accountName.getValue().toUpperCase()).setLabel(label.getValue().toUpperCase()).setCurrency(new Currency().setDisplayName(currencyDropdown.getValue()));
                viewService.addAccount(account);
                UI.getCurrent().access(() -> {
                    remove(accountGrid);
                    addGrid(viewService);
                    add(accountGrid);
                    accountName.clear();
                    label.clear();
                    currencyDropdown.clear();
                });
                success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                success.open();
            }
        });
        add(currencyDropdown, save);
    }

    private static ComboBox<String> getCurrencyDropdown(ViewService viewService) {
        var currencyDropdown = new ComboBox<String>("Select Currency");
        currencyDropdown.setItemLabelGenerator(String::toString);
        try {
            var currencyNames = viewService.fetchCurrencyNames();
            currencyDropdown.setItems(currencyNames);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return currencyDropdown;
    }

    private void addGrid(ViewService viewService) {
        accountGrid.removeAllColumns();
        var dataProvider = DataProvider.ofCollection(viewService.accounts());
        accountGrid.setItems(dataProvider);
        accountGrid.addColumn(Account::getDisplayName).setHeader("Name");
        accountGrid.addColumn(account -> account.getCurrency().getDisplayName()).setHeader("Currency");
        accountGrid.addColumn(Account::getLabel).setHeader("Type");
    }
}
