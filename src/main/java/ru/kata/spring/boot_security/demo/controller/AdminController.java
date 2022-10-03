package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.ConstraintValidatorContext;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String findAll(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("/create")
    public String createUserForm(User user, Model model) {
        model.addAttribute("roles", userService.listRoles());
        model.addAttribute(user);
        return "create";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                             @RequestParam(value = "role") String role) {
        String uniqueLoginErrorMessage = "Аккаунт с такой почтой уже существует";
        User user1 = userService.findByEmail(user.getEmail());
        if (user1 != null) {
            bindingResult.addError(new FieldError("user", "email", uniqueLoginErrorMessage));
        }
        if (bindingResult.hasErrors()) {
            return "create";
        }
        user.setRoles(userService.findRolesByName(role));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.removeUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String edit(Model model, @PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        List<Role> listRoles = userService.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);

        return "update";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @PathVariable("id") Long id) {
        String uniqueLoginErrorMessage = "Аккаунт с такой почтой уже существует";
        user.setId(id);
        User userDb = userService.getUserById(id);
        String user1 = userDb.getEmail();
        String user2 = user.getEmail();
        if (!user1.equals(user2)) {
            bindingResult.addError(new FieldError("user", "email", uniqueLoginErrorMessage));
        }


        if (bindingResult.hasErrors()) {
            return "update";
        }
        userService.update(id, user);
        return "redirect:/admin";
    }
}



