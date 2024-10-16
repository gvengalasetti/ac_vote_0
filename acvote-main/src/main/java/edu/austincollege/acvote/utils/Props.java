package edu.austincollege.acvote.utils;

import org.springframework.beans.factory.annotation.Autowired;

import edu.austincollege.acvote.ballot.dao.BallotDao;
import edu.austincollege.acvote.properties.dao.PropertiesDao;
import edu.austincollege.acvote.vote.dao.VoteCastDao;

/**
 * Utility object primarily for adding to MVC model in order to access property values
 * in our AcProperty table.
 * 
 */
public class Props {

	
	@Autowired PropertiesDao propDao;

	public String propVal(String key) {
		try {
			return propDao.returnProperty(key).getPropval();
		} catch (Exception e) {
			return null;
		}
	}

}
