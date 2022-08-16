package com.luxoft.olshevchenko.mongodbdemo.repository;

import com.luxoft.olshevchenko.mongodbdemo.entity.ApiUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
@Repository
public interface ApiUserRepository extends MongoRepository<ApiUser, String> {
    Optional<ApiUser> findApiUserByUsername(String userName);
}
