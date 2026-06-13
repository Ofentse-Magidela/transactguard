package com.transactguard.transactguard.service;

import com.transactguard.transactguard.Role;
import com.transactguard.transactguard.dto.LoginUserDTO;
import com.transactguard.transactguard.dto.RegisterUserDTO;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.entity.UserPrincipal;
import com.transactguard.transactguard.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public User registerUser(RegisterUserDTO registerUserDTO) {

        User user = new User();

        String hashedPassword = encoder.encode(registerUserDTO.getPassword());
        user.setUsername(registerUserDTO.getUsername());
        user.setPassword(hashedPassword);
        user.setEmail(registerUserDTO.getEmail());
        user.setBalance(registerUserDTO.getBalance());
        user.setCreatedAt(LocalDate.now());

        if (user.getRole() == null) user.setRole(Role.USER);
        else user.setRole(Role.ADMIN);

        return repository.save(user);
    }

    public LoginUserDTO loginUser(LoginUserDTO loginUserDTO) {

        UserDetails userDetails = loadUserByUsername(loginUserDTO.getUsername());
        if(userDetails == null) return null;
        if (encoder.matches(loginUserDTO.getPassword(), userDetails.getPassword())) return loginUserDTO;
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException{
        Optional<User> user = repository.findByUsername(username);

        if (user.isEmpty()) return null;
        return new UserPrincipal(user.get());
    }
}
