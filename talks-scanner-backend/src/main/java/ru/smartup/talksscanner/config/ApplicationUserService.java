package ru.smartup.talksscanner.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.smartup.talksscanner.repos.UserRepo;

import java.util.ArrayList;

/**
 * Represents UserDetailsService implementation for Spring security httpBasic authentication.
 */
@Service
public class ApplicationUserService implements UserDetailsService {

    private final UserRepo userRepo;

    public ApplicationUserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ru.smartup.talksscanner.domain.User user = userRepo.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s not authorized", username)));
        return new User(username, user.getEncodedPassword(), new ArrayList<>());
    }
}
