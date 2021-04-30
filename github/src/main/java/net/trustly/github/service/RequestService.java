package net.trustly.github.service;

import org.springframework.stereotype.Service;


@Service
public class RequestService {

    public boolean isValidGithubUrl(String url) {

        if(!(url.contains("http://") || url.contains("https://"))) {
            return false;
        }

        if(!url.contains("github.com/")) {
            return false;
        }

        return true;
    }

}
