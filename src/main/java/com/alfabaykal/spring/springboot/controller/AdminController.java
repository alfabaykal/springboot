package com.alfabaykal.spring.springboot.controller;

import com.alfabaykal.spring.springboot.model.Role;
import com.alfabaykal.spring.springboot.model.User;
import com.alfabaykal.spring.springboot.service.RoleService;
import com.alfabaykal.spring.springboot.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final HttpServletResponse response;
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(HttpServletResponse response, UserService userService, RoleService roleService) {
        this.response = response;
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String getUsers(ModelMap model) {
        model.addAttribute("users", userService.getAllUser());
        return "users";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") int id, ModelMap model) {
        model.addAttribute("user", userService.getUser(id));
        return "user";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "new";
    }

    @PostMapping()
    public String addUser(@ModelAttribute("user") User user, @ModelAttribute("my_role") String my_role) throws IOException {
        try {
            Set<Role> roles = new HashSet<>();
            roles.add(roleService.getRoleByName("ROLE_" + my_role));
            roles.add(roleService.getRoleByName("ROLE_USER"));
            user.setRoles(roles);
            userService.addUser(user);
        } catch (DataAccessException e) {
            response.sendError(400, "Role does not exist");
            return "users";
        }
        return "redirect:/admin";
    }

    @GetMapping("/{id}/update")
    public String editUser(ModelMap modelMap, @PathVariable("id") int id) {
        modelMap.addAttribute("user", userService.getUser(id));
        return "update";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") int id) {
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}