package com.example.starter.customers;

import com.example.starter.base.MainLayout;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.quarkus.annotation.RouteScopeOwner;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

@ParentLayout(MainLayout.class)
public class CustomersLayout extends Div implements RouterLayout {

    @Inject
    @RouteScopeOwner(CustomersLayout.class)
    SelectedCustomerContext selectedCustomerContext;

    private final Div infoBar = buildInfoBar();
    private final Div leafSlot = new Div();
    private final SideNav sectionNav = new SideNav();
    private final SideNavItem contractsItem =
            new SideNavItem("Contracts", CustomerContractsView.class, VaadinIcon.FILE_TEXT_O.create());
    private final SideNavItem interactionsItem =
            new SideNavItem("Interactions", CustomerInteractionsView.class, VaadinIcon.COMMENTS_O.create());

    public CustomersLayout() {
        addClassNames("flex", "flex-col", "w-full", "h-full");
        leafSlot.addClassNames("flex-1");
        add(infoBar, leafSlot);

        sectionNav.addItem(
                new SideNavItem("Search", CustomerSearchView.class, VaadinIcon.SEARCH.create()),
                contractsItem,
                interactionsItem
        );
    }

    @PostConstruct
    private void init() {
        selectedCustomerContext.addSelectionListener(this::refreshSectionNavState);
        refreshSectionNavState();
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        leafSlot.getElement().removeAllChildren();
        if (content != null) {
            leafSlot.getElement().appendChild(content.getElement());
        }

        boolean showInfoBar = content instanceof CustomerContractsView || content instanceof CustomerInteractionsView;
        infoBar.setVisible(showInfoBar);
        if (showInfoBar) {
            selectedCustomerContext.getSelectedCustomer().ifPresentOrElse(
                    this::renderInfoBar,
                    () -> infoBar.setText("No customer selected")
            );
        }
    }

    public SideNav buildSectionNav() {
        return sectionNav;
    }

    public SelectedCustomerContext getSelectedCustomerContext() {
        return selectedCustomerContext;
    }

    private void refreshSectionNavState() {
        boolean hasSelection = selectedCustomerContext.getSelectedCustomer().isPresent();
        contractsItem.setEnabled(hasSelection);
        interactionsItem.setEnabled(hasSelection);
    }

    private void renderInfoBar(Customer customer) {
        infoBar.setText(customer.name() + " — " + customer.company() + " · " + customer.email());
    }

    private static Div buildInfoBar() {
        Div bar = new Div();
        bar.addClassNames("bg-brand-90", "text-grey-0", "rounded-t-lg", "px-4", "py-3", "font-medium");
        bar.setVisible(false);
        return bar;
    }
}
