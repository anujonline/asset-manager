package com.example.application.views;

import com.example.application.data.AssetEntity;
import com.example.application.services.RoundingUtil;
import com.example.application.services.ViewService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Comparator;

import static com.example.application.services.RoundingUtil.round;

@Route(value = "", layout = MainLayout.class)
@PageTitle("View Assets")
public class ViewAssets extends VerticalLayout {
    private final Grid<AssetEntity> grid = new Grid<>(AssetEntity.class, false);
    private final H4 sum = new H4();
    private final Button calculate = new Button("Calculate");
    private final ComboBox<LocalDate> month = new ComboBox<>("Month");
    private static final DecimalFormat formatter = new DecimalFormat("#,###.00");

    public ViewAssets(ViewService viewService) {
        scrollIntoView();
        grid.setSizeFull();
        var assets = viewService.assets().stream().sorted(Comparator.comparing(o -> o.getAccount().getDisplayName())).toList();
        var months = assets.stream().map(assetEntity -> assetEntity.getLocalDateTime().toLocalDate())
                .distinct()
                .toList();
        month.setItems(months);
        add(month, calculate, this.sum);

        var assetEntityGridListDataView = grid.setItems(assets);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(assetEntity1 -> assetEntity1.getAccount().getDisplayName()).setHeader("Account");
        grid.addColumn(assetEntity1 -> assetEntity1.getAccount().getLabel()).setHeader("Type");
        grid.addColumn(assetEntity1 -> assetEntity1.getCurrency().getDisplayName()).setHeader("Currency");
        grid.addColumn(assetEntity1 -> {
            var rate = assetEntity1.getRate();
            return rate>0.0?assetEntity1.getRate():"NA";

        }).setHeader("Rate");
        grid.addColumn(AssetEntity::getAmount).setHeader("Amount");
        grid.addColumn(RoundingUtil::displayAmount).setHeader("Amount in rupees").setSortable(true);
        grid.addColumn(AssetEntity::getLocalDateTime).setHeader("Date").setSortable(true);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        setSizeFull();
        calculate.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        var delete = new Button("Delete");
        add(grid, delete);

        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(clickEvent -> {
            try{
                if(grid.getSelectedItems().size()<=0) throw new RuntimeException();
                var dialog = new Dialog("Are you sure?");
                var yes = new Button("Yes", c -> {
                    viewService.deleteAsset(grid.getSelectedItems());
                    dialog.close();
                    UI.getCurrent().getPage().reload();
                });
                var no = new Button("No", c -> dialog.close());
                yes.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                no.addThemeVariants(ButtonVariant.LUMO_ERROR);
                dialog.add(yes, no);
                add(dialog);
                dialog.open();
            }
            catch (Exception e){
                var noItemsSelected = Notification.show("No items selected");
                noItemsSelected.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        calculate.addClickListener(clickEvent -> {
            var optionalValue = month.getOptionalValue();
            optionalValue.ifPresentOrElse(date -> {
                var sum = assetEntityGridListDataView.getItems()
                        .filter(assetEntity -> (assetEntity.getLocalDateTime().toLocalDate().equals(date))
                                &&
                                (assetEntity.getLocalDateTime().getYear() == date.getYear()))
                        .map(RoundingUtil::displayAmount).reduce(0.0, Double::sum);
//            var sum = grid.getSelectedItems()
//                    .stream().filter(assetEntity -> assetEntity.getLocalDateTime().toLocalDate().equals(month.getValue())).map(RoundingUtil::displayAmount).reduce(0.0, Double::sum);
                UI.getCurrent().access(() -> {
                    this.sum.setText("For month " + date + " total value is ₹ " + formatter.format(BigDecimal.valueOf(round(sum))));
                });
            }, () -> {
                var selectDate = Notification.show("Select date");
                selectDate.addThemeVariants(NotificationVariant.LUMO_ERROR);
                selectDate.setPosition(Notification.Position.TOP_STRETCH);
            });

        });
    }


}
