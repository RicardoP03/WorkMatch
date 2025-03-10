package ro.unibuc.hello.service;

import ro.unibuc.hello.service.UserService;
import javax.servlet.*;
import javax.swing.text.html.parser.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.unibuc.hello.data.JobRepository;
import ro.unibuc.hello.data.JobEntity;
import ro.unibuc.hello.dto.Job;
import ro.unibuc.hello.exception.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JobService {
    @Autowired
    private JobRepository jobRepository;


    public JobEntity create(JobEntity job){
        return jobRepository.save(job);
    }

    public String getJob(String id) throws EntityNotFoundException{
        JobEntity jobEntity = jobRepository.findById(id).orElse(new JobEntity());
        if(jobEntity == null){
            throw new EntityNotFoundException(id);
        }
        return jobEntity.toString();
    }

}
