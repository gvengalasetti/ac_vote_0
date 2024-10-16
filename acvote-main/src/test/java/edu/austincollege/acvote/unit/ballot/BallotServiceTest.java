package edu.austincollege.acvote.unit.ballot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
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

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.BallotService;
import edu.austincollege.acvote.ballot.dao.BallotDao;
import edu.austincollege.acvote.ballot.dao.BallotOptionDao;
import edu.austincollege.acvote.ballot.dao.InvalidBallotIdException;
import edu.austincollege.acvote.ballot.dao.InvalidOptionIdException;
import edu.austincollege.acvote.ballot.option.VoteOption;
import edu.austincollege.acvote.faculty.Faculty;
import edu.austincollege.acvote.vote.VoteCast;
import edu.austincollege.acvote.vote.VoteResult;
import edu.austincollege.acvote.vote.dao.VoteCastDao;

@RunWith(MockitoJUnitRunner.class)
class BallotServiceTest {

	@Rule
	public MockitoRule initRule = MockitoJUnit.rule();

	@Mock
	private BallotDao mockBallotDao;

	@Mock
	private BallotOptionDao mockOptionDao;
	
	@Mock
	private VoteCastDao mockVcDao;

	@Mock
	private Ballot mockBallot;

	@InjectMocks
	private BallotService bs = new BallotService();

	private Ballot b1 = new Ballot();
	private Ballot b2 = new Ballot();
	private Ballot b3 = new Ballot(); 
	private Ballot b1a;
	private Ballot b1b;

	private List<Ballot> ballots = new ArrayList<>();

	@BeforeEach
	void setUp() throws Exception {
		ArrayList<VoteOption> candidates = new ArrayList<>();
		candidates.add(new VoteOption("0629102", "Higgs", true));
		candidates.add(new VoteOption("0629104", "Block", true));

		b1 = new Ballot(1, "Coolest CS Professor", "Please drag and drop these candidates around!", "This is a description for ballot 1!", true, candidates, "IRV", 2, LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(20), "CW", 124);
		b1a = new Ballot(1, "Coolest CS Professor", "Please drag and drop these candidates around!", "This is a description for ballot 1!", false, new ArrayList<>(), "IRV", 2, LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(20), "CW", 124); 
		b1b = new Ballot(1, "Coolest CS Professor", "Please drag and drop these candidates around!", "This is a description for ballot 1!", false, new ArrayList<>(), "IRV", 2, LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(20), "CW", 10); 
		ballots.add(b1);

		candidates.add(new VoteOption("80085", "Rosenberg", true));
		
		b2 = new Ballot(2, "Which ones are CS Professors?", "Please drag and drop these candidates around!", "This is a description for ballot 2!", true, candidates, "IRV", 2, LocalDateTime.now().plusDays(50), LocalDateTime.now().plusDays(60), "CW", 7);
		ballots.add(b2);
		candidates = new ArrayList<>();

		candidates.add(new VoteOption("629102", "Higgs", true));
		candidates.add(new VoteOption("629105", "Not Higgs", true));
		candidates.add(new VoteOption("678392", "Nguyen", true));
		
		b3 = new Ballot(3, "This is a different example", "Please drag and drop these candidates around!", "This is a description for ballot 3!", true, candidates, "IRV", 2, LocalDateTime.now().plusDays(8), LocalDateTime.now().plusMonths(1), "CW", 7);
		ballots.add(b3);

		MockitoAnnotations.openMocks(this);
	}

