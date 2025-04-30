package ro.unibuc.hello.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ro.unibuc.hello.dto.User;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private Counter userCreateCounter;

    @Mock
    private Counter userUpdateCounter;

    @Mock
    private Counter userDeleteCounter;

    @Mock
    private Timer userRequestTimer;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(meterRegistry.counter("user_create_requests_total")).thenReturn(userCreateCounter);
        when(meterRegistry.counter("user_update_requests_total")).thenReturn(userUpdateCounter);
        when(meterRegistry.counter("user_delete_requests_total")).thenReturn(userDeleteCounter);
        when(meterRegistry.timer("user_request_duration_seconds")).thenReturn(userRequestTimer);

       
    }

}
