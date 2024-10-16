package edu.austincollege.acvote.faculty.dao;

import java.util.List;
import java.util.ArrayList;
import edu.austincollege.acvote.faculty.Faculty;

/**
 * An instance of this class can be used for testing and for initial
 * development. We provide the basic faculty dao functions. To use this kind of
 * doa, we need see the DaoRegistry config file.
 * 
 * <p>
 * Sorry, could not wait any longer for this
 * </p>
 * 
 * @author mahiggs and others
 *
 */
public class DummyFacultyDAO implements FacultyDAO {

	private List<Faculty> faculty = new ArrayList<>();
	private int IDcounter;

	/**
	 * Constructor
	 */
	public DummyFacultyDAO() {
		faculty = new ArrayList<>();
		IDcounter = 0;

		initialize();
	}

	/**
	 * listAll() returns a list of all Faculty stored in the database.
	 * 
	 * @return List<Faculty> of all faculty
	 * @throws Exception when issues with communicating with database
	 */
	@Override
	public List<Faculty> listAll() throws Exception {
		return faculty;
	}

	/**
	 * create() will tell the database to create a new Faculty tuple. Uses arguments
	 * to construct Faculty with specific fields.
	 * 
	 * @return Faculty object that has been created
	 * @throws Exception when issues with communicating with database
	 */
	@Override
	public Faculty create(String acId, String lastName, String firstName, String dept, String div, String rank,
			String email, String tenure, boolean voting, boolean active) throws Exception {

		Faculty f = new Faculty();
		IDcounter++;

		f.setAcId(String.valueOf(IDcounter));
		f.setLastName(lastName);
		f.setFirstName(firstName);
		f.setDept(dept);
		f.setDiv(div);
		f.setRank(rank);
		f.setEmail(email);
		f.setTenure(tenure);
		f.setVoting(voting);
		f.setVoting(active);

		faculty.add(f);

		return f;
	}

	/**
	 * delete() will tell the database to delete a Faculty tuple. Argument specifies
	 * which Faculty object to be deleted.
	 * 
	 * @throws Exception when issues with communicating with database
	 */
	@Override
	public void delete(String acId) throws Exception {

		Faculty f = this.findFacultyByID(acId);

		if (f == null)
			throw new Exception(String.format("unable to delete faculty [%s]", acId));

		faculty.remove(f);
	}

	/**
	 * update() will tell the database to update a Faculty tuple. Uses arguments to
	 * update Faculty tuple with specific fields.
	 * 
	 * @throws Exception when issues with communicating with database
	 */
	@Override
	public void update(String acId, String lastName, String firstName, String dept, String div, String rank,
			String email, String tenure, boolean voting, boolean active) throws Exception {

		Faculty f = this.findFacultyByID(acId);

		if (f == null)
			throw new Exception(String.format("unable to update faculty [%s]; not found", acId));

		f.setLastName(lastName);
		f.setFirstName(firstName);
		f.setDept(dept);
		f.setDiv(div);
		f.setRank(rank);
		f.setEmail(email);
		f.setTenure(tenure);
		f.setVoting(voting);
		f.setVoting(active);

	}

	/**
	 * findFacultyByID will tell the data to return a Faculty tuple. Uses argument
	 * to specify exactly which tuple to return
	 * 
	 * @param acId
	 * @return Faculty object found, or null if not found
	 * @throws Exception when issues with communicating with database
	 */
	@Override
	public Faculty findFacultyByID(String acId) throws Exception {

		if (acId == null)
			throw new Exception("missing faculty id");
		acId = acId.trim();

		if (acId.length() == 0)
			throw new Exception("empty faculty id");

		for (Faculty f : faculty) {
			if (f.getAcId().equalsIgnoreCase(acId))
				return f;
		}

		return null;
	}

	/*
	 * generates example faculty with example data, testing purposes
	 */
	public void initialize() {
		Faculty f1 = new Faculty("1", "Higgs", "Michael", "MACS", "SC", "PROF", "mhiggs@austincollege.edu", "T", true, true);
		Faculty f2 = new Faculty("2", "Block", "Aaron", "MACS", "SC", "ASOP", "ablock@austincollege.edu", "T", true, true);
		Faculty f3 = new Faculty("3", "Edge", "Josh", "MACS", "SS", "ASOP", "jedge@austincollege.edu", "TT", true, true);
		Faculty f4 = new Faculty("4", "Bumpus", "J'Lee", "MACS", "SC", "ASOP", "jbumpus@austincollege.edu", "T", true, true);
		Faculty f5 = new Faculty("5", "Mealy", "Jack", "MACS", "SC", "PROF", "jmealy@austincollege.edu", "T", true, true);

		faculty.add(f1);
		faculty.add(f2);
		faculty.add(f3);
		faculty.add(f4);
		faculty.add(f5);
	}

	/**
	 * Removes all existing faculty from our data store.
	 */
	@Override
	public void deleteAll() throws Exception {

		this.faculty.clear();
	}

	/**
	 * Inserts each of the faculty objects into our data store
	 * 
	 * @param lst
	 * @throws Exception
	 */
	@Override
	public void insertAll(List<Faculty> lst) throws Exception {

		for (Faculty f : lst) {
			this.faculty.add(f);
		}

	}

}
