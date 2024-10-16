package edu.austincollege.acvote.faculty;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import edu.austincollege.acvote.faculty.dao.FacultyDAO;
import edu.austincollege.acvote.properties.AcProperties;
import edu.austincollege.acvote.properties.dao.PropertiesDao;

@Service
public class FacultyService {
	
	private static final String KEY_FACULTY_LAST_IMPORT = "faculty.last.import";


	private static Logger log = LoggerFactory.getLogger(FacultyService.class);
	
	
	@Autowired
	FacultyIO facultyIO = new FacultyIO();
	
	@Autowired
	FacultyDAO facultyDao;
	
	@Autowired
	PropertiesDao pDao;
	
	/**
	 * Generic constructor for Faculty Service
	 */
	public FacultyService() {
	}
	
	public List<Faculty> getAllFaculty() {
		
		try {
			return facultyDao.listAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return new ArrayList<Faculty>();
		}
		
	}
	

	/**
	 * This method will provide access to the ALL faculty in our data store
	 * 
	 * @return a list of faculty in the data store.
	 */
	public List<Faculty> listAll(){
		try {
			return facultyDao.listAll();
		} catch (Exception e) {
			return new ArrayList<Faculty>();
		}
	}
	
	/**
	 * Method to fetch a specific Faculty with the specific ac id.
	 * 
	 * @param acid
	 * @return the faculty or null if not successful
	 */
	public Faculty findFaculty(String acId) {
		try {
			return facultyDao.findFacultyByID(acId);
		} catch (Exception e) {
			//e.printStackTrace(); I don't know why this is here, but it's in BMI service/
			return null;
		}
	}
	
	//Function below could possibly be merged with the one above
	
	/**
	 * Method to fetch a list of specified Faculty with their specific ac id.
	 * 
	 * @param String[] acid
	 * @return the list of faculty or null if not successful
	 */
	public List<Faculty> findFacultyMembers(String[] acId) {
		List<Faculty> faculty = new ArrayList<Faculty>();
		for (String id:acId) {
			try {
				faculty.add(facultyDao.findFacultyByID(id));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return faculty;
	}
	
	/**
	 * Method to remove the specified faculty from the data store.
	 * 
	 * @param pid
	 * @return true when successful
	 */
	public boolean deleteFaculty(String acId) {
		try{
			facultyDao.delete(acId);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * This method updates an existing faculty identified by pid using the attributes provided.
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
	 * @throws Exception
	 */
	public void updateFaculty(String acId, String lastName, String firstName, String dept, String div, String rank, String email,
			String tenure, boolean voting, boolean active) throws Exception{
		facultyDao.update(acId, lastName, firstName, dept, div, rank, email, tenure, voting, active);
	}
	
	/**This method will create new faculty members 
	 * 
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
	 * @throws IDLengthException 
	 */
	public void addFaculty(String acId, String lastName, String firstName, String dept, String div, String rank, String email,
			String tenure, boolean voting, boolean active) throws DuplicateFacultyException, IDLengthException {
		
		log.debug("service creating faculty #{}", acId);
		
		/*
		 * Check that the faculty is not already there
		 */
		try {
			Faculty f = facultyDao.findFacultyByID(acId);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("no faculty exists with acId #{}", acId);
		}
		
		/*
		 * construct the faculty
		 */
		try {
			Faculty f = facultyDao.create(acId, lastName, firstName, dept, div, rank, email, tenure, voting, active);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new DuplicateFacultyException(acId);
		}
	}
	
	
	
	/**
	 * Given a handle on a CSV text file input stream, we read and replace the faculty
	 * in our data store.   
	 *  
	 * @param stream -- CSV text file
	 * @throws Exception
	 * 
	 * @return number of new faculty read
	 */
	public int importFacultyFromStream(InputStream stream) throws Exception {
		
		log.info("importing faculty...");
		
		facultyDao.deleteAll();   // gets rid of all faculty
		
		List<Faculty> faculty = facultyIO.readFacultyFile(stream);
		
		facultyDao.insertAll(faculty);
		
		pDao.setProperty(KEY_FACULTY_LAST_IMPORT, LocalDateTime.now().toString());
		
		return faculty.size();
	}
	
	
	/**
	 * Returns the local datetime instance from faculty.last.import property
	 * in data store or null if not known.  
	 * 
	 * @return
	 */
	public LocalDateTime mostRecentImportDate() {
		try {
			AcProperties prop = pDao.returnProperty(KEY_FACULTY_LAST_IMPORT);
			return LocalDateTime.parse(prop.getPropval());
		} catch (Exception e) {
			return null;
		}
	}
	

	//----------Getters-and-Setters----------\\
	public void setDao(FacultyDAO dao) {
		this.facultyDao = dao;
	}

	
	public FacultyDAO getDao() {
		return facultyDao;
	}


	
	
}
