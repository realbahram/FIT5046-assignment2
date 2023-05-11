package com.example.goal.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.goal.entity.Customer;
import com.example.goal.repository.CustomerRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CustomerViewModel extends AndroidViewModel {
    private CustomerRepository customerRepository;
    private LiveData<List<Customer>> allCustomers;

    public CustomerViewModel(Application application) {
        super(application);
        customerRepository = new CustomerRepository(application);
        allCustomers = customerRepository.getAllCustomersLiveData();
    }

    public CompletableFuture<Customer> findByIDFuture(final int customerId) {
        return customerRepository.findByIDFuture(customerId);
    }

    public LiveData<List<Customer>> getAllCustomers() {
        return allCustomers;
    }

    public void insert(Customer customer) {
        customerRepository.insert(customer);
    }

    public void deleteAll() {
        customerRepository.deleteAll();
    }

    public void update(Customer customer) {
        customerRepository.updateCustomer(customer);
    }

    public Customer getCustomerByEmail(String email) {
        return CustomerRepository.getCustomerByEmail(email);
    }

    public LiveData<Customer> getCustomerlByEmail(String email){
        return customerRepository.getCustomerlByEmail(email);
    }

    public void setCustomer(Customer customer) {
        customerRepository.setCustomer(customer);
    }
}