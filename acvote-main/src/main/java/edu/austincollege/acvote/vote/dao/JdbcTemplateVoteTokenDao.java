package edu.austincollege.acvote.vote.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import edu.austincollege.acvote.db.JdbcTemplateAbstractDao;
import edu.austincollege.acvote.vote.VoteToken;

@Component("JdbcTemplateVoteTokenDao")
public class JdbcTemplateVoteTokenDao extends JdbcTemplateAbstractDao implements VoteTokenDao {
	private Logger log = LoggerFactory.getLogger(JdbcTemplateVoteTokenDao.class);
	
	/**
	 * Guides JdbcTemplate how to map the result set to our VoteToken object class.
	 * Column name must match those in our db schema.
	 * 
	 * @author brandonhill
	 *
	 */
	protected class VoteTokenMapper implements RowMapper<VoteToken> {

		@Override
		public VoteToken mapRow(ResultSet rs, int rowNum) throws SQLException {

			VoteToken vt = VoteToken.newToken(0, "0");
			vt.setBid(rs.getInt("bid"));
			vt.setAcId(rs.getString("acid"));
			vt.setToken(rs.getString("keyval"));

			return vt;
		}

	}
	
	
	/**
	 * Method returns a list of vote tokens that all have the provided bid
	 */
	@Override
	public List<VoteToken> tokensForBallot(Integer bid) throws Exception {
		log.debug("Getting tokens for ballot: " + bid);
		String sql = String.format("SELECT * FROM token WHERE bid = %d", bid);
		
		List<VoteToken> results = getJdbcTemplate().query(sql, new VoteTokenMapper());
		return results;
	}

	/**
	 * Method returns true if the token that matches the unique id "tok" is not in the list for the provided ballot
	 */
	@Override
	public boolean tokenIsAbsent(Integer bid, String tok) throws Exception {
		String sql = String.format("SELECT * FROM token WHERE bid = %d AND keyval = '%s'", bid, tok);
		
		List<VoteToken> results = getJdbcTemplate().query(sql, new VoteTokenMapper());
		
		if(results.size() > 0) {
			return false;
		}
		
		return true;
	}

	/**
	 * Method removes the token that matches the unique id "tok" from the provided ballot and returns true if there were no issues
	 */
	@Override
	public boolean removeToken(Integer bid, String tok) throws Exception {
		int rc = getJdbcTemplate().update(String.format("DELETE FROM token WHERE bid = %d AND keyval = '%s'", bid, tok));
		log.debug("...got {}", rc);
		if(rc != 1) {
			String msg = String.format("unable to remove token [%s]", tok);
			log.error(msg);
			throw new Exception(msg);
		}
		
		log.debug("sucessfully removed token #{}",tok);
		return true;
	}

	/**
	 * Method adds the token given to the provided ballot and returns true if there were no issues
	 */
	@Override
	public boolean addToken(Integer bid, VoteToken vt) throws Exception {
		String acId = vt.getAcId();
		String tok = vt.getToken();
		String sql = String.format("INSERT INTO token (bid, acid, keyval) VALUES (%d, '%s', '%s')", bid, acId, tok);
		
		KeyHolder k = new GeneratedKeyHolder();
		
		int num = getJdbcTemplate().update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			return ps;
		}, k);
		
		if(num != 1) {
			String msg = String.format("unable to add token");
			log.error(msg);
			throw new Exception(msg);
		}
		
		log.debug("successfully added token #{}", k.getKey());
		return true;
	}

	/**
	 * Method removes all tokens related to the given ballot
	 */
	@Override
	public void clearTokensForBallot(Integer bid) throws Exception {
		int rc = getJdbcTemplate().update(String.format("DELETE FROM token WHERE bid = %d", bid));
		log.debug("...got {}", rc);
		if(rc < 1) {
			String msg = String.format("unable to clear tokens or is already empty from ballot %d", bid);
			log.error(msg);
			//throw new Exception(msg);
		}
		

	}

	/**
	 * Method adds all tokens from the list to the given ballot and returns true if there were no issues
	 */
	@Override
	public boolean addAllTokens(Integer bid, List<VoteToken> toks) throws Exception {
		for(int i = 0; i < toks.size(); i++)
		{
			addToken(bid, toks.get(i));
		}
		
		return true;
	}

}
