package edu.austincollege.acvote.integration.ballot;

import static edu.austincollege.acvote.unit.StringUtil.dquote;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

/**
 * This class provides junit tests for testing the controller, with service and all dependences (full stack integration) for the
 * ballot subsystem.  The tests here only exercise tests related to managing ballot options.
 * <p>
 * In these tests the MVC framework is mocked allowing us to exercise the controller and all its dependancies. It uses our real 
 * controller with a real service object and real production DAO objects and other helper objects.
 * </p>
 * <p>
 * So we expect to see content based on our schema.sql data store.  Before every method, the context is "dirty" and needs to be
 * reloaded fresh (ie, the schema.sql file is reexecuted and all the "beans" are autowired fresh).
 * </p>
 *  
 * @author mahiggs
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest							// needed to load the application context (ie, the entire stack of important objects) 
@AutoConfigureMockMvc					
@WithMockUser(username="admin",roles={"ADMIN"})
@ContextConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BallotTests {

	@Autowired
	private MockMvc mvc;



	/* --------------------------------------------------------------
	 *  When viewing ballots, there should be more than a couple in the 
	 *  list.
	 *  
	 *  Currently 3 test ballots.
	 */
	@Test
	void testViewBallots() throws Exception {

		mvc.perform(get("/ballots")
				.contentType(MediaType.TEXT_HTML))
		
		// should not be an err
		.andExpect(status().isOk())

		.andDo(MockMvcResultHandlers.print())
		
		// should see html contents in the response with a h1 with "Ballot List" text inside
		.andExpect(xpath("//h1").string("Ballot List"))
		
		// should see a table with at least 3 test ballots
		.andExpect(xpath(dquote("//table")).exists())
		.andExpect(xpath(dquote("//tr[@bid='1']")).exists())
		.andExpect(xpath(dquote("//tr[@bid='2']")).exists())
		.andExpect(xpath(dquote("//tr[@bid='3']")).exists())
		
		// and the first ballot row should display key elements 
		.andExpect(xpath( dquote("//tr[@bid='1']/td[2]/text()")).string(containsString("Coolest CS Professor")))
		
		// and a date for when polls closed
		.andExpect(xpath( dquote("//tr[@bid='1']/td[3]/text()")).string(containsString("2023-07-08T16:00")));		

	}



	/* --------------------------------------------------------------
	 * 
	 */
	@Test
	void testViewBallotDetails() throws Exception {

		mvc.perform(get("/ballot?bid=1").contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())

				.andExpect(xpath(dquote("//div[@bid='1']")).exists())
				.andExpect(xpath(
						"//div[@id='idHolder']//div[@id='title']//p[@id='titleText' and normalize-space()='Coolest CS Professor']")
						.exists())

				.andExpect(xpath(
						"//div[@id='idHolder']//div[@id='description']//div[@id='descriptionText' and normalize-space()='This is a description for ballot 1!']")
						.exists());


	}
	
	@Test
	void testViewBallotResults() throws Exception {
		
		mvc.perform(get("/ballot/1/results").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())

		.andExpect(xpath(dquote("//div[@id='header']")).exists())
		.andExpect(xpath(dquote("//div[@id='title']/p/text()")).string(containsString("Coolest CS Professor")));
	}
	
	@Test
	void testDeleteOptionFromBallot() throws Exception {
		
		mvc.perform(delete("/ballot/ajax/deleteOption?bid=1&oid=5854836").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk());
	}
	
	@Test
	void testDeleteOptionFromBallotFailure() throws Exception {
		
		mvc.perform(delete("/ballot/ajax/deleteOption?bid=1&oid=0").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	void testEnableOption() throws Exception {
		
		mvc.perform(put("/ballot/ajax/enableOption?bid=1&oid=5854836&state=false").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk());
	}
	
	@Test
	void testEnableOptionFailure() throws Exception {
		
		mvc.perform(put("/ballot/ajax/enableOption?bid=1&oid=0&state=true").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	void testClearOptions() throws Exception {
		
		mvc.perform(delete("/ballot/ajax/deleteAllOptions?bid=1").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk());
	}
	
	@Test
	void testClearOptionsFailure() throws Exception {
		
		mvc.perform(delete("/ballot/ajax/deleteAllOptions?bid=5").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isBadRequest());
	}

	/* --------------------------------------------------------------
	 * 
	 */
	@Test
	void testDeleteBallotSuccess() throws Exception {

		mvc.perform(delete("/ballot/ajax/deleteBallot?bid=1").with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("ballot 1 has been deleted")));

	}
	
	/* --------------------------------------------------------------
	 * 
	 */
	@Test
	void testDeleteBallotFailure() throws Exception {

		mvc.perform(delete("/ballot/ajax/deleteBallot?bid=-1").with(csrf())
				.contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(content().string(containsString("ballot -1 has NOT been deleted")));

	}

	/* --------------------------------------------------------------
	 * 
	 */
	@Test
	void testCreateBallot() throws Exception {

		mvc.perform(post("/ballot/ajax/createBallot")
				.with(csrf())
				.contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(content().string(containsString("5")));

	}
	
	@Test
	void testCreateFromTemplate() throws Exception {
		
		mvc.perform(post("/ballot/ajax/createBallotFromTemplate?tid=1").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("5")));
	}
	
	@Test
	void testAddCustomOption() throws Exception {
		
		mvc.perform(post("/ballot/ajax/addOption?bid=1&oid=0&title=zero").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk());
	}
	
	@Test
	void testAddCustomOptionFailure() throws Exception {
		
		mvc.perform(post("/ballot/ajax/addOption?bid=1&oid=5854836&title=zero").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	void testAddFacultyOptions() throws Exception {
		
		mvc.perform(post("/ballot/ajax/addCandidates?bid=1&newOptionIDs[]=1299808").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("faculty options")));
	}
	
	@Test
	void testAddDuplicateFacultyOptions() throws Exception {
		
		mvc.perform(post("/ballot/ajax/addCandidates?bid=1&newOptionIDs[]=5854836").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("faculty options")));
	}
	
	@Test
	void testAddFacultyOptionsFailure() throws Exception {
		
		mvc.perform(post("/ballot/ajax/addCandidates?bid=0&newOptionIds[]=5854836").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	void testTokenToFaculty() throws Exception {
		
		mvc.perform(get("/ballot/ajax/tokenToFaculty?bid=1")
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk());
	}
	
	@Test
	void testSetBallotTitle() throws Exception {
		
		mvc.perform(put("/ballot/ajax/setBallotTitle?bid=1&ballotTitle=title").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("title updated")));
	}
	
	@Test
	void testSetBallotDescription() throws Exception {
		
		mvc.perform(put("/ballot/ajax/setBallotDescription?bid=1&ballotDescription=desc").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("description updated")));
	}
	
	@Test
	void testSetBallotInstructions() throws Exception {
		
		mvc.perform(put("/ballot/ajax/setBallotInstructions?bid=1&ballotInstructions=instr").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("instructions updated")));
	}
	
	@Test
	void testSetBallotTypeOfVote() throws Exception {
		
		mvc.perform(put("/ballot/ajax/setBallotTypeOfVote?bid=1&ballotTypeOfVote=car").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("vote type updated")));
	}
	
	@Test
	void testSetBallotBasis() throws Exception {
		
		mvc.perform(put("/ballot/ajax/setBasis?bid=1&basis=false").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("basis updated")));
	}
	
	@Test
	void testSetBallotBasisFailure() throws Exception {
		
		mvc.perform(put("/ballot/ajax/setBasis?bid=5&basis=false").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("unable to change the basis for ballot 5")));
	}
	
	@Test
	void testSetBallotOutcomes() throws Exception {
		
		mvc.perform(put("/ballot/ajax/setBallotOutcomes?bid=1&ballotOutcomes=5").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("number of outcomes updated to 5")));
	}
	
	@Test
	void testSetBallotStartTime() throws Exception {
		
		LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
		
		mvc.perform(put("/ballot/ajax/setBallotStartTime?bid=1&ballotStartTime=" + now.toString()).with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("Vote start time updated to " + now.toString())));
	}
	
	@Test
	void testSetBallotEndTime() throws Exception {
		
		LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
		
		mvc.perform(put("/ballot/ajax/setBallotEndTime?bid=1&ballotEndTime=" + now.toString()).with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("Vote end time updated to " + now.toString())));
	}
	
	@Test
	void testSetBallotVotingGroup() throws Exception {
		
		mvc.perform(put("/ballot/ajax/setVotingGroup?bid=1&group=AC").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("Voting group updated to AC")));
	}
	
	@Test
	void testToggleOption() throws Exception {
		
		mvc.perform(put("/ballot/ajax/setOptionEnabled?bid=1&oid=5854836&enabled=false").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("toggled option #5854836 to false")));
	}
	
	@Test
	void testToggleOptionFailure() throws Exception {
		
		mvc.perform(put("/ballot/ajax/setOptionEnabled?bid=2&oid=5854836&enabled=false").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("toggle failed")));
	}

}
