package edu.austincollege.acvote.ballot.dao;

/**
 * An instance of this class is thrown when the option id is not a valid one.
 * 
 * @author mahiggs
 *
 */
public class InvalidOptionIdException extends Exception {

	private static final long serialVersionUID = 1376670299538693524L;

	public InvalidOptionIdException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
