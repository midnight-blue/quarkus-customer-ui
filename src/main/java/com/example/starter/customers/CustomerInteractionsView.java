package com.example.starter.customers;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.quarkus.annotation.RouteScopeOwner;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;

@Route(value = "customers/interactions", layout = CustomersLayout.class)
@PermitAll
public class CustomerInteractionsView extends VerticalLayout {

    @Inject
    public CustomerInteractionsView(@RouteScopeOwner(CustomersLayout.class) SelectedCustomerContext selectedCustomerContext) {
        addClassNames("p-4");
        selectedCustomerContext.getSelectedCustomer().ifPresentOrElse(
                customer -> add("Interactions for " + customer.name() + " (no interaction data yet — dummy view)."),
                () -> add("Select a customer in Search first.")
        );
    }
}
