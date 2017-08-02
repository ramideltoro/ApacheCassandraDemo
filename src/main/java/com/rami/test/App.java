package com.rami.test;


import com.datastax.driver.core.utils.UUIDs;
import com.rami.test.dao.CustomerRepository;
import com.rami.test.vo.CustomerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements CommandLineRunner
{

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomerRepository repository;

    @Override
    public void run(String... args) throws Exception {
        this.repository.deleteAll();

        // save a couple of customers
        this.repository.save(new CustomerVO(UUIDs.timeBased(), "Alice", "Smith"));
        this.repository.save(new CustomerVO(UUIDs.timeBased(), "Bob", "Smith"));

        // fetch all customers
        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        for (CustomerVO customer : this.repository.findAll()) {
            System.out.println(customer);
        }
        System.out.println();

        // fetch an individual customer
        System.out.println("Customer found with findByFirstName('Alice'):");
        System.out.println("--------------------------------");
        System.out.println(this.repository.findByFirstName("Alice"));

        System.out.println("Customers found with findByLastName('Smith'):");
        System.out.println("--------------------------------");
        for (CustomerVO customer : this.repository.findByLastName("Smith")) {
            System.out.println(customer);
        }
    }

    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }
}
