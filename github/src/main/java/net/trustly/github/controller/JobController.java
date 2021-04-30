package net.trustly.github.controller;

import net.trustly.github.domain.ApiSuccess;
import net.trustly.github.domain.Job;
import net.trustly.github.exception.InvalidRepositoryException;
import net.trustly.github.service.JobManagerService;
import net.trustly.github.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class JobController {

    private final JobManagerService jobManagerService;

    private final RequestService requestService;

    private Logger logger = LoggerFactory.getLogger(JobController.class);

    public JobController(JobManagerService jobProducer,
                         RequestService requestService) {
        this.jobManagerService = jobProducer;
        this.requestService = requestService;
    }

    @PostMapping(value = "/info-repository/job")
    public ApiSuccess createJobInfoRepository(@RequestParam String url, HttpServletRequest request) {

        logger.info("received request in /info-repository/job");

        if(!requestService.isValidGithubUrl(url)) {
            throw new InvalidRepositoryException("Invalid input "+url);
        }

        ApiSuccess apiSuccess = new ApiSuccess(HttpStatus.OK.value(),
                request.getRequestURI(),
                jobManagerService.send(url));

        return apiSuccess;
    }

    @GetMapping(value = "/info-repository/job/{id}")
    public ApiSuccess getJobInfoRepository(@PathVariable Long id, HttpServletRequest request) {

        logger.info("request received: /info-repository/job/"+String.valueOf(id));

        Job jobResult = jobManagerService.getResultJobById(id);
        ApiSuccess apiSuccess = new ApiSuccess(HttpStatus.OK.value(),
                request.getRequestURI(),
                jobResult);

        return apiSuccess;
    }
}
