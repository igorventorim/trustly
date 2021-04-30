package net.trustly.github;

import net.trustly.github.controller.CustomRestExceptionHandlerController;
import net.trustly.github.controller.JobController;
import net.trustly.github.service.JobManagerService;
import net.trustly.github.service.RequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GithubApplicationTests {

	@Autowired
	private JobController jobController;

	@Autowired
	private CustomRestExceptionHandlerController customRestExceptionHandlerController;

	@Autowired
	private JobManagerService jobManagerService;

	@Autowired
	private RequestService requestService;

	@Test
	void contextLoads() {
		assertThat(jobController).isNotNull();
		assertThat(customRestExceptionHandlerController).isNotNull();
		assertThat(jobManagerService).isNotNull();
		assertThat(requestService).isNotNull();
	}


}
