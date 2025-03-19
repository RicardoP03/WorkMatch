package ro.unibuc.hello.dto;
import java.util.Date;

public class Application {
    private String id;
    private String jobId;
    private String seekerId;
    private Date date;

    public Application(String id, String jobId, String seekerId, Date date) {
        this.id = id;
        this.jobId = jobId;
        this.seekerId = seekerId;
        this.date = date;
    }

    public String getId() { return id; }
    public String getJobId() { return jobId; }
    public String getSeekerId() { return seekerId; }
    public Date getDate() { return date; }
}