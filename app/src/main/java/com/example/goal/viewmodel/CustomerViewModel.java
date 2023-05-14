package com.example.goal.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.goal.entity.Customer;
import com.example.goal.repository.CustomerRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
/**
 * ViewModel class for managing the interaction between the UI and the CustomerRepository.
 * Acts as a communication center between the UI and the data repository.
 */
public class CustomerViewModel extends AndroidViewModel {
    private CustomerRepository customerRepository;
    private LiveData<List<Customer>> allCustomers;
    /**
     * Constructor of the CustomerViewModel class.
     * Initializes the CustomerViewModel with the given Application context.
     *
     * @param application The application context.
     */
    public CustomerViewModel(Application application) {
        super(application);
        customerRepository = new CustomerRepository(application);
        allCustomers = customerRepository.getAllCustomersLiveData();
    }
    /**
     * Retrieves a CompletableFuture containing the customer with the specified ID.
     *
     * @param customerId The ID of the customer to retrieve.
     * @return A CompletableFuture containing the customer.
     */
    public CompletableFuture<Customer> findByIDFuture(final int customerId) {
        return customerRepository.findByIDFuture(customerId);
    }
    /**
     * Retrieves a LiveData object containing a list of all customers.
     *
     * @return A LiveData object containing a list of all customers.
     */
    public LiveData<List<Customer>> getAllCustomers() {
        return allCustomers;
    }
    /**
     * Inserts a new customer into the database.
     *
     * @param customer The customer to be inserted.
     */
    public void insert(Customer customer) {
        customerRepository.insert(customer);
    }
    /**
     * Deletes all customers from the database.
     */
    public void deleteAll() {
        customerRepository.deleteAll();
    }
    /**
     * Updates an existing customer in the database.
     *
     * @param customer The customer to be updated.
     */
    public void update(Customer customer) {
        customerRepository.updateCustomer(customer);
    }
    /**
     * Retrieves a customer from the database based on the email.
     *
     * @param email The email of the customer.
     * @return The customer with the specified email, or null if not found.
     */
    public Customer getCustomerByEmail(String email) {
        return CustomerRepository.getCustomerByEmail(email);
    }
    /**
     * Retrieves a LiveData object containing the customer with the specified email.
     *
     * @param email The email of the customer.
     * @return A LiveData object containing the customer, or null if not found.
     */
    public LiveData<Customer> getCustomerlByEmail(String email){
        return customerRepository.getCustomerlByEmail(email);
    }
    /**
     * Sets a customer in the database, either by updating an existing customer or inserting a new customer.
     *
     * @param customer The customer to be set.
     */
    public void setCustomer(Customer customer) {
        customerRepository.setCustomer(customer);
    }
}