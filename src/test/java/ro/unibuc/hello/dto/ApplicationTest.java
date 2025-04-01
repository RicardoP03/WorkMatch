package ro.unibuc.hello.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Date;

class ApplicationTest {
    Date date = new Date();
    Application ap = new Application("1", "10", "20", date);

    @Test
    void test_id() {
        Assertions.assertEquals("1", ap.getId());
    }

    @Test
    void test_content() {
        Assertions.assertEquals("10", ap.getJobId());
    }

    @Test
    void test_senderId() {
        Assertions.assertEquals("20", ap.getSeekerId());
    }

    @Test
    void test_date() {
        Assertions.assertEquals(date, ap.getDate());
    }

    @Test
    void test_setId() {
        Application testAp = new Application("1", "10", "20", date);
        testAp.setId("2");
        Assertions.assertEquals("2", testAp.getId());
    }

    @Test
    void test_setJobId() {
        Application testAp = new Application("1", "10", "20", date);
        testAp.setJobId("15");
        Assertions.assertEquals("15", testAp.getJobId());
    }

    @Test
    void test_setSeekerId() {
        Application testAp = new Application("1", "10", "20", date);
        testAp.setSeekerId("25");
        Assertions.assertEquals("25", testAp.getSeekerId());
    }

    @Test
    void test_setDate() {
        Date newDate = new Date();
        Application testAp = new Application("1", "10", "20", date);
        testAp.setDate(newDate);
        Assertions.assertEquals(newDate, testAp.getDate());
    }
}
