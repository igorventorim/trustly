package net.trustly.github;

import net.trustly.github.service.RequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class RequestServiceTests {

    @Autowired
    private RequestService requestService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void whenValidUrlScrapy() {

        final String URL = "https://github.com/igorventorim/wiki";
        boolean result = requestService.isValidGithubUrl(URL);
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void whenInvalidUrlScrapy() {

        final String URL = "https://google.com";

        boolean result = requestService.isValidGithubUrl(URL);

        assertThat(result).isEqualTo(false);
    }


    @Test
    public void whenInvalidRepositoryToCreateJob() throws Exception {

        final String ENDPOINT = "/info-repository/job";
        final String PARAMETER = "url";
        final String VALUE = "https://google.com";

        mvc.perform(MockMvcRequestBuilders
                .post(ENDPOINT)
                .param(PARAMETER,VALUE)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(containsString("The url parameter must be a valid public repository")));
    }


    @Test
    public void whenCreateJobRequestSuccessful() throws Exception {

        final String ENDPOINT = "/info-repository/job";
        final String PARAMETER = "url";
        final String VALUE = "https://github.com/igorventorim/wiki";

        mvc.perform(MockMvcRequestBuilders
                .post(ENDPOINT)
                .param(PARAMETER,VALUE)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(containsString("jobId")));
    }

    @Test
    public void whenGetInfoRepositoryJobSuccessful() throws Exception {

        final String ENDPOINT = "/info-repository/job/{id}";
        final int PATH_PARAMETER = 1;

        this.whenCreateJobRequestSuccessful();

        mvc.perform(MockMvcRequestBuilders
                .get(ENDPOINT,PATH_PARAMETER)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(containsString("jobId")));
    }

    @Test
    public void whenGetInfoRepositoryJobFail() throws Exception {

        final String ENDPOINT = "/info-repository/job/{id}";
        final int PATH_PARAMETER = -1;

        mvc.perform(MockMvcRequestBuilders
                .get(ENDPOINT,PATH_PARAMETER)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }


}
