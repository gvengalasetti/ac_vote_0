package edu.austincollege.acvote.ballot.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.DuplicateKeyException;

import edu.austincollege.acvote.ballot.option.VoteOption;
import edu.austincollege.acvote.db.JdbcTemplateAbstractDao;

/**
 * An instance of this class provides DATA ACCESS services mapping the data in
 * our ballot options table to instances of VoteOption.class. When dealing with
 * ballot options, we always do so for an existing ballot. So in all cases, we
 * assert the ballotId is valid and exists in our database.
 * 
 * @author mahiggs
 *
 */
public class JdbcBallotOptionDao extends JdbcTemplateAbstractDao implements BallotOptionDao {

	private Logger log = LoggerFactory.getLogger(JdbcBallotOptionDao.class);

	public JdbcBallotOptionDao() {
		super();
	}

	/**
	 * Guides JdbcTemplte how to map the result set to our VoteOption object class.
	 * Column name must match those in our db schema.
	 * 
	 * @author mahiggs
	 * @see schema.sql
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
	 * Strings with embedded single quotes must be doubled for SQL statement. This
	 * method doubles the embedded single quotes so SQL will process correctly. For
	 * example, if a faculty first name is J'Lee then SQL wants the literal value in
	 * the WHERE clause to be J''Lee. So we replace all single occurrences with
	 * doubles.
	 * 
	 * @param str
	 * @return
	 */
	private String dq(String str) {
		return str.replace("'", "''");
	}

	@Override
	public boolean validBallotId(int bid) {

		int result;
		try {
			result = getJdbcTemplate().queryForObject("SELECT 1 from ballot where bid=" + bid, Integer.class);
			return result == 1;

		} catch (DataAccessException e) {
			return false; // no match found. return false
		}

	}

	/**
	 * We use this method to ensure that the specified bid exist in our database.
	 * 
	 * @param bid
	 * @throws InvalidBallotIdException
	 */
	private void assertBallotIdIsValid(int bid) throws InvalidBallotIdException {
		if (!this.validBallotId(bid)) {
			String msg = String.format("ballot %d does not exist", bid);
			log.error(msg);
			throw new InvalidBallotIdException(msg);
		}
	}

	/**
	 * Creates an option in our datastore for the associated ballot.
	 * 
	 * @param bid      integer id of ballot (assigned by db)
	 * @param oid      string unique within the ballot
	 * @param olabel   human friendly display string
	 * @param oEnabled boolean indicating state of option
	 * @return
	 * @throws Exception
	 */
	@Override
	public VoteOption createOption(int bid, String oid, String olabel, Boolean oEnabled) throws Exception {

		log.debug("create ballot option");

		/*
		 * First we make sure the ballot id provides is valid/exists.
		 */
		assertBallotIdIsValid(bid);

		String sql = String.format(
				"INSERT INTO ballotOption (bid,oid,label,enabled) " + " VALUES  (%d, '%s', '%s', %b)", bid, dq(oid),
				dq(olabel), oEnabled);

		try {
			int num = getJdbcTemplate().update(connection -> {
				PreparedStatement ps = connection.prepareStatement(sql);
				return ps;
			});

			if (num != 1) {
				String msg = String.format("unable to create ballot option");
				log.error(msg);
				throw new Exception(msg);
			}

		} catch (DuplicateKeyException e) {

			String msg = String.format("unable to create ballot option; duplicate option id=[%s] ", oid);
			log.error(msg);

			throw new InvalidOptionIdException(msg);

		}

		return this.option(bid, oid);
	}

