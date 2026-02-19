package com.example.issuetracker.init;

import com.example.issuetracker.entity.Role;
import com.example.issuetracker.entity.RoleName;
import com.example.issuetracker.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception{
        if (roleRepository.count() == 0){
            roleRepository.save(new Role(null, RoleName.USER));
            roleRepository.save(new Role(null, RoleName.ADMIN));
        }
    }
}
