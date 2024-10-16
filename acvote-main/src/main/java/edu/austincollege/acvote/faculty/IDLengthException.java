package edu.austincollege.acvote.faculty;

public class IDLengthException extends Exception{
	public IDLengthException() {
		super("The entered ID is too long");
	}
}