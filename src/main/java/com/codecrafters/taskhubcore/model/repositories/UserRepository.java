package com.codecrafters.taskhubcore.model.repositories;

import com.codecrafters.taskhubcore.model.entities.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
}
