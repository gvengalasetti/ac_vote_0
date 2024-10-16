package edu.austincollege.acvote.ballot.dao;

import java.util.List;

import edu.austincollege.acvote.ballot.option.VoteOption;

public interface BallotOptionDao {
	

	/**
	 * Checks the specified ballot id to make sure it is valid;
	 * 
	 * @param bid
	 * @return true when ballot id specified is present in datastore; false otherwise
	 */
	public boolean validBallotId(int bid) throws Exception;
		
		
	/**
	 * Creates an option in our datastore for the associated ballot.
	 * 
	 * @param bid integer id of ballot (assigned by db)
	 * @param oid string unique within the ballot
	 * @param olabel human friendly display string
	 * @param oEnabled boolean indicating state of option
	 * @return
	 * @throws Exception
	 */
	public VoteOption createOption( int bid, String oid, String olabel, Boolean oEnabled) throws Exception;
	
	
	/**
	 * Finds or fetches an option associated with the specific ballot specified by bid.
	 * 
	 * @param bid integer id of ballot (assigned by db)
	 * @param oid string unique within the ballot
	 * @return
	 * @throws Exception
	 */
	public VoteOption option(int bid, String oid) throws Exception;
	
	/**
	 * Fetches all options on the specified Ballot
	 * @param bid integer id of ballot (assigned by db)
	 * @throws Exception
	 */
	public List<VoteOption> allOptionsOnBallot(int bid) throws Exception;
	
	/**
	 * Clears all options on the specified Ballot
	 * @param bid integer id of ballot (assigned by db)
	 * @throws Exception
	 */
	public boolean clearOptionsOnBallot(int bid) throws Exception;
	
	/**
	 * Add and option to the specified ballot if it does not exist.
	 * 
	 * @param bid integer id of ballot (assigned by db)
	 * @param opt string unique within the ballot
	 */
	public void addOptionOnBallot(int bid, VoteOption opt) throws Exception;
	
	
	/**
	 * Finds and removes the specified option from the specified ballot.
	 * 
	 * @param bid integer id of ballot (assigned by db)
	 * @param oid string unique within the ballot
	 */
	public boolean deleteOptionOnBallot(int bid, String oid) throws Exception;
	
	/**
	 * Updates values of specified option from specified ballot with parameters
	 * 
	 * @param bid
	 * @param oid
	 * @param oLabel
	 * @param enabled
	 * @return Updated option
	 * @throws Exception if updaten't
	 */
	public boolean updateOptionOnBallot(int bid, String oid, boolean enabled) throws Exception;

}
