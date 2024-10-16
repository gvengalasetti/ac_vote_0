package edu.austincollege.acvote.ballot;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.austincollege.acvote.ballot.option.VoteOption;

/**
 * Simple data wrapper for ballots. Also capable of adding and removing options
 *
 */
public class Ballot implements Serializable {

	private static final long serialVersionUID = -7550887784646246797L;

	// Unique identifier
	private int id;

	// Title of the ballot
	private String title;

	// Instructions for the ballot
	private String instructions; // TODO: Consider changing this to a more efficient string storage system

	// Description of the ballot
	private String description; // TODO: Consider changing this to a more efficient string storage system

	// Whether options are based on faculty or custom options
	private boolean facultyBased;

	// List of candidates/options available to vote for
	private List<VoteOption> options;

	// Type of vote, just instant run-off vote for now
	private String typeOfVote;

	// Number of outcomes possible for a ballot
	private Integer outcomes;

	// Start date and time for a ballot
	private LocalDateTime startTime;

	// End date and time for a ballot
	private LocalDateTime endTime;

	// Group allow to vote on ballot
	private String voters;

	// Count of how many votes are expected on this ballot
	private int totalVotesExpected;

	/**
	 * Default constructor Assigns unique id and initializes options to empty list
	 */
	public Ballot() {

		super();
		this.id = (int) ((System.currentTimeMillis() / 1000) % Integer.MAX_VALUE);
		options = new ArrayList<>();
	}

	/**
	 * Parameterized constructor
	 * 
	 * @param id
	 * @param title
	 * @param instructions
	 * @param description
	 * @param options
	 * @param typeOfVote
	 * @param outcomes
	 * @param startTime
	 * @param endTime
	 * @param voters
	 * @param votesRecieved
	 * @param totalVotesExpected
	 */
	public Ballot(int id, String title, String instructions, String description, boolean facBased,
			List<VoteOption> options, String typeOfVote, Integer outcomes, LocalDateTime startTime,
			LocalDateTime endTime, String voters, int totalVotesExpected) {

		super();
		this.id = id;
		this.title = title;
		this.instructions = instructions;
		this.description = description;
		this.facultyBased = facBased;
		this.options = options;
		this.typeOfVote = typeOfVote;
		this.outcomes = outcomes;
		this.startTime = startTime;
		this.endTime = endTime;
		this.voters = voters;
		this.totalVotesExpected = totalVotesExpected;
	}

	/**
	 * Allows candidates to be added to the list easily
	 * 
	 * @param additional candidates to be added to candidate list
	 */
	public void addCandidates(VoteOption... additionalCandidates) {

		for (VoteOption cand : additionalCandidates) {
			this.options.add(cand);
		}
	}

	/**
	 * Allows candidates to be added to the list easily
	 * 
	 * @param additional candidates to be added to candidate list
	 */
	public void addCandidates(List<VoteOption> additionalCandidates) {

		for (VoteOption cand : additionalCandidates) {
			this.options.add(cand);
		}
	}

	/**
	 * Removes the given faculty from the candidate list
	 * 
	 * @param list of faculty to be removed from candidate list
	 */
	public boolean removeCandidates(VoteOption... removeList) {

		for (VoteOption cand : removeList) {
			if (!this.options.remove(cand)) { // removes the given candidate

				StringBuffer buffer = new StringBuffer();
				buffer.append("WARN: ");
				buffer.append(cand.getoptionID());
				buffer.append("");
				buffer.append(cand.getTitle());
				buffer.append(" was not in the list of candidates. (Ballot.removeCandidates)");

				System.err.println(buffer);
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, endTime, facultyBased, id, instructions, options, outcomes, startTime, title,
				totalVotesExpected, typeOfVote, voters);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ballot other = (Ballot) obj;
		return id == other.id;
	}

	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("Ballot [id=");
		buffer.append(id);
		buffer.append(", title=");
		buffer.append(title);
		buffer.append(", instructions=");
		buffer.append(instructions);
		buffer.append(", descirption=");
		buffer.append(description);
		buffer.append(", facultyBased=");
		buffer.append(facultyBased);
		buffer.append(", options=");
		buffer.append(options);
		buffer.append(", typeOfVote=");
		buffer.append(typeOfVote);
		buffer.append(", outcomes=");
		buffer.append(outcomes);
		buffer.append(", startTime=");
		buffer.append(startTime);
		buffer.append(", endTime=");
		buffer.append(endTime);
		buffer.append(", voters=");
		buffer.append(voters);
		buffer.append(", votesExpected=");
		buffer.append(totalVotesExpected);
		buffer.append("]");

		return buffer.toString();
	}

	/*
	 * Getters and Setters
	 */

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the instructions
	 */
	public String getInstructions() {
		return instructions;
	}

	/**
	 * @param instructions the instructions to set
	 */
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the facultyBased
	 */
	public boolean isFacultyBased() {
		return facultyBased;
	}

	/**
	 * @param facultyBased the facultyBased to set
	 */
	public void setFacultyBased(boolean facultyBased) {
		this.facultyBased = facultyBased;
	}

	/**
	 * @return the options
	 */
	public List<VoteOption> getOptions() {
		return options;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(List<VoteOption> options) {
		this.options = options;
	}

	/**
	 * @return the typeOfVote
	 */
	public String getTypeOfVote() {
		return typeOfVote;
	}

	/**
	 * @param typeOfVote the typeOfVote to set
	 */
	public void setTypeOfVote(String typeOfVote) {
		this.typeOfVote = typeOfVote;
	}

	/**
	 * @return the outcomes
	 */
	public Integer getOutcomes() {
		return outcomes;
	}

	/**
	 * @param outcomes the outcomes to set
	 */
	public void setOutcomes(Integer outcomes) {
		this.outcomes = outcomes;
	}

	/**
	 * @return the startTime
	 */
	public LocalDateTime getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public LocalDateTime getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the voters
	 */
	public String getVoters() {
		return voters;
	}

	/**
	 * @param voters the voters to set
	 */
	public void setVoters(String voters) {
		this.voters = voters;
	}

	/**
	 * @return the totalVotesExpected
	 */
	public int getTotalVotesExpected() {
		return totalVotesExpected;
	}

	/**
	 * @param totalVotesExpected the totalVotesExpected to set
	 */
	public void setTotalVotesExpected(int totalVotesExpected) {
		this.totalVotesExpected = totalVotesExpected;
	}

}
