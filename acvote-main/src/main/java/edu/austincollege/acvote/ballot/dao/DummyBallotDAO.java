package edu.austincollege.acvote.ballot.dao;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.option.VoteOption;

/**
 * DummyBallotDAO objects implement the BallotDAO interface. They will handle
 * the CRUD functions regarding ballot objects.
 * 
 * @author Alan Rosenberg
 *
 */
public class DummyBallotDAO implements BallotDao {

	private List<Ballot> ballots;
	private int IDcounter;

	/*
	 * Constructs DummyBallotDAO and performs initial operations to populate ballot
	 * list (dummy style)
	 */
	public DummyBallotDAO() throws Exception {
		// initializing ballot list
		ballots = new ArrayList<>();
		IDcounter = 0;

		/*
		 * creating sample ballot items
		 */
		initialize();
	}

	/**
	 * Lists all ballot objects
	 * 
	 * @return list of all ballots
	 * @throws Exception if problem communicating with database
	 * @author Alan Rosenberg
	 */
	@Override
	public List<Ballot> listAll() throws Exception {
		return ballots;
	}

	/**
	 * Creates a new ballot using the parameter values. Adds the new ballot to a
	 * local list of ballots (the dummy way)
	 * 
	 * @return created ballot object
	 * @throws exception if sql query is bad
	 * @author Alan Rosenberg
	 */
	@Override
	public Ballot createBallot(String title, String instructions, String description, boolean facultyFlag,
			ArrayList<VoteOption> candidates, String typeOfVote, Integer outcomes, LocalDateTime startTime,
			LocalDateTime endTime, String groupsAllowedToVote, int totalVotesExpected)
			throws Exception {

		// generating unique ID
		IDcounter++;

		// creating new ballot and adding to list
		Ballot b = new Ballot(IDcounter, title, instructions, description, facultyFlag, candidates, typeOfVote,
				outcomes, startTime, endTime, groupsAllowedToVote, totalVotesExpected);
		ballots.add(b);

		return b;
	}

	/**
	 * Deletes a ballot from the local list of ballots (dummy way)
	 * 
	 * @throws exception if no matching ballot is found
	 * @author Alan Rosenberg
	 */
	@Override
	public boolean deleteBallot(int id) throws Exception {

		Ballot b = null;
		
		try {
			
			b = this.getBallot(id);
		}
		catch (Exception e) {
			
			throw e;
		}

		ballots.remove(b);
		return true;
	}

	/**
	 * Updates an existing ballot with new values from parameter
	 * 
	 * @throws excpetion if no matching ballot is found
	 * @author Alan Rosenberg
	 */
	@Override
	public boolean updateBallot(int id, String title, String instructions, String description, boolean facultyFlag,
			ArrayList<VoteOption> options, String typeOfVote, Integer outcomes, LocalDateTime startTime,
			LocalDateTime endTime, String groupsAllowedToVote, int totalVotesExpected)
			throws Exception {

		// Searching for matching ballot
		for (Ballot b : ballots) {
			if (b.getId() == id) {

				// updating ballot with new values
				b.setTitle(title);
				b.setInstructions(instructions);
				b.setDescription(description);
				b.setFacultyBased(facultyFlag);
				b.setTypeOfVote(typeOfVote);
				b.setOutcomes(outcomes);
				b.setStartTime(startTime);
				b.setEndTime(endTime);
				b.setVoters(groupsAllowedToVote);
				b.setTotalVotesExpected(totalVotesExpected);

				return true;
			}
		}

		// throwing exception if no match is found
		throw new Exception();
	}

	/**
	 * Finds a ballot with matching ID to parameter
	 * 
	 * @return matching ballot, null if no match
	 * @throws Exception when issues with communicating with database
	 * @author Alan Rosenberg
	 */
	@Override
	public Ballot getBallot(int id) throws Exception {

		for (Ballot b : ballots) {
			if (b.getId() == id)
				return b;
		}

		throw new Exception("invalid ballot id");
	}

	/**
	 * generates example ballots with example data
	 * 
	 * @throws SQLException
	 */
	private void initialize() throws Exception {
		ArrayList<VoteOption> candidates = new ArrayList<>();
		candidates.add(new VoteOption("0629102", "Higgs", true));
		candidates.add(new VoteOption("0629104", "Block", true));

		ArrayList<String> groups = new ArrayList<>();
		createBallot("Coolest CS Professor", "Please drag and drop these candidates around!",
				"This is a description for ballot 1!", true, candidates, "IRV", 2, LocalDateTime.of(2023, 6, 8, 16, 0, 0), LocalDateTime.of(2023, 7, 8, 16, 0, 0), "CW", 124);

		candidates.add(new VoteOption("80085", "Rosenberg", true));

		createBallot("Which ones are CS Professors?", "Please drag and drop these candidates around!",
				"This is a description for ballot 2!", true, candidates, "IRV", 2, LocalDateTime.of(2023, 6, 8, 16, 0, 0), LocalDateTime.of(2023, 7, 8, 16, 0, 0), "CW", 7);

		candidates = new ArrayList<>();

		candidates.add(new VoteOption("629102", "Higgs", true));
		candidates.add(new VoteOption("629105", "Not Higgs", true));
		candidates.add(new VoteOption("678392", "Nguyen", true));

		createBallot("This is a different example", "Please drag and drop these candidates around!",
				"This is a description for ballot 3!", true, candidates, "IRV", 2, LocalDateTime.now().plusDays(8),
				LocalDateTime.now().plusMonths(1), "CW", 7);

		for (int i = 4; i < 15; i++) {
			createBallot("This is a random example", "Please drag and drop these candidates around!",
					"This is a description for a ballot!", true, candidates, "IRV", 3, LocalDateTime.now().plusDays(15),
					LocalDateTime.now().plusMonths(2), "CW", (int) Math.pow(Math.PI, i));
		}
	}

	/*
	 * For testing purposes only
	 */
	public void setBallots(List<Ballot> ballots) {
		this.ballots = ballots;
	}

}
