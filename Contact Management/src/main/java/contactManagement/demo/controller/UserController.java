package contactManagement.demo.controller;

import contactManagement.demo.bindingModel.UserBindingModel;
import contactManagement.demo.entity.Role;
import contactManagement.demo.entity.User;
import contactManagement.demo.repository.RoleRepository;
import contactManagement.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/register")
    public String register(Model model) {

        // model object works with key-value pairs -> view should be replaced by user/register
        model.addAttribute("view", "user/register");

        return "base-layout";
    }

    //  html vs json, xml csv
    // corresponds to the form method. first check if the passwords match
    //
    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public String registerProcess(UserBindingModel userBindingModel) {
        if (!userBindingModel.getPassword().equals((userBindingModel.getConfirmPassword()))) {
            return "redirect:/register";
        }

        // create a password encoder and create new object form User Entity
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user =
            new User(userBindingModel.getEmail(), userBindingModel.getFullName(), bCryptPasswordEncoder.encode((userBindingModel.getPassword())));

        // create method in User that will add a new role, get the ROLE from the database and add it to the user (add ROLES manually)
        Role userRole = this.roleRepository.findByName("ROLE_USER");
        user.addRole(userRole);

        // save the user in the database and return login view
        this.userRepository.saveAndFlush(user);

        return "redirect:/login";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String login(Model model) {
        // return the login view
        model.addAttribute("view", "user/login");

        return "base-layout";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {

        // check if there is logged in user and IF there is, the authentication module log outs the user
        //  -> then redirect to the login page
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    // only for logged in users, guests redirected to login page
    @RequestMapping(method = RequestMethod.GET, value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profilePage(Model model) {

        // this gives only basic properties of the User - email, roles and password
        // using them, we extract the current user from the database

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userRepository.findByEmail(principal.getUsername());

        // we add the user to the model and return the view (templates/user/profile.html
        model.addAttribute("user", user);
        model.addAttribute("view", "user/profile");
        return "base-layout";
    }
}
