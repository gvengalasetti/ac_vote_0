package edu.austincollege.acvote.faculty.dao;

import java.util.List;

import edu.austincollege.acvote.faculty.Faculty;

/**
 * Data access object interface for Faculty objects. Should be implemented
 * by and and all objects attempting to access Faculty data from database.
 * 
 * @author Alan Rosenberg
 *
 */
public interface FacultyDAO {

	/**
	 * listAll() returns a list of all Faculty stored in the database.
	 * 
	 * @return List<Faculty> of all faculty
	 * @throws Exception when issues with communicating with database
	 */
	public List<Faculty> listAll() throws Exception;
	
	/**
	 * create() will tell the database to create a new Faculty tuple.
	 * Uses arguments to construct Faculty with specific fields.
	 * 
	 * @param acId
	 * @param lastName
	 * @param firstName
	 * @param dept
	 * @param div
	 * @param rank
	 * @param email
	 * @param tenure
	 * @param voting
	 * @param active
	 * @return Faculty object that has been created
	 * @throws Exception when issues with communicating with database
	 */
	public Faculty create(String acId, String lastName, String firstName, String dept, String div, String rank, String email, String tenure, boolean voting, boolean active) throws Exception;
	
	/**
	 * delete() will tell the database to delete a Faculty tuple.
	 * Argument specifies which Faculty object to be deleted.
	 * 
	 * @param pId
	 * @throws Exception when issues with communicating with database
	 */
	public void delete(String pId) throws Exception;
	
	/**
	 * update() will tell the database to update a Faculty tuple.
	 * Uses arguments to update Faculty tuple with specific fields.
	 * 
	 * @param acId
	 * @param lastName
	 * @param firstName
	 * @param dept
	 * @param div
	 * @param rank
	 * @param email
	 * @param tenure
	 * @param voting
	 * @param active
	 * @throws Exception when issues with communicating with database
	 */
	public void update(String acId, String lastName, String firstName, String dept, String div, String rank, String email, String tenure, boolean voting, boolean active) throws Exception;
	
	/**
	 * findFacultyByID will tell the data to return a Faculty tuple.
	 * Uses argument to specify exactly which tuple to return
	 * 
	 * @param pId
	 * @return Faculty object found, or null if not found
	 * @throws Exception when issues with communicating with database
	 */
	public Faculty findFacultyByID(String acId) throws Exception;
	
	
	/**
	 * Removes all existing faculty from our data store.
	 */
	public void deleteAll() throws Exception;
	

	/**
	 * Inserts each of the faculty objects into our data store
	 * 
	 * @param lst
	 * @throws Exception
	 */
	public void insertAll(List<Faculty> lst) throws Exception;
	
	
	
}
