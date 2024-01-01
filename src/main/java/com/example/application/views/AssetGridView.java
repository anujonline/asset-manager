package com.example.application.views;

import com.example.application.services.ViewService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "asset-grid", layout = MainLayout.class)
public class AssetGridView extends VerticalLayout {

    public AssetGridView(ViewService viewService) {
        add("EMPTY CONTENT TO BE DONE");
    }
}
