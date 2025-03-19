package ro.unibuc.hello.dto;
import java.util.Date;

public class Job {
    public String id;
    public String positionName;
    public String jobDescription;
    public int experienceInYears;
    public Date postDate;

    public Job(String id, String positionName, String jobDescription, int experience, Date postdate) {
        this.id = id;
        this.positionName = positionName;
        this.jobDescription = jobDescription;
        this.experienceInYears = experience;
        this.postDate = postdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoisitonName() {
        return this.positionName;
    }

    public void setPoisitonName(String name) {
        this.positionName = name;
    }

    public int getExperince() {
        return this.experienceInYears;
    }

    public void setExperience(int years) {
        this.experienceInYears = years;
    }

    public String getDescription() {
        return this.jobDescription;
    }

    public void setDescription(String description) {
        this.jobDescription = description;
    }

    
    public Date getPostDate() {
        return this.postDate;
    }

    public void setPostDate(Date PostDate) {
        this.postDate = PostDate;
    }
}
