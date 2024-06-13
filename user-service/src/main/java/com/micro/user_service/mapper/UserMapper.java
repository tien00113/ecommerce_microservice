package com.micro.user_service.mapper;

import java.util.stream.Collectors;

import com.micro.user_service.dto.UserDTO;
import com.micro.user_service.models.User;
import com.micro.user_service.models.User.Role;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRoles(user.getRoles().stream().map(Role::name).collect(Collectors.toList()));
        
        return userDTO;
    }
}
