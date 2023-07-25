package com.example.springbootdemo.controller;

import com.example.springbootdemo.model.User;
import com.example.springbootdemo.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;




@Controller
@RequestMapping("/user")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "home"; // Переход на домашнюю страницу
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "admin"; // Переход на страницу администратора
    }

    @GetMapping("/user")
    public String user() {
        return "user"; // Переход на страницу пользователя
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Переход на страницу входа
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied"; // Переход на страницу доступа запрещен
    }

    // CRUD операции

    @GetMapping("/admin/users")
    public String getAllUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users"; // Страница со списком пользователей
    }

    @GetMapping("/admin/users/{id}")
    public String getUserById(@PathVariable("id") long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user-details"; // Страница с деталями о пользователе
    }

    @GetMapping("/admin/users/new")
    public String createUserForm() {
        return "create-user"; // Страница создания нового пользователя
    }

    @PostMapping("/admin/users")
    public String createUser(@RequestParam("name") String name, @RequestParam("email") String email) {
        User user = new User();
        user.setUsername(name);
        user.setPassword(email);
        userService.saveUser(user);
        return "redirect:/user/admin/users"; // Редирект на список пользователей
    }

    @GetMapping("/admin/users/{id}/edit")
    public String updateUserForm(@PathVariable("id") long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "update-user"; // Страница обновления информации о пользователе
    }

    @PostMapping("/admin/users/{id}")
    public String updateUser(@PathVariable("id") long id, @RequestParam("name") String name, @RequestParam("email") String email) {
        User user = userService.findById(id);
        user.setUsername(name);
        user.setEmail(email);
        userService.saveUser(user);
        return "redirect:/user/admin/users"; // Редирект на список пользователей
    }

    @GetMapping("/admin/users/{id}/delete")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteById(id);
        return "redirect:/user/admin/users"; // Редирект на список пользователей
    }
}