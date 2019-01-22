package contactManagement.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //        http.csrf().disable(); // login a
        // define what permissions are needed to access the system
        http.authorizeRequests()
            .anyRequest().permitAll() // every page can be accessed by every user
            .and()
            .formLogin().loginPage("/login") // login request should be expected at the /login route
            .usernameParameter("email").passwordParameter("password") // login parameter is email and password parameter is password
            .and()
            .logout().logoutSuccessUrl("/login?logout") // logout leads to "/login?logout if there is error - we wont have access
            .and()
            .exceptionHandling().accessDeniedPage("/error/403")
            .and()
            .csrf(); // cross site request forgery enabled
    }
}
