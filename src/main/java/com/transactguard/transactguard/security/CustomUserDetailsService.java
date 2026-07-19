package com.transactguard.transactguard.security;

import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.entity.UserPrincipal;
import com.transactguard.transactguard.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    final private UserRepository repository;
    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<User> user = repository.findByEmail(username);

        if (user.isEmpty()) throw new UsernameNotFoundException("User not found");
        return new UserPrincipal(user.get());
    }
}
