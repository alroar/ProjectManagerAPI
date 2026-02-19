package com.example.issuetracker.service;

import com.example.issuetracker.dto.LoginDTO;
import com.example.issuetracker.dto.UserDTO;
import com.example.issuetracker.entity.Role;
import com.example.issuetracker.entity.RoleName;
import com.example.issuetracker.entity.User;
import com.example.issuetracker.exceptions.UserAlreadyExistsException;
import com.example.issuetracker.exceptions.UserRoleNotFound;
import com.example.issuetracker.repository.RoleRepository;
import com.example.issuetracker.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public void registerUser(UserDTO dto) throws Exception {
        if(userRepository.findByUserName(dto.getUsername()).isPresent()){
            throw new UserAlreadyExistsException();
        }
        if(userRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("Email already used");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = new User();
        user.setUserName(dto.getUsername());
        user.setPassword(encodedPassword);
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());

        Role userRole = roleRepository.findByRoleType(RoleName.USER)
                        .orElseThrow(() -> new UserRoleNotFound("Default role USER not found"));

        user.getRoles().add(userRole);

        userRepository.save(user);
    }

    public User loginUser(LoginDTO loginDTO){
        User user = userRepository.findByUserName(loginDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username couldn't be found"));

        if(!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())){
            throw new UsernameNotFoundException("Invalid credentials");
        }

        return user;


    }
}
