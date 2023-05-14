package com.example.goal.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.goal.dao.CustomerDAO;
import com.example.goal.database.CustomerDatabase;
import com.example.goal.entity.Customer;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
/**
 * The CustomerRepository class acts as a bridge between the data source (CustomerDAO) and the ViewModel,
 * providing methods to interact with the Customer data.
 */
public class CustomerRepository {
    private static CustomerDAO customerDao;
    private LiveData<List<Customer>> allCustomersLiveData;
    /**
     * Constructs a new CustomerRepository instance.
     *
     * @param application The application context.
     */
    public CustomerRepository(Application application) {
        CustomerDatabase database = CustomerDatabase.getInstance(application);
        customerDao = database.customerDao();
        allCustomersLiveData = customerDao.getAllCustomersLiveData();
    }
    /**
     * Returns the LiveData object containing the list of all customers.
     *
     * @return The LiveData object containing the list of all customers.
     */
    public LiveData<List<Customer>> getAllCustomersLiveData() {
        return allCustomersLiveData;
    }
    /**
     * Inserts a new customer into the database asynchronously.
     *
     * @param customer The customer to be inserted.
     */
    public void insert(final Customer customer) {
        CompletableFuture.runAsync(() -> {
            try {
                customerDao.insert(customer);
                Log.d("TAG", "Customer added: " + customer.getName());
            } catch (Exception e) {
                Log.e("TAG", "Error inserting customer: " + e.getMessage());
            }
        }, CustomerDatabase.databaseWriteExecutor);
    }
    /**
     * Deletes all customers from the database.
     */
    public void deleteAll() {
        CustomerDatabase.databaseWriteExecutor.execute(() -> customerDao.deleteAll());
    }
    /**
     * Deletes a specific customer from the database.
     *
     * @param customer The customer to be deleted.
     */
    public void delete(final Customer customer) {
        CustomerDatabase.databaseWriteExecutor.execute(() -> customerDao.delete(customer));
    }
    /**
     * Updates the information of a customer in the database.
     *
     * @param customer The customer to be updated.
     */
    public void updateCustomer(final Customer customer) {
        CustomerDatabase.databaseWriteExecutor.execute(() -> customerDao.updateCustomer(customer));
    }
    /**
     * Retrieves a customer with the specified email from the database synchronously.
     *
     * @param email The email of the customer to retrieve.
     * @return The customer with the specified email, or null if not found.
     */
    public static Customer getCustomerByEmail(String email) {
        return customerDao.getCustomerByEmailSync(email);
    }

    /**
     * Retrieves a LiveData object containing the customer with the specified email.
     *
     * @param email The email of the customer to retrieve.
     * @return A LiveData object containing the customer with the specified email.
     */
    public LiveData<Customer> getCustomerlByEmail(String email) {
        return customerDao.getCustomerByEmail(email);
    }
    /**
     * Retrieves a CompletableFuture object that completes with the customer of the specified ID.
     *
     * @param customerId The ID of the customer to retrieve.
     * @return A CompletableFuture object that completes with the customer of the specified ID.
     */
    public CompletableFuture<Customer> findByIDFuture(final int customerId) {
        return CompletableFuture.supplyAsync(() -> customerDao.findByID(customerId), CustomerDatabase.databaseWriteExecutor);
    }
    /**
     * Sets a customer in the database using an AsyncTask.
     */
    public void setCustomer(Customer customer) {
        new SetCustomerAsyncTask(customerDao).execute(customer);
    }
    /**
     * An AsyncTask that sets a customer in the database.
     */
    private static class SetCustomerAsyncTask extends AsyncTask<Customer, Void, Void> {
        private CustomerDAO customerDao;

        private SetCustomerAsyncTask(CustomerDAO customerDao) {
            this.customerDao = customerDao;
        }

        @Override
        protected Void doInBackground(Customer... customers) {
            Customer customer = customers[0];
            Customer existingCustomer = customerDao.findByID(customer.getUid());

            if (existingCustomer != null) {
                // Update the existing customer
                existingCustomer.setName(customer.getName());
                existingCustomer.setEmail(customer.getEmail());
                existingCustomer.setAddress(customer.getAddress());
                customerDao.updateCustomer(existingCustomer);
            } else {
                // Insert the new customer
                customerDao.insert(customer);
            }

            return null;
        }
    }
}