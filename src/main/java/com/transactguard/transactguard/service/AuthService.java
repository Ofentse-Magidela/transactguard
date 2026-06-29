package com.transactguard.transactguard.service;

import com.transactguard.transactguard.Role;
import com.transactguard.transactguard.dto.LoginUserDTO;
import com.transactguard.transactguard.dto.RegisterUserDTO;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.entity.UserPrincipal;
import com.transactguard.transactguard.repo.UserRepository;
import com.transactguard.transactguard.security.JWTService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    final private UserRepository repository;
    final private JWTService jwtService;
    final private BCryptPasswordEncoder encoder;
    final private AuthenticationManager auth;

    public AuthService(UserRepository repository
                     ,JWTService jwtService
                     ,BCryptPasswordEncoder encoder
                     ,AuthenticationManager auth) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.encoder = encoder;
        this.auth = auth;
    }

    public void registerUser(RegisterUserDTO registerUserDTO) {

        User user = new User();

        String hashedPassword = encoder.encode(registerUserDTO.getPassword());
        user.setUsername(registerUserDTO.getUsername());
        user.setPassword(hashedPassword);
        user.setEmail(registerUserDTO.getEmail());
        user.setBalance(registerUserDTO.getBalance());
        user.setCreatedAt(LocalDate.now());

        if (user.getRole() == null) user.setRole(Role.USER);
        else user.setRole(Role.ADMIN);

        repository.save(user);
    }

    public String loginUser(LoginUserDTO loginUserDTO) {

        Map<String, Object> extraClaims = new HashMap<>();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginUserDTO.getUsername(),
                loginUserDTO.getPassword());
        Authentication authentication = auth.authenticate(token);

        if(authentication.isAuthenticated()) {


            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            extraClaims.put("userId", principal.getId());

            List<String> roles = new ArrayList<>();
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                roles.add(grantedAuthority.getAuthority());
            }
            extraClaims.put("roles", roles);
            return jwtService.generateToken(extraClaims, loginUserDTO.getUsername());
        }
        throw new RuntimeException("Username Or Password is Incorrect");
    }
}
