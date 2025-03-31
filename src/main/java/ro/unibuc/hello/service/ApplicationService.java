package ro.unibuc.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.unibuc.hello.data.ApplicationEntity;
import ro.unibuc.hello.data.ApplicationRepository;
import ro.unibuc.hello.dto.Application;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.data.JobRepository;
import ro.unibuc.hello.exception.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    public List<Application> findApplicationByJobId(String jobId) {
        return applicationRepository.findByJobId(jobId).stream()
                .map(ap -> new Application(ap.getId(), 
                                           ap.getJobId(), 
                                           ap.getSeekerId(), 
                                           ap.getDate()))
                .collect(Collectors.toList());
    }


    public List<Application> findApplicationBySeekerId(String sekId) {
        return applicationRepository.findBySeekerId(sekId).stream()
                .map(ap -> new Application(ap.getId(), 
                                           ap.getJobId(), 
                                           ap.getSeekerId(), 
                                           ap.getDate()))
                .collect(Collectors.toList());
    }

    public Application findApplicationById(String id) {
        Optional<ApplicationEntity> apEnt = applicationRepository.findById(id);

        ApplicationEntity applicationEntity = apEnt.orElse(null);

        if(applicationEntity == null) {
            return null;
        }

        return new Application (
            applicationEntity.getId(),
            applicationEntity.getJobId(),
            applicationEntity.getSeekerId(),
            applicationEntity.getDate()
        );
    }

    public Application saveAplication(Application ap) throws EntityNotFoundException, RuntimeException {
        userRepository.findById(ap.getSeekerId())
            .orElseThrow(() -> new EntityNotFoundException("Seeker with ID " + ap.getSeekerId() + " not found"));
        jobRepository.findById(ap.getJobId())
            .orElseThrow(() -> new EntityNotFoundException("Job with ID " + ap.getJobId() + " not found"));
            
        List<ApplicationEntity> listOfApplciations = applicationRepository.findByJobIdAndSeekerId(ap.getJobId(), ap.getSeekerId());
        if(listOfApplciations.size() != 0) {
            throw new RuntimeException("Application already exists for this job and seeker.");
        }

        ApplicationEntity apEnt = new ApplicationEntity(
            ap.getJobId(),
            ap.getSeekerId()
        );

        return new Application (
            apEnt.getId(),
            apEnt.getJobId(),
            apEnt.getSeekerId(),
            apEnt.getDate()
        );
    }

    public void deleteApplication(String id) throws EntityNotFoundException {
        ApplicationEntity applicationEntity = applicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Application with ID " + id + " not found"));
        applicationRepository.delete(applicationEntity);
    }
}
