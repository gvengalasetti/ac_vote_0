package edu.austincollege.acvote.unit.vote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.dao.BallotDao;
import edu.austincollege.acvote.ballot.option.VoteOption;
import edu.austincollege.acvote.faculty.Faculty;
import edu.austincollege.acvote.faculty.dao.FacultyDAO;
import edu.austincollege.acvote.vote.InvalidTokenException;
import edu.austincollege.acvote.vote.VoteCast;
import edu.austincollege.acvote.vote.VoteService;
import edu.austincollege.acvote.vote.VoteToken;
import edu.austincollege.acvote.vote.dao.VoteCastDao;
import edu.austincollege.acvote.vote.dao.VoteTokenDao;

@RunWith(MockitoJUnitRunner.class)
class VoteServiceTest {

	@Rule
	public MockitoRule initRule = MockitoJUnit.rule();

	@Mock
	private VoteCastDao mockVcDao;

	@Mock
	private VoteTokenDao mockVtDao;

	@Mock
	private BallotDao mockBallotDao;

	@Mock
	private FacultyDAO mockFacultyDao;

	@InjectMocks
	private VoteService vs = new VoteService();

	private List<String> testRankings;
	private VoteCast vc;
	private Ballot b1;
	private Ballot b2;

	private VoteToken t1;
	private VoteToken t2;
	private VoteToken t3;
	private List<VoteToken> ballotOneTokens = new ArrayList<VoteToken>();
	private List<VoteToken> ballotTwoTokens = new ArrayList<VoteToken>();

	private Faculty f1;
	private Faculty f2;
	private List<Faculty> fac = new ArrayList<Faculty>();

	private Logger log = LoggerFactory.getLogger(VoteServiceTest.class);

	@BeforeEach
	void setUp() throws Exception {
		testRankings = new ArrayList<>();
		testRankings.add("ip");
		testRankings.add("js");
		testRankings.add("mt");
		testRankings.add("th");

		t1 = VoteToken.newToken(1, "5854836");
		t2 = VoteToken.newToken(1, "4138778");
		t3 = VoteToken.newToken(2, "5488165");

		ballotOneTokens = new ArrayList<VoteToken>();
		ballotOneTokens.add(t1);
		ballotOneTokens.add(t2);

		ballotTwoTokens = new ArrayList<VoteToken>();
		ballotTwoTokens.add(t3);

		f1 = new Faculty();
		f1.setDiv("CW");
		f1.setActive(true);
		f1.setVoting(true);
		f1.setAcId("5854836");
		f2 = new Faculty();
		f2.setDiv("CW");
		f2.setActive(true);
		f2.setVoting(true);
		f2.setAcId("4138778");

		fac.add(f1);
		fac.add(f2);

		vc = new VoteCast(3, "0d354582ca957a881ac85b7dfdbe8163", testRankings);

		ArrayList<VoteOption> candidates = new ArrayList<>();
		candidates.add(new VoteOption("0629102", "Higgs", true));
		candidates.add(new VoteOption("0629104", "Block", true));
		b1 = new Ballot(3, "Coolest CS Professor", "Please drag and drop these candidates around!",
				"This is a description for ballot 1!", true, candidates, "IRV", 2, LocalDateTime.now().plusDays(10),
				LocalDateTime.now().plusDays(20), "CW", 124);
		b2 = new Ballot(3, "Coolest CS Professor", "Please drag and drop these candidates around!",
				"This is a description for ballot 1!", true, candidates, "IRV", 2,
				LocalDateTime.of(2007, 10, 10, 0, 0, 0), LocalDateTime.of(2008, 10, 10, 0, 0, 0), "CW", 124);

		MockitoAnnotations.openMocks(this);
	}

	/*
	 * -------------------------------- castVote Tests
	 * ------------------------------------
	 */

