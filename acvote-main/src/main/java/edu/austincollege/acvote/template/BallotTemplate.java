package edu.austincollege.acvote.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * BallotTemplate will store some fields of a ballot to enable admins to quickly and
 * easily create new ballots from a template. Templates will have a unique ID and title
 * to identify the template, as well as ballot fields in order to fill a new ballot.
 * 
 * @author Alan Rosenberg, Tobias Ward
 *
 */
public class BallotTemplate implements Serializable {

	private static final long serialVersionUID = -2995481055010004423L;

	//unique id number of a template
	private int id;
	
	//title of template to give context to template
	private String templateTitle;
	
	//title of the ballot to display
	private String ballotTitle;
	
	//instructions for the current ballot
	private String instructions;
	
	//description of the current ballot
	private String description;
	
	//type of vote selection
	private String typeOfVote;
	
	//number of ballot outcomes
	private int outcomes;
	
	//Indicates whether template will be faculty based or custom based
	private boolean basis;
	
	//default constructor
	public BallotTemplate() {
		super();
		this.id = (int) ((System.currentTimeMillis()/1000) % Integer.MAX_VALUE);
	}

	/**
	 * parameterized!
	 * 
	 * @param templateID
	 * @param templateTitle
	 * @param ballotTitle
	 * @param instructions
	 * @param description
	 * @param typeOfVote
	 * @param outcomes
	 * @param basis
	 */
	public BallotTemplate(int templateID, String templateTitle, String ballotTitle, String instructions,
			String description, String typeOfVote, int outcomes, boolean basis) {
		super();
		this.id = templateID;
		this.templateTitle = templateTitle;
		this.ballotTitle = ballotTitle;
		this.instructions = instructions;
		this.description = description;
		this.typeOfVote = typeOfVote;
		this.outcomes = outcomes;
		this.basis = basis;
	}
	

	@Override
	public int hashCode() {
		return Objects.hash(ballotTitle, basis, description, id, instructions, outcomes,
				templateTitle, typeOfVote);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BallotTemplate other = (BallotTemplate) obj;
		return Objects.equals(ballotTitle, other.ballotTitle) && basis == other.basis
				&& Objects.equals(description, other.description) && id == other.id
				&& Objects.equals(instructions, other.instructions) && outcomes == other.outcomes
				&& Objects.equals(templateTitle, other.templateTitle)
				&& Objects.equals(typeOfVote, other.typeOfVote);
	}

	@Override
	public String toString() {
		return "BallotTemplate [id=" + id + ", templateTitle=" + templateTitle + ", ballotTitle=" + ballotTitle
				+ ", instructions=" + instructions + ", description=" + description + ", typeOfVote=" + typeOfVote
				+ ", outcomes=" + outcomes + ", basis=" + basis + "]";
	}
	
	/*
	 * Getters and Setters
	 */

	public int getId() {
		return id;
	}

	public void setId(int templateID) {
		this.id = templateID;
	}

	public String getTemplateTitle() {
		return templateTitle;
	}

	public void setTemplateTitle(String templateTitle) {
		this.templateTitle = templateTitle;
	}
	
	public String getBallotTitle() {
		return ballotTitle;
	}

	public void setBallotTitle(String ballotTitle) {
		this.ballotTitle = ballotTitle;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTypeOfVote() {
		return typeOfVote;
	}

	public void setTypeOfVote(String typeOfVote) {
		this.typeOfVote = typeOfVote;
	}

	public int getOutcomes() {
		return outcomes;
	}

	public void setOutcomes(int outcomes) {
		this.outcomes = outcomes;
	}

	public boolean isBasis() {
		return basis;
	}

	public void setBasis(boolean basis) {
		this.basis = basis;
	}
	
}