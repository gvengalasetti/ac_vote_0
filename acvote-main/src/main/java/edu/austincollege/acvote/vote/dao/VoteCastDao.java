package edu.austincollege.acvote.vote.dao;

import java.util.List;

import edu.austincollege.acvote.vote.VoteCast;

public interface VoteCastDao {

	
	/**
	 * Returns a list of VoteCast objects for the specified ballot derived
	 * from our data store.
	 * 
	 * @param bid integer identifier for the ballot
	 * @return corresponding votes cast
	 * @throws Exception
	 */
	public List<VoteCast> votesForBallot(Integer bid) throws Exception;
	
	/**
	 * Saves/Persists the vote cast by a voter into the data store.
	 * 
	 * @param vote
	 * @throws Exception
	 */
	public void castVote(VoteCast vote) throws Exception;
	/**
	 * returns the amount of votes expected 
	 * @param bid
	 * @return
	 * @throws Exception
	 */

	public int voteCountForBallot(Integer bid)throws Exception;
	/**
	 * Deletes all VoteCast objects for bid from data store.
	 * 
	 * @param bid
	 * @throws Exception
	 */
	public void clearVotesForBallot(Integer bid) throws Exception;

}