	/*
	 * function to test if add ballot function works using the create ballot dao
	 * function
	 */
	@Test
	void addBallotTest() throws Exception {
		assertNotNull(mockBallotDao);
		Mockito.when(mockBallotDao.createBallot("title", "instructions", "description", true, null, "irv", 2,
				LocalDateTime.parse("2023-06-08T16:00:00"), LocalDateTime.parse("2023-07-08T16:00:00"), "SS", 0))
				.thenReturn(new Ballot(5, "title", "instructions", "description", true, null, "irv", 2,
						LocalDateTime.parse("2023-06-08T16:00:00"), LocalDateTime.parse("2023-07-08T16:00:00"), "SS", 0));

		int ballotId = bs.addBallot("title", "instructions", "description", true, null, "irv", 2,
				LocalDateTime.parse("2023-06-08T16:00:00"), LocalDateTime.parse("2023-07-08T16:00:00"), "SS", 0);
		assertEquals(5, ballotId);
	}

	@Test
	void addBallotTestNull() throws Exception {
		
		assertNotNull(mockBallotDao);
		Mockito.when(mockBallotDao.createBallot("title", "instructions", "description", true, null, "irv", 2,
				LocalDateTime.parse("2023-06-08T16:00:00"), LocalDateTime.parse("2023-07-08T16:00:00"), "SS", 0))
				.thenReturn(null);
		
		int ballotId = bs.addBallot("title", "instructions", "description", true, null, "irv", 2,
				LocalDateTime.parse("2023-06-08T16:00:00"), LocalDateTime.parse("2023-07-08T16:00:00"), "SS", 0);
		assertEquals(-1, ballotId);
	}
	
	@Test
	void addBallotTestException() throws Exception {
		
		assertNotNull(mockBallotDao);
		Mockito.when(mockBallotDao.createBallot("title", "instructions", "description", true, null, "irv", 2,
				LocalDateTime.parse("2023-06-08T16:00:00"), LocalDateTime.parse("2023-07-08T16:00:00"), "SS", 0))
				.thenThrow(new Exception("shorts"));
		
		int ballotId = bs.addBallot("title", "instructions", "description", true, null, "irv", 2,
				LocalDateTime.parse("2023-06-08T16:00:00"), LocalDateTime.parse("2023-07-08T16:00:00"), "SS", 0);
		assertEquals(-1, ballotId);
	}

	/*
	 * getBallots Tests
	 */

	@Test
	void getBallotsTestNormal() throws Exception {
		assertNotNull(mockBallotDao);

		Mockito.when(mockBallotDao.listAll()).thenReturn(ballots);

		List<Ballot> list = bs.getBallots();
		assertNotNull(list);
		assertEquals(ballots, list);
	}

	@Test
	void getBallotsTestEmptyList() throws Exception {
		assertNotNull(mockBallotDao);

		Mockito.when(mockBallotDao.listAll()).thenReturn(null);

		List<Ballot> list = bs.getBallots();
		assertNull(list);
		assertNotEquals(ballots, list);
	}

	@Test
	void getBallotsTestException() throws Exception {
		assertNotNull(mockBallotDao);

		Mockito.when(mockBallotDao.listAll()).thenThrow(new Exception("on no"));

		List<Ballot> list = bs.getBallots();
		assertNotNull(list);
		assertNotEquals(ballots, list);
	}

	/*
	 * getBallotByID Tests
	 */

	@Test
	void getBallotByIDTestNormal() throws Exception {
		assertNotNull(mockBallotDao);

		Mockito.when(mockBallotDao.getBallot(1)).thenReturn(b1);

		Ballot b = bs.getBallotById(1);
		assertNotNull(b);
		assertEquals(b1, b);
	}

	@Test
	void getBallotByIDTestInvalidID() throws Exception {
		assertNotNull(mockBallotDao);

		Mockito.when(mockBallotDao.getBallot(20)).thenReturn(null);

		Ballot b = bs.getBallotById(20);
		assertNull(b);
		assertNotEquals(b1, b);
	}
	
	@Test
	void getBallotByIDTestException() throws Exception {
		assertNotNull(mockBallotDao);
		
		Mockito.when(mockBallotDao.getBallot(20)).thenThrow(new Exception("fork"));
		
		Ballot b = bs.getBallotById(20);
		assertNull(b);
		assertNotEquals(b1, b);
	}

