package edu.austincollege.acvote.vote;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.austincollege.acvote.ballot.option.VoteOption;

public class VoteRound {
	
	private int rnum;   // round number
	
	private List<OptionScoreCard> options = Collections.emptyList();   // viable options for the current round
	
	private List<String> notes = new LinkedList<String>();  // collection of notes for audit log
	
	private VoteOption loser;   // losing option eliminated by this round

	public int getRnum() {
		return rnum;
	}

	public List<OptionScoreCard> getOptions() {
		return options;
	}

	public List<String> getNotes() {
		return notes;
	}

	public VoteOption getLoser() {
		return loser;
	}

	public void setRnum(int rnum) {
		this.rnum = rnum;
	}

	public void setOptions(List<OptionScoreCard> options) {
		this.options = options;
	}

	public void setNotes(List<String> notes) {
		this.notes = notes;
	}

	public void setLoser(VoteOption loser) {
		this.loser = loser;
	}

	public VoteRound(int rnum, List<OptionScoreCard> options) {
		super();
		this.rnum = rnum;
		this.options = options;
	}
	
	public void addNote(String note) {
		this.notes.add(note);
	}

	
	
	
}
