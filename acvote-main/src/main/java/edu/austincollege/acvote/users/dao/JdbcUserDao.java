package edu.austincollege.acvote.users.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import edu.austincollege.acvote.db.JdbcTemplateAbstractDao;
import edu.austincollege.acvote.users.AcUser;
import edu.austincollege.acvote.users.DuplicateUIDException;
import edu.austincollege.acvote.users.NullUIDException;

/**
 * An instance of this class will handle the CRUD operations relating to user
 * objects. Interacts with the actual database to manage users.
 *
 */
@Component("JdbcUserDao")
public class JdbcUserDao extends JdbcTemplateAbstractDao implements UserDao {

	private Logger log = LoggerFactory.getLogger(JdbcUserDao.class);

	private class UserMapper implements RowMapper<AcUser> {

		@Override
		public AcUser mapRow(ResultSet rs, int rowNum) throws SQLException {
			AcUser u = new AcUser();

			u.setUid(rs.getString("uid"));
			u.setRole(rs.getString("role"));

			return u;
		}

	}

	/**
	 * Lists all users
	 * 
	 * @return List of users
	 * @throws Exception
	 */
	@Override
	public List<AcUser> listAll() throws Exception {

		log.debug("listing all users...");

		// querying
		String sql = "SELECT * FROM AcUser";
		List<AcUser> results = getJdbcTemplate().query(sql, new UserMapper());

		log.debug("listed all users");
		return results;

	}

	/**
	 * Lists all user with a particular role
	 * 
	 * @param role
	 * @return List of matching users
	 * @throws Exception if stupid
	 */
	@Override
	public List<AcUser> listAll(String role) throws Exception {

		log.debug("listing {} users", role);

		// preventing empty strings
		if (role == null || "".equals(role.strip())) {

			log.warn("unable to list {} users", role);
			throw new Exception("invalid role");
		}

		// querying
		String sql = String.format("SELECT * FROM AcUser WHERE role = '%s'", role);
		List<AcUser> results = getJdbcTemplate().query(sql, new UserMapper());

		log.debug("listed {} users", role);
		return results;
	}

	/**
	 * Returns user with specific uid
	 * 
	 * @param uid
	 * @return matching user
	 * @throws Exception
	 */
	@Override
	public AcUser userById(String uid) throws Exception {

		log.debug("finding user {}", uid);

		// preventing empty strings
		if (uid == null || "".equals(uid.strip())) {
			log.debug("unable to get user {}", uid);
			throw new NullUIDException("unable to create user: null uid");
		}

		// querying
		String sql = String.format("SELECT * FROM AcUser WHERE uid = '%s'", uid);
		List<AcUser> results = getJdbcTemplate().query(sql, new UserMapper());

		// throwing exception more or less than 1 result
		if (results.size() < 1) {
			String msg = String.format("no user %s exists", uid);
			log.debug(msg);
			throw new Exception(msg);
		} else if (results.size() > 1) {
			String msg = String.format("more than one user %s", uid);
			log.error(msg);
			throw new Exception(msg);
		}

		log.debug("found user {}", uid);
		return results.get(0);
	}

	/**
	 * Creates new user with parameters as attributes
	 * 
	 * @param uid
	 * @param role
	 * @return new user
	 * @throws NullUIDException      if uid null
	 * @throws DuplicateUIDException if duplicate uid
	 * @throws Exception             if generic error with creation
	 */
	@Override
	public void create(String uid, String role) throws NullUIDException, DuplicateUIDException, Exception {

		log.debug("creating user {}", uid);

		// preventing empty strings
		if (uid == null || "".equals(uid.strip())) {
			String msg = String.format("unable to create user %s: null uid", uid);
			log.warn(msg);
			throw new NullUIDException(msg);
		}

		// querying
		String sql = String.format("INSERT INTO AcUser (uid, role) VALUES ('%s', '%s')", uid, role);

		int num = 0;
		try {
			num = getJdbcTemplate().update(connection -> {
				PreparedStatement ps = connection.prepareStatement(sql);
				return ps;
			});
		}
		// preventing duplicate uids
		catch (DuplicateKeyException e) {
			String msg = String.format("unable to create user %s: duplicate uid", uid);
			log.warn(msg);
			throw new DuplicateUIDException(msg);
		}
		// catching other exceptions
		catch (Exception e) {
			String msg = String.format("unable to create user %s", uid);
			log.warn(msg);
			throw new Exception(msg);
		}

		if (num != 1) {
			String msg = String.format("unable to create user %s", uid);
			log.warn(msg);
			throw new Exception(msg);
		}

		log.debug("created user {}", uid);
	}

	/**
	 * Deletes user with matching uid
	 * 
	 * @param uid
	 * @throws Exception
	 */
	@Override
	public void delete(String uid) throws Exception {

		log.debug("deleting user {}", uid);

		int rc = getJdbcTemplate().update(String.format("DELETE FROM AcUser WHERE uid = '%s'", uid));

		if (rc != 1) {
			String msg = String.format("unable to delete user %s", uid);
			log.warn(msg);
			throw new Exception(msg);
		} else {
			log.debug("deleted user {}", uid);
		}
	}
}