	/*
	 * updateBallot Tests
	 */
	
	/**
	 * test the update ballot function using the ballot dao update ballot function
	 * 
	 * @throws Exception
	 */
	@Test
	void updateBallotTest() throws Exception {
		assertNotNull(mockBallotDao);
		mockBallot = b1;
		Mockito.when(mockBallotDao.updateBallot(mockBallot.getId(), mockBallot.getTitle(), mockBallot.getInstructions(),
				mockBallot.getDescription(), mockBallot.isFacultyBased(),
				new ArrayList<VoteOption>(mockBallot.getOptions()), mockBallot.getTypeOfVote(),
				mockBallot.getOutcomes(), mockBallot.getStartTime(), mockBallot.getEndTime(), mockBallot.getVoters(),
				mockBallot.getTotalVotesExpected())).thenReturn(true);
		Mockito.when(mockBallotDao.getBallot(1)).thenReturn(mockBallot);
		Ballot resultingBallot = bs.updateBallot(1, mockBallot);

		assertEquals(mockBallot.getId(), resultingBallot.getId());
	}
	
	@Test
	void updateBallotTestException() throws Exception {
		assertNotNull(mockBallotDao);
		mockBallot = b1;
		Mockito.when(mockBallotDao.updateBallot(mockBallot.getId(), mockBallot.getTitle(), mockBallot.getInstructions(),
				mockBallot.getDescription(), mockBallot.isFacultyBased(),
				new ArrayList<VoteOption>(mockBallot.getOptions()), mockBallot.getTypeOfVote(),
				mockBallot.getOutcomes(), mockBallot.getStartTime(), mockBallot.getEndTime(), mockBallot.getVoters(),
				mockBallot.getTotalVotesExpected())).thenThrow(new Exception("what the fork"));
		
		Ballot result = bs.updateBallot(1, mockBallot);
		
		assertNull(result);
	}

	/**
	 * test the set ballot title function using the ballot dao update ballot
	 * function
	 * 
	 * @throws Exception
	 */
	@Test
	void setBallotTitleTest() throws Exception {
		assertNotNull(mockBallotDao);
		mockBallot = b1;
		Mockito.when(mockBallotDao.updateBallot(mockBallot.getId(), mockBallot.getTitle(), mockBallot.getInstructions(),
				mockBallot.getDescription(), mockBallot.isFacultyBased(),
				new ArrayList<VoteOption>(mockBallot.getOptions()), mockBallot.getTypeOfVote(),
				mockBallot.getOutcomes(), mockBallot.getStartTime(), mockBallot.getEndTime(), mockBallot.getVoters(),
				mockBallot.getTotalVotesExpected())).thenReturn(true);
		Mockito.when(mockBallotDao.getBallot(1)).thenReturn(mockBallot);
		Ballot resultingBallot = bs.setBallotTitle(1, "New Title");

		assertEquals("New Title", resultingBallot.getTitle());
	}

	/**
	 * test the set ballot description function using the ballot dao update ballot
	 * function
	 * 
	 * @throws Exception
	 */
	@Test
	void setBallotDescriptionTest() throws Exception {
		assertNotNull(mockBallotDao);
		mockBallot = b1;
		Mockito.when(mockBallotDao.updateBallot(mockBallot.getId(), mockBallot.getTitle(), mockBallot.getInstructions(),
				mockBallot.getDescription(), mockBallot.isFacultyBased(),
				new ArrayList<VoteOption>(mockBallot.getOptions()), mockBallot.getTypeOfVote(),
				mockBallot.getOutcomes(), mockBallot.getStartTime(), mockBallot.getEndTime(), mockBallot.getVoters(),
				mockBallot.getTotalVotesExpected())).thenReturn(true);
		Mockito.when(mockBallotDao.getBallot(1)).thenReturn(mockBallot);
		Ballot resultingBallot = bs.setBallotDescription(1, "New Description");

		assertEquals("New Description", resultingBallot.getDescription());
	}

