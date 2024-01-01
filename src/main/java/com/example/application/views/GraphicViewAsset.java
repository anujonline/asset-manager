package com.example.application.views;

import com.example.application.data.Account;
import com.example.application.data.AssetEntity;
import com.example.application.services.RoundingUtil;
import com.example.application.services.ViewService;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.Responsive;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.HorizontalAlign;
import com.github.appreciated.apexcharts.config.xaxis.TickPlacement;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Route(value = "graphic", layout = MainLayout.class)
@PageTitle("View Assets")
public class GraphicViewAsset extends VerticalLayout {

    public GraphicViewAsset(ViewService viewService) {
        var expenses = viewService.assets();

        var userMonthSums = new HashMap<Account, Map<LocalDate, Double>>();
        for (AssetEntity transaction : expenses) {
            userMonthSums.computeIfAbsent(transaction.getAccount(), key -> new TreeMap<>()).merge(transaction.getLocalDateTime().toLocalDate(), RoundingUtil.displayAmount(transaction), Double::sum);
        }
        var collect = userMonthSums.values().stream().map(Map::keySet).distinct().flatMap(Collection::stream).sorted(LocalDate::compareTo).map(LocalDate::toString).toList();

        var apexChartsBuilder = new ApexChartsBuilder();
        apexChartsBuilder.withColors(
                "#e6194b", "#3cb44b", "#ffe119", "#4363d8", "#f58231", "#911eb4", "#46f0f0", "#f032e6", "#bcf60c", "#fabebe", "#008080", "#e6beff", "#9a6324", "#fffac8", "#800000", "#aaffc3", "#808000", "#ffd8b1", "#000075", "#808080", "#ffffff", "#000000"
        );
        apexChartsBuilder.withChart(ChartBuilder.get().withType(Type.AREA).build()).withXaxis(XAxisBuilder.get().withMin(100.0).withCategories(collect).withTickPlacement(TickPlacement.BETWEEN).build());


        var list = new ArrayList<Series<Double>>();

        userMonthSums.forEach((s, monthDoubleMap) -> {
            Series<Double> tSeries = new Series<>("%s %s".formatted(s.getDisplayName(), s.getLabel()), monthDoubleMap.values().toArray(Double[]::new));
            list.add(tSeries);
        });
        apexChartsBuilder.withSeries(list.toArray(Series[]::new));
        apexChartsBuilder.withLegend(LegendBuilder.get().withHorizontalAlign(HorizontalAlign.LEFT).build());

        add(apexChartsBuilder.build());

        setSizeFull();

    }
}
