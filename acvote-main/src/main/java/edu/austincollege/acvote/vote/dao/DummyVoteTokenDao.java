package edu.austincollege.acvote.vote.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.austincollege.acvote.ballot.dao.InvalidBallotIdException;
import edu.austincollege.acvote.vote.VoteToken;

public class DummyVoteTokenDao implements VoteTokenDao {

	private final static List<VoteToken> tokens = new ArrayList<>();
	
	{
		tokens.clear();
		tokens.add(VoteToken.newToken(1, "3594249", LocalDateTime.of(2023, 6, 8, 16, 0)));
		tokens.add(VoteToken.newToken(1, "1299808", LocalDateTime.of(2023, 6, 8, 16, 0)));
		tokens.add(VoteToken.newToken(1, "3931528", LocalDateTime.of(2023, 6, 8, 16, 0)));
		tokens.add(VoteToken.newToken(1, "4580739", LocalDateTime.of(2023, 6, 8, 16, 0)));
		
		tokens.add(VoteToken.newToken(3, "0298799", LocalDateTime.of(2025, 12, 31, 23, 0)));
		tokens.add(VoteToken.newToken(3, "4443407", LocalDateTime.of(2025, 12, 31, 23, 0)));
		
	}
	
	
	public DummyVoteTokenDao() {
		// TODO Auto-generated constructor stub
	}

	private void assertValidBallot(Integer bid) throws InvalidBallotIdException {
		boolean ballotFound = false;
		for (VoteToken vt : tokens) {
			if (vt.getBid().equals(bid)) {
				ballotFound = true;
				break;
			}
		}
		
		if (!ballotFound) throw new InvalidBallotIdException(bid+" is not valid");
	}
	
	@Override
	public List<VoteToken> tokensForBallot(Integer bid) throws Exception {

		this.assertValidBallot(bid);
		
		List<VoteToken> results = new ArrayList<>();
		for (VoteToken vt : tokens) {
			if (vt.getBid().equals(bid)) results.add(vt);
		}
		
		return results;
	}

	/**
	 * token is used when the token is missing from the data store (ie, no match found)
	 */
	@Override
	public boolean tokenIsAbsent(Integer bid, String tok) throws Exception {
		
		this.assertValidBallot(bid);
		
		for (VoteToken vt : tokens) {
			if (vt.getBid().equals(bid)) 
				if (vt.getToken().equals(tok)) return false;
		}
		return true;
	}

	/**
	 * When a matching token for the current ballot is found, remove it from
	 * the store and return success indicator (true).   If no match found, return false;
	 */
	@Override
	public boolean removeToken(Integer bid, String tok) throws Exception {
		
		this.assertValidBallot(bid);
		
		for (VoteToken vt : tokens) {
			if (vt.getBid().equals(bid)) 
				if (vt.getToken().equals(tok)) {
					tokens.remove(vt);
					return true;
				}
		}

		return false;
	}

	@Override
	public boolean addToken(Integer bid, VoteToken newt) throws Exception {
		this.assertValidBallot(bid);

		if (this.tokenIsAbsent(bid, newt.getToken())) {
			tokens.add(newt);
		} else {
			throw new DuplicateVotingTokenException("ballot "+bid+": duplicate token="+newt.getToken());
		}
		
		return true;
	}

	@Override
	public void clearTokensForBallot(Integer bid) throws Exception {
		this.assertValidBallot(bid);
		
		List<VoteToken> balToks = this.tokensForBallot(bid);
		for (VoteToken vt : balToks) {
			this.removeToken(bid, vt.getToken());
		}
	}

	@Override
	public boolean addAllTokens(Integer bid, List<VoteToken> toks) throws Exception {
		this.assertValidBallot(bid);
		
		for (VoteToken vt : toks) {
			this.addToken(bid, vt);
		}
		return true;
	}

}
