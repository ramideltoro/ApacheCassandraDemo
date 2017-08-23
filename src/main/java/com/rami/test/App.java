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

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class App implements CommandLineRunner
{

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomerRepository repository;

    @Override
    public void run(String... args) throws Exception {
        //this.repository.deleteAll();

        long executions = 0;
        double totalTime = 0;
        List<Long> timeContainer = new ArrayList<Long>();
        // save a couple of customers
        while(true) {
            final String name = "Rami" + System.currentTimeMillis();
            final long startMillis = System.currentTimeMillis();
            //this.repository.save(new CustomerVO(UUIDs.timeBased(), name, "Smith")); //Total time = 124171.0   Current Average = 62.0855 executions = 2000
            this.repository.save(getCustomerList()); //time = 22259.0   Current Average = 111.295 executions = 200
            final long totalMillis = System.currentTimeMillis() - startMillis;
            timeContainer.add(totalMillis);
            executions++;
            totalTime+=totalMillis;
            double currentAverage = totalTime / executions;

            if(executions % 10 == 0) {
                System.out.println("Total time = " + totalTime + "   Current Average = " + currentAverage + " executions = " + executions);
                System.out.println("Each time = " +timeContainer.toString());
                timeContainer.clear();
            }
            Thread.sleep(5000);
        }
        //this.repository.save(new CustomerVO(UUIDs.timeBased(), "Alice", "Smith"));
        //this.repository.save(new CustomerVO(UUIDs.timeBased(), "Bob", "Smith"));
        //this.repository.save(new CustomerVO(UUIDs.timeBased(), "Rami", "Stefanidis"));


        // fetch all customers
/*        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        for (CustomerVO customer : this.repository.findAll()) {
            System.out.println(customer);
        }
        System.out.println();

        // fetch an individual customer
        System.out.println("Customer found with findByFirstName('Alice'):");
        System.out.println("--------------------------------");
        System.out.println(this.repository.findByFirstName("Alice"));

        // fetch an individual customer
    *//*    System.out.println("Customer found with findbyFirstAndLastName(\"Rami\",\"Stefanidis\"):");
        System.out.println("--------------------------------");
        System.out.println(this.repository.findbyFirstAndLastName("Rami","Stefanidis"));*//*


        System.out.println("Customers found with findByLastName('Smith'):");
        System.out.println("--------------------------------");
        for (CustomerVO customer : this.repository.findByLastName("Smith")) {
            System.out.println(customer);
        }*/
    }

    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }

    public static List<CustomerVO> getCustomerList() {
        final List<CustomerVO> customers = new ArrayList<CustomerVO>();

        for(int i=0;i<50;i++) { //500 may cause : Caused by: org.springframework.cassandra.support.exception.CassandraWriteTimeoutException: Cassandra timeout during write query at consistency ONE (1 replica were required but only 0 acknowledged the write); nested exception is com.datastax.driver.core.exceptions.WriteTimeoutException: Cassandra timeout during write query at consistency ONE (1 replica were required but only 0 acknowledged the write)
            final String name = "Rami" + System.currentTimeMillis();
            customers.add(new CustomerVO(UUIDs.timeBased(), name, "Smith"));
        }
        return customers;
    }
}
