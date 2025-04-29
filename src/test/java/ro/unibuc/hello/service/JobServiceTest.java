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

    @Test
    void testGetAllJobs() {
        // Arrange
        when(jobRepository.findAll()).thenReturn(Collections.singletonList(testJobEntity));
    
        // Act
        List<Job> jobs = jobService.getAllJobs();
    
        // Assert
        assertNotNull(jobs);
        assertFalse(jobs.isEmpty());
        assertEquals(1, jobs.size());
        assertEquals("Software Engineer", jobs.get(0).getPoisitonName());
    }
    
    @Test
    void testUpdateJob_Success() throws EntityNotFoundException {
        // Arrange
        JobEntity updatedJobEntity = new JobEntity("1", "Senior Software Engineer", "Leads software development", 5, new Date());
        when(jobRepository.findById("1")).thenReturn(Optional.of(testJobEntity));
        when(jobRepository.save(any(JobEntity.class))).thenReturn(updatedJobEntity);
    
        // Act
        String result = jobService.updateJob("1", updatedJobEntity);
    
        // Assert
        assertEquals("Succes", result);
        verify(jobRepository).save(any(JobEntity.class));
    }
    
    @Test
    void testUpdateJob_EntityNotFound() {
        // Arrange
        JobEntity updatedJobEntity = new JobEntity("999", "Senior Software Engineer", "Leads software development", 5, new Date());
        when(jobRepository.findById("999")).thenReturn(Optional.empty());
    
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> jobService.updateJob("999", updatedJobEntity));
    }
    
    @Test
    void testDeleteJob_Success() throws EntityNotFoundException {
        // Arrange
        when(jobRepository.existsById("1")).thenReturn(true);
        doNothing().when(jobRepository).deleteById("1");
    
        // Act
        jobService.deleteJob("1");
    
        // Assert
        verify(jobRepository).deleteById("1");
    }
    
    @Test
    void testDeleteJob_EntityNotFound() {
        // Arrange
        when(jobRepository.existsById("999")).thenReturn(false);
    
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> jobService.deleteJob("999"));
    }

    @Test
    void testExistsById_NotFound() {
        // Arrange
        when(jobRepository.existsById("999")).thenReturn(false);

        // Act
        boolean exists = jobService.existsById("999");

        // Assert
        assertFalse(exists);
        verify(jobRepository).existsById("999");
    }

    @Test
    void testEquals() {
        // Arrange
        JobEntity job1 = new JobEntity("1", "Software Engineer", "Develops software", 3, new Date());
        JobEntity job2 = new JobEntity("1", "Software Engineer", "Develops software", 3, new Date());

        // Act & Assert
        assertEquals(job1, job2);
    }

    @Test
    void testHashCode() {
        // Arrange
        JobEntity job1 = new JobEntity("1", "Software Engineer", "Develops software", 3, new Date());
        JobEntity job2 = new JobEntity("1", "Software Engineer", "Develops software", 3, new Date());

        // Act & Assert
        assertEquals(job1.hashCode(), job2.hashCode());
    }

}