	/**
	 * Normal test case
	 * 
	 * @throws Exception
	 */
	@Test
	void castVoteTestNormal() throws Exception {

		assertNotNull(mockVcDao);
		assertNotNull(mockVtDao);
		assertNotNull(mockBallotDao);

		Mockito.when(mockBallotDao.getBallot(3)).thenReturn(b1);
		Mockito.doNothing().when(mockVcDao).castVote(vc);
		Mockito.when(mockVtDao.removeToken(3, "0d354582ca957a881ac85b7dfdbe8163")).thenReturn(true);

		try {
			vs.castVote(3, "0d354582ca957a881ac85b7dfdbe8163", testRankings);
		} catch (Exception e) {
			fail("No Exception expected");
		}

	}

	/**
	 * Test case in which there is a bad/missing token
	 * 
	 * @throws Exception
	 */
	@Test
	void castVoteTestBadToken() throws Exception {

		assertNotNull(mockVcDao);
		assertNotNull(mockVtDao);
		assertNotNull(mockBallotDao);

		Mockito.when(mockBallotDao.getBallot(3)).thenReturn(b1);
		Mockito.when(mockVtDao.tokenIsAbsent(3, "0d354582ca957a881ac85b7dfdbe8163")).thenReturn(true);

		try {
			vs.castVote(3, "0d354582ca957a881ac85b7dfdbe8163", testRankings);
			fail("Exception not thrown like expected");
		} catch (InvalidTokenException e) {

		} catch (Exception e) {
			fail("Incorrect exception thrown");
		}
	}

	/**
	 * Test case in which the ballot is closed
	 * 
	 * @throws Exception
	 */
	@Test
	void castVoteTestClosedBallot() throws Exception {

		assertNotNull(mockVcDao);
		assertNotNull(mockVtDao);
		assertNotNull(mockBallotDao);

		Mockito.when(mockBallotDao.getBallot(3)).thenReturn(b2);

		try {
			vs.castVote(3, "0d354582ca957a881ac85b7dfdbe8163", testRankings);
			fail("Exception not thrown like expected");
		} catch (Exception e) {

		}
	}

	/**
	 * Test case in which the ballot does not exist/bid invalid
	 * 
	 * @throws Exception
	 */
	@Test
	void castVoteTestInvalidBid() throws Exception {

		assertNotNull(mockVcDao);
		assertNotNull(mockVtDao);
		assertNotNull(mockBallotDao);

		Mockito.when(mockBallotDao.getBallot(3)).thenThrow(new Exception());

		try {
			vs.castVote(3, "0d354582ca957a881ac85b7dfdbe8163", testRankings);
			fail("Exception not thrown like expected");
		} catch (Exception e) {

		}
	}

	@Test
	void createVoteTokenTest() throws Exception {
		VoteToken result = vs.createVoteToken(3, f1);

		assertNotNull(result);
		assertEquals(f1.getAcId(), result.getAcId());
	}

	/**
	 * Test getting all tokens for ballot
	 * 
	 * @throws Exception
	 */
	@Test
	void tokensForBallotTest() throws Exception {
		Mockito.when(mockVtDao.tokensForBallot(1)).thenReturn(ballotOneTokens);

		List<VoteToken> list = vs.getAllTokensForBallot(1);

		log.debug("the list contains: ");
		for (int i = 0; i < list.size(); i++) {
			log.debug(list.get(i).getAcId());
		}
		log.debug("the list is size: " + list.size());

		assertNotNull(list);
		assertEquals(2, list.size());
	}

	/**
	 * Test looking for a token that doesnt exist
	 * 
	 * @throws Exception
	 */
	@Test
	void tokenIsAbsentTest() throws Exception {
		VoteToken tok = VoteToken.newToken(2, "");
		Mockito.when(mockVtDao.tokenIsAbsent(2, tok.getToken())).thenReturn(true);
		boolean isAbsent = vs.tokenIsAbsent(2, tok);

		Mockito.verify(mockVtDao, Mockito.times(1)).tokenIsAbsent(2, tok.getToken());
		assertTrue(isAbsent);
	}

