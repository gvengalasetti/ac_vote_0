package edu.austincollege.acvote.unit.voter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.BallotService;
import edu.austincollege.acvote.ballot.option.VoteOption;
import edu.austincollege.acvote.vote.VoteService;
import edu.austincollege.acvote.vote.dao.VoteTokenDao;
import edu.austincollege.acvote.voter.VoterController;

@RunWith(SpringRunner.class)
@WebMvcTest(VoterController.class)
@WithMockUser(username = "admin", roles = { "ADMIN" })
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class VoterControllerTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private BallotService mockBalSrv;

	@MockBean
	private VoteService mockVoteSrv;

	@MockBean
	private VoteTokenDao mockVtDao;

	List<Ballot> ballots;

	CsrfToken csrfToken;

	HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();

	@BeforeEach
	void setUp() throws Exception {

		MockitoAnnotations.openMocks(this);

		csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

		ballots = new ArrayList<>();

		/*
		 * going to add sample ballots to local list
		 */
		ArrayList<VoteOption> candidates = new ArrayList<>();
		candidates.add(new VoteOption("0629102", "Michael Higgs", true));
		candidates.add(new VoteOption("0629104", "Aaron Block", true));
		candidates.add(new VoteOption("0629106", "Josh Edge", true));
		
		//IRV ballot with 3 candidates
		ballots.add(new Ballot(1, "Coolest CS Professor", "Please drag and drop these candidates around!",
				"This is a description for ballot 1!", true, candidates, "IRV", 1, LocalDateTime.now().minusDays(20),
				LocalDateTime.now().plusDays(20), "CW", 124));

		candidates.add(new VoteOption("80085", "Alan Rosenberg", true));
		candidates.add(new VoteOption("12345", "Brandon Hill", true));
		candidates.add(new VoteOption("23456", "Guna Vengalasetti", true));
		candidates.add(new VoteOption("34567", "Kieran Leahy", true));
		candidates.add(new VoteOption("45678", "Tobias Andrew Ward II", true));

		//IRV2 ballot with 8 candidates
		ballots.add(new Ballot(2, "Which ones are CS Professors?", "Please drag and drop these candidates around!",
				"This is a description for ballot 2!", true, candidates, "IRV2", 2, LocalDateTime.now().minusDays(50),
				LocalDateTime.now().plusDays(50), "CW", 7));
		
		//copy of ballot 2 except polls are closed
		ballots.add(new Ballot(2, "Which ones are CS Professors?", "Please drag and drop these candidates around!",
				"This is a description for ballot 2!", true, candidates, "IRV2", 2, LocalDateTime.now().minusDays(100),
				LocalDateTime.now().minusDays(50), "CW", 7));
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
	 * Testing that controller properly displays IRV vote page
	 * 
	 * @throws Exception
	 */
	@Test
	void testVoteIRV() throws Exception {

		Mockito.when(mockBalSrv.getBallotById(1)).thenReturn(ballots.get(0));
		Mockito.when(mockVtDao.tokenIsAbsent(1, "token")).thenReturn(false);

		mvc.perform(get("/voter/vote?bid=1&token=token").with(csrf()).contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk())

				// should include header with ballot info, and sortable list with items
				.andExpect(xpath(dquote("//div[@id='ballotInfo']/h1/text()"))
						.string(containsString("Coolest CS Professor")))
				.andExpect(xpath(dquote("//ol[@id='sortable-list']")).exists())
				.andExpect(xpath(dquote("//li[@oid='0629102']")).exists());
	}

	/**
	 * Testing that controller properly displays IRV2 vote page
	 * 
	 * @throws Exception
	 */
	@Test
	void testVoteIRV2() throws Exception {

		Mockito.when(mockBalSrv.getBallotById(2)).thenReturn(ballots.get(1));
		Mockito.when(mockVtDao.tokenIsAbsent(2, "token")).thenReturn(false);

		mvc.perform(get("/voter/vote?bid=2&token=token").with(csrf()).contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk())

				// should include filter modal with table full of options
				.andExpect(xpath(dquote("//div[@id='filterModal']")).exists())
				.andExpect(xpath(dquote("//table[@id='optionTable']")).exists())
				.andExpect(xpath(dquote("//tr[@id='op_0629104']")).exists())
				.andExpect(xpath(dquote("//tr[@id='op_45678']")).exists())

				// should include header with ballot info, and sortable list with items
				.andExpect(xpath(dquote("//div[@id='ballotInfo']/h1/text()"))
						.string(containsString("Which ones are CS Professors")))
				.andExpect(xpath(dquote("//ol[@id='sortable-list']")).exists())
				.andExpect(xpath(dquote("//li[@oid='0629102']")).exists());
	}

	/**
	 * Testing that controller properly handles invalid bid request when viewing voting page
	 * @throws Exception
	 */
	@Test
	void testVoteInvalidBid() throws Exception {

		Mockito.when(mockBalSrv.getBallotById(5)).thenReturn(null);

		//should redirect to deny page
		mvc.perform(get("/voter/vote?bid=5&token=token").with(csrf()).contentType(MediaType.TEXT_HTML))
				.andExpect(status().isFound());
	}
	
	/**
	 * Testing that controller properly handles closed ballot request when viewing voting page
	 * @throws Exception
	 */
	@Test
	void testVoteClosedBallot() throws Exception {
		
		Mockito.when(mockBalSrv.getBallotById(2)).thenReturn(ballots.get(2));
		
		//should redirect to deny page
		mvc.perform(get("/voter/vote?bid=2&token=token").with(csrf()).contentType(MediaType.TEXT_HTML))
				.andExpect(status().isFound());
	}
	
	/**
	 * Testing that controller properly handles bad token request when viewing voting page
	 * @throws Exception
	 */
	@Test
	void testVoteBadToken() throws Exception {
		
		Mockito.when(mockBalSrv.getBallotById(2)).thenReturn(ballots.get(1));
		Mockito.when(mockVtDao.tokenIsAbsent(2, "token")).thenReturn(true);
		
		//should redirect to deny page
		mvc.perform(get("/voter/vote?bid=2&token=token").with(csrf()).contentType(MediaType.TEXT_HTML))
				.andExpect(status().isFound());
	}

	/**
	 * Testing that controller properly displays deny page
	 * 
	 * @throws Exception
	 */
	@Test
	void testDeny() throws Exception {

		mvc.perform(get("/voter/deny?errMsg=that_didnt_work").with(csrf()).contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk())

				// should see header with error message
				.andExpect(xpath(dquote("//h5[@class='card-body']/text()")).string(containsString("that_didnt_work")));
	}

	/**
	 * Testing that controller properly handles cast vote request
	 * 
	 * @throws Exception
	 */
	@Test
	void testCastVoteSuccess() throws Exception {

		List<String> options = new ArrayList<>();

		options.add("0629102");
		options.add("0629104");
		options.add("0629106");

		Mockito.doNothing().when(mockVoteSrv).castVote(1, "token", options);

		mvc.perform(post("/voter/ajax/submitBallot?bid=1&token=token&options=0629102,0629104,0629106").with(csrf())
				.contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())

				//should see success message
				.andExpect(content().string(containsString("successfully submitted vote")));

	}

	/**
	 * Testing that controller properly handles cast vote request with a failure
	 * 
	 * @throws Exception
	 */
	@Test
	void testCastVoteFailure() throws Exception {

		List<String> options = new ArrayList<>();

		options.add("0629102");
		options.add("0629104");
		options.add("0629106");

		Mockito.doThrow(new Exception("piece of shorts")).when(mockVoteSrv).castVote(1, "token", options);

		mvc.perform(post("/voter/ajax/submitBallot?bid=1&token=token&options=0629102,0629104,0629106").with(csrf())
				.contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())

				//should see failure message
				.andExpect(content().string(containsString("could not submit vote")));

	}
	
	/**
	 * Testing that controller with properly redirect users to vote page
	 * @throws Exception
	 */
	@Test
	void testVoterHome() throws Exception {
		
		//should redirect to vote page
		mvc.perform(get("/voter").with(csrf()).contentType(MediaType.TEXT_HTML))
				.andExpect(status().isFound());
	}

}
