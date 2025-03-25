package ro.unibuc.hello.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ro.unibuc.hello.data.UserEntity;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.User;
import ro.unibuc.hello.exception.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        List<UserEntity> entities = Arrays.asList(
                new UserEntity("Alice", "employer", "pass123"),
                new UserEntity("Bob", "applicant", "pass456")
        );
        when(userRepository.findAll()).thenReturn(entities);

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertEquals(2, users.size());
        assertEquals("Alice", users.get(0).getName());
        assertEquals("Bob", users.get(1).getName());
    }

    @Test
    void testGetUserById_NonExistingUser() {
        // Arrange
        String id = "99";
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(id));
    }

    @Test
    void testSaveUser() {
        // Arrange
        User user = new User(null, "Alice", "Admin", "pass123"); 
        UserEntity entity = new UserEntity("Alice", "Admin", "pass123");
        when(userRepository.save(any(UserEntity.class))).thenReturn(entity);

        // Act
        User savedUser = userService.saveUser(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals("Alice", savedUser.getName());
        assertEquals("Admin", savedUser.getRole());
    }

    @Test
    void testUpdateUser_NonExistingUser() {
        // Arrange
        String id = "99";
        User user = new User(id, "NonExistent", "User", "pass999");
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(id, user));
    }

    @Test
    void testDeleteUser_ExistingUser() throws EntityNotFoundException {
        // Arrange
        String id = "1";
        UserEntity entity = new UserEntity("Alice", "Admin", "pass123");
        when(userRepository.findById(id)).thenReturn(Optional.of(entity));

        // Act
        userService.deleteUser(id);

        // Assert
        verify(userRepository, times(1)).delete(entity);
    }

    @Test
    void testDeleteUser_NonExistingUser() {
        // Arrange
        String id = "99";
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(id));
    }

    @Test
    void testGetUserByName_ExistingUser() throws EntityNotFoundException {
        // Arrange
        String name = "Alice";
        UserEntity entity = new UserEntity(name, "employer", "pass123");
        when(userRepository.findByName(name)).thenReturn(entity);

        // Act
        User user = userService.getUserByName(name);

        // Assert
        assertNotNull(user);
        assertEquals(name, user.getName());
        assertEquals("employer", user.getRole());
    }

    @Test
    void testGetUserByName_NonExistingUser() {
        // Arrange
        String name = "Unknown";
        when(userRepository.findByName(name)).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.getUserByName(name));
    }
}
