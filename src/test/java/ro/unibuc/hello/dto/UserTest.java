package ro.unibuc.hello.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserConstructorAndGetters() {
        User user = new User("1", "Alice", "admin", "password123");

        assertEquals("1", user.getId());
        assertEquals("Alice", user.getName());
        assertEquals("admin", user.getRole());
        assertEquals("password123", user.getPassword());
    }

    @Test
    void testUserSetters() {
        User user = new User("1", "Alice", "admin", "password123");
        user.setId("2");
        user.setName("Bob");
        user.setRole("user");
        user.setPassword("newpass");

        assertEquals("2", user.getId());
        assertEquals("Bob", user.getName());
        assertEquals("user", user.getRole());
        assertEquals("newpass", user.getPassword());
    }
}

