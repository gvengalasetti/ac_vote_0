package edu.austincollege.acvote.ballot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.austincollege.acvote.ballot.dao.BallotDao;
import edu.austincollege.acvote.ballot.dao.BallotOptionDao;
import edu.austincollege.acvote.ballot.dao.InvalidBallotIdException;
import edu.austincollege.acvote.ballot.option.VoteOption;
import edu.austincollege.acvote.faculty.Faculty;
import edu.austincollege.acvote.vote.VoteCast;
import edu.austincollege.acvote.vote.VoteCounter;
import edu.austincollege.acvote.vote.VoteResult;
import edu.austincollege.acvote.vote.dao.VoteCastDao;
import edu.austincollege.acvote.vote.dao.VoteTokenDao;

/**
 * <h1>Helper object for BallotController</h1>
 * 
 * <p>
 * Handles Ballot CRUD requests from controller by passing on requests to an
 * injected BallotDao. Also handles updates of values of individual ballot
 * fields.
 * </p>
 * 
 */
@Service
public class BallotService {

	private static Logger log = LoggerFactory.getLogger(BallotService.class);

	@Autowired
	BallotDao ballotDao;

	@Autowired
	BallotOptionDao optionDao;

	@Autowired
	VoteCastDao vcDao;

	@Autowired
	VoteTokenDao vtDao;

	public BallotService() {

	}

