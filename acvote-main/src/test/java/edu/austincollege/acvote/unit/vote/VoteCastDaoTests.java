package edu.austincollege.acvote.unit.vote;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.austincollege.acvote.ballot.dao.InvalidBallotIdException;
import edu.austincollege.acvote.vote.VoteCast;
import edu.austincollege.acvote.vote.dao.DummyVoteCastDao;
import edu.austincollege.acvote.vote.dao.DuplicateVotingTokenException;
import edu.austincollege.acvote.vote.dao.VoteCastDao;

class VoteCastDaoTests {

	VoteCastDao dao;
	
	@BeforeEach
	void test() throws Exception  {
		dao = new DummyVoteCastDao();

	}
	
	@Test
	public void test_votesForBallot() throws Exception {
		List<VoteCast> votes = dao.votesForBallot(1);
		for (VoteCast vc : votes) {
			System.out.println(vc);
		}
			
	}

	@Test
	public void test_votesForBallot_whenInvalidBallotId() throws Exception {
		
		Assertions.assertThrows(InvalidBallotIdException.class, () -> {
			List<VoteCast> votes = dao.votesForBallot(-1);	
		}, "expected a invalid ballot id exception");
			
	}
	

	@Test
	public void test_castVote_whenTokenUnique() throws Exception {
		List<VoteCast> beforeVotes = dao.votesForBallot(3);
		for (VoteCast vc : beforeVotes) {
			System.out.println(vc);
		}
		
		System.out.println();
		System.out.println();
		
		VoteCast vv = new VoteCast(3,"xyzzy", "ip","js","mt","th");
		dao.castVote(vv);
		
		
		List<VoteCast> afterVotes = dao.votesForBallot(3);
		for (VoteCast vc : afterVotes) {
			System.out.println(vc);
		}
		
		assertEquals(beforeVotes.size()+1,afterVotes.size());
			
	}
	
	@Test
	public void test_castVote_whenTokenDuplicated() throws Exception {

		Assertions.assertThrows(DuplicateVotingTokenException.class, () -> {
			VoteCast vv = new VoteCast(3,"t01", "ip","js","mt","th");
			dao.castVote(vv);
		});		

		

	}
	
}
