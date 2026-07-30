package com.transactguard.transactguard.service;

import com.transactguard.transactguard.dto.UpdateUserDTO;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.exception.RequestException;
import com.transactguard.transactguard.repo.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;
    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("old_user");
        existingUser.setPassword("encoded_Pass123");
        existingUser.setEmail("old@email.com");
        existingUser.setBalance(150.0);
    }

    @Nested
    @DisplayName("getUserProfile")
    class GetUserTest {
        @Test
        @DisplayName("getUserProfile - Success")
        void getUserProfile_ReturnUser_WhenExists() {
            //Arrange
            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));

            //Act
            User result = userService.getUserProfile(1L);

            //Assert
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("old@email.com", result.getEmail());
            assertEquals("old_user", result.getUsername());
            assertEquals(150.0, result.getBalance());

            //Verify
            verify(repository, times(1)).findById(1L);

        }

        @Test
        @DisplayName("getUseProfile - Exception User Not Found")
        void getUserProfile_ThrowException_whenUserNotFound() {

            when(repository.findById(99L)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> userService.getUserProfile(99L)
            );

            assertEquals("Profile with ID 99 not found.", ex.getMessage());

            verify(repository, times(1)).findById(99L);

        }
    }

    @Nested
    @DisplayName("getBalance")
    class GetBalanceTest {

        @Test
        @DisplayName("getBalance - Success")
        void getBalance_ReturnsBalance_whenUserExists() {

            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));

            Double balance = userService.getBalance(1L);

            assertEquals(150.0, balance);
            verify(repository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("getBalance - Exception User Not Found")
        void getBalance_throwsException_whenUserNotFound() {

            when(repository.findById(99L)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> userService.getBalance(99L)
            );

            assertEquals("Profile with ID 99 not found.", ex.getMessage());

            verify(repository, times(1)).findById(99L);
        }
    }

    @Nested
    @DisplayName("updateUser")
    class updateUserTest  {

        @Test
        @DisplayName("updateUser -Success updating fields")
        void updateUser_ReturnUser_whenUpdateIsValid() {
            UpdateUserDTO dto = new UpdateUserDTO();
            dto.setUsername("new_username");
            dto.setPassword("new_raw_Pass123");
            dto.setEmail("new@gmail.com");

            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));

            when(encoder.matches("new_raw_Pass123","encoded_Pass123")).thenReturn(false);
            when(encoder.encode("new_raw_Pass123")).thenReturn("hashed_new_raw_Pass123");

            when(repository.findByEmail("new@gmail.com")).thenReturn(Optional.empty());
            when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));


            User result = userService.updateUser(dto, 1L);

            assertNotNull(result);
            assertEquals("new_username", result.getUsername());
            assertEquals("hashed_new_raw_Pass123", result.getPassword());
            assertEquals("new@gmail.com", result.getEmail());

            verify(repository, times(1)).findById(1L);
            verify(repository, times(1)).findByEmail("new@gmail.com");
            verify(encoder, times(1)).
                    matches("new_raw_Pass123", "encoded_Pass123");
            verify(encoder, times(1)).encode("new_raw_Pass123");
            verify(repository, times(1)).save(existingUser);

        }

        @Test
        @DisplayName("updateUser - Exception matching passwords")
        void updateUser_ThrowException_whenPasswordIsUnchanged() {
            UpdateUserDTO dto = new UpdateUserDTO();
            dto.setPassword("encoded_Pass123");

            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(encoder.matches(dto.getPassword(), existingUser.getPassword())).thenReturn(true);

            RequestException ex = assertThrows(
                    RequestException.class,
                    () -> userService.updateUser(dto, 1L)
            );

            assertEquals("password", ex.getField());
            assertEquals("New password must be different from your current password.", ex.getMessage());

            verify(repository, times(1)).findById(1L);
            verify(repository, never()).save(any());
            verify(encoder, times(1)).matches(dto.getPassword(), existingUser.getPassword());
        }

        @Test
        @DisplayName("updateUser - Exception matching usernames")
        void updateUser_ThrowsException_WhenUsernameIsUnchanged() {
            UpdateUserDTO dto = new UpdateUserDTO();
            dto.setUsername("old_user");

            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));


            RequestException ex = assertThrows(
                    RequestException.class,
                    () -> userService.updateUser(dto, 1L)
            );

            assertEquals("username", ex.getField());
            assertEquals("New username must be different from your current username.", ex.getMessage());

            verify(repository, times(1)).findById(1L);
            verify(repository, never()).save(any());


        }

        @Test
        @DisplayName("updateUser - Exception matching emails")
        void updateUser_ThrowsException_WhenEmailIsUnchanged() {

            UpdateUserDTO dto = new UpdateUserDTO();
            dto.setEmail("old@email.com");

            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));

            RequestException ex = assertThrows(
                    RequestException.class,
                    () -> userService.updateUser(dto, 1L)
            );

            assertEquals("email", ex.getField());
            assertEquals("New email must be different from your current email.", ex.getMessage());

            verify(repository, times(1)).findById(1L);
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("updateUser - Exception email already exist")
        void updateUser_ThrowsException_WhenEmailIsTaken() {
            User takenEmailUser = new User();
            UpdateUserDTO dto = new UpdateUserDTO();
            dto.setEmail("taken@email.com");

            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.of(takenEmailUser));

            RequestException ex = assertThrows(
                    RequestException.class,
                    () -> userService.updateUser(dto, 1L)
            );

            assertEquals("email", ex.getField());
            assertEquals("Email is already in use.", ex.getMessage());

            verify(repository, times(1)).findById(1L);
            verify(repository, never()).save(any());
            verify(repository, times(1)).findByEmail(dto.getEmail());
        }
    }
}