	/**
	 * Creates and adds to database a new ballot with the parameters as its
	 * attributes
	 * 
	 * @param title
	 * @param instructions
	 * @param description
	 * @param facBased
	 * @param candidates
	 * @param typeOfVote
	 * @param outcomes
	 * @param startTime
	 * @param endTime
	 * @param groupsAllowedToVote
	 * @param votesRecieved
	 * @param totalVotesExpected
	 * @return id of new ballot
	 */
	public int addBallot(String title, String instructions, String description, boolean facBased,
			ArrayList<VoteOption> candidates, String typeOfVote, Integer outcomes, LocalDateTime startTime,
			LocalDateTime endTime, String groupsAllowedToVote, int totalVotesExpected) {

		try {

			log.debug("service creating new ballot");

			// creating ballot
			Ballot b = ballotDao.createBallot(title, instructions, description, facBased, candidates, typeOfVote,
					outcomes, startTime, endTime, groupsAllowedToVote, totalVotesExpected);

			if (b != null) {

				log.debug("service created new ballot");
				return b.getId();
			} else {

				log.warn("service could not create new ballot");
				return -1;
			}
		} catch (Exception e) {

			log.warn("service could not create new ballot");
//			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Returns all ballots
	 * 
	 * @return list of ballots
	 */
	public List<Ballot> getBallots() {

		try {

			log.debug("service getting all ballots");

			// listing ballots
			List<Ballot> ballots = ballotDao.listAll();

			log.debug("service got all ballots");
			return ballots;
		} catch (Exception e) {

			log.warn("service could not get ballots");
//			e.printStackTrace();
			return new ArrayList<Ballot>();
		}
	}

	/**
	 * Returns a ballot with matching ID
	 * 
	 * @param id id of matching ballot
	 * @return matching ballot, null if no match
	 */
	public Ballot getBallotById(int id) {

		try {

			log.debug("service getting ballot #{}", id);

			// retrieving ballot
			Ballot b = ballotDao.getBallot(id);

			log.debug("service got ballot #{}", id);
			return b;
		} catch (Exception e) {

			log.error("service could not get ballot #{}", id);
//			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Deletes ballot from database
	 * 
	 * @param id of ballot to be deleted
	 * @return true if deleted, false if invalid ID
	 */
	public boolean deleteBallot(int id) {

		try {

			log.debug("service deleting ballot #{}", id);

			// deleting ballot
			ballotDao.deleteBallot(id);

			log.debug("service deleted ballot #{}", id);
			return true;
		} catch (Exception e) {

			log.error("service could not delete ballot #{}", id);
//			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method to update the ballot with the matching id with the data from the given
	 * ballot
	 * 
	 * @param id
	 * @param bal
	 * @return Ballot
	 */
	public Ballot updateBallot(int id, Ballot bal) {

		try {

			log.debug("service updating ballot #{}", id);

			// updating ballot
			ballotDao.updateBallot(id, bal.getTitle(), bal.getInstructions(), bal.getDescription(),
					bal.isFacultyBased(), new ArrayList<VoteOption>(bal.getOptions()), bal.getTypeOfVote(),
					bal.getOutcomes(), bal.getStartTime(), bal.getEndTime(), bal.getVoters(),
					bal.getTotalVotesExpected());

			log.debug("service updated ballot #{}", id);
			return getBallotById(id);
		} catch (Exception e) {

			log.debug("service could not update ballot #{}", id);
//			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This method sets the title of the given ballot to the desired title
	 * 
	 * @param id       - the id of the ballot
	 * @param newTitle - the title that we want the ballot to have
	 * @return Ballot - the updated version of previous ballot
	 */
	public Ballot setBallotTitle(int id, String newTitle) {

		log.debug("service editing ballot #{} title", id);

		Ballot currentBallot = getBallotById(id);
		currentBallot.setTitle(newTitle);

		// editing title
		return updateBallot(id, currentBallot);
	}

	/**
	 * This method sets the description of the given ballot to the desired
	 * description
	 * 
	 * @param id             - the id of the ballot
	 * @param newDescription - the description that we want the ballot to have
	 * @return Ballot - the updated version of previous ballot
	 */
	public Ballot setBallotDescription(int id, String newDescription) {

		log.debug("service editing ballot #{} description", id);

		Ballot currentBallot = getBallotById(id);
		currentBallot.setDescription(newDescription);

		// editing description
		return updateBallot(id, currentBallot);
	}

	/**
	 * This method sets the instructions of the given ballot to the desired
	 * instructions
	 * 
	 * @param id              - the id of the ballot
	 * @param newInstructions - the instructions that we want the ballot to have
	 * @return Ballot - the updated version of previous ballot
	 */
	public Ballot setBallotInstructions(int id, String newInstructions) {

		log.debug("service editing ballot #{} instructions", id);

		Ballot currentBallot = getBallotById(id);
		currentBallot.setInstructions(newInstructions);

		// editing instructions
		return updateBallot(id, currentBallot);
	}

	/**
	 * This method sets the typeOfVote of the given ballot to the desired typeOfVote
	 * 
	 * @param id            - the id of the ballot
	 * @param newTypeOfVote - the typeOfVote that we want the ballot to have
	 * @return Ballot - the updated version of previous ballot
	 */
	public Ballot setBallotTypeOfVote(int id, String newTypeOfVote) {

		log.debug("service editing ballot #{} vote type", id);

		Ballot currentBallot = getBallotById(id);
		currentBallot.setTypeOfVote(newTypeOfVote);

		// editing vote type
		return updateBallot(id, currentBallot);
	}

	/**
	 * This method sets the outcomes of the given ballot to the desired outcomes
	 * 
	 * @param id          - the id of the ballot
	 * @param newOutcomes - the outcomes that we want the ballot to have
	 * @return Ballot - the updated version of previous ballot
	 */
	public Ballot setBallotOutcomes(int id, Integer newOutcomes) {

		log.debug("service editing ballot #{} outcomes", id);

		Ballot currentBallot = getBallotById(id);
		currentBallot.setOutcomes(newOutcomes);

		// editing outcomes
		return updateBallot(id, currentBallot);
	}

	/**
	 * This method sets the startTime of the given ballot to the desired startTime. endTime is automatically 
	 * updated to be 1 week after the startTime.
	 * 
	 * @param id           - the id of the ballot
	 * @param newStartTime - the startTime that we want the ballot to have
	 * @return Ballot - the updated version of previous ballot
	 */
	public Ballot setBallotStartTime(int id, LocalDateTime newStartTime) {

		log.debug("service editing ballot #{} start time");

		Ballot currentBallot = getBallotById(id);
		currentBallot.setStartTime(newStartTime);
		currentBallot.setEndTime(newStartTime.plusWeeks(1));

		// editing start time
		return updateBallot(id, currentBallot);
	}

	/**
	 * This method sets the endTime of the given ballot to the desired endTime. Checks to make sure startTime is not after the 
	 * endTime, and if so updates the startTime to be 1 week ahead.
	 * 
	 * @param id         - the id of the ballot
	 * @param newEndTime - the endTime that we want the ballot to have
	 * @return Ballot - the updated version of previous ballot
	 */
	public Ballot setBallotEndTime(int id, LocalDateTime newEndTime) {

		log.debug("service editing ballot #{} end time", id);

		Ballot currentBallot = getBallotById(id);
		if(currentBallot.getStartTime().isAfter(newEndTime)) {
			currentBallot.setStartTime(newEndTime.minusWeeks(1));
		}

		currentBallot.setEndTime(newEndTime);

		// editing end time
		return updateBallot(id, currentBallot);
	}

	/**
	 * This method sets the voting group of the given ballot to the desired voting
	 * group
	 * 
	 * @param id    - the id of the ballot
	 * @param group - the voting group that we want the ballot to have
	 * @return Ballot - the updated version of previous ballot
	 */
	public Ballot setBallotVotingGroup(Integer id, String group) {

		log.debug("service editing ballot #{} voters", id);

		Ballot currentBallot = getBallotById(id);
		currentBallot.setVoters(group);

		// editing voting group
		return updateBallot(id, currentBallot);
	}

	/**
	 * Converts faculty objects into vote option objects
	 * 
	 * @param faculties list of faculty to be converted
	 * @return list of faculty vote options
	 */
	public ArrayList<VoteOption> convertFacultytoVoteOptions(List<Faculty> faculties) {

		log.debug("service converting faculty to vote options");

		ArrayList<VoteOption> voteOptions = new ArrayList<VoteOption>();
		for (Faculty member : faculties) {
			voteOptions.add(new VoteOption(member.getAcId(),
					String.format("%s %s", member.getFirstName(), member.getLastName()), true));
		}

		log.debug("service converted faculty into vote options");
		return voteOptions;

	}

	/**
	 * Method works with the dao to clear all of the voting options from the
	 * specified ballot.
	 * 
	 * @param bid ballot id
	 */
	public boolean clearOptionsFromBallot(int bid) {

		log.debug("service clearing options from ballot #{}", bid);

		try {

			// clearing
			optionDao.clearOptionsOnBallot(bid);

			log.debug("service cleared options from ballot #{}", bid);
			return true;
		} catch (Exception e) {

			log.warn("service could not clear options from ballot #{}", bid);
//			e.printStackTrace();
			return false;
		}

	}

	/**
	 * Works with the dao to update the voting option in the specified ballot to be
	 * enabled or disabled according to the specified state.
	 * 
	 * @param bid   ballot id
	 * @param oid   option id
	 * @param state true or false
	 */
	public boolean toggleOption(int bid, String oid, boolean state) {

		log.debug("service toggling option #{} in ballot #{} to {}", oid, bid, state);

		try {

			// toggling options
			optionDao.updateOptionOnBallot(bid, oid, state);

			log.debug("service toggled option");
			return true;
		} catch (Exception e) {

			log.warn("service could not toggle option");
//			e.printStackTrace();
			return false;
		}

	}

	/**
	 * Works with the DAO to delete the specified options from the current ballot.
	 * 
	 * @param bid
	 * @param oid
	 * @throws Exception 
	 */
	public void deleteOptionFromBallot(int bid, String oid) throws Exception {

		try {

			log.debug("service deleting option #{} from ballot #{}", oid, bid);

			// deleting option
			optionDao.deleteOptionOnBallot(bid, oid);

			log.debug("service deleted option #{} from ballot #{}", oid, bid);
		} catch (Exception e) {

			log.error("service could not delete option #{} from ballot #{}", oid, bid);
//			e.printStackTrace();
			throw e;
		}

	}

	/**
	 * Works with the DAO to add the list of voting options to the specified ballot.
	 * 
	 * @param bid
	 * @param newVoteOptions
	 * @throws Exception
	 */
	public void addBallotOptions(int bid, List<VoteOption> newVoteOptions) throws Exception {

		log.debug("service adding options to ballot #{}", bid);

		for (VoteOption vo : newVoteOptions) {
			optionDao.addOptionOnBallot(bid, vo);
		}

		log.debug("service added options to ballot #{}", bid);

	}

	/**
	 * Sets the current ballot's basis for option. If fbas is true, the current
	 * ballot is faculty based otherwise, the current ballot options are custom.
	 * 
	 * @param id
	 * @param fbas
	 * @return
	 */
	public Ballot setBasis(int id, Boolean fbas) throws Exception {

		log.debug("service editing ballot #{} basis", id);

		Ballot currentBallot = getBallotById(id);

		// assert that we found the ballot
		if (currentBallot == null) {
			throw new InvalidBallotIdException(String.format("Ballot %d does not exist.", id));
		}

		// variables for later comparison
		boolean oldval = currentBallot.isFacultyBased();
		boolean newval = fbas;

		currentBallot.setFacultyBased(fbas);

		// clearing options if basis changed
		if (oldval != newval) {
			this.clearOptionsFromBallot(id);
		}

		// editing basis
		return updateBallot(id, currentBallot);

	}

	/**
	 * Returns the results for a ballot
	 * 
	 * @param bal ballot
	 * @return results of ballot
	 */
	public VoteResult resultsForBallot(Ballot bal) {

		log.debug("service getting results for ballot #{}", bal.getId());

		try {

			// retrieving votes for ballot
			List<VoteCast> votes = vcDao.votesForBallot(bal.getId());
			log.debug("{} votes received for this ballot={}", votes.size(), bal.getId());

			// counting votes
			VoteCounter vctr = VoteCounter.newCounterFor(bal);

			// forming results
			VoteResult results = vctr.voteResults(votes);

			log.debug("service got results for ballot #{}", bal.getId());

			return results;

		} catch (Exception e) {

			log.warn("service could not get results for ballot #{}", bal.getId());
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Method changes the votes expected on the given ballot then updates the
	 * information in the data store.
	 * 
	 * @param bal
	 * @param votesExpected
	 */
	public Ballot setVotesExpected(Ballot bal, int votesExpected) {

		log.debug("service editing ballot #{} votes expected", bal.getId());

		Ballot newBal = bal;
		newBal.setTotalVotesExpected(votesExpected);

		// editing votes expected
		return updateBallot(bal.getId(), newBal);
	}

	/**
	 * Method to set the dao for possible testing
	 * 
	 * @param dao
	 */
	public void setBallotDao(BallotDao dao) {
		this.ballotDao = dao;
	}

}
