package edu.austincollege.acvote.unit.ballot;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.BallotController;
import edu.austincollege.acvote.ballot.BallotService;
import edu.austincollege.acvote.ballot.option.VoteOption;
import edu.austincollege.acvote.email.DummyEmailService;
import edu.austincollege.acvote.email.EmailService;
import edu.austincollege.acvote.faculty.Faculty;
import edu.austincollege.acvote.faculty.FacultyService;
import edu.austincollege.acvote.lut.LutDao;
import edu.austincollege.acvote.template.BallotTemplate;
import edu.austincollege.acvote.template.TemplateService;

import edu.austincollege.acvote.vote.Votes;
import edu.austincollege.acvote.vote.VoteResult;
import edu.austincollege.acvote.vote.VoteService;
import edu.austincollege.acvote.vote.VoteToken;

@RunWith(SpringRunner.class)
@WebMvcTest(BallotController.class)
@WithMockUser(username = "admin", roles = { "ADMIN" })
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class BallotControllerTests {

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

	@MockBean
	private VoteService mockVoteSrv;

	@MockBean
	private DummyEmailService mockEmailService;

	@MockBean
	private EmailService mockEmailServiceImpl;

	@MockBean
	private Votes mockVotes;

	private List<Ballot> ballots = new ArrayList<>();
	private List<BallotTemplate> templates = new ArrayList<>();

	HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();

	CsrfToken csrfToken;

	private TestInfo testInfo;

	@BeforeEach
	void init(TestInfo testInfo) {
	    this.testInfo = testInfo;
	    System.out.println(String.format("\n\n\n>>> ----------- %s ----------- <<<< ",testInfo.getDisplayName()));
	}
	
	
	@BeforeEach
	void setUp() throws Exception {

		MockitoAnnotations.openMocks(this); // make the annotations work

		csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

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

	/**
	 * Testing that dquote function works as expected
	 */
	@Test
	void test_replace_single_quotes_function() {
		String str = "tr/td[@id='xyz']";
		// System.err.println(dquote(str));
		assertEquals("tr/td[@id=\"xyz\"]", dquote(str));
	}

	/**
	 * Testing that dquote function works when fed null string
	 */
	@Test
	void test_replace_single_quotes_function_null_string() {
		String str = null;
		assertNull(dquote(str));
	}

	/**
	 * Testing that controller properly displays ballot list view
	 * 
	 * @throws Exception
	 */
	@Test
	void testViewBallots() throws Exception {

		// Mock dependencies
		Mockito.when(mockBalSrv.getBallots()).thenReturn(ballots);

		mvc.perform(get("/ballots").with(csrf()).contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())

				.andExpect(xpath(dquote("//table")).exists()).andExpect(xpath(dquote("//tr[@bid='1']")).exists())
				.andExpect(xpath(dquote("//tr[@bid='2']")).exists())
				.andExpect(xpath(dquote("//tr[@bid='3']")).exists());

		Mockito.verify(mockBalSrv).getBallots();
	}

	/**
	 * Testing that controller properly displays ballot 1 details
	 * 
	 * @throws Exception
	 */
	@Test
	void testViewBallot_1() throws Exception {
		// Mock dependencies
		Mockito.when(mockBalSrv.getBallotById(1)).thenReturn(ballots.get(0));

		System.err.println(ballots.get(0).getTitle());
		System.err.println(csrfToken);

		mvc.perform(get("/ballot?bid=1").with(csrf()).contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())

				.andExpect(xpath(dquote("//div[@bid='1']")).exists())
				.andExpect(xpath(
						"//div[@id='idHolder']//div[@id='title']//p[@id='titleText' and normalize-space()='Coolest CS Professor']")
						.exists());
		// .andExpect(xpath("//p[text()='Coolest CS Professor']").exists());

		Mockito.verify(mockBalSrv).getBallotById(1);

	}

	/**
	 * Testing that controller properly displays ballot 2 details
	 * 
	 * @throws Exception
	 */
	@Test
	void testViewBallot_2() throws Exception {
		// Mock dependencies
		Mockito.when(mockBalSrv.getBallotById(2)).thenReturn(ballots.get(1));

		System.err.println(ballots.get(0).getTitle());

		mvc.perform(get("/ballot?bid=2").with(csrf()).contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())

				.andExpect(xpath(dquote("//div[@bid='2']")).exists())
				.andExpect(xpath(
						"//div[@id='idHolder']//div[@id='title']//p[@id='titleText' and normalize-space()='Which ones are CS Professors?']")
						.exists());

		Mockito.verify(mockBalSrv).getBallotById(2);

	}

	/**
	 * Testing that controller properly displays results page
	 * 
	 * @throws Exception
	 */
	@Test
	void testViewResults() throws Exception {

		Mockito.when(mockBalSrv.getBallotById(1)).thenReturn(ballots.get(0));
		Mockito.when(mockBalSrv.resultsForBallot(ballots.get(0))).thenReturn(new VoteResult());

		mvc.perform(get("/ballot/1/results").with(csrf()).contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())

				.andExpect(xpath(dquote("//div[@id='header']")).exists())
				.andExpect(xpath(dquote("//div[@id='title']/p/text()")).string(containsString("Coolest CS Professor")));
		
		Mockito.verify(mockBalSrv).getBallotById(1);
		Mockito.verify(mockBalSrv).resultsForBallot(ballots.get(0));
	}

	/**
	 * Testing that controller properly handles deletion of an option from a ballot
	 * @throws Exception
	 */
	@Test
	void testDeleteOptionFromBallot() throws Exception {
		
		Mockito.doNothing().when(mockBalSrv).deleteOptionFromBallot(1, "0");
		
		mvc.perform(delete("/ballot/ajax/deleteOption?bid=1&oid=0").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk());
		
		Mockito.verify(mockBalSrv).deleteOptionFromBallot(1, "0");
	}
	
	/**
	 * Testing that controller properly handles deletion of an option from a ballot when service throws and exception
	 * @throws Exception
	 */
	@Test
	void testDeleteOptionFromBallotFailure() throws Exception {
		
		Mockito.doThrow(new Exception("fork")).when(mockBalSrv).deleteOptionFromBallot(1, "0");
		
		mvc.perform(delete("/ballot/ajax/deleteOption?bid=1&oid=0").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isBadRequest());
		
		Mockito.verify(mockBalSrv).deleteOptionFromBallot(1, "0");
	}
	
	/**
	 * Testing that controller properly handles enabling of option
	 * @throws Exception
	 */
	@Test
	void testEnableOption() throws Exception {
		
		Mockito.when(mockBalSrv.toggleOption(1, "0", true)).thenReturn(true);
		
		mvc.perform(put("/ballot/ajax/enableOption?bid=1&oid=0&state=true").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk());
		
		Mockito.verify(mockBalSrv).toggleOption(1, "0", true);
	}
	
	/**
	 * Testing that controller properly handles enabling of option when service fails
	 * @throws Exception
	 */
	@Test
	void testEnableOptionFailure() throws Exception {
		
		Mockito.when(mockBalSrv.toggleOption(1, "0", true)).thenReturn(false);
		
		mvc.perform(put("/ballot/ajax/enableOption?bid=1&oid=0&state=true").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isBadRequest());
		
		Mockito.verify(mockBalSrv).toggleOption(1, "0", true);
	}
	
	/**
	 * Testing that controller properly handles clearing options
	 * @throws Exception
	 */
	@Test
	void testClearOptions() throws Exception {
		
		Mockito.when(mockBalSrv.clearOptionsFromBallot(1)).thenReturn(true);
		
		mvc.perform(delete("/ballot/ajax/deleteAllOptions?bid=1").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk());
		
		Mockito.verify(mockBalSrv).clearOptionsFromBallot(1);
	}
	
	/**
	 * Testing that controller properly handles clearing options when service fails
	 * @throws Exception
	 */
	@Test
	void testClearOptionsFailure() throws Exception {
		
		Mockito.when(mockBalSrv.clearOptionsFromBallot(1)).thenReturn(false);
		
		mvc.perform(delete("/ballot/ajax/deleteAllOptions?bid=1").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isBadRequest());
		
		Mockito.verify(mockBalSrv).clearOptionsFromBallot(1);
	}

	/**
	 * Testing that controller properly handles deleting ballot
	 * @throws Exception
	 */
	@Test
	void testDeleteBallotNormal() throws Exception {
		
		Mockito.when(mockBalSrv.deleteBallot(1)).thenReturn(true);

		mvc.perform(delete("/ballot/ajax/deleteBallot?bid=1").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("ballot 1 has been deleted")));

		Mockito.verify(mockBalSrv).deleteBallot(1);
	}
	
	/**
	 * Testing that controller properly handles deleting ballot when service fails
	 * @throws Exception
	 */
	@Test
	void testDeleteBallotFailure() throws Exception {
		
		Mockito.when(mockBalSrv.deleteBallot(1)).thenReturn(false);
		
		mvc.perform(delete("/ballot/ajax/deleteBallot?bid=1").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("ballot 1 has NOT been deleted")));
		
		Mockito.verify(mockBalSrv).deleteBallot(1);
	}
	
	/**
	 * Testing that controller properly handles creating ballot
	 * @throws Exception
	 */
	@Test
	void testCreateBallot() throws Exception {
		
		LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
		Mockito.when(mockBalSrv.addBallot("Enter Title", "Enter Instructions", "Enter Description", true, null, "IRV",
				0, now, now.plusDays(10), "CW", 1)).thenReturn(3);

		mvc.perform(post("/ballot/ajax/createBallot").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("3")));

		Mockito.verify(mockBalSrv).addBallot("Enter Title", "Enter Instructions", "Enter Description", true, null,
				"IRV", 0, now, now.plusDays(10), "CW", 1);
	}

	/**
	 * Testing that controller properly handles creating ballot from template
	 * @throws Exception
	 */
	@Test
	void testCreateBallotFromTemplate() throws Exception {
		
		Mockito.when(mockTemplSrv.getTemplate(12345)).thenReturn(templates.get(0));
		LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
		Mockito.when(mockBalSrv.addBallot(templates.get(0).getBallotTitle(), templates.get(0).getInstructions(),
				templates.get(0).getDescription(), templates.get(0).isBasis(), null, templates.get(0).getTypeOfVote(),
				templates.get(0).getOutcomes(), now, now.plusDays(10), "CW", 1)).thenReturn(17);

		mvc.perform(post("/ballot/ajax/createBallotFromTemplate?tid=12345").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk());
		
//		.andExpect(content().string(containsString("17")));
		
		Mockito.verify(mockBalSrv).addBallot(templates.get(0).getBallotTitle(), templates.get(0).getInstructions(),
				templates.get(0).getDescription(), templates.get(0).isBasis(), null, templates.get(0).getTypeOfVote(),
				templates.get(0).getOutcomes(), now, now.plusDays(10), "CW", 1);
	}
	
	/**
	 * Testing that controller properly handles adding custom options to a ballot
	 * @throws Exception
	 */
	@Test
	void testAddCustomOption() throws Exception {
		
		List<VoteOption> options = new ArrayList<>();
		options.add(new VoteOption("0", "zero", true));
		Mockito.doNothing().when(mockBalSrv).addBallotOptions(1, options);
		
		mvc.perform(post("/ballot/ajax/addOption?bid=1&oid=0&title=zero").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk());
		
		Mockito.verify(mockBalSrv).addBallotOptions(1, options);
	}
	
	/**
	 * Testing that controller properly handles adding custom options when service fails
	 * @throws Exception
	 */
	@Test
	void testAddCustomOptionFailure() throws Exception {
		
		List<VoteOption> options = new ArrayList<>();
		options.add(new VoteOption("0", "zero", true));
		Mockito.doThrow(new Exception("shorts")).when(mockBalSrv).addBallotOptions(1, options);
		
		mvc.perform(post("/ballot/ajax/addOption?bid=1&oid=0&title=zero").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isBadRequest());
		
		Mockito.verify(mockBalSrv).addBallotOptions(1, options);
	}

	/**
	 * Testing that controller properly adds faculty as options to ballot
	 * @throws Exception
	 */
	@Test
	void testAddFacultyOptions() throws Exception {
		String[] listOfIds = new String[1];
		listOfIds[0] = "678392";
		List<Faculty> listOfFac = new ArrayList<Faculty>();
		listOfFac.add(new Faculty());
		listOfFac.get(0).setAcId("678392");
		listOfFac.get(0).setFirstName("Nguyen");
		VoteOption vo = new VoteOption("678392", "Nguyen", true);
		ArrayList<VoteOption> vol = new ArrayList<VoteOption>();
		vol.add(vo);

		Mockito.when(mockBalSrv.getBallotById(1)).thenReturn(ballots.get(0));
		Mockito.when(mockFacSrv.findFacultyMembers(listOfIds)).thenReturn(listOfFac);
		Mockito.when(mockBalSrv.convertFacultytoVoteOptions(listOfFac)).thenReturn(vol);

		String[] newOptionIds = { "678392" };
		mvc.perform(post("/ballot/ajax/addCandidates").param("bid", "1")
				.param("newOptionIDs[]", String.join(",", newOptionIds)).contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk()).andExpect(content().string(containsString("faculty options")));

		Mockito.verify(mockBalSrv).addBallotOptions(1, vol);

	}

	/**
	 * Testing that controller properly handles duplicate faculty option additions
	 * @throws Exception
	 */
	@Test
	void testAddDuplicateFacultyOptions() throws Exception {
		String[] listOfIds = new String[1];
		listOfIds[0] = "80085";
		List<Faculty> listOfFac = new ArrayList<Faculty>();
		listOfFac.add(new Faculty());
		listOfFac.get(0).setAcId("80085");
		listOfFac.get(0).setFirstName("Rosenberg");
		VoteOption vo = new VoteOption("80085", "Rosenberg", true);
		ArrayList<VoteOption> vol = new ArrayList<VoteOption>();
		vol.add(vo);

		Mockito.when(mockBalSrv.getBallotById(1)).thenReturn(ballots.get(0));
		Mockito.when(mockFacSrv.findFacultyMembers(listOfIds)).thenReturn(listOfFac);
		Mockito.when(mockBalSrv.convertFacultytoVoteOptions(listOfFac)).thenReturn(vol);

		String[] newOptionIds = { "80085" };
		mvc.perform(post("/ballot/ajax/addCandidates").param("bid", "1")
				.param("newOptionIDs[]", String.join(",", newOptionIds)).contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk()).andExpect(content().string(containsString("faculty options")));

		Mockito.verify(mockBalSrv, never()).addBallotOptions(1, vol);
	}
	
	/**
	 * Testing that controller properly handles exception when adding faculty options to ballot
	 * @throws Exception
	 */
	@Test
	void testAddFacultyOptionsFailure() throws Exception {
		
		String[] listOfIds = new String[1];
		listOfIds[0] = "678392";
		List<Faculty> listOfFac = new ArrayList<Faculty>();
		listOfFac.add(new Faculty());
		listOfFac.get(0).setAcId("678392");
		listOfFac.get(0).setFirstName("Nguyen");
		VoteOption vo = new VoteOption("678392", "Nguyen", true);
		ArrayList<VoteOption> vol = new ArrayList<VoteOption>();
		vol.add(vo);
		
		Mockito.when(mockBalSrv.getBallotById(1)).thenReturn(ballots.get(0));
		Mockito.when(mockFacSrv.findFacultyMembers(listOfIds)).thenReturn(listOfFac);
		Mockito.when(mockBalSrv.convertFacultytoVoteOptions(listOfFac)).thenReturn(vol);
		Mockito.doThrow(new Exception("what the fork")).when(mockBalSrv).addBallotOptions(1, vol);
		
		String[] newOptionIds = { "678392" };
		mvc.perform(post("/ballot/ajax/addCandidates").param("bid", "1")
				.param("newOptionIDs[]", String.join(",", newOptionIds)).with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isBadRequest());
		
		Mockito.verify(mockBalSrv).getBallotById(1);
		Mockito.verify(mockFacSrv).findFacultyMembers(listOfIds);
		Mockito.verify(mockBalSrv).convertFacultytoVoteOptions(listOfFac);
		Mockito.verify(mockBalSrv).addBallotOptions(1, vol);
	}

	/**
	 * Test starting and restating vote
	 * 
	 * @throws Exception
	 */
	@Test
	void testStartRestartVote() throws Exception {
		List<VoteToken> tokens = new ArrayList<VoteToken>();
		Mockito.when(mockBalSrv.getBallotById(1)).thenReturn(ballots.get(0));
		Mockito.when(mockVoteSrv.startRestartVote(ballots.get(0))).thenReturn(tokens);

		mvc.perform(post("/ballot/ajax/startVote").param("bid", "1")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Starting Vote")));

		Mockito.verify(mockBalSrv).getBallotById(1);
		Mockito.verify(mockVoteSrv).startRestartVote(ballots.get(0));
	}
	
	/**
	 * Test starting and restating vote when email service fails
	 * 
	 * @throws Exception
	 */
	@Test
	void testStartRestartVoteFailure() throws Exception {
		List<VoteToken> tokens = new ArrayList<VoteToken>();
		Mockito.when(mockBalSrv.getBallotById(1)).thenReturn(ballots.get(0));
		Mockito.when(mockVoteSrv.startRestartVote(ballots.get(0))).thenReturn(tokens);
		Mockito.doThrow(new Exception("fork these shorts")).when(mockEmailServiceImpl).sendVoteTemplateEmailToList("ACVote", ballots.get(0), tokens);

		mvc.perform(post("/ballot/ajax/startVote").param("bid", "1")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("failed to start vote")));

		Mockito.verify(mockBalSrv).getBallotById(1);
		Mockito.verify(mockVoteSrv).startRestartVote(ballots.get(0));
	}

	/**
	 * Test reminding voters
	 * 
	 * @throws Exception
	 */
	@Test
	void testRemindVoters() throws Exception {
		List<VoteToken> tokens = new ArrayList<VoteToken>();
		tokens.add(VoteToken.newToken(1, "5854836"));
		tokens.add(VoteToken.newToken(1, "4138778"));
		List<Faculty> faculty = new ArrayList<Faculty>();
		String[] facIds = new String[2];
		facIds[0] = "5854836";
		facIds[1] = "4138778";
		Mockito.when(mockBalSrv.getBallotById(1)).thenReturn(ballots.get(0));
		Mockito.when(mockVoteSrv.getAllTokensForBallot(1)).thenReturn(tokens);
		Mockito.when(mockVoteSrv.convertFacultyToTokens(tokens, faculty)).thenReturn(tokens);

		mvc.perform(post("/ballot/ajax/remindVoters").param("bid", "1").param("facIds[]", facIds)
				.param("body", "test message").contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(content().string(containsString("Sending Reminders")));

		Mockito.verify(mockBalSrv).getBallotById(1);
		Mockito.verify(mockVoteSrv).getAllTokensForBallot(1);
		Mockito.verify(mockVoteSrv).convertFacultyToTokens(tokens, faculty);
	}
	
	/**
	 * Test reminding voters when email service fails
	 * 
	 * @throws Exception
	 */
	@Test
	void testRemindVotersFailure() throws Exception {
		List<VoteToken> tokens = new ArrayList<VoteToken>();
		tokens.add(VoteToken.newToken(1, "5854836"));
		tokens.add(VoteToken.newToken(1, "4138778"));
		List<Faculty> faculty = new ArrayList<Faculty>();
		String[] facIds = new String[2];
		facIds[0] = "5854836";
		facIds[1] = "4138778";
		Mockito.when(mockBalSrv.getBallotById(1)).thenReturn(ballots.get(0));
		Mockito.when(mockVoteSrv.getAllTokensForBallot(1)).thenReturn(tokens);
		Mockito.when(mockVoteSrv.convertFacultyToTokens(tokens, faculty)).thenReturn(tokens);
		Mockito.doThrow(new Exception("what the scallop")).when(mockEmailServiceImpl).sendVoteTemplateEmailWithTextToList("test message", "ACVote Reminder", ballots.get(0), tokens);

		mvc.perform(post("/ballot/ajax/remindVoters").param("bid", "1").param("facIds[]", facIds)
				.param("body", "test message").contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(content().string(containsString("failed to remind")));

		Mockito.verify(mockBalSrv).getBallotById(1);
		Mockito.verify(mockVoteSrv).getAllTokensForBallot(1);
		Mockito.verify(mockVoteSrv).convertFacultyToTokens(tokens, faculty);
	}

	@Test
	void tokenToFacultyTest() throws Exception {
		List<VoteToken> tokens = new ArrayList<VoteToken>();
		tokens.add(VoteToken.newToken(1, "5854836"));
		tokens.add(VoteToken.newToken(1, "4138778"));

		List<Faculty> faculty = new ArrayList<Faculty>();
		Faculty f1 = new Faculty();
		f1.setAcId("5854836");
		faculty.add(f1);
		Faculty f2 = new Faculty();
		f2.setAcId("4138778");
		faculty.add(f2);

		Mockito.when(mockVoteSrv.getAllTokensForBallot(1)).thenReturn(tokens);
		Mockito.when(mockVoteSrv.convertTokensToFaculty(tokens)).thenReturn(faculty);

		mvc.perform(get("/ballot/ajax/tokenToFaculty").param("bid", "1").contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk());
	}
	
	/**
	 * Testing that controller properly handles changing the ballot title
	 * @throws Exception
	 */
	@Test
	void testSetBallotTitle() throws Exception {
		
		Ballot b = ballots.get(0);
		b.setTitle("title");
		Mockito.when(mockBalSrv.setBallotTitle(1, "title")).thenReturn(b);
		
		mvc.perform(put("/ballot/ajax/setBallotTitle?bid=1&ballotTitle=title").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("title updated")));
	}

	
	/**
	 * Testing that controller properly handles changing the ballot description
	 * @throws Exception
	 */
	@Test
	void testSetBallotDesription() throws Exception {
		
		Ballot b = ballots.get(0);
		b.setDescription("desc");
		Mockito.when(mockBalSrv.setBallotDescription(1, "desc")).thenReturn(b);
		
		mvc.perform(put("/ballot/ajax/setBallotDescription?bid=1&ballotDescription=desc").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("description updated")));
	}
	
	/**
	 * Testing that controller properly handles changing the ballot instructions
	 * @throws Exception
	 */
	@Test
	void testSetBallotInstructions() throws Exception {
		
		Ballot b = ballots.get(0);
		b.setInstructions("instr");
		Mockito.when(mockBalSrv.setBallotInstructions(1, "instr")).thenReturn(b);
		
		mvc.perform(put("/ballot/ajax/setBallotInstructions?bid=1&ballotInstructions=instr").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("instructions updated")));
	}
	
	/**
	 * Testing that controller properly handles changing the ballot vote type
	 * @throws Exception
	 */
	@Test
	void testSetBallotTypeOfVote() throws Exception {
		
		Ballot b = ballots.get(0);
		b.setTypeOfVote("voteType");
		Mockito.when(mockBalSrv.setBallotTypeOfVote(1, "voteType")).thenReturn(b);
		
		mvc.perform(put("/ballot/ajax/setBallotTypeOfVote?bid=1&ballotTypeOfVote=voteType").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("vote type updated")));
	}
	
	/**
	 * Testing that controller properly handles changing the ballot basis
	 * @throws Exception
	 */
	@Test
	void testSetBallotBasis() throws Exception {
		
		Ballot b = ballots.get(0);
		b.setFacultyBased(false);
		Mockito.when(mockBalSrv.setBasis(1, false)).thenReturn(b);
		
		mvc.perform(put("/ballot/ajax/setBasis?bid=1&basis=false").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("basis updated")));
	}
	
	/**
	 * Testing that controller properly handles changing the ballot basis when the service fails
	 * @throws Exception
	 */
	@Test
	void testSetBallotBasisFailure() throws Exception {
		
		Ballot b = ballots.get(0);
		b.setFacultyBased(false);
		Mockito.when(mockBalSrv.setBasis(1, false)).thenThrow(new Exception("fork sticks"));
		
		mvc.perform(put("/ballot/ajax/setBasis?bid=1&basis=false").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("unable to change the basis for ballot 1")));
	}
	
	/**
	 * Testing that controller properly handles changing the ballot outcomes
	 * @throws Exception
	 */
	@Test
	void testSetBallotOutcomes() throws Exception {
		
		Ballot b = ballots.get(0);
		b.setOutcomes(5);
		Mockito.when(mockBalSrv.setBallotOutcomes(1, 5)).thenReturn(b);
		
		mvc.perform(put("/ballot/ajax/setBallotOutcomes?bid=1&ballotOutcomes=5").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("number of outcomes updated to 5")));
	}
	
	/**
	 * Testing that controller properly handles changing the ballot start time
	 * @throws Exception
	 */
	@Test
	void testSetBallotStartTime() throws Exception {
		
		Ballot b = ballots.get(0);
		LocalDateTime now = LocalDateTime.now();
		b.setStartTime(now);
		Mockito.when(mockBalSrv.setBallotStartTime(1, now)).thenReturn(b);
		
		mvc.perform(put("/ballot/ajax/setBallotStartTime?bid=1&ballotStartTime=" + now.toString()).with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("Vote start time updated to " + now.toString())));
	}
	
	/**
	 * Testing that controller properly handles changing the ballot end time
	 * @throws Exception
	 */
	@Test
	void testSetBallotEndTime() throws Exception {
		
		Ballot b = ballots.get(0);
		LocalDateTime now = LocalDateTime.now();
		b.setEndTime(now);
		Mockito.when(mockBalSrv.setBallotEndTime(1, now)).thenReturn(b);
		
		mvc.perform(put("/ballot/ajax/setBallotEndTime?bid=1&ballotEndTime=" + now.toString()).with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("Vote end time updated to " + now.toString())));
	}
	
	/**
	 * Testing that controller properly handles changing the ballot voters
	 * @throws Exception
	 */
	@Test
	void testSetBallotVotingGroup() throws Exception {
		
		Ballot b = ballots.get(0);
		b.setVoters("voters");
		Mockito.when(mockBalSrv.setBallotVotingGroup(1, "voters")).thenReturn(b);
		
		mvc.perform(put("/ballot/ajax/setVotingGroup?bid=1&group=voters").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("Voting group updated to voters")));
	}
	
	/**
	 * Testing that controller properly handles changing ballot basis
	 * @throws Exception
	 */
	@Test
	void testToggleOption() throws Exception {
		
		Mockito.when(mockBalSrv.toggleOption(1, "0", false)).thenReturn(true);
		
		mvc.perform(put("/ballot/ajax/setOptionEnabled?bid=1&oid=0&enabled=false").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("toggled option #0 to false")));
	}
	
	/**
	 * Testing that controller properly handles changing ballot basis when service fails
	 * @throws Exception
	 */
	@Test
	void testToggleOptionFailure() throws Exception {
		
		Mockito.when(mockBalSrv.toggleOption(1, "0", false)).thenReturn(false);
		
		mvc.perform(put("/ballot/ajax/setOptionEnabled?bid=1&oid=0&enabled=false").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		.andExpect(content().string(containsString("toggle failed")));
	}

}
