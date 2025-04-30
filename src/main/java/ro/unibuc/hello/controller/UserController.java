package ro.unibuc.hello.controller;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.hello.dto.User;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.service.UserService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    private final AtomicInteger activeUsersGauge;

    @Autowired
    public UserController(MeterRegistry meterRegistry) {
        this.activeUsersGauge = new AtomicInteger(0);
        Gauge.builder("active_users_count", activeUsersGauge, AtomicInteger::get)
                .description("Numărul de utilizatori activi")
                .register(meterRegistry);
    }

    @GetMapping("/users")
    @ResponseBody
    @Timed(value = "user.get.all", description = "Timp pentru GET toți utilizatorii")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    @ResponseBody
    @Timed(value = "user.get.byId", description = "Timp pentru GET utilizator după ID")
    public User getUserById(@PathVariable String id) throws EntityNotFoundException {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    @ResponseBody
    @Counted(value = "user.create.count", description = "Număr de cereri POST pentru creare utilizator")
    public User createUser(@RequestBody User user) {
        activeUsersGauge.incrementAndGet();
        return userService.saveUser(user);
    }

    @PutMapping("/users/{id}")
    @ResponseBody
    @Counted(value = "user.update.count", description = "Număr de cereri PUT pentru actualizare utilizator")
    public User updateUser(@PathVariable String id, @RequestBody User user) throws EntityNotFoundException {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    @ResponseBody
    @Counted(value = "user.delete.count", description = "Număr de cereri DELETE pentru ștergere utilizator")
    public void deleteUser(@PathVariable String id) throws EntityNotFoundException {
        activeUsersGauge.decrementAndGet();
        userService.deleteUser(id);
    }

    @GetMapping("/users/name/{name}")
    @ResponseBody
    @Timed(value = "user.get.byName", description = "Timp pentru GET utilizator după nume")
    public User getUserByName(@PathVariable String name) throws EntityNotFoundException {
        return userService.getUserByName(name);
    }
}
