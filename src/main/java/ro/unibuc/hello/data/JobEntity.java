package ro.unibuc.hello.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "jobs")
public class JobEntity {

    @Id
    private String id;
    private String positionName;
    private String jobDescription;
    private int experienceInYears;
    private Date postDate;

}
