package edu.austincollege.acvote.template;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.austincollege.acvote.template.dao.TemplateDao;

/**
 * Helper for controller. Handles CRUD requests from controller by passing on requests
 * to injected TemplateDao.
 *
 */
@Service
public class TemplateService {
	
	private static Logger log = LoggerFactory.getLogger(TemplateService.class);
	
	@Autowired
	TemplateDao templateDao;
	
	public TemplateService() {
		
	}
	
	/**
	 * Lists all ballot templates in database
	 * 
	 * @return list of all templates
	 */
	public List<BallotTemplate> listTemplates() {
		
		log.debug("service listing templates");
		
		try {
			//calling dao to list all
			return templateDao.listAll();
		}
		catch (Exception e) {
			
			log.error("cannot list templates");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Creates ballot template with parameters
	 * 
	 * @param tTitle
	 * @param bTitle
	 * @param instr
	 * @param desc
	 * @param voteType
	 * @param group
	 * @param basis
	 * @return new template
	 */
	public BallotTemplate createTemplate(String tTitle, String bTitle, String instr, String desc, String voteType, int outcomes, boolean basis) {
		
		log.debug("service creating template");
		
		try {
			//calling dao to create template
			return templateDao.create(tTitle, bTitle, instr, desc, voteType, outcomes, basis);
		}
		catch (Exception e) {
			
			log.error("cannot create template");
			e.printStackTrace();
			return null;
		}
	}

	
	/**
	 * Gets ballot template with matching id
	 * 
	 * @param id of desired template
	 * @return matching template
	 */
	public BallotTemplate getTemplate(int tid) {
		
		log.debug("getting template #{}", tid);
		
		try {
			//calling dao to get template
			return templateDao.get(tid);
		}
		catch (Exception e) {
			
			log.error("cannot get template #{}", tid);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Edits ballot template of matching id with parameters
	 * 
	 * @param tid
	 * @param tTitle
	 * @param bTitle
	 * @param instr
	 * @param desc
	 * @param voteType
	 * @param group
	 * @param basis
	 * @return edited template
	 */
	public BallotTemplate editTemplate(int tid, String tTitle, String bTitle, String instr, String desc, String voteType, int outcomes, boolean basis) {
		
		log.debug("editing template #{}", tid);
		
		try {
			//calling dao to edit template
			return templateDao.edit(tid, tTitle, bTitle, instr, desc, voteType, outcomes, basis);
		}
		catch (Exception e) {
			
			log.error("cannot edit template #{}", tid);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Deletes ballot template of matching id
	 * 
	 * @param id of desired template
	 * @return true if deleted, false if not
	 */
	public boolean deleteTemplate(int tid) {
		
		log.debug("deleting template #{}", tid);
		
		try {
			//calling dao to delete template
			return templateDao.delete(tid);
		}
		catch (Exception e) {
			
			log.error("cannot delete template #{}", tid);
			e.printStackTrace();
			return false;
		}
	}
}
