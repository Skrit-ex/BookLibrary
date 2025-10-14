package com.example.booklib.mapper;

import com.example.booklib.dto.RegUserDto;
import com.example.booklib.entity.User;

public class UserMapper {

    public static User regUserDtoToUser(RegUserDto regUserDto){
        User user = new User();
        user.setUserName(regUserDto.getUsername());
        user.setPassword(regUserDto.getPassword());
        user.setEmail(regUserDto.getEmail());
        return user;
    }


}
