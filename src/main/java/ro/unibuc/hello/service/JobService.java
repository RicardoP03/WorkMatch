package ro.unibuc.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.unibuc.hello.data.JobRepository;
import ro.unibuc.hello.data.JobEntity;
import ro.unibuc.hello.dto.Job;
import ro.unibuc.hello.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public JobEntity create(JobEntity job) {
        return jobRepository.save(job);
    }

    public String getJob(String id) throws EntityNotFoundException {
        JobEntity jobEntity = jobRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        return jobEntity.toString();
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public String updateJob(String id, JobEntity newJobData) throws EntityNotFoundException {
        JobEntity jobEntity = jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        jobEntity.setPositionName(newJobData.getPositionName()); 
        jobEntity.setJobDescription(newJobData.getJobDescription()); 
        jobEntity.setExperienceInYears(newJobData.getExperienceInYears()); 
        jobEntity.setPostDate(newJobData.getPostDate()); 
        jobRepository.save(jobEntity);
        return "Succes";
    }

    public void deleteJob(String id) throws EntityNotFoundException {
        if (!jobRepository.existsById(id)) {
            throw new EntityNotFoundException(id);
        }
        jobRepository.deleteById(id);
    }

    public List<Job> findByPositionName(String positionName) {
        return jobRepository.findByPositionName(positionName).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public boolean existsById(String id) {
        return jobRepository.existsById(id);
    }

    private Job toDto(JobEntity jobEntity) {
        return new Job(
                jobEntity.getId(),
                jobEntity.getPositionName(), 
                jobEntity.getJobDescription(), 
                jobEntity.getExperienceInYears(), 
                jobEntity.getPostDate()
        );
    }
}
