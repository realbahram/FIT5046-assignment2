package com.example.goal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.goal.entity.Customer;

import java.util.List;

@Dao
public interface CustomerDAO {
    @Query("SELECT * FROM customer ORDER BY name ASC")
    LiveData<List<Customer>> getAllCustomersLiveData();

    @Query("SELECT * FROM customer WHERE uid = :customerId LIMIT 1")
    Customer findByID(int customerId);

    @Insert
    void insert(Customer customer);

    @Delete
    void delete(Customer customer);

    @Update
    void updateCustomer(Customer customer);

    @Query("DELETE FROM customer")
    void deleteAll();

    @Query("SELECT * FROM customer")
    List<Customer> getAllCustomersList();

    @Query("SELECT * FROM customer WHERE email = :email")
    Customer getCustomerByEmailSync(String email);
    @Query("SELECT * FROM customer WHERE email = :email LIMIT 1")
    LiveData<Customer> getCustomerByEmail(String email);
}






