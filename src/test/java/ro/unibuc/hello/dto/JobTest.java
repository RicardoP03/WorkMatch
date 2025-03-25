package ro.unibuc.hello.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Date;

class JobTest {

    Date testDate = new Date();
    Job myJob = new Job("1", "Software Engineer", "Develops software", 3, testDate);

    @Test
    void test_id() {
        Assertions.assertEquals("1", myJob.getId());
    }
    
    @Test
    void test_positionName() {
        Assertions.assertEquals("Software Engineer", myJob.getPoisitonName());
    }
    
    @Test
    void test_jobDescription() {
        Assertions.assertEquals("Develops software", myJob.getDescription());
    }
    
    @Test
    void test_experienceInYears() {
        Assertions.assertEquals(3, myJob.getExperince());
    }
    
    @Test
    void test_postDate() {
        Assertions.assertEquals(testDate, myJob.getPostDate());
    }
}