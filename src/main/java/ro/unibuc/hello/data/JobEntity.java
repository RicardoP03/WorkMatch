package ro.unibuc.hello.data;

import java.lang.annotation.Inherited;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import lombok.*;

@Data
@Document(collection = "jobs")
public class JobEntity {
    @Id
    public String id;
    public String positionName;
    public String jobDescription;
    public int experienceInYears;
    public Date postDate;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Job ID: ").append(id).append("\n");
        sb.append("Position Name: ").append(positionName).append("\n");
        sb.append("Job Description: ").append(jobDescription).append("\n");
        sb.append("Experience In Years: ").append(experienceInYears).append("\n");
        sb.append("PostDate: ").append(postDate).append("\n");
        
        return sb.toString();
    }

}
