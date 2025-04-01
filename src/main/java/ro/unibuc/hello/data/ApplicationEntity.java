package ro.unibuc.hello.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "application")
public class ApplicationEntity {
    @Id
    private String id;
    private String jobId;
    private String seekerId;
    private Date date;

    public ApplicationEntity() {}

    public ApplicationEntity(String id, String jobId, String seekerId, Date date) {
        this.id = id;
        this.jobId = jobId;
        this.seekerId = seekerId;
        this.date = date;
    }

    public ApplicationEntity(String jobId, String seekerId, Date date) {
        this.jobId = jobId;
        this.seekerId = seekerId;
        this.date = date;
    }

    public String getId() { return id; }
    public String getJobId() { return jobId; }
    public String getSeekerId() { return seekerId; }
    public Date getDate() { return date; }

    public String toString() {
        return String.format("Application[id='%s', jobId='%s', seekerId='%s', date='%s']",
            id, jobId, seekerId, date); 
    }
}