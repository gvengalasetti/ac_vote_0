package edu.austincollege.acvote.template.dao;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.austincollege.acvote.template.BallotTemplate;

/**
 * DummyTemplateDao objects implement TemplateDao. They will handle the CRUD
 * functions regarding ballot objects but managing an ArrayList of templates
 *
 */
public class DummyTemplateDao implements TemplateDao {
	
	private Logger log = LoggerFactory.getLogger(DummyTemplateDao.class);
	
	private List<BallotTemplate> templates;		//list of templates
	private int IDCounter;		//used to make unique IDs
	
	/*
	 * Constructs DummyTemplateDao and performs initial operations to
	 * populate template list (dummy style)
	 */
	public DummyTemplateDao() throws Exception {
		templates = new ArrayList<>();
		IDCounter = 0;
		
		
			initialize();
		
	}
	
	private void initialize() throws Exception {
		this.create("Sample Ballot Template", "Best CS Prof", "sumbit your ballot, or else",
				"who's the goat?", "IRV", 2, true);
		this.create("Another Ballot Template", "Who is a CS Prof", "choose",
				"do you know who is actually a prof", "IRV", 2, true);
		this.create("One more Ballot Template", "Insert Title Here",
				"idk just vote on something", "italian beef", "IRV", 1, false);
	}

	/**
	 * Lists all ballot templates
	 * 
	 * @return list of all templates
	 * @throws Exception if unable (but not really)
	 */
	@Override
	public List<BallotTemplate> listAll() throws Exception {
		
		log.debug("listing all tempaltes");
		
		return templates;
	}

	/**
	 * Creates a new ballot template with the given parameters
	 * 
	 * @return new template
	 * @throws Exception if unable (but not really)
	 */
	@Override
	public BallotTemplate create(String tTitle, String bTitle, String instr, String desc, String voteType,
			int outcomes, boolean basis) throws Exception {
		
		log.debug("creating new template");
		
		//getting unique id
		IDCounter++;
		
		//creating
		BallotTemplate bt = new BallotTemplate(IDCounter, tTitle, bTitle, instr, desc, voteType, outcomes, basis);
		templates.add(bt);
		
		return bt;
	}

	/**
	 * Finds ballot template with matching id
	 * 
	 * @return matching template
	 * @throws Exception if unable
	 */
	@Override
	public BallotTemplate get(Integer tid) throws Exception {
		
		log.debug("getting template #{}", tid);
		
		//searching
		for(BallotTemplate bt : templates) {
			
			//returning when a match is found
			if(bt.getId() == tid) return bt;
		}
		
		log.debug("unable to get template #{}", tid);
		throw new Exception();
	}

	/**
	 * Edits a matching ballot template with parameters
	 * 
	 * @return edited template
	 * @throws Exception if unable
	 */
	@Override
	public BallotTemplate edit(Integer tid, String tTitle, String bTitle, String instr, String desc, String voteType,
			int outcomes, boolean basis) throws Exception {
		
		log.debug("editing template #{}", tid);
		
		//finding template
		BallotTemplate bt = this.get(tid);
		
		//editing
		bt.setTemplateTitle(tTitle);
		bt.setBallotTitle(bTitle);
		bt.setInstructions(instr);
		bt.setDescription(desc);
		bt.setTypeOfVote(voteType);
		bt.setOutcomes(outcomes);
		bt.setBasis(basis);
		
		return bt;
	}

	/**
	 * Deletes a ballot template with matching id
	 * 
	 * @return true if deleted
	 * @throws Exception if unable (but not really)
	 */
	@Override
	public boolean delete(Integer tid) throws Exception {
		
		log.debug("deleting template #{}", tid);
		
		//finding template
		BallotTemplate bt = this.get(tid);
		
		//deleting
		templates.remove(bt);
		
		return true;
	}
	
	//Testing purposes only
	public void setTemplates(List<BallotTemplate> templates) {
		this.templates = templates;
	}

}
