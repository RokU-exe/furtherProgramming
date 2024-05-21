package controllers;

import models.Customer;
import models.User;
import models.UserRole;

import java.util.ArrayList;
import java.util.List;

public class CustomerManagerImpl implements CustomerManager {
    private List<Customer> customers;

    public CustomerManagerImpl() {
        this.customers = new ArrayList<>();
    }

    @Override
    public void add(Customer customer) {
        customers.add(customer);
    }

    @Override
    public void update(Customer customer) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId().equals(customer.getId())) {
                customers.set(i, customer);
                break;
            }
        }
    }

    @Override
    public void delete(String customerId) {
        customers.removeIf(customer -> customer.getId().equals(customerId));
    }

    @Override
    public Customer getOne(String customerId) {
        for (Customer customer : customers) {
            if (customer.getId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }

    @Override
    public List<Customer> getAll() {
        return new ArrayList<>(customers);
    }

    @Override
    public List<Customer> getFilteredCustomers(String filterCriteria) {
        // Placeholder for filtering logic
        return new ArrayList<>(customers);
    }

    public static class SystemAdmin extends User {
        public SystemAdmin(String id, String fullName, String email, String password, UserRole role) {
            super(id, fullName, email, password, role);
        }
        // Additional methods specific to System Admins
    }
}
