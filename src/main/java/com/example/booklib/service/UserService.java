package com.example.booklib.service;

import com.example.booklib.configuration.CustomUserDetail;
import com.example.booklib.dto.RegUserDto;
import com.example.booklib.entity.User;
import com.example.booklib.mapper.UserMapper;
import com.example.booklib.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public boolean saveUser(RegUserDto regUserDto) {
        if(userRepository.existsByUserNameAndEmail(regUserDto.getUsername(), regUserDto.getEmail())){
            log.info("User already exists");
            return false;
        }
            User user = UserMapper.regUserDtoToUser(regUserDto);
//        user.setRole
//                setPassword;
        userRepository.save(user);
        log.info("User saved");
        return true;
    }

    public Optional<User> findByEmail(String email) {
        Optional<User> userEmail = userRepository.findByEmail(email);
        if(!(userEmail.isPresent())){
            log.error("User with email {} not found", email);
            return Optional.empty();
        }else {
            log.info("User found with email {}", email);
            return userEmail;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        } else {
        return new CustomUserDetail (user);
        }
    }
}