	/**
	 * test the set ballot instructions function using the ballot dao update ballot
	 * function
	 * 
	 * @throws Exception
	 */
	@Test
	void setBallotInstructionsTest() throws Exception {
		assertNotNull(mockBallotDao);
		mockBallot = b1;
		Mockito.when(mockBallotDao.updateBallot(mockBallot.getId(), mockBallot.getTitle(), mockBallot.getInstructions(),
				mockBallot.getDescription(), mockBallot.isFacultyBased(),
				new ArrayList<VoteOption>(mockBallot.getOptions()), mockBallot.getTypeOfVote(),
				mockBallot.getOutcomes(), mockBallot.getStartTime(), mockBallot.getEndTime(), mockBallot.getVoters(),
				mockBallot.getTotalVotesExpected())).thenReturn(true);
		Mockito.when(mockBallotDao.getBallot(1)).thenReturn(mockBallot);
		Ballot resultingBallot = bs.setBallotInstructions(1, "New Instructions");

		assertEquals("New Instructions", resultingBallot.getInstructions());
	}

	/**
	 * test the set ballot isFacultyBased function using the ballot dao update
	 * ballot function
	 * 
	 * @throws Exception
	 */
	@Test
	void setBallotIsFacultyBasedTest() throws Exception {
		assertNotNull(mockBallotDao);
		mockBallot = b1;
		Mockito.when(mockBallotDao.updateBallot(mockBallot.getId(), mockBallot.getTitle(), mockBallot.getInstructions(),
				mockBallot.getDescription(), mockBallot.isFacultyBased(),
				new ArrayList<VoteOption>(mockBallot.getOptions()), mockBallot.getTypeOfVote(),
				mockBallot.getOutcomes(), mockBallot.getStartTime(), mockBallot.getEndTime(), mockBallot.getVoters(),
				mockBallot.getTotalVotesExpected())).thenReturn(true);
		Mockito.when(mockBallotDao.getBallot(1)).thenReturn(mockBallot);
		Ballot resultingBallot = bs.setBasis(1, false);

		assertEquals(false, resultingBallot.isFacultyBased());
	}

	/**
	 * test the set ballot type of vote function using the ballot dao update ballot
	 * function
	 * 
	 * @throws Exception
	 */
	@Test
	void setBallotTypeOfVoteTest() throws Exception {
		assertNotNull(mockBallotDao);
		mockBallot = b1;
		Mockito.when(mockBallotDao.updateBallot(mockBallot.getId(), mockBallot.getTitle(), mockBallot.getInstructions(),
				mockBallot.getDescription(), mockBallot.isFacultyBased(),
				new ArrayList<VoteOption>(mockBallot.getOptions()), mockBallot.getTypeOfVote(),
				mockBallot.getOutcomes(), mockBallot.getStartTime(), mockBallot.getEndTime(), mockBallot.getVoters(),
				mockBallot.getTotalVotesExpected())).thenReturn(true);
		Mockito.when(mockBallotDao.getBallot(1)).thenReturn(mockBallot);
		Ballot resultingBallot = bs.setBallotTypeOfVote(1, "Test");

		assertEquals("Test", resultingBallot.getTypeOfVote());
	}

	/**
	 * test the set ballot outcomes function using the ballot dao update ballot
	 * function
	 * 
	 * @throws Exception
	 */
	@Test
	void setBallotOutcomesTest() throws Exception {
		assertNotNull(mockBallotDao);
		mockBallot = b1;
		Mockito.when(mockBallotDao.updateBallot(mockBallot.getId(), mockBallot.getTitle(), mockBallot.getInstructions(),
				mockBallot.getDescription(), mockBallot.isFacultyBased(),
				new ArrayList<VoteOption>(mockBallot.getOptions()), mockBallot.getTypeOfVote(),
				mockBallot.getOutcomes(), mockBallot.getStartTime(), mockBallot.getEndTime(), mockBallot.getVoters(),
				mockBallot.getTotalVotesExpected())).thenReturn(true);
		Mockito.when(mockBallotDao.getBallot(1)).thenReturn(mockBallot);
		Ballot resultingBallot = bs.setBallotOutcomes(1, 10);

		assertEquals(10, resultingBallot.getOutcomes());
	}

