package com.example.goal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.goal.entity.Customer;

import java.util.List;
/**
 * DAO interface for the Customer entity
 */
@Dao
public interface CustomerDAO {

    /**
     * Retrieves all customers as LiveData
     *
     * @return LiveData containing the list of customers
     */
    @Query("SELECT * FROM customer ORDER BY name ASC")
    LiveData<List<Customer>> getAllCustomersLiveData();

    /**
     * Retrieves a customer by their id
     *
     * @param customerId The ID of the customer
     * @return The customer object with their id, or null if not found.
     */
    @Query("SELECT * FROM customer WHERE uid = :customerId LIMIT 1")
    Customer findByID(int customerId);

    /**
     * Inserts a new customer into the database
     *
     * @param customer The customer to insert
     */
    @Insert
    void insert(Customer customer);

    /**
     * Deletes a customer from the database
     *
     * @param customer The customer to delete
     */
    @Delete
    void delete(Customer customer);

    /**
     * Updates an existing customer in the database
     *
     * @param customer The customer to update
     */
    @Update
    void updateCustomer(Customer customer);

    /**
     * Deletes all customers from the database
     */
    @Query("DELETE FROM customer")
    void deleteAll();

    /**
     * Retrieves all customers as list
     *
     * @return The list of all customers
     */
    @Query("SELECT * FROM customer")
    List<Customer> getAllCustomersList();

    /**
     * Retrieves a customer by their email address synchronously.
     *
     * @param email The email address of the customer to retrieve
     * @return The customer object with their email, or null if not found.
     */
    @Query("SELECT * FROM customer WHERE email = :email")
    Customer getCustomerByEmailSync(String email);

    /**
     * Retrieves a customer by their email address as LiveData
     *
     * @param email The email address of the customer to retrieve
     * @return LiveData containing the customer object with their email, or null if not found
     */
    @Query("SELECT * FROM customer WHERE email = :email LIMIT 1")
    LiveData<Customer> getCustomerByEmail(String email);
}

