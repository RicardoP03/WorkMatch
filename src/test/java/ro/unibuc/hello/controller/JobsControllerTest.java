package ro.unibuc.hello.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.unibuc.hello.dto.Job;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.service.JobService;
import ro.unibuc.hello.data.JobEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class JobControllerTest {

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

    private MockMvc mockMvc;
    private Job testJob;
    private Date testDate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(jobController).build();
        testDate = new Date();
        testJob = new Job("1", "Software Engineer", "Develops software", 3, testDate);
    }

    @Test
    void test_getJob() throws Exception {
        when(jobService.getJob("1")).thenReturn(testJob.toString());
    
        mockMvc.perform(get("/job/getJob?id=1"))
               .andExpect(status().isOk())
               .andExpect(content().string(testJob.toString()));
    }

    @Test
    void test_getJob_cascadesException() {
        String id = "999";
        when(jobService.getJob(id)).thenThrow(new EntityNotFoundException(id));

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> jobController.getJob(id),
                "Expected getJob() to throw EntityNotFoundException, but it didn't");

        assertTrue(exception.getMessage().contains(id));
    }

    @Test
    void test_createJob() throws Exception {
        JobEntity testJobEntity = new JobEntity("1", "Software Engineer", "Develops software", 3, testDate);
        when(jobService.create(any(JobEntity.class))).thenReturn(testJobEntity);
    
        mockMvc.perform(post("/job/create")
           .content("{" +
                   "\"id\":\"1\","
                   + "\"positionName\":\"Software Engineer\","
                   + "\"jobDescription\":\"Develops software\","
                   + "\"experienceInYears\":3,"
                   + "\"postDate\":" + testDate.getTime() + "}")
           .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value("1"))
           .andExpect(jsonPath("$.positionName").value("Software Engineer"));
    }

    @Test
    void test_updateJob() throws Exception {
        JobEntity updatedJobEntity = new JobEntity("1", "Senior Software Engineer", "Develops complex software", 5, testDate);
        
        when(jobService.updateJob(eq("1"), any(JobEntity.class))).thenReturn("Succes");
        int id = 1;

        mockMvc.perform(put("/job/update/{id}",id)
               .content("{" +
                       "\"positionName\":\"Senior Software Engineer\"," +
                       "\"jobDescription\":\"Develops complex software\"," +
                       "\"experienceInYears\":5}")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string("Succes"));
    }
    
    @Test
    void test_getAllJobs() throws Exception {
        List<Job> jobs = Arrays.asList(
            new Job("1", "Software Engineer", "Develops software", 3, testDate),
            new Job("2", "Data Scientist", "Analyzes data", 2, testDate)
        );
        when(jobService.getAllJobs()).thenReturn(jobs);

        mockMvc.perform(get("/job/jobs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("1"))
            .andExpect(jsonPath("$[0].positionName").value("Software Engineer"))
            .andExpect(jsonPath("$[1].id").value("2"))
            .andExpect(jsonPath("$[1].positionName").value("Data Scientist"));
    }

    @Test
    void test_deleteJob() throws Exception {
        String id = "1";
        doNothing().when(jobService).deleteJob(id);
    
        mockMvc.perform(delete("/job/delete/{id}", id))
               .andExpect(status().isOk());
    
        verify(jobService, times(1)).deleteJob(id);
    }
}
