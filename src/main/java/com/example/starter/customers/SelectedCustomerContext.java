package com.example.starter.customers;

import com.vaadin.quarkus.annotation.NormalRouteScoped;
import com.vaadin.quarkus.annotation.RouteScopeOwner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NormalRouteScoped
@RouteScopeOwner(CustomersLayout.class)
public class SelectedCustomerContext implements Serializable {

    private Customer selectedCustomer;
    private transient List<Runnable> selectionListeners;

    public Optional<Customer> getSelectedCustomer() {
        return Optional.ofNullable(selectedCustomer);
    }

    public void setSelectedCustomer(Customer customer) {
        this.selectedCustomer = customer;
        for (Runnable listener : listeners()) {
            listener.run();
        }
    }

    public void addSelectionListener(Runnable listener) {
        listeners().add(listener);
    }

    private List<Runnable> listeners() {
        if (selectionListeners == null) {
            selectionListeners = new ArrayList<>();
        }
        return selectionListeners;
    }
}
