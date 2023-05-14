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

    public CustomerRepository(Application application) {
        CustomerDatabase database = CustomerDatabase.getInstance(application);
        customerDao = database.customerDao();
        allCustomersLiveData = customerDao.getAllCustomersLiveData();
    }

    public LiveData<List<Customer>> getAllCustomersLiveData() {
        return allCustomersLiveData;
    }

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

    public void deleteAll() {
        CustomerDatabase.databaseWriteExecutor.execute(() -> customerDao.deleteAll());
    }

    public void delete(final Customer customer) {
        CustomerDatabase.databaseWriteExecutor.execute(() -> customerDao.delete(customer));
    }

    public void updateCustomer(final Customer customer) {
        CustomerDatabase.databaseWriteExecutor.execute(() -> customerDao.updateCustomer(customer));
    }
    public static Customer getCustomerByEmail(String email) {
        return customerDao.getCustomerByEmailSync(email);
    }

    public LiveData<Customer> getCustomerlByEmail(String email) {
        return customerDao.getCustomerByEmail(email);
    }

    public CompletableFuture<Customer> findByIDFuture(final int customerId) {
        return CompletableFuture.supplyAsync(() -> customerDao.findByID(customerId), CustomerDatabase.databaseWriteExecutor);
    }

    public void setCustomer(Customer customer) {
        new SetCustomerAsyncTask(customerDao).execute(customer);
    }

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