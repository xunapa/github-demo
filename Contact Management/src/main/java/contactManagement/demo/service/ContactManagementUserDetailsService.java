package contactManagement.demo.service;

import contactManagement.demo.entity.User;
import contactManagement.demo.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service("contactManagementUserDetailsService")
public class ContactManagementUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public ContactManagementUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        // throw exception if user does not exist
        if (user == null) {
            throw new UsernameNotFoundException("Invalid user");
        } else {
            // get all of the user's permissions and create a collection of authorities
            // -> create a spring security user with the email password and authorities

            Set<GrantedAuthority> grantedAuthorities =
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
        }
    }
}
