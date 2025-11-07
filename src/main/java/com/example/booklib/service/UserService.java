package com.example.booklib.service;

import com.example.booklib.configuration.CustomUserDetail;
import com.example.booklib.dto.RegUserDto;
import com.example.booklib.entity.User;
import com.example.booklib.mapper.UserMapper;
import com.example.booklib.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean saveUser(RegUserDto regUserDto) {
        if(userRepository.existsByUserNameAndEmail(regUserDto.getUsername(), regUserDto.getEmail())){
            log.info("User already exists");
            return false;
        }
        User user = UserMapper.regUserDtoToUser(regUserDto);
        user.getRoles().add("ROLE_USER");
        user.setPassword(passwordEncoder.encode(regUserDto.getPassword()));
        userRepository.save(user);
        log.info("User saved");
        return true;
    }

    public Optional<User> findUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByEmail(username);
        if (userOptional.isPresent()) {
            log.info("User {} found", userOptional);
            return userOptional;
        } else {
            log.warn("User {} not found", username);
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email) {
        Optional<User> userEmail = userRepository.findByEmail(email);
        if(userEmail.isEmpty()){
            log.error("User with email {} not found", email);
            return Optional.empty();
        }else {
            log.info("User with email {} was founded", email);
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
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            log.error("User not found or not authenticated");
            throw new RuntimeException("User is not authenticated");
        }
        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails userDetails){
            return findUserByUsername(userDetails.getUsername()).orElseThrow(() -> {
                log.error("User not found");
                return new RuntimeException("User not found");
            });
            }
        if(principal instanceof String userName){
            return findUserByUsername(userName).orElseThrow(() -> {
                log.error("User {} not found", userName);
                return new RuntimeException("User not found");
            });
        }
        log.error("Unsupported authentication principal type: {}", principal.getClass().getName());
        throw new RuntimeException("Unsupported authentication principal");
    }
}