	/**
	 * Test looking for token after its removed
	 * 
	 * @throws Exception
	 */
	@Test
	void tokenIsAbsentAfterRemovingTest() throws Exception {
		Mockito.when(mockVtDao.tokensForBallot(1)).thenReturn(ballotOneTokens);
		Mockito.when(mockVtDao.removeToken(1, t1.getToken())).thenReturn(true);
		Mockito.when(mockVtDao.tokenIsAbsent(1, t1.getToken())).thenReturn(true);
		List<VoteToken> list = vs.getAllTokensForBallot(1);

		VoteToken tokenTwo = list.get(0);

		// log.debug(tokenTwo);

		boolean removed = vs.removeToken(1, tokenTwo);

		boolean isAbsent = vs.tokenIsAbsent(1, tokenTwo);

		assertTrue(removed);
		assertTrue(isAbsent);
	}

	/**
	 * Test removing a token
	 * 
	 * @throws Exception
	 */
	@Test
	void removeTokenTest() throws Exception {
		Mockito.when(mockVtDao.tokensForBallot(1)).thenReturn(ballotOneTokens);
		Mockito.when(mockVtDao.removeToken(1, t1.getToken())).thenReturn(true);
		List<VoteToken> list = vs.getAllTokensForBallot(1);

		VoteToken tokenTwo = list.get(0);

		if (vs.removeToken(1, tokenTwo)) {
			ballotOneTokens.remove(t1);
		}

		list = vs.getAllTokensForBallot(1);

		log.debug("the list contains: ");
		for (int i = 0; i < list.size(); i++) {
			log.debug(list.get(i).getAcId());
		}
		log.debug("the list is size: " + list.size());

		assertEquals(1, list.size());
	}

	/**
	 * Test Removing all tokens from a ballot
	 * 
	 * @throws Exception
	 */
	@Test
	void clearTokensForBallotTest() throws Exception {
		Mockito.when(mockVtDao.tokensForBallot(1)).thenReturn(new ArrayList<VoteToken>());
		vs.clearVoteTokens(1);

		List<VoteToken> list = vs.getAllTokensForBallot(1);

		assertTrue(list.isEmpty());
		Mockito.verify(mockVtDao, Mockito.times(1)).tokensForBallot(1);
	}

	/**
	 * Test cleaning up votes and tokens for a ballot
	 * 
	 * @throws Exception
	 */
	@Test
	void voteCleanUpTest() throws Exception {
		Mockito.when(mockVcDao.votesForBallot(1)).thenReturn(new ArrayList<VoteCast>());
		Mockito.when(mockVtDao.tokensForBallot(1)).thenReturn(new ArrayList<VoteToken>());
		vs.voteCleanUp(1);

		List<VoteToken> tokens = vs.getAllTokensForBallot(1);
		List<VoteCast> votes = vs.getAllVotes(1);

		assertTrue(tokens.isEmpty());
		assertTrue(votes.isEmpty());
		Mockito.verify(mockVcDao, Mockito.times(1)).votesForBallot(1);
		Mockito.verify(mockVtDao, Mockito.times(1)).tokensForBallot(1);
	}

	/**
	 * Test Removing all tokens from a ballot
	 * 
	 * @throws Exception
	 */
	@Test
	void clearVoteCastForBallotTest() throws Exception {
		// Mockito.when(mockVcDao.clearVotesForBallot(1)).thenReturn(true);
		Mockito.when(mockVcDao.votesForBallot(1)).thenReturn(new ArrayList<VoteCast>());
		boolean cleared = vs.clearVoteCast(1);

		List<VoteCast> list = vs.getAllVotes(1);

		assertTrue(list.isEmpty());
		assertTrue(cleared);
		Mockito.verify(mockVcDao, Mockito.times(1)).votesForBallot(1);
	}

