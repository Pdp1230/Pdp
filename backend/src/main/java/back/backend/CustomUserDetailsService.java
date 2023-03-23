package back.backend;

import back.backend.entity.User;
import back.backend.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.HashSet;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
          User user = userRepository.findByEmail(userEmail)
                 .orElseThrow(() ->
                         new UsernameNotFoundException("User not found with email: "+ userEmail));

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                new HashSet<>());
    }
}