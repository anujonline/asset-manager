package com.example.application.views;

import com.example.application.data.Currency;
import com.example.application.services.ViewService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "add-currency", layout = MainLayout.class)
@PageTitle("Add Currency")
public class AddCurrency extends VerticalLayout {
    private final TextField textField = new TextField("Currency");
    private final Button save = new Button("Save");
    private final ViewService viewService;

    public AddCurrency(ViewService viewService) {
        this.viewService = viewService;
        add(textField, save);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(clickEvent -> {
            var currency = new Currency().setDisplayName(textField.getValue());
            viewService.addCurrency(currency);
            updateUI();
        });
        updateUI();
    }

    private void updateUI() {
        UI.getCurrent()
                .access(() -> viewService.currencies()
                        .forEach(currency -> {
                            var span = new Span(currency.getDisplayName());
                            span.getElement().getThemeList().add("badge");
                            add(span);
                        }));
    }
}
