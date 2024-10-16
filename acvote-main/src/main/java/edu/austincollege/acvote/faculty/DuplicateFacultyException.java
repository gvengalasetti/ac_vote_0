package edu.austincollege.acvote.faculty;

public class DuplicateFacultyException extends Exception{
	public DuplicateFacultyException(String dupId) {
		super("Duplicate Patient Exception: "+dupId+" already exists.");
	}
}
