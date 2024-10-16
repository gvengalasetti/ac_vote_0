package edu.austincollege.acvote.vote.dao;

import java.util.List;

import edu.austincollege.acvote.vote.VoteToken;

public interface VoteTokenDao {

	/**
	 * Used to return the REMAINING tokens for a specific ballot.   We can use
	 * this method to send reminder emails.
	 * 
	 * @param bid the ballot id
	 * @return list of VoteToken instances
	 * @throws Exception
	 */
	public List<VoteToken> tokensForBallot(Integer bid) throws Exception;
		
	/**
	 * Answers true when a specific token is absent from the data store, indicating
	 * that the token has already been consumed/used.  We can use this method to 
	 * assert that the token provided is still valid and the vote is still eligible 
	 * to vote.
	 * 
	 * @param bid id of the ballot
	 * @param tok token carried by the voter
	 * @return true if token has been used and should be rejected 
	 * @throws Exception
	 */
	public boolean tokenIsAbsent(Integer bid, String tok) throws Exception;
	
	/**
	 * Removes a token from the data store. When the user votes, we remove the token
	 * from the persistent data store, the token is no longer valid.
	 * 
	 * @param bid id of the ballot
	 * @param tok token carried by the voter
	 * @return true if token is removed; false otherwise
	 * @throws Exception
	 */
	public boolean removeToken(Integer bid, String tok) throws Exception;
	
	/**
	 * Adds a token to the data store for the specific ballot. We use this to populate
	 * the store of tokens representing specific voters for the specified ballot.  
	 * 
	 * @param bid id of the ballot
	 * @param vt voting token generated for the voter for the ballot to be saved
	 * @return returns true if token has been added; false otherwise
	 * 
	 * @throws Exception
	 */
	public boolean addToken(Integer bid, VoteToken vt) throws Exception;
	
	
	/**
	 * Clears all tokens stored for a specific ballot, essentially not allowing any
	 * more voters to vote.    This is used to restart or finish a vote.  
	 * 
	 * @param bid id of the ballot
	 * @throws Exception
	 */
	public void clearTokensForBallot(Integer bid) throws Exception;
	
	/**
	 * A convenience method for storing many generated tokens for a ballot.
	 *  
	 * @param bid id of the ballot
	 * @param toks a list of VoteToken instances generated for each voter.
	 * 
	 * @return true if successful, false otherwise
	 * @throws Exception
	 */
	public boolean addAllTokens(Integer bid, List<VoteToken> toks) throws Exception;
	
}
