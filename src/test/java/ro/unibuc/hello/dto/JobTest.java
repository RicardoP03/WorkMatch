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

    @Test
    void testUserSetters() {        
        Job testJob = new Job("1", "Software Engineer", "Develops software", 3, new Date());

        
        testDate = new Date();

        testJob.setId("2");
        testJob.setPoisitonName("TEST");
        testJob.setExperience(5);
        testJob.setDescription("TEST DESC");
        testJob.setPostDate(testDate);

        Assertions.assertEquals("2", testJob.getId());
        Assertions.assertEquals("TEST", testJob.getPoisitonName());
        Assertions.assertEquals(5, testJob.getExperince());
        Assertions.assertEquals("TEST DESC", testJob.getDescription());
        Assertions.assertEquals(testDate, testJob.getPostDate());
    }
}