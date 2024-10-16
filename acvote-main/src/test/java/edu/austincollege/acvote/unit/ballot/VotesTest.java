package edu.austincollege.acvote.unit.ballot;

import static org.junit.jupiter.api.Assertions.*;

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
import org.springframework.boot.test.mock.mockito.MockBean;

import edu.austincollege.acvote.ballot.dao.BallotDao;
import edu.austincollege.acvote.vote.Votes;
import edu.austincollege.acvote.vote.dao.VoteCastDao;
import edu.austincollege.acvote.ballot.Ballot;

@RunWith(MockitoJUnitRunner.class)
class VotesTest {
	
	@Rule
	public MockitoRule initRule = MockitoJUnit.rule();
	@Mock
	private VoteCastDao mockVCDao;
	@Mock
	private BallotDao mockBallotDao;
	@InjectMocks
	private Votes voteGuy = new Votes();
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		
		Ballot test = new Ballot();
	}

	@Test
	void testVotesExpected() throws Exception {
		Ballot test = new Ballot();
		Mockito.when(mockBallotDao.getBallot(0)).thenReturn(test);
		
		test.setTotalVotesExpected(100);
		
		assertEquals(100, voteGuy.votesExpected(0));
	}
	@Test
	void testVotesReceived() throws Exception {
	
		Mockito.when(mockVCDao.voteCountForBallot(0)).thenReturn(10);
		

		
		assertEquals(10, voteGuy.votesReceived(0));
	}

	
	
	@Test
	void testPercentage() throws Exception {
	
		Mockito.when(mockVCDao.voteCountForBallot(0)).thenReturn(10);
		Ballot test = new Ballot();
		Mockito.when(mockBallotDao.getBallot(0)).thenReturn(test);
		
		test.setTotalVotesExpected(100);

		
		assertEquals(10, voteGuy.votesPercentage(0));
	}
	@Test
	void testPercentageZeroCase() throws Exception {
	
		Mockito.when(mockVCDao.voteCountForBallot(0)).thenReturn(10);
		Ballot test = new Ballot();
		Mockito.when(mockBallotDao.getBallot(0)).thenReturn(test);
		
		test.setTotalVotesExpected(0);

		
		assertEquals(1, voteGuy.votesPercentage(0));
	}
}
