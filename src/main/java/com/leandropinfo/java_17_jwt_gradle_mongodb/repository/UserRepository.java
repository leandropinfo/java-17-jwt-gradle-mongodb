package com.leandropinfo.java_17_jwt_gradle_mongodb.repository;

import com.leandropinfo.java_17_jwt_gradle_mongodb.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findTop1ByLoginIgnoreCase(String login);
}
