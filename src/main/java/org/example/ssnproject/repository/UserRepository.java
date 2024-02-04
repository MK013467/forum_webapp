package org.example.ssnproject.repository;

import org.apache.catalina.User;
import org.example.ssnproject.dto.SignUpForm;
import org.example.ssnproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);

}