	/**
	 * test the set ballot start time function using the ballot dao update ballot
	 * function
	 * 
	 * @throws Exception
	 */
	@Test
	void setBallotStartTimeTest() throws Exception {
		assertNotNull(mockBallotDao);
		mockBallot = b1;
		Mockito.when(mockBallotDao.updateBallot(mockBallot.getId(), mockBallot.getTitle(), mockBallot.getInstructions(),
				mockBallot.getDescription(), mockBallot.isFacultyBased(),
				new ArrayList<VoteOption>(mockBallot.getOptions()), mockBallot.getTypeOfVote(),
				mockBallot.getOutcomes(), mockBallot.getStartTime(), mockBallot.getEndTime(), mockBallot.getVoters(),
				mockBallot.getTotalVotesExpected())).thenReturn(true);
		Mockito.when(mockBallotDao.getBallot(1)).thenReturn(mockBallot);
		Ballot resultingBallot = bs.setBallotStartTime(1, LocalDateTime.parse("2023-01-08T16:00:00"));

		assertEquals(LocalDateTime.parse("2023-01-08T16:00:00"), resultingBallot.getStartTime());
	}

	/**
	 * test the set ballot end time function using the ballot dao update ballot
	 * function
	 * 
	 * @throws Exception
	 */
	@Test
	void setBallotEndTimeTest() throws Exception {
		assertNotNull(mockBallotDao);
		mockBallot = b1;
		Mockito.when(mockBallotDao.updateBallot(mockBallot.getId(), mockBallot.getTitle(), mockBallot.getInstructions(),
				mockBallot.getDescription(), mockBallot.isFacultyBased(),
				new ArrayList<VoteOption>(mockBallot.getOptions()), mockBallot.getTypeOfVote(),
				mockBallot.getOutcomes(), mockBallot.getStartTime(), mockBallot.getEndTime(), mockBallot.getVoters(),
				mockBallot.getTotalVotesExpected())).thenReturn(true);
		Mockito.when(mockBallotDao.getBallot(1)).thenReturn(mockBallot);
		Ballot resultingBallot = bs.setBallotEndTime(1, LocalDateTime.parse("2023-02-08T16:00:00"));

		assertEquals(LocalDateTime.parse("2023-02-08T16:00:00"), resultingBallot.getEndTime());
	}

	/**
	 * test the set ballot voting group function using the ballot dao update ballot
	 * function
	 * 
	 * @throws Exception
	 */
	@Test
	void setBallotVotingGroupTest() throws Exception {
		assertNotNull(mockBallotDao);
		mockBallot = b1;
		Mockito.when(mockBallotDao.updateBallot(mockBallot.getId(), mockBallot.getTitle(), mockBallot.getInstructions(),
				mockBallot.getDescription(), mockBallot.isFacultyBased(),
				new ArrayList<VoteOption>(mockBallot.getOptions()), mockBallot.getTypeOfVote(),
				mockBallot.getOutcomes(), mockBallot.getStartTime(), mockBallot.getEndTime(), mockBallot.getVoters(),
				mockBallot.getTotalVotesExpected())).thenReturn(true);
		Mockito.when(mockBallotDao.getBallot(1)).thenReturn(mockBallot);
		Ballot resultingBallot = bs.setBallotVotingGroup(1, "SC");

		assertEquals("SC", resultingBallot.getVoters());
	}

