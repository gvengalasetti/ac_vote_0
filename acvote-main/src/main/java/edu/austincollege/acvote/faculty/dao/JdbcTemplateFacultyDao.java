package edu.austincollege.acvote.faculty.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import edu.austincollege.acvote.db.JdbcTemplateAbstractDao;
import edu.austincollege.acvote.faculty.Faculty;

/**
 * The JDBC Template that implements the FacultyDAO interface.
 * <p>
 * An instance of this class is responsible for getting data
 * the Faculty table in the database.
 * </p>
 * 
 */
@Component("JdbcTemplateFacultyDao")
public class JdbcTemplateFacultyDao extends JdbcTemplateAbstractDao implements FacultyDAO {

	private static Logger log = LoggerFactory.getLogger(JdbcTemplateFacultyDao.class);
	
	
	
	public JdbcTemplateFacultyDao() {
		super();
	}
	
	/**
	 * This class maps a faculty database tuple to the Faculty object by using
	 * a RowMapper interface to fetch the records for a Patient from the database.
	 */
	private class FacultyMapper implements RowMapper<Faculty> {
		@Override
		public Faculty mapRow(ResultSet rs, int rowNum) throws SQLException {
			Faculty f = new Faculty();
			f.setAcId(rs.getString("acid"));
			f.setLastName(undoDq(rs.getString("lname")));
			f.setFirstName(undoDq(rs.getString("fname")));
			f.setDept(rs.getString("dept"));
			f.setDiv(rs.getString("div"));
			f.setRank(rs.getString("rank"));
			f.setEmail(rs.getString("email"));
			f.setTenure(rs.getString("tenure"));
			f.setVoting(rs.getString("voting").equals("TRUE"));
			f.setActive(rs.getString("active").equals("TRUE"));

			return f;
		}
		
	}
	
	/**
	 * Lists all current Faculty that are in the database
	 * 
	 * @return list of all Faculty
	 */
	@Override
	public List<Faculty> listAll() throws Exception {

		log.debug("listing faculty");
		
		String sql = "SELECT * FROM faculty";
		List<Faculty> results = getJdbcTemplate().query(sql, new FacultyMapper());
		return results;
	}

	private String dq(String s) {
		return s.replace("'", "''");
	}
	
	private String undoDq(String s) {
		return s.replace("''", "'");
	}
	
	
	
	/**
	 * Creates and inserts a new Faculty into database using arguments
	 * 
	 * @throws exception when insertion fails
	 */
	@Override
	public Faculty create(String acId, String lastName, String firstName, String dept, String div, String rank,
			String email, String tenure, boolean voting, boolean active) throws Exception {
		
		log.debug("creating faculty = {}", acId);
		
		String sql = String.format("insert into faculty (acid, lname, fname, dept, div, rank, tenure, voting, email, active)"
				+ " values ('%s', '%s', '%s', '%s', '%s', '%s', '%s', %b, '%s', %b)", acId, dq(lastName), 
				dq(firstName), dept, div, rank, tenure, voting, email, active);
		
		/*
		 * in the following code we are using java8's closure (lambda expression)
		 * feature . It's like an anonymous inline class definition of a listener.
		 * JdbcTemplate update allows us to pass a snippet of code, given an untyped
		 * parameter (connection). Inside our code snippet, we can refer to the
		 * parameter by name. Our snippet returns a prepared statement (which is what
		 * the JdbcTemplate.update method requires as the first parameter. The second
		 * parameter of the update method is a keyholder object that we can ask for the
		 * database assigned auto number key value (a number).
		 * 
		 */
		int num = getJdbcTemplate().update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql);
			return ps;
		});

		if (num != 1) {
			String msg = String.format("Unable to create faculty [%s]", acId);
			log.error(msg);
			throw new Exception(msg);
		}

		return this.findFacultyByID(acId);
		
	}

	/**
	 * Deletes a Faculty identified by acId from database
	 * 
	 * @throws exception when delete fails
	 */
	@Override
	public void delete(String acId) throws Exception {

		log.debug("deleting faculty = {}",acId);
		int rc = getJdbcTemplate().update(String.format("DELETE FROM faculty WHERE acid='%s'",acId));
		log.debug("...back with {}", rc);
		if (rc != 1) {
			String msg = String.format("Unable to delete faculty [%s]", acId);
			log.error(msg);
			throw new Exception(msg);
		}
	}

	/**
	 * Updates the specified faculty with new attributes
	 * 
	 * @throws exception when update fails
	 */
	@Override
	public void update(String acId, String lastName, String firstName, String dept, String div, String rank, String email, String tenure, boolean voting, boolean active) throws Exception {
		
		log.debug("updating faculty = {}",acId);
		
		int rc = getJdbcTemplate().update(String.format("UPDATE faculty SET acid = '%s', lname = '%s', fname = '%s', rank = '%s', dept = '%s', div = '%s', tenure = '%s', voting = %b, email = '%s', active = %b   WHERE acid = '%s'",
				acId, dq(lastName), dq(firstName), rank, dept, div, tenure, voting, email, active, acId));

		if (rc < 1) {
			String msg = String.format("Unable to update faculty [%s]", acId);
			log.error(msg);
			throw new Exception(msg);
		}

	}

	/**
	 * Locates and returns the faculty from database with matching acId
	 * 
	 * @throws exception when faculty not found
	 */
	@Override
	public Faculty findFacultyByID(String acId) throws Exception {
		
		log.debug("finding faculty by ID = {}",acId);
		
		if (acId == null || "".equals(acId.strip()))
			throw new Exception("Invalid faculty id.");

		String sqlStr = String.format("SELECT * FROM faculty WHERE acid = '%s'", acId);
		log.debug(sqlStr);

		List<Faculty> results = this.getJdbcTemplate().query(sqlStr, new FacultyMapper());

		if (results.size() != 1) {
			log.error("Unable to fetch contact [{}]", acId);
			return null;
		}

		return results.get(0);
	}

	/**
	 * Removes all faculty from our data store.  
	 */
	@Override
	public void deleteAll() throws Exception {
	
		log.debug("deleting all faculty ");
		int rc = getJdbcTemplate().update(String.format("DELETE FROM faculty "));
		log.debug("...back with {}", rc);
		if (rc == 0) {
			String msg = String.format("Unable to delete faculty");
			log.error(msg);
			throw new Exception(msg);
		}
	}

	
	/**
	 * Given a list of faculty object, we insert them into our data store one by one.
	 * 
	 */
	@Override
	public void insertAll(List<Faculty> lst) throws Exception {
		log.debug("inserting all faculty...");
		
		for (Faculty f : lst) {
			
			this.create(f.getAcId(), f.getLastName(), f.getFirstName(), f.getDept(),
					f.getDiv(), f.getRank(),f.getEmail(),f.getTenure(),f.isVoting(), f.isActive());
			
		}
	}
	
	

}
