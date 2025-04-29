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
        List<User> users = Arrays.asList(
                new User("1", "John", "Admin", "password"),
                new User("2", "Jane", "User", "password")
        );
        when(userService.getAllUsers()).thenReturn(users);

        List<User> response = userController.getAllUsers();  

        assertEquals(2, response.size());
        assertEquals("John", response.get(0).getName());
        assertEquals("Jane", response.get(1).getName());
    }

    @Test
    void testGetUserById_NonExistingUser() throws EntityNotFoundException {
        when(userService.getUserById("99")).thenThrow(new EntityNotFoundException("User not found"));

        assertThrows(EntityNotFoundException.class, () -> userController.getUserById("99"));
    }

    @Test
    void testSaveUser() {
        User user = new User("1", "Alice", "Admin", "password");
        when(userService.saveUser(any(User.class))).thenReturn(user);

        User response = userController.createUser(user);  

        assertNotNull(response);
        assertEquals("Alice", response.getName());
    }

    @Test
    void testUpdateUser_ExistingUser() throws EntityNotFoundException {
        User updatedUser = new User("1", "Alice Updated", "Admin", "newpassword");
        when(userService.updateUser(eq("1"), any(User.class))).thenReturn(updatedUser);

        User response = userController.updateUser("1", updatedUser);  

        assertNotNull(response);
        assertEquals("Alice Updated", response.getName());
    }

    @Test
    void testDeleteUser_ExistingUser() throws EntityNotFoundException {
        doNothing().when(userService).deleteUser("1");

        userController.deleteUser("1");  

        verify(userService, times(1)).deleteUser("1");
    }

    @Test
    void testDeleteUser_NonExistingUser() {
        doThrow(new EntityNotFoundException("User not found")).when(userService).deleteUser("99");

        assertThrows(EntityNotFoundException.class, () -> userController.deleteUser("99"));
    }

    @Test
    void testGetUserByName_ExistingUser() throws EntityNotFoundException {
        User user = new User("1", "Alice", "Admin", "password");
        when(userService.getUserByName("Alice")).thenReturn(user);

        User response = userController.getUserByName("Alice");

        assertNotNull(response);
        assertEquals("Alice", response.getName());
        assertEquals("Admin", response.getRole());
    }

    @Test
    void testGetUserByName_NonExistentUser() throws EntityNotFoundException {
        when(userService.getUserByName("NonExistentUser")).thenThrow(new EntityNotFoundException("User not found"));

        assertThrows(EntityNotFoundException.class, () -> userController.getUserByName("NonExistentUser"));
    }

    @Test
    void testSaveUser_InvalidData() {
        User invalidUser = new User("1", "", "Admin", ""); 
        when(userService.saveUser(any(User.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        assertThrows(IllegalArgumentException.class, () -> userController.createUser(invalidUser));
    }

    @Test
    void testUpdateUser_InvalidData() throws EntityNotFoundException {
        User invalidUser = new User("1", "", "Admin", ""); 
        when(userService.updateUser(eq("1"), any(User.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        assertThrows(IllegalArgumentException.class, () -> userController.updateUser("1", invalidUser));
    }
}
