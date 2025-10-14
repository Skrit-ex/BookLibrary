package com.example.booklib.service;

import com.example.booklib.dto.RegUserDto;
import com.example.booklib.entity.User;
import com.example.booklib.mapper.UserMapper;
import com.example.booklib.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
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
}
