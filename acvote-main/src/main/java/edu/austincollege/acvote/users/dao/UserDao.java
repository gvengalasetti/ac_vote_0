package edu.austincollege.acvote.users.dao;

import java.util.List;

import edu.austincollege.acvote.users.AcUser;

/**
 * An interface that creates a Dao object for the Users that can be used to make
 * the Users
 *
 */
public interface UserDao {
	/**
	 * Lists all users in the database
	 * 
	 * @return list of all Users.
	 * @throws Exception
	 */
	public List<AcUser> listAll() throws Exception;
	
	/**
	 * Lists all users of a particular role in db
	 * 
	 * @param role
	 * @return List of users with specific role
	 * @throws Exception
	 */
	public List<AcUser> listAll(String role) throws Exception;

	/**
	 * Finds user with matching id
	 * 
	 * @param id of desired user
	 * @return user with matching id
	 * @throws Exception if invalid id
	 */
	public AcUser userById(String uid) throws Exception;

	/**
	 * Creates a new user
	 * 
	 * @param uId
	 * @param role
	 * @return new user
	 * @throws Exception
	 */
	public void create(String uId, String role) throws Exception;

	/**
	 * Deletes user of matching uid
	 * 
	 * @param uid
	 * @throws Exception
	 */
	public void delete(String uid) throws Exception;
}
