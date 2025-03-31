package ro.unibuc.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.hello.dto.Application;
import ro.unibuc.hello.service.ApplicationService;
import java.util.List;

@Controller
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/applicationsBySeekerId/{seekerId}")
    @ResponseBody
    public List<Application> getApplicationsBySeekerId(
        @PathVariable String seekerId) {

        return applicationService.findApplicationBySeekerId(seekerId);
    }

    @GetMapping("/applicationsByJobId/{jobId}")
    @ResponseBody
    public List<Application> getApplicationsByJobId(
        @PathVariable String jobId) {

        return applicationService.findApplicationByJobId(jobId);
    }


    @GetMapping("/application/{appId}")
    @ResponseBody
    public Application getApplication(
        @PathVariable String appId) {

        return applicationService.findApplicationById(appId);
    }

    @PostMapping("/application")
    @ResponseBody
    public Application createApplication(@RequestBody Application app) {
        try {
            Application savedApp = applicationService.saveAplication(app);
            return savedApp;
        } 
        catch (Exception e) {
            System.err.println("Error saving application: " + e.getMessage());
            return null;
        }
    }

    @DeleteMapping("/application/{id}")
    @ResponseBody
    public void deleteApplication(@PathVariable String id) {
        try {
            applicationService.deleteApplication(id);
        }
        catch (Exception e) {
            System.err.println("Error deleting message: " + e.getMessage());
        }
    }
}
