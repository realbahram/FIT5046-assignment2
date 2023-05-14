package com.example.goal.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a customer entity in the application.
 */
@Entity
public class Customer {

    /**
     * The unique identifier for the customer.
     */
    @PrimaryKey(autoGenerate = true)
    public int uid;

    /**
     * The name of the customer.
     */
    @ColumnInfo(name = "name")
    @NonNull
    private String name;

    /**
     * The email address of the customer.
     */
    @ColumnInfo(name = "email")
    @NonNull
    private String email;

    /**
     * The address of the customer.
     */
    @ColumnInfo(name = "address")
    @NonNull
    private String address;

    /**
     * Default constructor for the Customer class.
     */
    public Customer() {
        // Default constructor required by Room.
    }

    /**
     * Constructor for creating a new Customer instance.
     *
     * @param id      The unique identifier for the customer.
     * @param name    The name of the customer.
     * @param email   The email address of the customer.
     * @param address The address of the customer.
     */
    public Customer(@NonNull int id, @NonNull String name, @NonNull String email, @NonNull String address) {
        this.uid = id;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    /**
     * Retrieves the unique identifier of the customer.
     *
     * @return The unique identifier of the customer.
     */
    public int getUid() {
        return uid;
    }

    /**
     * Sets the unique identifier of the customer.
     *
     * @param uid The unique identifier to set.
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * Retrieves the name of the customer.
     *
     * @return The name of the customer.
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the customer.
     *
     * @param name The name to set.
     */
    public void setName(@NonNull String name) {
        this.name = name;
    }

    /**
     * Retrieves the email address of the customer.
     *
     * @return The email address of the customer.
     */
    @NonNull
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the customer.
     *
     * @param email The email address to set.
     */
    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    /**
     * Retrieves the address of the customer.
     *
     * @return The address of the customer.
     */
    @NonNull
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the customer.
     *
     * @param address The address to set.
     */
    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    /**
     * Returns a string representation of the Customer object.
     *
     * @return A string representation of the Customer object.
     */
    @Override
    public String toString() {
        return "Customer{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

