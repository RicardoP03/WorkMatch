package ro.unibuc.hello.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ro.unibuc.hello.dto.User;
import ro.unibuc.hello.service.UserService;
import ro.unibuc.hello.exception.EntityNotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(
                new User("1", "John", "Admin", "password"),
                new User("2", "Jane", "User", "password")
        );
        when(userService.getAllUsers()).thenReturn(users);

        // Act
        List<User> response = userController.getAllUsers();  

        // Assert
        assertEquals(2, response.size());
        assertEquals("John", response.get(0).getName());
        assertEquals("Jane", response.get(1).getName());
    }

    @Test
    void testGetUserById_NonExistingUser() throws EntityNotFoundException {
        // Arrange
        when(userService.getUserById("99")).thenThrow(new EntityNotFoundException("User not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userController.getUserById("99"));
    }

    @Test
    void testSaveUser() {
        // Arrange
        User user = new User("1", "Alice", "Admin", "password");
        when(userService.saveUser(any(User.class))).thenReturn(user);

        // Act
        User response = userController.createUser(user);  

        // Assert
        assertNotNull(response);
        assertEquals("Alice", response.getName());
    }

    @Test
    void testUpdateUser_ExistingUser() throws EntityNotFoundException {
        // Arrange
        User updatedUser = new User("1", "Alice Updated", "Admin", "newpassword");
        when(userService.updateUser(eq("1"), any(User.class))).thenReturn(updatedUser);

        // Act
        User response = userController.updateUser("1", updatedUser);  

        // Assert
        assertNotNull(response);
        assertEquals("Alice Updated", response.getName());
    }

    @Test
    void testDeleteUser_ExistingUser() throws EntityNotFoundException {
        // Arrange
        doNothing().when(userService).deleteUser("1");

        // Act
        userController.deleteUser("1");  

        // Assert
        verify(userService, times(1)).deleteUser("1");
    }

    @Test
    void testDeleteUser_NonExistingUser() {
        // Arrange
        doThrow(new EntityNotFoundException("User not found")).when(userService).deleteUser("99");

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userController.deleteUser("99"));
    }
}
