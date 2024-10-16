package edu.austincollege.acvote.users;

import java.io.Serializable;
import java.util.Objects;

/**
 * Instances of AcUsers will have 2 attributes: uid (user id) and role
 * uid will be the users Austin College network id ("mhiggs") and the role
 * will describe the user's usage rights on the app. Roles include
 * ADMIN VIEWER and EDITOR
 *
 */
public class AcUser implements Serializable {

	private static final long serialVersionUID = -1721821566651579145L;

	//Austin College network id
	private String uid;

	//ADMIN or VIEWER or EDITOR
	private String role;
	
	//Default Constructor
	public AcUser() {
		this.uid = "";
		this.role = "";
	}

	/**
	 * Parameterized constructor
	 * 
	 * @param uid
	 * @param role
	 */
	public AcUser(String uid, String role) {
		this.uid = uid;
		this.role = role;
	}

	@Override
	public int hashCode() {
		return Objects.hash(role, uid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AcUser other = (AcUser) obj;
		return Objects.equals(role, other.role) && Objects.equals(uid, other.uid);
	}

	@Override
	public String toString() {
		return "User [uid=" + uid + ", role=" + role + "]";
	}

	/*
	 * Getters and Setters
	 */
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
