package edu.austincollege.acvote.template.dao;

import java.util.List;

import edu.austincollege.acvote.template.BallotTemplate;

/**
 * An instance of a template dao will handle all CRUD operations 
 * regarding ballot templates.
 *
 */
public interface TemplateDao {

	/**
	 * listAll will return the exhaustive list of all ballot templates
	 * 
	 * @return list of all templates
	 * @throws Exception if unable
	 */
	public List<BallotTemplate> listAll() throws Exception;
	
	/**
	 * create will create a new ballot template with the given parameters
	 * 
	 * @param tid
	 * @param tTitle
	 * @param bTitle
	 * @param instr
	 * @param desc
	 * @param voteType
	 * @param group
	 * @param basis
	 * @return template created
	 * @throws Exception if unable
	 */
	public BallotTemplate create(String tTitle, String bTitle, String instr, String desc, String voteType, int outcomes, boolean basis) throws Exception;
	
	/**
	 * get will return a ballot template with a matching id
	 * 
	 * @param id of desired template
	 * @return matching template
	 * @throws Exception if unable
	 */
	public BallotTemplate get(Integer tid) throws Exception;
	
	/**
	 * edit will edit a ballot template with a matching id with the parameters
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
	 * @throws Exception if unable
	 */
	public BallotTemplate edit(Integer tid, String tTitle, String bTitle, String instr, String desc, String voteType, int outcomes, boolean basis) throws Exception;
	
	/**
	 * delete will delete a ballot template with a matching id
	 * 
	 * @param id of desired template
	 * @return true if delete is successful
	 * @throws Exception if unable
	 */
	public boolean delete(Integer tid) throws Exception;
}
