package com.example.application.views;

import com.example.application.data.AssetEntity;
import com.example.application.services.RoundingUtil;
import com.example.application.services.ViewService;
import com.storedobject.chart.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Route(value = "graphic-total", layout = MainLayout.class)
@PageTitle("View Assets")
public class GraphicViewAssetAccumulated extends VerticalLayout {

    public GraphicViewAssetAccumulated(ViewService viewService) {
        setSizeFull();
        var soChartAssets = viewService.assets();
        Map<String, Double> map = new TreeMap<>();
        for (AssetEntity soChartAsset : soChartAssets) {
            var key = soChartAsset.getLocalDateTime().toLocalDate().toString();
            var orDefault = map.getOrDefault(key, 0.0);
            map.put(key, RoundingUtil.round(orDefault + RoundingUtil.displayAmount(soChartAsset)));
        }
        // Creating a chart display area.
        SOChart soChart = new SOChart();
        soChart.setSizeFull();

// Let us define some inline data.
        CategoryData labels = new CategoryData(map.keySet().toArray(String[]::new));
        Data data = new Data(map.values().toArray(Double[]::new));

// We are going to create a couple of charts. So, each chart should be positioned
// appropriately.
// Create a self-positioning chart.
        NightingaleRoseChart nc = new NightingaleRoseChart(labels, data);
        Position p = new Position();
        p.setTop(Size.percentage(50));
        nc.setPosition(p); // Position it leaving 50% space at the top

// Second chart to add.
        BarChart bc = new BarChart(labels, data);
        RectangularCoordinate rc;
        rc = new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));
        p = new Position();
        p.setBottom(Size.percentage(55));
        rc.setPosition(p); // Position it leaving 55% space at the bottom
        bc.plotOn(rc); // Bar chart needs to be plotted on a coordinate system

// Just to demonstrate it, we are creating a "Download" and a "Zoom" toolbox button.
        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download(), new Toolbox.Zoom());

// Let's add some titles.
        Title title = new Title("My First Chart");
        title.setSubtext("2nd Line of the Title");

// Add the chart components to the chart display area.
        soChart.add(nc, bc, toolbox, title);

// Now, add the chart display (which is a Vaadin Component) to your layout.
        add(soChart);
    }
}
