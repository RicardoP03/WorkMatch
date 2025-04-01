package ro.unibuc.hello.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.unibuc.hello.dto.Application;
import ro.unibuc.hello.service.ApplicationService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ApplicationControllerTest {
    @Mock
    private ApplicationService applicationService;

    @InjectMocks 
    private ApplicationController applicationController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(applicationController).build();
    }

    @Test
    void test_applicationsBySeekerId() throws Exception {
        Date sentDate = new Date();
        List<Application> applications = Arrays.asList(
                                    new Application("1", "10", "20", sentDate),
                                    new Application("2", "11", "20", sentDate)
                                );

        when(applicationService.findApplicationBySeekerId("20")).thenReturn(applications);

        mockMvc.perform(get("/applicationsBySeekerId/20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value("1"))
                    .andExpect(jsonPath("$[0].jobId").value("10"))
                    .andExpect(jsonPath("$[0].seekerId").value("20"))
                    .andExpect(jsonPath("$[0].date").value(sentDate))
                    .andExpect(jsonPath("$[1].id").value("2"))
                    .andExpect(jsonPath("$[1].jobId").value("11"))
                    .andExpect(jsonPath("$[1].seekerId").value("20"))
                    .andExpect(jsonPath("$[1].date").value(sentDate));
    }

    void test_applicationsByJobId() throws Exception {
        Date sentDate = new Date();
        List<Application> applications = Arrays.asList(
                                    new Application("1", "10", "20", sentDate),
                                    new Application("2", "10", "21", sentDate)
                                );

        when(applicationService.findApplicationByJobId("10")).thenReturn(applications);

        mockMvc.perform(get("/applicationsByJobId/10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value("1"))
                    .andExpect(jsonPath("$[0].jobId").value("10"))
                    .andExpect(jsonPath("$[0].seekerId").value("20"))
                    .andExpect(jsonPath("$[0].date").value(sentDate))
                    .andExpect(jsonPath("$[1].id").value("2"))
                    .andExpect(jsonPath("$[1].jobId").value("10"))
                    .andExpect(jsonPath("$[1].seekerId").value("21"))
                    .andExpect(jsonPath("$[1].date").value(sentDate));
    }

    void test_applicationById() throws Exception {
        Date sentDate = new Date();
        Application ap = new Application("1", "10", "20", sentDate);
        when(applicationService.findApplicationById("1")).thenReturn(ap);

        mockMvc.perform(get("/application/10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("1"))
                    .andExpect(jsonPath("$.jobId").value("10"))
                    .andExpect(jsonPath("$.seekerId").value("20"))
                    .andExpect(jsonPath("$.date").value(sentDate));
    }


    @Test
    void test_deleteApplication() throws Exception {
        String id = "1";
        doNothing().when(applicationService).deleteApplication(id);
    
        mockMvc.perform(delete("/application/1"))
               .andExpect(status().isOk());
    
        verify(applicationService, times(1)).deleteApplication(id);
    }
}