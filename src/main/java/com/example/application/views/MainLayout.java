package com.example.application.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("asset-manager");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {

        SideNav nav = new SideNav();
        var admin = new SideNavItem("Admin");
//        admin.addItem(new SideNavItem("Add Account", AddAccountView.class));
//        admin.addItem(new SideNavItem("Add Currency", AddCurrency.class));
        admin.addItem(new SideNavItem("Create Account", CreateAccount.class));

        var user = new SideNavItem("User");
        user.addItem(new SideNavItem("Add Asset", AddAssetView.class, LineAwesomeIcon.ADDRESS_BOOK.create()));
        user.addItem(new SideNavItem("View Assets", ViewAssets.class, LineAwesomeIcon.WATER_SOLID.create()));
        user.addItem(new SideNavItem("Visualize Assets", GraphicViewAsset.class, LineAwesomeIcon.DEV.create()));
        user.addItem(new SideNavItem("Visualize Assets Without currency conversion", GraphicViewAssetOwnCurrency.class, LineAwesomeIcon.DEV.create()));
        user.addItem(new SideNavItem("Visualize Assets Total", GraphicViewAssetAccumulated.class, LineAwesomeIcon.ANCHOR_SOLID.create()));

        nav.addItem(admin, user);
        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