	@Test
	void convertFacultyToVoteOptionTest() throws Exception {
		Faculty f1 = new Faculty();
		Faculty f2 = new Faculty();

		f1.setAcId("100");
		f1.setFirstName("First");
		f1.setLastName("Faculty f1");

		f2.setAcId("101");
		f2.setFirstName("Second");
		f2.setLastName("Faculty f2");

		List<Faculty> fac = new ArrayList<Faculty>();
		fac.add(f1);
		fac.add(f2);

		ArrayList<VoteOption> options = new ArrayList<VoteOption>();
		options.add(new VoteOption("100", "First Faculty f1", true));
		options.add(new VoteOption("101", "Second Faculty f2", true));

		ArrayList<VoteOption> resultingVoteOptionList = bs.convertFacultytoVoteOptions(fac);

		assertEquals(options.get(0), resultingVoteOptionList.get(0));
		assertEquals(options.get(1), resultingVoteOptionList.get(1));
	}

	/*
	 * toggleOption Tests
	 */
	
	@Test
	void toggleOptionTest() throws Exception {
		assertNotNull(mockOptionDao);
		// Mockito.when(mockOptionDao.updateOptionOnBallot(1, "0",
		// false)).thenCallRealMethod((b1.getOptions().get(1).setEnabled(false));
		Mockito.when(mockOptionDao.updateOptionOnBallot(1, "0", false)).thenReturn(false);
		boolean optionChanged = bs.toggleOption(1, "0", false);
		// optionDao.updateOptionOnBallot(bid, oid, state);

		assertTrue(optionChanged);
	}
	
	@Test
	void toggleOptionTestException() throws Exception {
		assertNotNull(mockOptionDao);
		Mockito.when(mockOptionDao.updateOptionOnBallot(1, "0", false)).thenThrow(new Exception("darn it"));
		boolean optionChanged = bs.toggleOption(1, "0", false);
		
		assertFalse(optionChanged);
	}
	
	/*
	 * deleteOptionFromBallot Tests
	 */
	
	@Test
	void deleteOptionFromBallotTest() throws Exception {
		assertNotNull(mockOptionDao);
		Mockito.when(mockOptionDao.deleteOptionOnBallot(1, "0")).thenReturn(true);
		
		try {
			bs.deleteOptionFromBallot(1, "0");
		}
		catch (Exception e) {
			fail("no exception expected");
		}
	}
	
	@Test
	void deleteOptionFromBallotTestException() throws Exception {
		assertNotNull(mockOptionDao);
		Mockito.when(mockOptionDao.deleteOptionOnBallot(1, "0")).thenThrow(new Exception("for these shorts"));
		
		try {
			bs.deleteOptionFromBallot(1, "0");
			fail("Exception not thrown like expected");
		}
		catch (Exception e) {
			
		}
	}

	/*
	 * deleteBallot Tests
	 */

	@Test
	void deleteBallotTestNormal() throws Exception {
		assertNotNull(mockBallotDao);

		Mockito.when(mockBallotDao.deleteBallot(1)).thenReturn(true);

		assertTrue(bs.deleteBallot(1));
	}

	@Test
	void deleteBallotTestInvalidID() throws Exception {
		assertNotNull(mockBallotDao);

		Mockito.when(mockBallotDao.deleteBallot(20)).thenThrow(new Exception("bad"));

		assertFalse(bs.deleteBallot(20));
	}

	/*
	 * clearOptionsFromBallot Tests
	 */

	@Test
	void clearOptionsTestNormal() throws Exception {
		assertNotNull(mockOptionDao);

		Mockito.when(mockOptionDao.clearOptionsOnBallot(1)).thenReturn(true);

		assertTrue(bs.clearOptionsFromBallot(1));
	}

	@Test
	void clearOptionsTestInvalidBID() throws Exception {
		assertNotNull(mockOptionDao);

		Mockito.when(mockOptionDao.clearOptionsOnBallot(15)).thenThrow(new Exception("no"));

		assertFalse(bs.clearOptionsFromBallot(15));
	}
	
