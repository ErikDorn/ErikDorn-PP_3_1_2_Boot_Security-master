package ru.kata.spring.boot_security.demo;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Component
public class Init {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    public Init(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }
PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @PostConstruct
    public void init() {

        User user = new User("User", "Userov", "user@mail.ru", passwordEncoder.encode("user")); //1234
        User admin = new User("Admin", "Adminov", "admin@mail.ru", passwordEncoder.encode("admin")); //4321

        Role roleUser = new Role("ROLE_USER");
        Role roleAdmin = new Role("ROLE_ADMIN");

        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);

        List<Role> userRoles = new ArrayList<>();
        List<Role> adminRoles = new ArrayList<>();

        userRoles.add(roleUser);
        adminRoles.add(roleAdmin);

        user.setRoles(userRoles);
        admin.setRoles(adminRoles);

        userRepository.save(user);
        userRepository.save(admin);

    }

}




