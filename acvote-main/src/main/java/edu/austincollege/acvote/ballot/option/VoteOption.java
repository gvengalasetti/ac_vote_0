package edu.austincollege.acvote.ballot.option;

import java.io.Serializable;
import java.util.Objects;

public class VoteOption implements Serializable {

	private static final long serialVersionUID = 5482490785414318099L;
	
	/**
	 * Unique identifier for the option
	 */
	protected String optionID;
	
	/**
	 * Title of the option (what shows on the ballot)
	 */
	protected String title;
	

	/**
	 * When the option is not enabled, it will NOT be counted when tallying 
	 * the results.
	 */
	protected boolean enabled;
	
	
	/**
	 * Basic empty constructor (Bean compliance)
	 */
	public VoteOption() {
		super();
	}

	/**
	 * Creates a vote option with the given ID and title
	 * 
	 * @param optionID unique identifier for a given option
	 * @param title display name for a given option
	 */
	public VoteOption(String optionID, String title, boolean enabledFlag ) {
		super();
		this.optionID = optionID;
		this.title = title;
		this.enabled = enabledFlag;
	}

	public VoteOption(String oid, String lbl) {
		this(oid,lbl,true);
	}

	/**
	 * Returns the unique option ID of the option
	 * 
	 * @return unique option ID as a String
	 */
	public String getoptionID() {
		return optionID;
	}

	/**
	 * Sets the option ID (must be unique - please do not duplicate an existing option)
	 * 
	 * @param optionID
	 */
	public void setoptionID(String optionID) {
		this.optionID = optionID;
	}

	/**
	 * Returns the option title
	 * 
	 * @return option title as a string
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the options title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public int hashCode() {
		return Objects.hash(optionID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VoteOption other = (VoteOption) obj;
		return enabled == other.enabled && Objects.equals(optionID, other.optionID)
				&& Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "VoteOption [optionID=" + optionID + ", title=" + title + ", enabled=" + enabled + "]";
	}
	
	
	

}
