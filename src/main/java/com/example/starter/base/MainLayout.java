package com.example.starter.base;

import com.example.starter.customers.CustomerSearchView;
import com.example.starter.customers.CustomersLayout;
import com.example.starter.customers.SelectedCustomerContext;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    private static final String SELECTED_BREADCRUMB_CLASS = "bg-success-15";

    private final SideNav globalNav = buildGlobalNav();
    private final Div breadcrumbRow = buildBreadcrumbRow();
    private Component currentDrawerNav;
    private SelectedCustomerContext registeredSelectionContext;

    public MainLayout() {
        DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.addClassNames("text-grey-0");

        H1 title = new H1("Base Starter");
        title.addClassNames("text-lg", "font-semibold", "m-0");

        Div navbarTop = new Div(drawerToggle, title);
        navbarTop.addClassNames("flex", "items-center", "gap-2", "bg-brand-100", "text-grey-0", "px-2", "py-1");

        Div navbarContainer = new Div(navbarTop, breadcrumbRow);
        navbarContainer.addClassNames("flex", "flex-col", "w-full");

        addToNavbar(true, navbarContainer);
        setDrawerContent(globalNav);
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        super.showRouterLayoutContent(content);
        boolean inCustomers = content instanceof CustomersLayout;
        breadcrumbRow.setVisible(inCustomers);
        setDrawerContent(inCustomers ? ((CustomersLayout) content).buildSectionNav() : globalNav);

        if (inCustomers) {
            SelectedCustomerContext context = ((CustomersLayout) content).getSelectedCustomerContext();
            if (context != registeredSelectionContext) {
                context.addSelectionListener(() -> updateBreadcrumbSelectionState(context));
                registeredSelectionContext = context;
            }
            updateBreadcrumbSelectionState(context);
        }
    }

    private void updateBreadcrumbSelectionState(SelectedCustomerContext context) {
        boolean hasSelection = context.getSelectedCustomer().isPresent();
        breadcrumbRow.setClassName(SELECTED_BREADCRUMB_CLASS, hasSelection);
    }

    private void setDrawerContent(Component nav) {
        if (currentDrawerNav != null) {
            currentDrawerNav.getElement().removeFromParent();
        }
        addToDrawer(nav);
        currentDrawerNav = nav;
    }

    private static SideNav buildGlobalNav() {
        SideNav nav = new SideNav();
        nav.addItem(
                new SideNavItem("Customers", CustomerSearchView.class, VaadinIcon.USERS.create()),
                new SideNavItem("Home", HomeView.class, VaadinIcon.HOME.create())
        );
        return nav;
    }

    private static Div buildBreadcrumbRow() {
        Div row = new Div();
        row.addClassNames("flex", "items-center", "gap-2", "px-4", "py-2", "text-sm");
        row.setVisible(false);

        RouterLink topLink = new RouterLink("Top", HomeView.class);
        topLink.addClassNames("text-action-100", "hover:underline");

        Span separator = new Span(">");
        separator.addClassNames("text-grey-45");

        Span current = new Span("Customers");
        current.addClassNames("text-grey-105", "font-medium");

        row.add(topLink, separator, current);
        return row;
    }
}
