package edu.austincollege.acvote.integration.ballot;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import edu.austincollege.acvote.ballot.BallotController;
import edu.austincollege.acvote.ballot.BallotService;
import edu.austincollege.acvote.faculty.FacultyService;
import edu.austincollege.acvote.lut.LutDao;

/**
 * This class provides junit tests for testing the controller, with service and all dependences (full stack integration) for the
 * ballot subsystem.  The tests here only exercise tests related to managing ballot options.
 * 
 * @author mahiggs
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest							// needed to load the application context (ie, the entire stack of important objects) 
@AutoConfigureMockMvc					
@WithMockUser(value = "admin")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BallotOptionTests {

	@Autowired
	private MockMvc mvc;




}
