package edu.austincollege.acvote.users.dao;

import java.util.ArrayList;
import java.util.List;

import edu.austincollege.acvote.users.AcUser;

public class DummyUserDao implements UserDao {

	private List<AcUser> users;

	public DummyUserDao() {
		users = new ArrayList<>();

		try {
			initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initialize() throws Exception {
		AcUser u1 = new AcUser("arosenberg20", "Admin");
		AcUser u2 = new AcUser("gvengalasetti19", "User");
		AcUser u3 = new AcUser("kleahy20", "Editor");
		AcUser u4 = new AcUser("bhill20", "Admin");
		AcUser u5 = new AcUser("mhiggs", "User");
		AcUser u6 = new AcUser("ablock", "Editor");
		users.add(u1);
		users.add(u2);
		users.add(u3);
		users.add(u4);
		users.add(u5);
		users.add(u6);
	}
	
	/**
	 * Lists all users with a particular role
	 * 
	 * @param role
	 * @retrun List of matching users
	 * @throws Exception if stupid
	 * 
	 */
	@Override
	public List<AcUser> listAll(String role) throws Exception {
		
		List<AcUser> list = new ArrayList<>();
		
		//appending matching users to list
		for(AcUser u : users) {
			if(u.getUid().equals(role)) {
				list.add(u);
			}
		}
		
		//telling them they are stupid
		if(list.isEmpty()) {
			throw new Exception("invalid role");
		}
		
		//otherwise, returning list
		else{
			return list;
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
		return users;
	}

	/**
	 * Creates new user with parameters as attributes
	 * 
	 * @param uid
	 * @param role
	 * @return new user
	 * @throws Exception
	 */
	@Override
	public void create(String uId, String role) throws Exception {

		AcUser u = new AcUser(uId, role);
		u.setUid(uId);
		u.setRole(role);
		users.add(u);
	}

	/**
	 * Returns user with matching uid
	 * 
	 * @param uid
	 * @return matching user
	 * @throws Exception
	 */
	@Override
	public AcUser userById(String uid) throws Exception {
		for (AcUser u : users) {
			if (u.getUid().equals(uid)) {
				return u;
			}
		}

		throw new Exception("invalid User Id");
	}

	/**
	 * Deletes user with matching uid
	 * 
	 * @param uid
	 * @throws Exception
	 */
	@Override
	public void delete(String uid) throws Exception {

		AcUser u = this.userById(uid);

		if (u == null)
			throw new Exception(String.format("unable to delete User [%s]", uid));

		users.remove(u);
	}
}
