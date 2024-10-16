package edu.austincollege.acvote.unit.vote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import edu.austincollege.acvote.ballot.dao.InvalidBallotIdException;
import edu.austincollege.acvote.vote.VoteToken;
import edu.austincollege.acvote.vote.dao.DummyVoteTokenDao;
import edu.austincollege.acvote.vote.dao.JdbcTemplateVoteTokenDao;
import edu.austincollege.acvote.vote.dao.VoteTokenDao;

class VoteTokenDaoTests {

	VoteTokenDao dao;
	
	@BeforeEach
	void setup(TestInfo testInfo) throws Exception  {
		System.out.println("\n --- "+testInfo.getDisplayName());		
		dao = new DummyVoteTokenDao();

		
		//dumpTokens(1);
		//System.err.println("---");			
	}
	
	@Test
	public void test_votesForBallot() throws Exception {
		dumpTokens(1);
		
	}

	private void dumpTokens(Integer bid) throws Exception {

		List<VoteToken> tokens = dao.tokensForBallot(bid);
		System.out.println("tokens for ballot "+bid);
		for (VoteToken vt : tokens) {
			System.out.println(vt);
		}
		System.out.println();
		
	}

	@Test
	public void test_votesForBallot_whenInvalidBallotId() throws Exception {

		Assertions.assertThrows(InvalidBallotIdException.class, () -> {
			List<VoteToken> tokens = dao.tokensForBallot(-1);
		}, "expected a invalid ballot id exception");
			
	}
	
	
	@Test
	public void test_tokenIsAbsent() throws Exception {

		assertTrue ( dao.tokenIsAbsent(1, "xyzzy"));  // token should not be there...ie, used. 
	}
	

	@Test
	public void test_removeToken() throws Exception {

		List<VoteToken> tokens = dao.tokensForBallot(1);
		
		VoteToken vt = tokens.get(1);   // choose a token
		String tok = vt.getToken();   
		
		dao.removeToken(1, tok);

		// make sure none of the remaining tokens match our choice
		for (VoteToken current : dao.tokensForBallot(1)) {
			System.out.println("checking "+current);
			assertFalse(current.getToken().equals(tok));
		}
		
		
	}
	
	
	@Test
	public void test_addToken() throws Exception {

		List<VoteToken> tokens = dao.tokensForBallot(1);
		int n = tokens.size();
		
		dao.addToken(1, VoteToken.newToken(1, "1234567"));  // token for acid=123456 for ballot 1
		
		tokens = dao.tokensForBallot(1);
		dumpTokens(1);
		
		assertEquals(n+1, tokens.size());
		
		
	}
	
	/*

	public boolean addAllTokens(Integer bid, List<VoteToken> toks) throws Exception;
	 */

	@Test
	public void test_clearTokensForBallot() throws Exception {

		
		dao.clearTokensForBallot(1);
		
		Assertions.assertThrows(InvalidBallotIdException.class, () -> {
			dao.tokensForBallot(1);
		}, "tokens for ballot 1 should be all gone, causing exception");

	}
	
	@Test
	public void test_addAllTokens() throws Exception {
		
		List<VoteToken> newbies = new ArrayList<>();
		Collections.addAll(newbies, 
				VoteToken.newToken(1, "one", LocalDateTime.of(2023, 6, 8, 1, 0)),
				VoteToken.newToken(1, "two", LocalDateTime.of(2023, 6, 8, 2, 0)),
				VoteToken.newToken(1, "three", LocalDateTime.of(2023, 6, 8, 3, 0))
				);
		
		
		assertEquals(4, dao.tokensForBallot(1).size());
		dao.addAllTokens(1, newbies);
		dumpTokens(1);
		
		assertEquals(7, dao.tokensForBallot(1).size());

		
	}

	
}