	/**
	 * Finds or fetches an option associated with the specific ballot specified by
	 * bid.
	 * 
	 * @param bid integer id of ballot (assigned by db)
	 * @param oid string unique within the ballot
	 * @return
	 * @throws Exception
	 */
	@Override
	public VoteOption option(int bid, String oid) throws Exception {
		// TODO Auto-generated method stub
		log.debug("fetching ballot {} option {} ", bid, oid);

		/*
		 * First we make sure the ballot id provides is valid/exists.
		 */
		assertBallotIdIsValid(bid);

		String sql = String.format("SELECT * FROM ballotOption WHERE bid=%d and oid='%s'", bid, oid);
		List<VoteOption> options = getJdbcTemplate().query(sql, new OptionMapper());

		if (options.size() <= 0) {
			throw new InvalidOptionIdException(
					String.format("ballot %s does not exist or has NO option with option id ='%s'", bid, oid));
		}

		if (options.size() > 1) {
			throw new InvalidOptionIdException(
					String.format("ballot %s has multiple options with non-unique key='%s'", bid, oid));
		}

		return options.get(0);
	}

	/**
	 * Fetches all options on the specified Ballot
	 * 
	 * @param bid integer id of ballot (assigned by db)
	 * @throws Exception
	 */
	@Override
	public List<VoteOption> allOptionsOnBallot(int bid) throws Exception {

		log.debug("fetching all ballot {} options", bid);

		/*
		 * First we make sure the ballot id provides is valid/exists.
		 */
		assertBallotIdIsValid(bid);

		String sql = String.format("SELECT * FROM ballotOption WHERE bid=%d", bid);
		List<VoteOption> options = getJdbcTemplate().query(sql, new OptionMapper());

		return options;
	}

	/**
	 * Clears all options on the specified Ballot
	 * 
	 * @param bid integer id of ballot (assigned by db)
	 * @throws Exception
	 */
	@Override
	public boolean clearOptionsOnBallot(int bid) throws Exception {

		log.debug("clearing ballot {} options", bid);

		/*
		 * First we make sure the ballot id provides is valid/exists.
		 */
		assertBallotIdIsValid(bid);

		int rc = getJdbcTemplate().update(String.format("DELETE FROM ballotOption WHERE bid=%d", bid));

		if (rc < 1) {
			String msg = String.format("unable to delete options from ballot [%d]", bid);
			log.error(msg);
			throw new Exception(msg);
		}

		return true;

	}

	/**
	 * Add and option to the specified ballot if it does not exist.
	 * 
	 * @param bid integer id of ballot (assigned by db)
	 * @param opt string unique within the ballot
	 */
	@Override
	public void addOptionOnBallot(int bid, VoteOption opt) throws Exception {
		// TODO Auto-generated method stub
		log.debug("adding option to ballot {}", bid);

		/*
		 * First we make sure the ballot id provides is valid/exists.
		 */
		assertBallotIdIsValid(bid);

		this.createOption(bid, opt.getoptionID(), opt.getTitle(), opt.isEnabled());

	}

	/**
	 * Finds and removes the specified option from the specified ballot.
	 * 
	 * @param bid integer id of ballot (assigned by db)
	 * @param oid string unique within the ballot
	 */
	@Override
	public boolean deleteOptionOnBallot(int bid, String oid) throws Exception {

		log.debug("deleting ballot {} option {}", bid, oid);

		/*
		 * First we make sure the ballot id provides is valid/exists.
		 */
		assertBallotIdIsValid(bid);

		int rc = getJdbcTemplate()
				.update(String.format("DELETE FROM ballotOption WHERE bid=%d and oid='%s'", bid, oid));

		if (rc != 1) {
			String msg = String.format("unable to delete ballot option [%d,%s]", bid, oid);
			log.error(msg);
			throw new InvalidOptionIdException(msg);
		}
		
		return true;

	}

	@Override
	public boolean updateOptionOnBallot(int bid, String oid, boolean enabled) throws Exception {
		
		log.debug("updating option #{} on ballot #{} to {}", oid, bid, enabled);
		
		assertBallotIdIsValid(bid);
		
		int rc = getJdbcTemplate().update(String.format("UPDATE ballotOption SET enabled = %b WHERE bid = %d AND oid = '%s'", enabled, bid, oid));
		
		if(rc < 1) {
			String msg = String.format("unable to update option #%s", oid);
			log.error(msg);
			throw new Exception(msg);
		}
		
		return true;
	}
	
	

}
