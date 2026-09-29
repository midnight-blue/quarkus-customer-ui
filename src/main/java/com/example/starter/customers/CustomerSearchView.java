package com.example.starter.customers;

import com.example.starter.base.ui.DataDetails;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.masterdetaillayout.MasterDetailLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.quarkus.annotation.RouteScopeOwner;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;

import java.util.Locale;

/**
 * Customer search: a list of results on the left with the selected
 * customer's condensed details on the right (collapsing to an overlay on
 * narrow viewports). Deeplinking works both ways: opening
 * {@code customers/{id}} pre-fills the search bar with that id so the list
 * narrows down to the single matching customer, and opening a customer from
 * the list updates the URL to {@code customers/{id}} so it can be bookmarked
 * or shared.
 */
@Route(value = "customers/:id?", layout = CustomersLayout.class)
@PermitAll
public class CustomerSearchView extends MasterDetailLayout implements BeforeEnterObserver {

    private final CustomersService customersService;
    private final SelectedCustomerContext selectedCustomerContext;

    private final TextField searchField = buildSearchField();
    private final Grid<Customer> grid = buildGrid();

    @Inject
    public CustomerSearchView(CustomersService customersService,
                               @RouteScopeOwner(CustomersLayout.class) SelectedCustomerContext selectedCustomerContext) {
        this.customersService = customersService;
        this.selectedCustomerContext = selectedCustomerContext;

        setSizeFull();
        setDetailPlaceholder(buildDetailPlaceholder());

        Div master = new Div(searchField, grid);
        master.addClassNames("flex", "flex-col", "gap-4", "p-4", "h-full", "box-border");
        setMaster(master);

        searchField.addValueChangeListener(e -> refreshGrid(e.getValue()));

        // Single source of truth for "a customer is open": fires for both user
        // clicks and programmatic grid.select() calls, so it also covers the
        // deeplink (beforeEnter) and restored-selection (below) cases.
        grid.asSingleSelect().addValueChangeListener(e -> {
            Customer selected = e.getValue();
            selectedCustomerContext.setSelectedCustomer(selected);
            updateDetails(selected);
            updateUrl(selected);
        });

        refreshGrid(null);
        selectedCustomerContext.getSelectedCustomer().ifPresent(grid::select);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.getRouteParameters().get("id").ifPresent(id -> {
            searchField.setValue(id);
            customersService.findById(id).ifPresent(grid::select);
        });
    }

    private void refreshGrid(String query) {
        grid.setItems(customersService.search(query));
    }

    private void updateDetails(Customer customer) {
        if (customer == null) {
            setDetail(null);
            return;
        }
        DataDetails details = DataDetails.builder()
                .avatarText(initials(customer.name()))
                .title(customer.name())
                .subtitle(customer.company())
                .field("Email", customer.email())
                .field("Phone", customer.phone())
                .field("Customer ID", customer.id())
                .build();

        Div wrapper = new Div(details);
        wrapper.addClassNames("p-4", "h-full", "box-border");
        setDetail(wrapper);
    }

    /** Keeps the browser URL in sync so the currently open customer is always deeplinkable. */
    private void updateUrl(Customer customer) {
        String path = customer == null ? "customers" : "customers/" + customer.id();
        UI.getCurrent().getPage().getHistory().replaceState(null, path);
    }

    private static String initials(String name) {
        String[] parts = name.trim().split("\\s+");
        StringBuilder initials = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                initials.append(Character.toUpperCase(part.charAt(0)));
            }
            if (initials.length() == 2) {
                break;
            }
        }
        return initials.toString().toUpperCase(Locale.ROOT);
    }

    private static TextField buildSearchField() {
        TextField field = new TextField();
        field.setPlaceholder("Search by name, email, company or id");
        field.setClearButtonVisible(true);
        field.setValueChangeMode(ValueChangeMode.LAZY);
        field.addClassNames("w-full", "max-w-md");
        return field;
    }

    private static Grid<Customer> buildGrid() {
        Grid<Customer> grid = new Grid<>(Customer.class, false);
        grid.addColumn(Customer::name).setHeader("Name");
        grid.addColumn(Customer::email).setHeader("Email");
        grid.addColumn(Customer::phone).setHeader("Phone");
        grid.addColumn(Customer::company).setHeader("Company");
        grid.addClassNames("flex-1");
        return grid;
    }

    private static Div buildDetailPlaceholder() {
        Span message = new Span("Select a customer to see details");
        message.addClassNames("text-sm", "text-grey-60");

        Div placeholder = new Div(message);
        placeholder.addClassNames("flex", "items-center", "justify-center", "h-full", "p-4");
        return placeholder;
    }
}
