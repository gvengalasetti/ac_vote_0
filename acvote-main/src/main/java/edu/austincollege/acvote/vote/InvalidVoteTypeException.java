package edu.austincollege.acvote.vote;

public class InvalidVoteTypeException extends Exception {

	private static final long serialVersionUID = -4273566348405393169L;

	public InvalidVoteTypeException(String kind) {
		super(String.format("Invalid type of vote: [%s]",kind));
	}

}
