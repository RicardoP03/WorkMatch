package ro.unibuc.hello.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ro.unibuc.hello.data.JobEntity;
import ro.unibuc.hello.data.JobRepository;
import ro.unibuc.hello.exception.EntityNotFoundException;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ro.unibuc.hello.dto.Job;

@ExtendWith(SpringExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    private JobEntity testJobEntity;
    private Date testDate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testDate = new Date();
        testJobEntity = new JobEntity("1", "Software Engineer", "Develops software", 3, testDate);
    }

    @Test
    void testCreateJob() {
        // Arrange
        when(jobRepository.save(any(JobEntity.class))).thenReturn(testJobEntity);

        // Act
        JobEntity createdJob = jobService.create(testJobEntity);

        // Assert
        assertNotNull(createdJob);
        assertEquals("1", createdJob.getId());
        assertEquals("Software Engineer", createdJob.getPositionName());
        assertEquals("Develops software", createdJob.getJobDescription());
        assertEquals(3, createdJob.getExperienceInYears());
        assertEquals(testDate, createdJob.getPostDate());
    }


    @Test
    void testGetJob_ExistingEntity() throws EntityNotFoundException {
        // Arrange
        when(jobRepository.findById("1")).thenReturn(Optional.of(testJobEntity));

        // Act
        String jobString = jobService.getJob("1");

        // Assert
        assertNotNull(jobString);
        assertTrue(jobString.contains("Software Engineer"));
        assertTrue(jobString.contains("Develops software"));
    }

    @Test
    void testGetJob_NonExistingEntity() {
        // Arrange
        when(jobRepository.findById("999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> jobService.getJob("999"));
    }

    @Test
    void testFindByPositionName_ExistingPosition() {
        // Arrange
        when(jobRepository.findByPositionName("Software Engineer"))
            .thenReturn(Collections.singletonList(testJobEntity)); // Returning List<JobEntity>

        // Act
        List<Job> foundJobs = jobService.findByPositionName("Software Engineer");

        // Assert
        assertNotNull(foundJobs);
        assertFalse(foundJobs.isEmpty());
        assertEquals("Software Engineer", foundJobs.get(0).getPoisitonName());
    }

    @Test
    void testFindByPositionName_NonExistingPosition() {
        // Arrange
        when(jobRepository.findByPositionName("Non-existent Position"))
            .thenReturn(Collections.emptyList());

        // Act
        List<Job> foundJobs = jobService.findByPositionName("Non-existent Position");

        // Assert
        assertNotNull(foundJobs);
        assertTrue(foundJobs.isEmpty());
    }
}
