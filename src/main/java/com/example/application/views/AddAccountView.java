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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Objects;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Route(value = "add-account", layout = MainLayout.class)
@PageTitle("Add Account")
public class AddAccountView extends VerticalLayout {
    private final TextField accountName = new TextField("Account Name");
    private final TextField label = new TextField("Label");
    private final ComboBox<Currency> accountCurrency = new ComboBox<>("Account currency");
    private final Button save = new Button("Save");
    private final Grid<Account> accountGrid = new Grid<>(Account.class, false);
    private final Notification error = new Notification("Required fields are empty", 5000, Notification.Position.TOP_STRETCH);
    private final Notification success = new Notification("Data saved", 5000, Notification.Position.TOP_STRETCH);

    public AddAccountView(ViewService viewService) {

        accountName.setRequired(true);
        label.setRequired(true);
        accountCurrency.setRequired(true);
        setSizeFull();
        add(accountName, accountCurrency, label, save, accountGrid);
        accountCurrency.setItems(viewService.currencies());
        accountCurrency.setItemLabelGenerator(Currency::getDisplayName);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(clickEvent -> {
            if (isBlank(accountName.getValue()) || isBlank(label.getValue()) || Objects.isNull(accountCurrency.getValue())) {
                error.addThemeVariants(NotificationVariant.LUMO_ERROR);
                error.open();
            } else {
                var account = new Account().setDisplayName(accountName.getValue().toUpperCase()).setLabel(label.getValue().toUpperCase()).setCurrency(accountCurrency.getValue());
                viewService.addAccount(account);
                UI.getCurrent().access(() -> {
                    remove(accountGrid);
                    addGrid(viewService);
                    add(accountGrid);
                    accountName.clear();
                    label.clear();
                    accountCurrency.clear();
                });
                success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                success.open();
            }
        });
        addGrid(viewService);
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