	@Test
	void resultsForBallot() throws Exception {
		assertNotNull(mockVcDao);
		
		Mockito.when(mockVcDao.votesForBallot(1)).thenReturn(new ArrayList<VoteCast>());
		
		VoteResult vr = bs.resultsForBallot(b1);
		
		assertNotNull(vr);
		assertEquals(vr.getBallot(), b1);
	}
	
	@Test
	void resultsForBallotException() throws Exception {
		assertNotNull(mockVcDao);
		
		Mockito.when(mockVcDao.votesForBallot(1)).thenThrow();
		
		VoteResult vr = bs.resultsForBallot(b1);
		
		assertNull(vr);
	}
	
	@Test
	void setVotesExpectedTest() throws Exception {
		assertNotNull(mockBallotDao);
		mockBallot = b1;
		
		Mockito.when(mockBallotDao.updateBallot(mockBallot.getId(), mockBallot.getTitle(), mockBallot.getInstructions(),
				mockBallot.getDescription(), mockBallot.isFacultyBased(),
				new ArrayList<VoteOption>(mockBallot.getOptions()), mockBallot.getTypeOfVote(),
				mockBallot.getOutcomes(), mockBallot.getStartTime(), mockBallot.getEndTime(), mockBallot.getVoters(), 10)).thenReturn(true);
		Mockito.when(mockBallotDao.getBallot(1)).thenReturn(b1b);
		
		Ballot b = bs.setVotesExpected(mockBallot, 10);
		
		assertNotNull(b);
		assertEquals(b.getTotalVotesExpected(), 10);
	}
	
	@Test
	void setVoteExpectedTestException() throws Exception {
		assertNotNull(mockBallotDao);
		mockBallot = b1;
		
		Mockito.when(mockBallotDao.updateBallot(mockBallot.getId(), mockBallot.getTitle(), mockBallot.getInstructions(),
				mockBallot.getDescription(), mockBallot.isFacultyBased(),
				new ArrayList<VoteOption>(mockBallot.getOptions()), mockBallot.getTypeOfVote(),
				mockBallot.getOutcomes(), mockBallot.getStartTime(), mockBallot.getEndTime(), mockBallot.getVoters(), 10)).thenThrow(new Exception ("shorts"));
		
		Ballot b = bs.setVotesExpected(mockBallot, 10);
		
		assertNull(b);
	}

	/*
	 * -----------------------------------------------------------------------------
	 * ------- If the service is asked to set the basis for a ballot whose id is
	 * invalid, should throw an exception.
	 */
	@Test
	public void test_setBasis_whenBallotIdNotValid() throws Exception {

		Mockito.when(mockBallotDao.getBallot(1)).thenReturn(null);

		Assertions.assertThrows(InvalidBallotIdException.class, () -> {

			bs.setBasis(1, true);

		}, "Invalid ballot id exception expected.");
		Mockito.verify(mockBallotDao, times(1)).getBallot(1);
	}

	/*
	 * -----------------------------------------------------------------------------
	 * ------- If we set the ballot's option, but is the same as it was, then the
	 * ballot should be updated (with no real change) and the options should remain.
	 */
	@Test
	public void test_setBasis_whenNewBasisDoesNotChange() throws Exception {

		Mockito.when(mockBallotDao.getBallot(1)).thenReturn(b1);
		Ballot bal = b1;

		ArrayList<VoteOption> options = (ArrayList<VoteOption>) bal.getOptions();
		
		Mockito.when(mockBallotDao.updateBallot(b1.getId(), bal.getTitle(), bal.getInstructions(), bal.getDescription(), 
				bal.isFacultyBased(), options, bal.getTypeOfVote(), bal.getOutcomes(), 
				bal.getStartTime(), bal.getEndTime(), 
				bal.getVoters(), bal.getTotalVotesExpected())).thenReturn(true);
		
		Ballot b = bs.setBasis(1, true);

		// should be faculty based
		assertTrue(b.isFacultyBased());

		// since it was already faculty based, we should see same options
		ArrayList<VoteOption> optionsAfter = (ArrayList<VoteOption>) b.getOptions();

		Assertions.assertArrayEquals(options.toArray(), optionsAfter.toArray());
		
		Mockito.verify(mockBallotDao, times(2)).getBallot(1);
	}

