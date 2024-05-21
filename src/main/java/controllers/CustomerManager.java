package controllers;

import models.Customer;

import java.util.List;

public interface CustomerManager {
    void add(Customer customer);
    void update(Customer customer);
    void delete(String customerId);
    Customer getOne(String customerId);
    List<Customer> getAll();
    List<Customer> getFilteredCustomers(String filterCriteria);
}
