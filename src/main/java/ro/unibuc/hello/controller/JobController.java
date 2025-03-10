package ro.unibuc.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import ro.unibuc.hello.data.JobEntity;
import ro.unibuc.hello.dto.Job;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.service.JobService;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/job")
public class JobController {
    @Autowired
    private JobService jobService;


    @GetMapping("/getJob")
    @ResponseBody
    public String getJob(
        @RequestParam(name = "id", required=false, defaultValue = "Test") String id
    ){
        return jobService.getJob(id);
    }

    @PostMapping
    @ResponseBody
    public JobEntity create(@RequestBody JobEntity newJob){
        return jobService.create(newJob);
    }
}
