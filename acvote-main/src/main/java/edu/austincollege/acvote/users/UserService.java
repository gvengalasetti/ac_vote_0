package edu.austincollege.acvote.users;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.austincollege.acvote.users.dao.UserDao;

@Service
public class UserService {
	
	private static Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	UserDao userDao;

	/**
	 * Lists all users in db
	 * 
	 * @return list of all users
	 */
	public List<AcUser> listAllUsers() {
		
		log.debug("service listing all users");
		
		try {
			
			List<AcUser> users = userDao.listAll();
			
			log.debug("service listed all users");
			return users;
		}
		catch (Exception e) {
			
			log.warn("service could not list all users");
			return new ArrayList<AcUser>();
		}

	}
	
	/**
	 * Creates and adds new user to db
	 * 
	 * @param uid
	 * @param role
	 * @return
	 * @throws DuplicateUIDException if duplicate uid
	 * @throws NullUIDException if uid null
	 * @throws Exception if generic error with creation
	 */
	public void addUser(String uid, String role) throws DuplicateUIDException, NullUIDException, Exception {
		
		log.debug("service creating {} user {}", role, uid);
		
		try {
			userDao.create(uid, role);
			
			log.debug("service created {} user {}", role, uid);
		}
		catch (DuplicateUIDException e) {
			
			log.warn("service could not create {} user {}: duplicate uid", role, uid);
			throw e;
		}
		catch (NullUIDException e) {
			
			log.warn("service could not create {} user {}: null uid", role, uid);
			throw e;
		}
		catch (Exception e) {
			
			log.warn("service could not create {} user {}", role, uid);
			throw e;
		}

	}
	
	/**
	 * Deletes a user with matching uid
	 * 
	 * @param uid
	 * @return true if success, false if not
	 */
	public boolean deleteUser(String uid) {
		
		log.debug("service deleting user {}", uid);
		
		try {
			
			userDao.delete(uid);
			
			log.debug("service deleted user {}", uid);
			return true;
		}
		catch (Exception e) {
			
			log.warn("service could not delete user {}", uid);
			return false;
		}
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	
}
