package edu.austincollege.acvote.unit.vote;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.dao.BallotDao;
import edu.austincollege.acvote.vote.Votes;
import edu.austincollege.acvote.vote.dao.VoteCastDao;

public class VotesTest {
	
	@Rule
	public MockitoRule initRule = MockitoJUnit.rule();
	
	@Mock
	BallotDao bDao;
	@Mock
	VoteCastDao vDao;
	
	@InjectMocks
	Votes votes;
	
	Ballot b1;
	
	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
		b1 = new Ballot();
	}
	
	/**
	 * test finding how many votes have been received
	 * @throws Exception
	 */
	@Test
	void votesRecivedTest() throws Exception
	{
		Mockito.when(vDao.voteCountForBallot(1)).thenReturn(3);
		int result = votes.votesReceived(1);
		assertEquals(3, result);
	}
	
	/**
	 * test finding the expected votes
	 * @throws Exception
	 */
	@Test
	void votesExpectedTest() throws Exception
	{
		b1.setTotalVotesExpected(5);
		Mockito.when(bDao.getBallot(1)).thenReturn(b1);
		int result = votes.votesExpected(1);
		assertEquals(5, result);
	}
	
	/**
	 * test finding the percentage of votes received out of votes expected
	 * @throws Exception
	 */
	@Test
	void votesPercentageTest() throws Exception
	{Mockito.when(vDao.voteCountForBallot(1)).thenReturn(3);
		b1.setTotalVotesExpected(5);
		Mockito.when(bDao.getBallot(1)).thenReturn(b1);
		int result = votes.votesPercentage(1);
		assertEquals(60, result);
	}

}
