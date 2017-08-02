package com.rami.test.vo;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.UUID;

/**
 * Created by ramistefanidis on 8/2/17.
 */

@Table("customer")
public class CustomerVO {


    @PrimaryKey
    private UUID id;

    private String firstName;

    private String lastName;

    public CustomerVO() {
    }

    public CustomerVO(UUID id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "CustomerVO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}

