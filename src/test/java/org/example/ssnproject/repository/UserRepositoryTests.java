package org.example.ssnproject.repository;

import org.assertj.core.api.Assertions;
import org.example.ssnproject.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    UserRepository userRepository;

    private UserEntity userEntity;

    private UserEntity userEntity2;

    @BeforeEach
    public void setUpTestData(){
        userEntity = UserEntity
                .builder()
                .username("tester")
                .password("1234")
                .email("tester@gmail.com")
                .build();

        userEntity2 = UserEntity
                .builder()
                .username("tester2")
                .password("1234")
                .email("tester2@gmain.com")
                .build();
    }

    @Test
    public void UserRepository_saveUser_ReturnSavedUser(){
        UserEntity savedUser = userRepository.save(userEntity);

        Assertions.assertThat(savedUser).isNotNull();
    }

    @Test
    public void UserRepository_findAll_ReturnListOfTwoUsers(){
        userRepository.save(userEntity);
        userRepository.save(userEntity2);

        List<UserEntity> users = userRepository.findAll();

        Assertions.assertThat(users.size()).isEqualTo(2);

    }

    @Test
    public void UserRepository_findByUsername_ReturnCorrectUser(){
        UserEntity savedUser = userRepository.save(userEntity);
        UserEntity returned_user = userRepository.findByUsername(savedUser.getUsername()).get();

        Assertions.assertThat(returned_user).isNotNull();
    }

    @Test
    public void UserRepository_findByEmail_ReturnCorrectUser(){
        UserEntity savedUser = userRepository.save(userEntity);
        UserEntity returned_user = userRepository.findByEmail(savedUser.getEmail()).get();

        Assertions.assertThat(returned_user).isNotNull();
    }

}
