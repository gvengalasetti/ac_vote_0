package edu.austincollege.acvote.ballot.dao;

/**
 * An instance of this class is thrown when ballot ids are invalid.
 * 
 * @author mahiggs
 *
 */
public class InvalidBallotIdException extends Exception {

	private static final long serialVersionUID = -1845649107681230934L;

	public InvalidBallotIdException(String message) {
		super(message);
	}

}
