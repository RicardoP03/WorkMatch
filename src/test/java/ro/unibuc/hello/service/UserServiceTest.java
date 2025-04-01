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
        List<UserEntity> entities = Arrays.asList(
                new UserEntity("Alice", "employer", "pass123"),
                new UserEntity("Bob", "applicant", "pass456")
        );
        when(userRepository.findAll()).thenReturn(entities);

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("Alice", users.get(0).getName());
        assertEquals("Bob", users.get(1).getName());
    }

    @Test
    void testGetUserById_NonExistingUser() {
        String id = "99";
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(id));
    }

    @Test
    void testSaveUser() {
        User user = new User(null, "Alice", "Admin", "pass123");
        UserEntity entity = new UserEntity("Alice", "Admin", "pass123");
        when(userRepository.save(any(UserEntity.class))).thenReturn(entity);

        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals("Alice", savedUser.getName());
        assertEquals("Admin", savedUser.getRole());
    }

    @Test
    void testUpdateUser_ExistingUser() throws EntityNotFoundException {
        String id = "1";
        UserEntity existingUser = new UserEntity("OldName", "OldRole", "oldpass");
        User userToUpdate = new User(id, "NewName", "NewRole", "newpass");

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(existingUser);

        User updatedUser = userService.updateUser(id, userToUpdate);

        assertNotNull(updatedUser);
        assertEquals("NewName", updatedUser.getName());
        assertEquals("NewRole", updatedUser.getRole());
        assertEquals("newpass", updatedUser.getPassword());

        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUser_NonExistingUser() {
        String id = "99";
        User user = new User(id, "NonExistent", "User", "pass999");
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(id, user));
    }

    @Test
    void testDeleteUser_ExistingUser() throws EntityNotFoundException {
        String id = "1";
        UserEntity entity = new UserEntity("Alice", "Admin", "pass123");
        when(userRepository.findById(id)).thenReturn(Optional.of(entity));

        userService.deleteUser(id);

        verify(userRepository, times(1)).delete(entity);
    }

    @Test
    void testDeleteUser_NonExistingUser() {
        String id = "99";
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(id));
    }

    @Test
    void testGetUserByName_ExistingUser() throws EntityNotFoundException {
        String name = "Alice";
        UserEntity entity = new UserEntity(name, "employer", "pass123");
        when(userRepository.findByName(name)).thenReturn(entity);

        User user = userService.getUserByName(name);

        assertNotNull(user);
        assertEquals(name, user.getName());
        assertEquals("employer", user.getRole());
    }

    @Test
    void testGetUserByName_NonExistingUser() {
        String name = "Unknown";
        when(userRepository.findByName(name)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> userService.getUserByName(name));
    }
}
