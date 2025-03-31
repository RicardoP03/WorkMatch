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
}
