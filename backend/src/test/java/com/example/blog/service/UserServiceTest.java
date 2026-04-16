package com.example.blog.service;

import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByEmail() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        User result = userService.getByEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testRegister() {
        // Arrange
        User user = new User();
        user.setPassword("plainPassword");
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode("plainPassword")).thenReturn(encodedPassword);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userService.register(user);

        // Assert
        assertNotNull(result);
        assertEquals(encodedPassword, result.getPassword());
        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testLogin() {
        // Arrange
        String email = "test@example.com";
        String password = "plainPassword";
        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPassword");
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(true);

        // Act
        User result = userService.login(email, password);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, "encodedPassword");
    }

    @Test
    void testLoginWithInvalidPassword() {
        // Arrange
        String email = "test@example.com";
        String password = "wrongPassword";
        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPassword");
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(false);

        // Act
        User result = userService.login(email, password);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, "encodedPassword");
    }
}


