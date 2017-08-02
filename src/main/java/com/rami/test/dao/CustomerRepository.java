package com.rami.test.dao;

import com.rami.test.vo.CustomerVO;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ramistefanidis on 8/2/17.
 */
public interface CustomerRepository extends CrudRepository<CustomerVO,String> {


    @Query("Select * from customer where firstname=?0")
    public CustomerVO findByFirstName(String firstName);

    @Query("Select * from customer where lastname=?0")
    public List<CustomerVO> findByLastName(String lastName);

    @Query("Select * from customer where firstname=?0 and lastname=?1")
    public CustomerVO findbyFirstAndLastName(String firstName, String lastName);



}