	/**
	 * Test adding a new token
	 * 
	 * @throws Exception
	 */
	@Test
	void addTokenTest() throws Exception {
		VoteToken newToken = VoteToken.newToken(2, "7095095");
		ballotTwoTokens.add(newToken);
		Mockito.when(mockVtDao.tokensForBallot(2)).thenReturn(ballotTwoTokens);

		List<VoteToken> tokens = new ArrayList<VoteToken>();
		tokens.add(newToken);

		vs.addAllTokens(2, tokens);

		List<VoteToken> list = vs.getAllTokensForBallot(2);

		assertEquals(2, list.size());
	}

	/**
	 * Test adding multiple tokens
	 * 
	 * @throws Exception
	 */
	@Test
	void addAllTokensTest() throws Exception {
		VoteToken tokenOne = VoteToken.newToken(2, "7095095");
		VoteToken tokenTwo = VoteToken.newToken(2, "2323136");

		ballotTwoTokens.add(tokenOne);
		ballotTwoTokens.add(tokenTwo);
		Mockito.when(mockVtDao.tokensForBallot(2)).thenReturn(ballotTwoTokens);

		List<VoteToken> tokenList = new ArrayList<VoteToken>();
		tokenList.add(tokenOne);
		tokenList.add(tokenTwo);

		vs.addAllTokens(2, tokenList);

		List<VoteToken> list = vs.getAllTokensForBallot(2);

		assertEquals(3, list.size());
	}

	/**
	 * Test starting a vote
	 * 
	 * @throws Exception
	 */
	@Test
	void startRestartVoteTest() throws Exception {
		Mockito.when(mockFacultyDao.listAll()).thenReturn(fac);
		Mockito.when(mockVcDao.votesForBallot(1)).thenReturn(new ArrayList<VoteCast>());
		Mockito.when(mockVtDao.tokensForBallot(1)).thenReturn(new ArrayList<VoteToken>());
		vs.voteCleanUp(1);

		List<VoteToken> tokens = vs.getAllTokensForBallot(1);
		List<VoteCast> votes = vs.getAllVotes(1);

		assertTrue(tokens.isEmpty());
		assertTrue(votes.isEmpty());
		Mockito.verify(mockVcDao, Mockito.times(1)).votesForBallot(1);
		Mockito.verify(mockVtDao, Mockito.times(1)).tokensForBallot(1);

		List<VoteToken> results = vs.startRestartVote(b1);

		assertNotNull(results);
		assertEquals(2, results.size());
	}

	/**
	 * Test converting matching list of tokens and faculty to tokens
	 */
	@Test
	void convertFacultyToTokensTest() {
		List<VoteToken> results = vs.convertFacultyToTokens(ballotOneTokens, fac);

		assertEquals(2, results.size());
	}

	/**
	 * Test converting matching list of tokens and faculty to tokens with empty
	 * faculty
	 */
	@Test
	void convertFacultyToTokensEmptyFacultyTest() {
		List<VoteToken> results = vs.convertFacultyToTokens(ballotOneTokens, new ArrayList<Faculty>());

		assertEquals(0, results.size());
	}

	/**
	 * Test converting matching list of tokens and faculty to tokens with empty
	 * tokens
	 */
	@Test
	void convertFacultyToTokensEmptyTokensTest() {
		List<VoteToken> results = vs.convertFacultyToTokens(new ArrayList<VoteToken>(), fac);

		assertEquals(0, results.size());
	}

	/**
	 * Test converting list of tokens to a list of faculty
	 * 
	 * @throws Exception
	 */
	@Test
	void convertTokensToFacultyTest() throws Exception {
		Mockito.when(mockFacultyDao.findFacultyByID("5854836")).thenReturn(f1);
		Mockito.when(mockFacultyDao.findFacultyByID("4138778")).thenReturn(f2);

		List<Faculty> results = vs.convertTokensToFaculty(ballotOneTokens);

		assertEquals(2, results.size());
	}

	/**
	 * Test converting list of tokens to a list of faculty with an empty list
	 */
	@Test
	void convertTokensToFacultyEmptyTest() {
		List<Faculty> results = vs.convertTokensToFaculty(new ArrayList<VoteToken>());

		assertEquals(0, results.size());
	}

}
