package org.example.ssnproject.service.PostService;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.ssnproject.dto.SignUpForm;
import org.example.ssnproject.entity.UserEntity;
import org.example.ssnproject.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public void createUser(SignUpForm signUpForm) {

        UserEntity userEntity = UserEntity.builder()
                .username(signUpForm.getUsername())
                .password(passwordEncoder.encode(signUpForm.getPassword1()))
                .email(signUpForm.getEmail())
                .build();

        userRepository.save(userEntity);
    }

    public UserEntity findUserByName(String username) throws UsernameNotFoundException {
        if(!userRepository.findByUsername(username).isPresent()){
            throw new UsernameNotFoundException("No such a user");
        }
        return  userRepository.findByUsername(username).get();
    }

    public UserEntity findUserById(Long id) throws UsernameNotFoundException{

        Optional<UserEntity> userEntity  =   userRepository.findById(id);

        if(!userEntity.isPresent()){
            throw new UsernameNotFoundException("No such a user");
        }

        return userEntity.get();
    }

    //return true if there is a duplicate username.
    public boolean checkUsernameDuplicate(String username){
        return userRepository.findByUsername(username).isPresent();
    }

    //return true if there is a duplicate email.
    public boolean checkEmailDuplicate(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public UserEntity findUser(String name) {
        Optional<UserEntity> _userEntity = userRepository.findByUsername(name);
        if(!_userEntity.isPresent()){
            throw new UsernameNotFoundException("Invalid User!!");
        }
        else{
           return  _userEntity.get();
        }
    }
}