	/*
	 * -----------------------------------------------------------------------------
	 * ------- If we set the ballot option to a different basis (was faculty, now
	 * not) or visa versa, then we the facultyBased field should reflect this change
	 * AND any existing options should be removed....because the basis has changed.
	 */
	@Test
	public void test_setBasis_whenNewBasisChanges() throws Exception {

		Mockito.when(mockBallotDao.getBallot(1)).thenReturn(b1).thenReturn(b1a);
		Ballot bal = b1;

		ArrayList<VoteOption> options = (ArrayList<VoteOption>) bal.getOptions();

		Mockito.when(mockBallotDao.updateBallot(b1.getId(), bal.getTitle(), bal.getInstructions(), bal.getDescription(),
				bal.isFacultyBased(), options, bal.getTypeOfVote(), bal.getOutcomes(), bal.getStartTime(),
				bal.getEndTime(), bal.getVoters(), bal.getTotalVotesExpected()))
				.thenReturn(true);

		Ballot b = bs.setBasis(1, false);

		// should be faculty based
		assertFalse(b.isFacultyBased());

		// since it was already faculty based, we should see same options
		ArrayList<VoteOption> optionsAfter = (ArrayList<VoteOption>) b.getOptions();

		assertTrue(optionsAfter.isEmpty());
		
		Mockito.verify(mockBallotDao, times(2)).getBallot(1);
		
	}

	/*
	 * -----------------------------------------------------------------------------
	 * ------- When adding 2 distinct options, everything should be fine, the
	 * service should tell the dao to add a voting options 2 times.
	 * 
	 */
	@Test
	public void test_addOptions_whenNoDuplicates() throws Exception {

		ArrayList<VoteOption> newOpts = new ArrayList<VoteOption>();

		VoteOption vo1 = new VoteOption("o1", "option 1", true);
		VoteOption vo2 = new VoteOption("o2", "option 2", true);

		newOpts.add(vo1);
		newOpts.add(vo2);

		Mockito.doNothing().when(mockOptionDao).addOptionOnBallot(1, vo1);
		Mockito.doNothing().when(mockOptionDao).addOptionOnBallot(1, vo2);

		// ready to test...

		bs.addBallotOptions(1, newOpts);

		// make sure the dao was told to add both voting options
		Mockito.verify(mockOptionDao).addOptionOnBallot(1, vo1);
		Mockito.verify(mockOptionDao).addOptionOnBallot(1, vo2);
	}

	/*
	 * -----------------------------------------------------------------------------
	 * ------- When adding voting option with a duplicate id, then our service
	 * should deal with the exception thrown by our dao.
	 */
	@Test
	public void test_addOptions_whenDuplicateId() throws Exception {

		ArrayList<VoteOption> newOpts = new ArrayList<VoteOption>();

		VoteOption vo1 = new VoteOption("o1", "option 1", true);
		VoteOption vo2 = new VoteOption("o1", "option 1 duplicate", true);

		newOpts.add(vo1);
		newOpts.add(vo2);

		Mockito.doNothing().when(mockOptionDao).addOptionOnBallot(1, vo1);
		Mockito.doThrow(InvalidOptionIdException.class).when(mockOptionDao).addOptionOnBallot(1, vo2);

		// ready to test... duplicate optionn id should throw an exception
		Assertions.assertThrows(InvalidOptionIdException.class, () -> {

			bs.addBallotOptions(1, newOpts);

		}, "Invalid option id exception expected.");

		// make sure the dao was told to add both voting options
		Mockito.verify(mockOptionDao).addOptionOnBallot(1, vo1);
		Mockito.verify(mockOptionDao).addOptionOnBallot(1, vo2);
	}

}
