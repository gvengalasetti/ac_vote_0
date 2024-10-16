package edu.austincollege.acvote.vote;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.dao.BallotDao;
import edu.austincollege.acvote.faculty.dao.FacultyDAO;
import edu.austincollege.acvote.faculty.Faculty;
import edu.austincollege.acvote.vote.dao.VoteCastDao;
import edu.austincollege.acvote.vote.dao.VoteTokenDao;

@Service
public class VoteService {

	private static Logger log = LoggerFactory.getLogger(VoteService.class);
	
	@Autowired
	VoteCastDao vcDao;
	
	@Autowired
	VoteTokenDao vtDao;
	
	@Autowired
	BallotDao ballotDao;
	
	@Autowired
	FacultyDAO facultyDao;
	
	public VoteService() {
		
	}
	
	/**
	 * Returns all vote cast objects related to a particular ballot
	 * 
	 * @param bid of ballot in question
	 * @return List of all vote cast objects related to ballot
	 * @throws Exception if there is a problem with retrieving votes
	 */
	public List<VoteCast> getAllVotes(Integer bid) throws Exception {
		
		List<VoteCast> votes = new ArrayList<>();
		
		try {
			//querying for all related vote casts and returning
			votes = vcDao.votesForBallot(bid);
			return votes;
		}
		catch(Exception e) {
			//handling problems
			log.warn(e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Creates and adds to database a new vote cast object with the parameters as its attributes
	 * Also removes token from db on success
	 * 
	 * @param bid
	 * @param rankings
	 * @throws Exception 
	 */
	public void castVote(Integer bid, String token, List<String> rankings) throws Exception {
		
		log.debug("casting vote for ballot #{}", bid);
		
		VoteCast v = new VoteCast(bid, token, rankings);
		
		try {
			//checking that ballot exists
			Ballot b = ballotDao.getBallot(bid);
			
			//checking that ballot is open
			if(b.getEndTime().compareTo(LocalDateTime.now()) < 0) {
				throw new Exception("ballot closed");
			}
			
			//checking that token in valid
			if(vtDao.tokenIsAbsent(bid, token)) {
				throw new InvalidTokenException("token has been used");
			}
			
			//casting vote
			List<VoteCast> votes = vcDao.votesForBallot(bid);
			log.debug(votes.toString());
			vcDao.castVote(v);
			List<VoteCast> newVotes = vcDao.votesForBallot(bid);
			log.debug(newVotes.toString());
			
			//removing token
			vtDao.removeToken(bid, token);
			
		} 
		catch (InvalidTokenException e) {
			
			log.warn("bad token:{}", e.getMessage());
			throw e;
		}
		catch (Exception e) {
			
			log.warn("could not cast vote for ballot #{}:{}", bid, e.getMessage());
			throw e;
		}
		
	}
	
	/**
	 * Method deletes all vote tokens for the ballot with matching bid
	 * @param bid
	 */
	public void clearVoteTokens(int bid)
	{
		try {
			vtDao.clearTokensForBallot(bid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method creates and adds a vote token to the data store then returns the new token
	 * @param bid
	 * @param fac
	 * @return VoteToken
	 */
	public VoteToken createVoteToken(int bid, Faculty fac)
	{
		VoteToken token = VoteToken.newToken(bid, fac.getAcId());
		try {
			//log.debug("adding token: " + token.getToken() + " for " + fac.getFirstName() + " " + fac.getLastName());
			vtDao.addToken(bid, token);
			return token;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Method gets all the vote tokens from the data store and returns them
	 * @param bid
	 * @return List<VoteToken>
	 */
	public List<VoteToken> getAllTokensForBallot(int bid)
	{
		try {
			return vtDao.tokensForBallot(bid);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Method looks for the token object in the data store and returns true if it is not found
	 * @param bid
	 * @param token
	 * @return
	 */
	public boolean tokenIsAbsent(int bid, VoteToken token)
	{
		try {
			return vtDao.tokenIsAbsent(bid, token.getToken());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Method removes the token object from the data store
	 * @param bid
	 * @param token
	 * @return boolean
	 */
	public boolean removeToken(int bid, VoteToken token)
	{
		try {
			return vtDao.removeToken(bid, token.getToken());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Method adds the list of tokens to the ballot that matches the given ballot
	 * @param bid
	 * @param tokens
	 * @return boolean
	 */
	public boolean addAllTokens(int bid, List<VoteToken> tokens)
	{
		try {
			return vtDao.addAllTokens(bid, tokens);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Method removes all vote cast from ballot with given bid
	 * @param bid
	 * @return boolean
	 */
	public boolean clearVoteCast(int bid)
	{
		try {
			vcDao.clearVotesForBallot(bid);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Method finds the token with matching acid then returns true if it exist
	 * @param bid
	 * @param acid
	 * @return
	 */
	public boolean facultyHasToken(int bid, String acid)
	{
		List<VoteToken> list = getAllTokensForBallot(bid);
		
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i).getAcId().equals(acid))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Method finds the token based on acid and attempts to remove it the returns its success or failure
	 * @param bid
	 * @param acid
	 * @return boolean
	 */
	public boolean removeAcidToken(int bid, String acid)
	{
		
		List<VoteToken> list = getAllTokensForBallot(bid);
		
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i).getAcId().equals(acid))
			{
				
				return removeToken(bid, list.get(i));
			}
		}
		
		return false;
	}
	
	/**
	 * Method will clear out all tokens and vote cast for the ballot at bid
	 * @param bid
	 */
	public void voteCleanUp(int bid)
	{
		clearVoteTokens(bid);
		clearVoteCast(bid);
	}
	
	/**
	 * Method will clear out any tokens or vote cast then recreate tokens to give to all
	 * voting and active faculty that are supposed to vote on given ballot
	 * @param bal
	 */
	public List<VoteToken> startRestartVote(Ballot bal)
	{
		//Create list to  hold created tokens and will be returned in the end
		List<VoteToken> tokens = new ArrayList<VoteToken>();
		
		//Get all the faculty in the data store
		List<Faculty> fac = new ArrayList<Faculty>();
		try {
			fac = facultyDao.listAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//clear tokens and vote cast
		voteCleanUp(bal.getId());
		
		
		//Check if the vote is happening campus wide
		if(bal.getVoters().equals("CW"))
		{
			//loop through all faculty
			for(int i = 0; i < fac.size(); i++)
			{
				//if a faculty member is active and voting
				if(fac.get(i).isActive() && fac.get(i).isVoting())
				{
					//create their token and add it to the list of all tokens created
					VoteToken token = createVoteToken(bal.getId(), fac.get(i));
					tokens.add(token);
				}
			}
		}
		else //if not campus wide
		{
			//loop through all faculty
			for(int i = 0; i < fac.size(); i++)
			{
				//if a faculty member is active and voting AND matches the selected voting div
				if(fac.get(i).getDiv().equals(bal.getVoters()) && fac.get(i).isActive() && fac.get(i).isVoting())
				{
					//create their token and add it to the list of all tokens created
					VoteToken token = createVoteToken(bal.getId(), fac.get(i));
					tokens.add(token);
				}
			}
		}
		
		
		//return the final list of all created tokens
		return tokens;
		}
	
		/**
		 * Convert a list of faculty to a list of tokens from the given list of tokens
		 * @param tokens
		 * @param fac
		 * @return
		 */
		public List<VoteToken> convertFacultyToTokens(List<VoteToken> tokens, List<Faculty> fac)
		{
			List<VoteToken> newTokens = new ArrayList<VoteToken>();
			
			for(int i = 0; i < tokens.size(); i++)
			{
				for(int j = 0; j < fac.size(); j++)
				{
					if(tokens.get(i).getAcId().equals(fac.get(j).getAcId()))
					{
						newTokens.add(tokens.get(i));
					}
				}
			}
			
			return newTokens;
		}
	
		/**
		 * Convert a list of tokens to a list of faculty using the acID
		 * @param tokens
		 * @return
		 */
		public List<Faculty> convertTokensToFaculty(List<VoteToken> tokens)
		{
			List<Faculty> fac = new ArrayList<Faculty>();
			
			for(int i = 0; i < tokens.size(); i++)
			{
				Faculty newFac = null;
				try {
					newFac = facultyDao.findFacultyByID(tokens.get(i).getAcId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(newFac != null)
					fac.add(newFac);
			}
			

			return fac;
		}
	}

