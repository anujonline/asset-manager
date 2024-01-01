package com.example.application.views;

import com.example.application.data.Account;
import com.example.application.data.AssetEntity;
import com.example.application.services.RoundingUtil;
import com.example.application.services.ViewService;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.HorizontalAlign;
import com.github.appreciated.apexcharts.config.xaxis.TickPlacement;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Route(value = "graphic-own-currency", layout = MainLayout.class)
@PageTitle("View Assets")
public class GraphicViewAssetOwnCurrency extends VerticalLayout {

    public GraphicViewAssetOwnCurrency(ViewService viewService) {
        var expenses = viewService.assets();
        var localDates = expenses.stream().map(assetEntity -> assetEntity.getLocalDateTime().toLocalDate()).sorted(LocalDate::compareTo).distinct().toList();


        Map<Account, Map<LocalDate, Double>> userMonthSums = new HashMap<>();
        for (AssetEntity transaction : expenses) {
            userMonthSums.computeIfAbsent(transaction.getAccount(), key -> new TreeMap<>()).merge(transaction.getLocalDateTime().toLocalDate(), transaction.getAmount(), Double::sum);
        }

        var collect = userMonthSums.values().stream().map(Map::keySet).distinct().flatMap(Collection::stream).sorted(LocalDate::compareTo).map(LocalDate::toString).toList();


        var apexChartsBuilder = new ApexChartsBuilder();
        apexChartsBuilder.withColors(
                "#e6194b", "#3cb44b", "#ffe119", "#4363d8", "#f58231", "#911eb4", "#46f0f0", "#f032e6", "#bcf60c", "#fabebe", "#008080", "#e6beff", "#9a6324", "#fffac8", "#800000", "#aaffc3", "#808000", "#ffd8b1", "#000075", "#808080", "#ffffff", "#000000"
        );
        apexChartsBuilder
                .withChart(ChartBuilder.get()
                        .withType(Type.AREA)
                        .build())
                .withXaxis(XAxisBuilder.get()
                        .withMin(100.0)
                        .withCategories(collect)
                        .withTickPlacement(TickPlacement.BETWEEN)
                        .build());

        setSizeFull();
        var list = new ArrayList<Series<Double>>();
        userMonthSums
                .forEach((s, monthDoubleMap) -> {
                    Series<Double> tSeries = new Series<>("%s %s".formatted(s.getDisplayName(), s.getLabel()), monthDoubleMap.values().toArray(Double[]::new));
                    list.add(tSeries);
                });
        apexChartsBuilder
                .withSeries(
                        list.toArray(Series[]::new)
                );
        apexChartsBuilder.withLegend(LegendBuilder.get().withHorizontalAlign(HorizontalAlign.LEFT).build());
        UI.getCurrent().getPage().executeJs("""
                ""","");
        add(apexChartsBuilder.build());

    }
}
