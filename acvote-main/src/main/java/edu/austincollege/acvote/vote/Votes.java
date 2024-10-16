package edu.austincollege.acvote.vote;

import org.springframework.beans.factory.annotation.Autowired;

import edu.austincollege.acvote.ballot.dao.BallotDao;
import edu.austincollege.acvote.vote.dao.VoteCastDao;

/**
 * helper object for passing along votes cast information
 * 
 * @author Kieran Leahy
 *
 */
public class Votes {


	public Votes() {

	}

	@Autowired VoteCastDao vcDao;
	@Autowired BallotDao ballotDao;
	

	public int votesReceived(int bid) throws Exception {
		// TODO the vote cast dao should be prompted for the number of votesCasts in the
		// table.

		return vcDao.voteCountForBallot(bid);

	}

	public int votesExpected(int bid) throws Exception {

		// TODO the ballot dao should be prompted for the expected number of votes for
		// ballot bid.

		return ballotDao.getBallot(bid).getTotalVotesExpected();
	}

	public int votesPercentage(int bid) throws Exception {
		int out=0;
		if (votesExpected(bid) == 0) {
			return out = 1;
		}
	 out = (100 * (this.votesReceived(bid)) / (votesExpected(bid)));
	
		return out;
	}

}
