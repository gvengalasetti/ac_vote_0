package edu.austincollege.acvote.unit.template;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.BallotService;
import edu.austincollege.acvote.ballot.option.VoteOption;
import edu.austincollege.acvote.faculty.FacultyService;
import edu.austincollege.acvote.lut.LutDao;
import edu.austincollege.acvote.template.BallotTemplate;
import edu.austincollege.acvote.template.TemplateController;
import edu.austincollege.acvote.template.TemplateService;

@RunWith(SpringRunner.class)
@WebMvcTest(TemplateController.class)
@WithMockUser(value = "admin")
public class TemplateControllerTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private BallotService mockBalSrv;

	@MockBean
	private LutDao mockLutDao;

	@MockBean
	private TemplateService mockTemplSrv;

	@MockBean
	private FacultyService mockFacSrv;

	private List<Ballot> ballots = new ArrayList<>();
	private List<BallotTemplate> templates = new ArrayList<>();

	@BeforeEach
	void setUp() throws Exception {

		MockitoAnnotations.openMocks(this); // make the annotations work

		/*
		 * Generates Example Ballots
		 */
		ArrayList<VoteOption> candidates = new ArrayList<>();
		candidates.add(new VoteOption("0629102", "Higgs", true));
		candidates.add(new VoteOption("0629104", "Block", true));

		ArrayList<String> groups = new ArrayList<>();
		groups.add("EveryBody");
		ballots.add(new Ballot(1, "Coolest CS Professor", "Please drag and drop these candidates around!",
				"This is a description for ballot 1!", true, candidates, "IRV", 2, LocalDateTime.now().plusDays(10),
				LocalDateTime.now().plusDays(20), "CW", 124));

		candidates.add(new VoteOption("80085", "Rosenberg", true));

		ballots.add(new Ballot(2, "Which ones are CS Professors?", "Please drag and drop these candidates around!",
				"This is a description for ballot 2!", true, candidates, "IRV", 2, LocalDateTime.now().plusDays(50),
				LocalDateTime.now().plusDays(60), "CW", 7));

		candidates = new ArrayList<>();

		candidates.add(new VoteOption("629102", "Higgs", true));
		candidates.add(new VoteOption("629105", "Not Higgs", true));
		candidates.add(new VoteOption("678392", "Nguyen", true));

		ballots.add(new Ballot(3, "This is a different example", "Please drag and drop these candidates around!",
				"This is a description for ballot 3!", true, candidates, "IRV", 2, LocalDateTime.now().plusDays(8),
				LocalDateTime.now().plusMonths(1), "CW", 7));

		for (int i = 4; i < 15; i++) {
			ballots.add(new Ballot(i, "This is a random example", "Please drag and drop these candidates around!",
					"This is a description for a ballot!", true, candidates, "IRV", 2, LocalDateTime.now().plusDays(15),
					LocalDateTime.now().plusMonths(2), "CW", (int) Math.pow(Math.PI, i)));
		}

		templates.add(new BallotTemplate(12345, "Sample Ballot Template", "Best CS Prof", "sumbit your ballot, or else",
				"who's the goat?", "IRV", 2, true));
		templates.add(new BallotTemplate(23456, "Another Ballot Template", "Who is a CS Prof", "choose",
				"do you know who is actually a prof", "IRV", 2, true));
		templates.add(new BallotTemplate(34567, "One more Ballot Template", "Insert Title Here",
				"idk just vote on something", "italian beef", "IRV", 1, false));
	}

	/**
	 * Private helper method to make it easier to write tests below. We can use
	 * single quotes and transform them to embedded double quotes.
	 * 
	 * @author mhiggs
	 * @param anyStr
	 * @return
	 */
	private String dquote(String anyStr) {
		if (anyStr == null)
			return null;
		return anyStr.replaceAll("[']", "\"");
	}

	@Test
	public void test_replace_single_quotes_function() {
		String str = "tr/td[@id='xyz']";
		// System.err.println(dquote(str));
		assertEquals("tr/td[@id=\"xyz\"]", dquote(str));
	}

	@Test
	void testCreateTemplateFromBallot() throws Exception {

		// Mock dependencies
		Mockito.when(mockBalSrv.getBallotById(1)).thenReturn(ballots.get(1));
		Mockito.when(mockTemplSrv.createTemplate("Template of ballot: " + ballots.get(1).getId(),
				ballots.get(1).getTitle(), ballots.get(1).getInstructions(), ballots.get(1).getDescription(),
				ballots.get(1).getTypeOfVote(), ballots.get(1).getOutcomes(), ballots.get(1).isFacultyBased()))
				.thenReturn(new BallotTemplate(4, "Template of ballot: " + ballots.get(1).getId(),
						ballots.get(1).getTitle(), ballots.get(1).getInstructions(), ballots.get(1).getDescription(),
						ballots.get(1).getTypeOfVote(), ballots.get(1).getOutcomes(), ballots.get(1).isFacultyBased()));

		mvc.perform(post("/template/ajax/createTemplateFromBallot?bid=1").contentType(MediaType.TEXT_HTML).with(csrf()))
				.andExpect(status().isOk())

				.andExpect(content().string(containsString("Template created with id: 4")));

		Mockito.verify(mockBalSrv).getBallotById(1);
	}
	
	@Test
	void testFailCreateTemplateFromBallot() throws Exception {

		// Mock dependencies
		Mockito.when(mockBalSrv.getBallotById(1)).thenReturn(ballots.get(1));
		Mockito.when(mockTemplSrv.createTemplate("Template of ballot: " + ballots.get(1).getId(),
				ballots.get(1).getTitle(), ballots.get(1).getInstructions(), ballots.get(1).getDescription(),
				ballots.get(1).getTypeOfVote(), ballots.get(1).getOutcomes(), ballots.get(1).isFacultyBased()))
				.thenReturn(null);

		mvc.perform(post("/template/ajax/createTemplateFromBallot?bid=1").contentType(MediaType.TEXT_HTML).with(csrf()))
				.andExpect(status().isOk())

				.andExpect(content().string(containsString("Template could not be created")));

		Mockito.verify(mockBalSrv).getBallotById(1);
		Mockito.verify(mockTemplSrv).createTemplate("Template of ballot: " + ballots.get(1).getId(),
				ballots.get(1).getTitle(), ballots.get(1).getInstructions(), ballots.get(1).getDescription(),
				ballots.get(1).getTypeOfVote(), ballots.get(1).getOutcomes(), ballots.get(1).isFacultyBased());
	}

	@Test
	void testDeleteTemplate() throws Exception {

		// Mock dependencies
		Mockito.when(mockTemplSrv.getTemplate(0)).thenReturn(templates.get(0));
		Mockito.when(mockTemplSrv.deleteTemplate(0)).thenReturn(true);

		mvc.perform(delete("/template/ajax/deleteTemplate?tid=0").contentType(MediaType.TEXT_HTML).with(csrf()))
				.andExpect(status().isOk())

				.andExpect(content().string(containsString("Deleted template with id: 12345")));

		Mockito.verify(mockTemplSrv).deleteTemplate(0);
	}
	
	@Test
	void testFailDeleteTemplate() throws Exception {

		// Mock dependencies
		Mockito.when(mockTemplSrv.getTemplate(0)).thenReturn(templates.get(0));
		Mockito.when(mockTemplSrv.deleteTemplate(0)).thenReturn(false);

		mvc.perform(delete("/template/ajax/deleteTemplate?tid=0").contentType(MediaType.TEXT_HTML).with(csrf()))
				.andExpect(status().isOk())

				.andExpect(content().string(containsString("Could not delete template with id: 12345")));

		Mockito.verify(mockTemplSrv).deleteTemplate(0);
	}

	@Test
	// fix this
	void testCreateTemplate() throws Exception {

		Mockito.when(mockTemplSrv.createTemplate("Enter Template Title", "Enter Ballot Title", "Enter Instructions",
				"Enter Description", "IRV", 1, true))
				.thenReturn(new BallotTemplate(4, "Template of ballot: " + templates.get(1).getId(),
						templates.get(1).getBallotTitle(), templates.get(1).getInstructions(),
						templates.get(1).getDescription(), templates.get(1).getTypeOfVote(),
						templates.get(1).getOutcomes(), templates.get(1).isBasis()));

		mvc.perform(post("/template/ajax/createTemplate").contentType(MediaType.TEXT_HTML).with(csrf())).andExpect(status().isOk())

				.andExpect(content().string(containsString("4")));
	}
	
	@Test
	void testViewTemplates() throws Exception {
		// Mock dependencies
				Mockito.when(mockTemplSrv.listTemplates()).thenReturn(templates);

				mvc.perform(get("/templates").with(csrf())
						.contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk())

				.andExpect(xpath(dquote("//table")).exists()).andExpect(xpath(dquote("//tr[@tid='12,345']")).exists())
				.andExpect(xpath(dquote("//tr[@tid='23,456']")).exists())
				.andExpect(xpath(dquote("//tr[@tid='34,567']")).exists());

				Mockito.verify(mockTemplSrv).listTemplates();
	}
	
	@Test
	void testViewTemplate() throws Exception {
		Mockito.when(mockTemplSrv.getTemplate(1)).thenReturn(templates.get(0));
		
		mvc.perform(get("/template?tid=1").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(xpath(dquote("//div[@tid='12345']")).exists())
		.andExpect(xpath(dquote("//div[@id='idHolder']")).exists())
		.andExpect(xpath(dquote("//div[@id='tTitle']")).exists())
		.andExpect(xpath(dquote("//div[@id='bTitle']")).exists());
		
		Mockito.verify(mockTemplSrv).getTemplate(1);
	}
	
	@Test
	void testEditTemplate() throws Exception {
		BallotTemplate temp = new BallotTemplate(12345, "Edited Template", "Best CS Prof", "submit your ballot, or else",
	            "who's the goat?", "IRV", 2, true);

	    Mockito.when(mockTemplSrv.editTemplate(
	            12345, "Edited Template", "Best CS Prof", "submit your ballot, or else",
	            "who's the goat?", "IRV", 2, true
	    )).thenReturn(temp);

	    // Build the request with all the variables included directly in the URL
	    mvc.perform(put("/template/ajax/editTemplate")
	            .param("tid", "12345")
	            .param("tTitle", "Edited Template")
	            .param("bTitle", "Best CS Prof")
	            .param("instructions", "submit your ballot, or else")
	            .param("description", "who's the goat?")
	            .param("voteType", "IRV")
	            .param("outcomes", "2")
	            .param("basis", "true")
	            .with(csrf())
	            .contentType(MediaType.TEXT_HTML))
	    .andExpect(status().isOk())
	    .andExpect(content().string(containsString("updated template")));

	    Mockito.verify(mockTemplSrv).editTemplate(
	            12345, "Edited Template", "Best CS Prof", "submit your ballot, or else",
	            "who's the goat?", "IRV", 2, true
	    );
	}
	
	@Test
	void testFailEditTemplate() throws Exception {
		BallotTemplate temp = new BallotTemplate(12345, "Edited Template", "Best CS Prof", "submit your ballot, or else",
	            "who's the goat?", "IRV", 2, true);

	    Mockito.when(mockTemplSrv.editTemplate(
	            12345, "Edited Template", "Best CS Prof", "submit your ballot, or else",
	            "who's the goat?", "IRV", 2, true
	    )).thenReturn(null);

	    // Build the request with all the variables included directly in the URL
	    mvc.perform(put("/template/ajax/editTemplate")
	            .param("tid", "12345")
	            .param("tTitle", "Edited Template")
	            .param("bTitle", "Best CS Prof")
	            .param("instructions", "submit your ballot, or else")
	            .param("description", "who's the goat?")
	            .param("voteType", "IRV")
	            .param("outcomes", "2")
	            .param("basis", "true")
	            .with(csrf())
	            .contentType(MediaType.TEXT_HTML))
	    
	    .andExpect(status().isOk())
	    .andExpect(content().string(containsString("unable to update template")));

	    Mockito.verify(mockTemplSrv).editTemplate(
	            12345, "Edited Template", "Best CS Prof", "submit your ballot, or else",
	            "who's the goat?", "IRV", 2, true
	    );
	}

}
