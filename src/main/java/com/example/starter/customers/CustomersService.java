package com.example.starter.customers;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@ApplicationScoped
public class CustomersService {

    private final List<Customer> customers = List.of(
            new Customer("1", "Alice Example", "alice@example.com", "+49 30 1111111", "Acme GmbH"),
            new Customer("2", "Bob Sample", "bob@example.com", "+49 30 2222222", "Sample AG"),
            new Customer("3", "Carol Demo", "carol@example.com", "+49 30 3333333", "Demo KG")
    );

    public List<Customer> findAll() {
        return customers;
    }

    public Optional<Customer> findById(String id) {
        return customers.stream().filter(c -> c.id().equals(id)).findFirst();
    }

    /**
     * Matches by exact id in addition to the usual substring search, so that
     * putting a customer's id in the search bar (e.g. via a deeplink) narrows
     * the list down to that single customer.
     */
    public List<Customer> search(String query) {
        if (query == null || query.isBlank()) {
            return customers;
        }
        String needle = query.toLowerCase(Locale.ROOT);
        return customers.stream()
                .filter(c -> c.id().equalsIgnoreCase(query)
                        || c.name().toLowerCase(Locale.ROOT).contains(needle)
                        || c.email().toLowerCase(Locale.ROOT).contains(needle)
                        || c.company().toLowerCase(Locale.ROOT).contains(needle))
                .toList();
    }
}
