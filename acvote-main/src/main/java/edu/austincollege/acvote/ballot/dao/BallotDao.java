package edu.austincollege.acvote.ballot.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.option.VoteOption;

/**
 * <h1>Blueprint for Ballot Dao Objects</h1>
 * 
 * <p>
 * Describes capabilities of all Ballot Daos: handles CRUD operations pertaining to ballots 
 * </p>
 *
 */
public interface BallotDao {

	/**
	 * Lists all ballots
	 * 
	 * @return list of all ballots
	 * @throws Exception when unable to list ballots
	 */
	public List<Ballot> listAll() throws Exception;

	/**
	 * Creates a new ballot
	 * 
	 * @param title
	 * @param instructions
	 * @param description
	 * @param basis
	 * @param options
	 * @param typeOfVote
	 * @param outcomes
	 * @param startTime
	 * @param endTime
	 * @param voters
	 * @param votesRecieved
	 * @param totalVotesExpected
	 * @return new Ballot
	 * @throws Exception when unable to create ballot
	 */
	public Ballot createBallot(String title, String instructions, String description, boolean basis,
			ArrayList<VoteOption> options, String typeOfVote, Integer outcomes, LocalDateTime startTime,
			LocalDateTime endTime, String voters, int totalVotesExpected)
			throws Exception;

	/**
	 * Deletes ballot with matching id from database
	 * 
	 * @param bid
	 * @return true if deleted
	 * @throws Exception when unable to delete ballot
	 */
	public boolean deleteBallot(int bid) throws Exception;

	/**
	 * Updates a specified ballot
	 * 
	 * @param bid
	 * @param title
	 * @param instructions
	 * @param description
	 * @param basis
	 * @param options
	 * @param typeOfVote
	 * @param outcomes
	 * @param startTime
	 * @param endTime
	 * @param voters
	 * @param votesRecieved
	 * @param totalVotesExpected
	 * @return true if updated
	 * @throws Exception when unable to update ballot
	 */
	public boolean updateBallot(int bid, String title, String instructions, String description, boolean basis,
			ArrayList<VoteOption> options, String typeOfVote, Integer outcomes, LocalDateTime startTime,
			LocalDateTime endTime, String voters, int totalVotesExpected)
			throws Exception;

	/**
	 * Finds matching ballot
	 * 
	 * @param bid
	 * @return Ballot matching ballot
	 * @throws Exception when unable to get ballot
	 */
	public Ballot getBallot(int bid) throws Exception;

}
