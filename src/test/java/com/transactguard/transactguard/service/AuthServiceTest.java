package com.transactguard.transactguard.service;

import com.transactguard.transactguard.Role;
import com.transactguard.transactguard.dto.LoginUserDTO;
import com.transactguard.transactguard.dto.RegisterUserDTO;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.entity.UserPrincipal;
import com.transactguard.transactguard.exception.RequestException;
import com.transactguard.transactguard.repo.UserRepository;
import com.transactguard.transactguard.security.JWTService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository repository;
    @Mock
    private JWTService jwtService;
    @Mock
    private BCryptPasswordEncoder encoder;
    @Mock
    private AuthenticationManager auth;

    @InjectMocks
    private AuthService authService;

    private RegisterUserDTO dto;
    @BeforeEach
    void setup() {
        dto = new RegisterUserDTO();
        dto.setUsername("newUser");
        dto.setEmail("newuser@gmail.com");
        dto.setPassword("!raw_Pass123");
        dto.setBalance(750.0);
    }

    @Nested
    @DisplayName("registerUser")
    class registerUser {

        @Test
        @DisplayName("registerUser - Success")
        void registerUser_ReturnUser_RegisterDtoIsValid() {

            when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
            when(encoder.encode(dto.getPassword())).thenReturn("hashedPassword");

            authService.registerUser(dto);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(repository).save(userCaptor.capture());

            User savedUser = userCaptor.getValue();

            assertNotNull(savedUser);
            assertEquals(dto.getUsername(), savedUser.getUsername());
            assertEquals("hashedPassword", savedUser.getPassword());
            assertEquals(dto.getEmail(), savedUser.getEmail());
            assertEquals(dto.getBalance(), savedUser.getBalance());
            assertEquals(Role.USER, savedUser.getRole());
            assertNotNull(savedUser.getCreatedAt());

            verify(repository, times(1)).findByEmail(dto.getEmail());
            verify(encoder, times(1)).encode(dto.getPassword());

        }

        @Test
        @DisplayName("registerUser - Exception email already exist")
        void registerUser_ThrowsException_WhenTheEmailAlreadyExist() {

            when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new User()));

            RequestException ex = assertThrows(
                    RequestException.class,
                    () -> authService.registerUser(dto)
            );

            assertEquals("email", ex.getField());
            assertEquals("Email already exist", ex.getMessage());

            verify(repository, times(1)).findByEmail(dto.getEmail());
            verify(repository, never()).save(any(User.class));
            verifyNoInteractions(encoder);
        }

    }

    @Nested
    @DisplayName("loginUser")
    class loginUser {

        @Test
        @DisplayName("loginUser - Success")
        void loginUser_ReturnJwtToken_WhenLoginIsSuccessful() {

            LoginUserDTO dto = new LoginUserDTO();
            dto.setEmail("newuser@gmail.com");
            dto.setPassword("!raw_Pass123");

            UserPrincipal mockPrincipal = mock(UserPrincipal.class);
            when(mockPrincipal.getId()).thenReturn(1L);
            when(mockPrincipal.getEmail()).thenReturn(dto.getEmail());

            GrantedAuthority mockGrantedAuthority = () -> "ROLE_USER";

            Authentication mockAuth = mock(Authentication.class);
            when(mockAuth.getPrincipal()).thenReturn(mockPrincipal);
            doReturn(List.of(mockGrantedAuthority)).when(mockAuth).getAuthorities();

            when(auth.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(mockAuth);
            when(jwtService.generateToken(anyMap(), eq("newuser@gmail.com")))
                    .thenReturn("jwt.Token");

            String token = authService.loginUser(dto);
            @SuppressWarnings("unchecked")
            ArgumentCaptor<Map<String, Object>> captured = ArgumentCaptor.forClass(Map.class);

            verify(jwtService).generateToken(captured.capture(), eq("newuser@gmail.com"));
            Map<String, Object> claims = captured.getValue();

            assertEquals(1L, claims.get("userId"));
            assertEquals(List.of("ROLE_USER"), claims.get("roles"));
            assertNotNull(token);
            assertEquals("jwt.Token", token);

            verify(auth, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }

        @Test
        @DisplayName("loginUser - Exception Bad Credentials")
        void loginUser_ThrowsRequestException_WhenCredentialsAreInvalid() {

            LoginUserDTO dto = new LoginUserDTO();
            dto.setEmail("wrong@gmail.com");
            dto.setPassword("wrongPassword");

            when(auth.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Invalid credentials"));

            RequestException ex = assertThrows(
                    RequestException.class,
                    () -> authService.loginUser(dto)
            );

            assertEquals("form", ex.getField());
            assertEquals("Email or password is incorrect", ex.getMessage());

            verify(auth, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verifyNoInteractions(jwtService);
        }

    }

}