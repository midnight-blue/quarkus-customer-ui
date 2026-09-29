package com.example.starter.customers;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.quarkus.annotation.RouteScopeOwner;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;

@Route(value = "customers/contracts", layout = CustomersLayout.class)
@PermitAll
public class CustomerContractsView extends VerticalLayout {

    @Inject
    public CustomerContractsView(@RouteScopeOwner(CustomersLayout.class) SelectedCustomerContext selectedCustomerContext) {
        addClassNames("p-4");
        selectedCustomerContext.getSelectedCustomer().ifPresentOrElse(
                customer -> add("Contracts for " + customer.name() + " (no contract data yet — dummy view)."),
                () -> add("Select a customer in Search first.")
        );
    }
}
