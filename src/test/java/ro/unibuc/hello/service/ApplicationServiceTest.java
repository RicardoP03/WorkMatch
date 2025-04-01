package ro.unibuc.hello.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ro.unibuc.hello.data.ApplicationEntity;
import ro.unibuc.hello.data.UserEntity;
import ro.unibuc.hello.data.ApplicationRepository;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.data.JobRepository;
import ro.unibuc.hello.data.JobEntity;
import ro.unibuc.hello.dto.Application;
import ro.unibuc.hello.exception.EntityNotFoundException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ApplicationServiceTest {
    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private ApplicationService applicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_findApplicationByJobId() {
        Date sentDate = new Date();
        List<ApplicationEntity> applications = Arrays.asList(
                                    new ApplicationEntity("1", "10", "20", sentDate),
                                    new ApplicationEntity("2", "10", "21", sentDate)
                                );

        when(applicationRepository.findByJobId("10")).thenReturn(applications);
        List<Application> apps = applicationService.findApplicationByJobId("10");

        assertEquals(2, apps.size());
        assertEquals("1", apps.get(0).getId());
        assertEquals("2", apps.get(1).getId());
        assertEquals("10", apps.get(0).getJobId());
        assertEquals("10", apps.get(1).getJobId());
        assertEquals("20", apps.get(0).getSeekerId());
        assertEquals("21", apps.get(1).getSeekerId());
        assertEquals(sentDate, apps.get(0).getDate());
        assertEquals(sentDate, apps.get(1).getDate());
    }

    @Test
    void test_findApplicationBySeekerId() {
        Date sentDate = new Date();
        List<ApplicationEntity> applications = Arrays.asList(
                                    new ApplicationEntity("1", "10", "20", sentDate),
                                    new ApplicationEntity("2", "11", "20", sentDate)
                                );

        when(applicationRepository.findBySeekerId("20")).thenReturn(applications);
        List<Application> apps = applicationService.findApplicationBySeekerId("20");

        assertEquals(2, apps.size());
        assertEquals("1", apps.get(0).getId());
        assertEquals("2", apps.get(1).getId());
        assertEquals("10", apps.get(0).getJobId());
        assertEquals("11", apps.get(1).getJobId());
        assertEquals("20", apps.get(0).getSeekerId());
        assertEquals("20", apps.get(1).getSeekerId());
        assertEquals(sentDate, apps.get(0).getDate());
        assertEquals(sentDate, apps.get(1).getDate());
    }

    @Test
    void test_findApplicationById_NoError() {
        Date sentDate = new Date();
        ApplicationEntity apEnt = new ApplicationEntity("1", "10", "20", sentDate);
        when(applicationRepository.findById("1")).thenReturn(Optional.of(apEnt));

        Application ap = applicationService.findApplicationById("1");

        assertEquals("1", ap.getId());
        assertEquals("10", ap.getJobId());
        assertEquals("20", ap.getSeekerId());
        assertEquals(sentDate, ap.getDate());
    }

    @Test
    void test_findApplicationById_NotFound() {
        String id = "99";
        when(applicationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> applicationService.findApplicationById(id));
    }

    @Test
    void test_saveApplication_noError() {
        Date sentDate = new Date();
        Application ap = new Application("1", "10", "20", sentDate);
        ApplicationEntity apEnt = new ApplicationEntity("1", "10", "20", sentDate);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(mock(UserEntity.class)));
        when(jobRepository.findById(anyString())).thenReturn(Optional.of(mock(JobEntity.class)));
        when(applicationRepository.findByJobIdAndSeekerId("10", "20")).thenReturn(Collections.emptyList());
        when(applicationRepository.save(any(ApplicationEntity.class))).thenReturn(apEnt);

        Application savedApp = applicationService.saveAplication(ap);

        assertEquals("1", savedApp.getId());
        assertEquals("10", savedApp.getJobId());
        assertEquals("20", savedApp.getSeekerId());
        assertEquals(sentDate, savedApp.getDate());
    }

    @Test
    void test_saveApplication_UserNotFound() {
        Date sentDate = new Date();
        Application ap = new Application("1", "10", "20", sentDate);;

        when(userRepository.findById("20")).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> applicationService.saveAplication(ap));

        verify(userRepository).findById("20");
        verify(jobRepository, never()).findById("10");
        verify(applicationRepository, never()).findByJobIdAndSeekerId("10", "20");
        verify(applicationRepository, never()).save(any(ApplicationEntity.class));
    }

    @Test
    void test_saveApplication_JobNotFound() {
        Date sentDate = new Date();
        Application ap = new Application("1", "10", "20", sentDate);;

        when(userRepository.findById(anyString())).thenReturn(Optional.of(mock(UserEntity.class)));
        when(jobRepository.findById("10")).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> applicationService.saveAplication(ap));

        verify(userRepository).findById("20");
        verify(jobRepository).findById("10");
        verify(applicationRepository, never()).findByJobIdAndSeekerId("10", "20");
        verify(applicationRepository, never()).save(any(ApplicationEntity.class));
    }

    @Test
    void test_saveApplication_AlreadyApplied() {
        Date sentDate = new Date();
        Application ap = new Application("1", "10", "20", sentDate);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(mock(UserEntity.class)));
        when(jobRepository.findById(anyString())).thenReturn(Optional.of(mock(JobEntity.class)));
        when(applicationRepository.findByJobIdAndSeekerId("10", "20")).thenReturn(Arrays.asList(
                                                                                new ApplicationEntity("1", "10", "20", sentDate)
                                                                                ));
        
        assertThrows(RuntimeException.class, () -> applicationService.saveAplication(ap));

        verify(userRepository).findById("20");
        verify(jobRepository).findById("10");
        verify(applicationRepository).findByJobIdAndSeekerId("10", "20");
        verify(applicationRepository, never()).save(any(ApplicationEntity.class));
    }

    @Test
    void test_deleteApplication_NoError() {
        String id = "1";
        Date sentDate = new Date();
        ApplicationEntity ap = new ApplicationEntity("1", "10", "20", sentDate);
        when(applicationRepository.findById(id)).thenReturn(Optional.of(ap));

        applicationService.deleteApplication(id);
        
        verify(applicationRepository, times(1)).delete(ap);
    }

    @Test
    void test_deleteApplication_NotFound() {
        String id = "100";
        when(applicationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> applicationService.deleteApplication(id));
        verify(applicationRepository, never()).delete(any(ApplicationEntity.class));
    }
}
