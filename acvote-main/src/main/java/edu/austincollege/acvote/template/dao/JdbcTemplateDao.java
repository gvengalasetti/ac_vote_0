package edu.austincollege.acvote.template.dao;

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

import edu.austincollege.acvote.db.JdbcTemplateAbstractDao;
import edu.austincollege.acvote.template.BallotTemplate;

public class JdbcTemplateDao extends JdbcTemplateAbstractDao implements TemplateDao {
	
	private static Logger log = LoggerFactory.getLogger(JdbcTemplateDao.class);
	
	public JdbcTemplateDao() {
		super();
	}
	
	/**
	 * This class maps a template database tuple to the Ballot Template object by using
	 * a RowMapper interface to fetch the records for a Ballot Template from the database.
	 */
	private class TemplateMapper implements RowMapper<BallotTemplate> {
		@Override
		public BallotTemplate mapRow(ResultSet rs, int rowNum) throws SQLException {
			BallotTemplate bt = new BallotTemplate();
			bt.setId(rs.getInt("tid"));
			bt.setTemplateTitle(rs.getString("template_title"));
			bt.setBallotTitle(rs.getString("ballot_title"));
			bt.setInstructions(rs.getString("instructions"));
			bt.setDescription(rs.getString("description"));
			bt.setTypeOfVote(rs.getString("vote_type"));
			bt.setOutcomes(rs.getInt("outcomes"));
			bt.setBasis(rs.getString("faculty").equals("TRUE"));
			
			return bt;
		}
		
	}
	
	/*
	 * Some helper methods to modify strings for queries
	 */
	public String dq(String s) {
		return s.replace("'", "''");
	}
	
	public String undoDq(String s) {
		return s.replace("''", "'");
	}
	

	/**
	 * Lists all current Ballot Templates in database
	 * 
	 * @return list of all templates
	 * @throws exception if query fails
	 */
	@Override
	public List<BallotTemplate> listAll() throws Exception {
		
		log.debug("listing templates");
		
		String sql = "SELECT * FROM template";
		List<BallotTemplate> results = getJdbcTemplate().query(sql, new TemplateMapper());
		return results;
	}
	
	/**
	 * Creates and inserts a new Ballot Template into database using parameters
	 * 
	 * @return new template
	 * @throws exception if query fails
	 */
	@Override
	public BallotTemplate create(String tTitle, String bTitle, String instr, String desc, String voteType,
			int outcomes, boolean basis) {
		
		log.debug("creating new template");
		
		String sql = String.format("INSERT INTO template (template_title, ballot_title, instructions, description, vote_type, outcomes, faculty) VALUES ('%s', '%s', '%s', '%s', '%s', %d, %s)",
				tTitle, bTitle, instr, desc, voteType, outcomes, String.valueOf(basis).toUpperCase());
		
		try {
		KeyHolder k = new GeneratedKeyHolder();
		int num = getJdbcTemplate().update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			return ps;
		}, k);
		
		return this.get(k.getKey().intValue());
		}
		catch(Exception e)
		{
			String msg = "unable to create template";
			log.error(msg);
			//throw new Exception(msg);
		
		return null;
		}
		
		
	}

	/**
	 * Locates and returns the Ballot Template from database with matching id
	 * 
	 * @return matching template
	 * @throws exception if query fails
	 */
	@Override
	public BallotTemplate get(Integer tid) throws Exception {
		
		log.debug("getting template #{}", tid);
		
		if(tid < 0)
			throw new Exception("invalid template id");
		
		String sql = String.format("SELECT * FROM template WHERE tid = %d", tid);
		
		List<BallotTemplate> results = this.getJdbcTemplate().query(sql, new TemplateMapper());
		
		if(results.size() != 1) {
			String msg = String.format("unable to get template #%d", tid);
			log.error(msg);
			throw new Exception(msg);
		}
		
		return results.get(0);
	}

	/**
	 * Edits the specified Ballot Template with new attributes
	 * 
	 * @return edited template
	 * @throws exception if query fails
	 */
	@Override
	public BallotTemplate edit(Integer tid, String tTitle, String bTitle, String instr, String desc, String voteType,
			int outcomes, boolean basis) throws Exception {
		
		log.debug("editing template #{}", tid);
		
		String sql = String.format("UPDATE template SET template_title = '%s', ballot_title = '%s', instructions = '%s', description = '%s', vote_type = '%s', outcomes = %d, faculty = %b WHERE tid = %d",
				tTitle, bTitle, instr, desc, voteType, outcomes, basis, tid);
		int rc = getJdbcTemplate().update(sql);
		
		if(rc < 1) {
			String msg = String.format("unable to edit template #%d", tid);
			log.error(msg);
			throw new Exception(msg);
		}
		
		return this.get(tid);
	}

	/**
	 * Deletes a Ballot Template with matching id from database
	 * 
	 * @return true if deleted
	 * @throws exception if query fails
	 */
	@Override
	public boolean delete(Integer tid) throws Exception {
		
		log.debug("deleting template #{}", tid);
		int rc = getJdbcTemplate().update(String.format("DELETE FROM template WHERE tid = %d", tid));
		
		if(rc != 1) {
			String msg = String.format("unable to delete template #%d", tid);
			log.error(msg);
			throw new Exception(msg);
		}
		
		return true;
	}

}
