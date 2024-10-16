package edu.austincollege.acvote.ballot.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.support.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.option.VoteOption;
import edu.austincollege.acvote.db.JdbcTemplateAbstractDao;

/**
 * <h1>JdbcTemplate Implementation of BallotDao</h1>
 * 
 * <p>
 * Handles CRUD operations pertaining to ballots. Queries the local H2 database.
 * </p>
 *
 */
@Component("JdbcTemplateBallotDao")
public class JdbcBallotDao extends JdbcTemplateAbstractDao implements BallotDao {

	private Logger log = LoggerFactory.getLogger(JdbcBallotDao.class);

	public JdbcBallotDao() {
		super();
	}

	/**
	 * This class maps a ballot database tuple to the Ballot object by using a
	 * RowMapper interface to fetch the records for a ballot from the database.
	 */
	private class BallotMapper implements RowMapper<Ballot> {
		@Override
		public Ballot mapRow(ResultSet rs, int rowNum) throws SQLException {
			Ballot b = new Ballot();
			b.setId(rs.getInt("bid"));
			b.setTitle(rs.getString("title"));
			b.setInstructions(rs.getString("instructions"));
			b.setDescription(rs.getString("description"));
			b.setFacultyBased(rs.getString("faculty").equals("TRUE"));
			b.setTypeOfVote(rs.getString("vote_type"));
			b.setOutcomes(rs.getInt("outcomes"));
			b.setEndTime(rs.getTimestamp("close_time").toLocalDateTime());
			b.setStartTime(rs.getTimestamp("open_time").toLocalDateTime());
			b.setVoters(rs.getString("voters"));
			b.setTotalVotesExpected(rs.getInt("vote_expected"));

			return b;
		}
	}

	/**
	 * This class maps an option database tuple to the VoteOption object by using a
	 * RowMapper interface to fetch the records for an option from the database.
	 */
	private class OptionMapper implements RowMapper<VoteOption> {

		@Override
		public VoteOption mapRow(ResultSet rs, int rowNum) throws SQLException {
			VoteOption vo = new VoteOption();
			vo.setoptionID(rs.getString("oid"));
			vo.setTitle(rs.getString("label"));
			vo.setEnabled(rs.getString("enabled").equals("TRUE"));

			return vo;
		}

	}

	/**
	 * Lists all ballots in the database
	 * 
	 * @return list of all ballots
	 * @throws Exception when unable to communicate with database
	 */
	@Override
	public List<Ballot> listAll() throws Exception {

		log.debug("listing ballots");

		//querying for all ballots
		String sql = "SELECT * FROM ballot";
		List<Ballot> results = getJdbcTemplate().query(sql, new BallotMapper());
		
		for (Ballot b : results) {
			
			//querying for all options for each ballot
			sql = String.format("SELECT * FROM ballotOption WHERE bid = %d", b.getId());
			List<VoteOption> moreResults = getJdbcTemplate().query(sql, new OptionMapper());
			
			//attaching options to ballot
			log.debug("found" + moreResults.size() + " options");
			b.setOptions(moreResults);
		}

		log.debug("listed ballots");
		return results;
	}

	/**
	 * Creates and adds a new ballot to the database
	 * 
	 * @return Ballot created ballot
	 * @throws Exception when unable to communicate with database
	 */
	@Override
	public Ballot createBallot(String title, String instructions, String description, boolean facultyFlag,
			ArrayList<VoteOption> options, String typeOfVote, Integer outcomes, LocalDateTime startTime,
			LocalDateTime endTime, String groupsAllowedToVote, int totalVotesExpected)
			throws Exception {

		log.debug("creating new ballot");

		//building query
		String sql = String.format(
				"INSERT INTO ballot (title, instructions, description, outcomes, open_time, close_time, vote_expected, vote_type, voters, faculty) VALUES ('%s', '%s', '%s', %d, '%s', '%s', %d, '%s', '%s', %s)",
				title, instructions, description, outcomes, startTime, endTime, totalVotesExpected, typeOfVote,
				groupsAllowedToVote, String.valueOf(facultyFlag).toUpperCase());

		//will capture id of new ballot upon creation
		KeyHolder k = new GeneratedKeyHolder();

		//querying
		int num = getJdbcTemplate().update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			return ps;
		}, k); 

		//if something went wrong with create, throwing exception
		if (num != 1) {
			String msg = String.format("could not create ballot");
			log.error(msg);
			throw new Exception(msg);
		}

		log.debug("created ballot #{}", k.getKey());
		return this.getBallot(k.getKey().intValue());
	}

	/**
	 * Deletes ballot identified by id from database
	 * 
	 * @return true if deleted
	 * @throws Exception when unable to communicate with database
	 */
	@Override
	public boolean deleteBallot(int bid) throws Exception {

		log.debug("deleting ballot #{}", bid);
		
		//querying
		int rc = getJdbcTemplate().update(String.format("DELETE FROM ballot WHERE bid=%d", bid));
		
		//if something went wrong with delete, throwing exception
		if (rc != 1) {
			String msg = String.format("could not delete ballot #%s", bid);
			log.error(msg);
			throw new Exception(msg);
		}

		log.debug("deleted ballot #{}", bid);
		return true;
	}

	/**
	 * Updates ballot identified by bid with new attributes
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
	 * @param totlaVotesExpected
	 * @return true if updated
	 * @throws Exception when unable to communicate with database
	 */
	@Override
	public boolean updateBallot(int bid, String title, String instructions, String description, boolean basis,
			ArrayList<VoteOption> options, String typeOfVote, Integer outcomes, LocalDateTime startTime,
			LocalDateTime endTime, String voters, int totalVotesExpected)
			throws Exception {

		log.debug("updating ballot #{}", bid);

		//querying
		int rc = getJdbcTemplate().update(String.format(
				"UPDATE ballot SET title = '%s', instructions = '%s', description = '%s', faculty=%b, outcomes = '%d', open_time = '%s', close_time = '%s', vote_expected = %d, vote_type = '%s', voters = '%s' WHERE bid = %d",
				title, instructions, description, basis, outcomes, startTime, endTime, totalVotesExpected,
				typeOfVote, voters, bid));

		//if something went wrong with update, throwing exception
		if (rc < 1) {
			String msg = String.format("could not update ballot #%d", bid);
			log.error(msg);
			throw new Exception(msg);
		}

		log.debug("updated ballot #{}", bid);
		return true;
	}

	/**
	 * Locates and returns ballot identified by bid from database
	 * 
	 * @param bid
	 * @returns matching ballot
	 * @throws Exception when issues communicating with database
	 */
	@Override
	public Ballot getBallot(int bid) throws Exception {

		log.debug("getting ballot #{}", bid);

		//rejecting invalid bids
		if (bid < 0)
			throw new Exception("invalid ballot id");

		//querying and storing results
		String sql = String.format("SELECT * FROM ballot WHERE bid = %d", bid);
		List<Ballot> results = this.getJdbcTemplate().query(sql, new BallotMapper());

		//ensuring only one result
		if (results.size() != 1) {
			
			//throwing exception otherwise
			String msg = String.format("could not get ballot #%s", bid);
			log.error(msg);
			throw new Exception(msg);
		}

		Ballot b = results.get(0);

		/*
		 * Now eagerly prefetch the options for this ballot.
		 */
		sql = String.format("SELECT * FROM ballotOption WHERE bid = %d", b.getId());
		List<VoteOption> moreResults = getJdbcTemplate().query(sql, new OptionMapper());
		b.setOptions(moreResults);

		log.debug("got ballot #{}", bid);
		return b;
	}

}
