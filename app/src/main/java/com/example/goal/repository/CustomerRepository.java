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
public class CustomerRepository {
    private static CustomerDAO customerDao;
    private LiveData<List<Customer>> allCustomersLiveData;
    /**
     * Constructor of CustomerRepository class.
     * Initializes the CustomerRepository with the given application context.
     *
     * @param application The application context.
     */
    public CustomerRepository(Application application) {
        CustomerDatabase database = CustomerDatabase.getInstance(application);
        customerDao = database.customerDao();
        allCustomersLiveData = customerDao.getAllCustomersLiveData();
    }
    /**
     * Retrieves LiveData of all customers.
     *
     * @return LiveData object containing a list of all customers.
     */
    public LiveData<List<Customer>> getAllCustomersLiveData() {
        return allCustomersLiveData;
    }
    /**
     * Inserts a customer into the database.
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
     * Updates the information of a specific customer in the database.
     *
     * @param customer The customer to be updated.
     */
    public void updateCustomer(final Customer customer) {
        CustomerDatabase.databaseWriteExecutor.execute(() -> customerDao.updateCustomer(customer));
    }
    /**
     * Retrieves a customer with the given email synchronously.
     *
     * @param email The email of the customer to retrieve.
     * @return The customer object with the specified email.
     */
    public static Customer getCustomerByEmail(String email) {
        return customerDao.getCustomerByEmailSync(email);
    }
    /**
     * Retrieves a customer with the given email asynchronously as LiveData.
     *
     * @param email The email of the customer to retrieve.
     * @return LiveData object containing the customer with the specified email.
     */
    public LiveData<Customer> getCustomerlByEmail(String email) {
        return customerDao.getCustomerByEmail(email);
    }
    /**
     * Retrieves a customer with the given customer ID asynchronously as CompletableFuture.
     *
     * @param customerId The ID of the customer to retrieve.
     * @return CompletableFuture object that completes with the customer.
     */
    public CompletableFuture<Customer> findByIDFuture(final int customerId) {
        return CompletableFuture.supplyAsync(() -> customerDao.findByID(customerId), CustomerDatabase.databaseWriteExecutor);
    }
    /**
     * Sets the customer in the database.
     *
     * @param customer The customer to be set.
     */
    public void setCustomer(Customer customer) {
        new SetCustomerAsyncTask(customerDao).execute(customer);
    }
    /**
     * AsyncTask to set the customer in the database.
     */
    private static class SetCustomerAsyncTask extends AsyncTask<Customer, Void, Void> {
        private CustomerDAO customerDao;
        /**
         * Constructor of SetCustomerAsyncTask class.
         * Initializes the SetCustomerAsyncTask with the given CustomerDAO.
         *
         * @param customerDao The CustomerDAO object.
         */
        private SetCustomerAsyncTask(CustomerDAO customerDao) {
            this.customerDao = customerDao;
        }
        /**
         * Performs the database operation to set the customer.
         * Inserts a new customer if it doesn't exist, or updates the existing customer.
         *
         * @param customers The customer to be set.
         * @return Always returns null.
         */
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