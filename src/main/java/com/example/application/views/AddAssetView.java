package com.example.application.views;

import com.example.application.data.Account;
import com.example.application.services.ViewService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.Comparator;

@Route(value = "add-asset", layout = MainLayout.class)
@PageTitle("Add Asset")
public class AddAssetView extends VerticalLayout {
    private final NativeLabel amountPrefix = new NativeLabel("");
    private final NumberField amount = new NumberField("Amount");
    private final ComboBox<Account> accountComboBox = new ComboBox<>("Account");
    private final Button save = new Button("Save");
    private final FormLayout formLayout = new FormLayout(amount, accountComboBox, save);

    public AddAssetView(ViewService viewService) {
        amount.setPrefixComponent(amountPrefix);
        accountComboBox.addValueChangeListener(c -> {
            amountPrefix.setText(CurrencySign.valueOf(c.getValue().getCurrency().getDisplayName()).getSymbol());
        });
        accountComboBox.setAllowCustomValue(false);
        accountComboBox.setItems(viewService.accounts().stream().sorted(Comparator.comparing(Account::getDisplayName)).toList());

        accountComboBox.setItemLabelGenerator(account -> account.getDisplayName() + " " + account.getLabel() + " " + account.getCurrency().getDisplayName());

        add(formLayout);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(clickEvent -> {
            viewService.saveAsset(amount.getValue(), accountComboBox.getValue(), accountComboBox.getValue().getCurrency());
            var assetsSaved = Notification.show("Assets saved");
            assetsSaved.setPosition(Notification.Position.TOP_STRETCH);
            clear();
        });
    }

    private void clear() {
        amount.clear();
        accountComboBox.clear();
    }
}

