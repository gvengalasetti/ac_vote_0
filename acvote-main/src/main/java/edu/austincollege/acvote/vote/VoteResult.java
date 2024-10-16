package edu.austincollege.acvote.vote;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.austincollege.acvote.ballot.Ballot;

/**
 * An instance of this class holds the results of a vote for a specific ballot.   The results
 * are coded in an ordered list of OptionScoreCard instances.  The first in the list is the 
 * most preferred option for the vote.   The second represents the second most preferred option.
 * <p>
 * If a ballot needs 3 outcomes, our <code>scoreCards</code> should contain 3 cards.   The cards
 * provide the initial distribution of votes as well as a list of ballots accumulated by the option
 * during the vote (and run-offs).
 * </p>
 * <p>
 * 
 * </p>
 * 
 * @author mahiggs
 * @See edu.austincollege.acvote.vote.OptionScoreCard
 */
public class VoteResult {

	private Ballot ballot;
	
	private List<OptionScoreCard> scoreCards;
	
	private List<VoteRound> rounds = new LinkedList<>();
	
	public void addRound(VoteRound vr) {
		rounds.add(vr);
	}
	
	
	public VoteResult() {
		super();
	}

	public VoteResult(Ballot ballot) {
		this.ballot = ballot;
		this.scoreCards = Collections.emptyList();
	}

	public Ballot getBallot() {
		return ballot;
	}

	public List<OptionScoreCard> getScoreCards() {
		return scoreCards;
	}

	public void setBallot(Ballot ballot) {
		this.ballot = ballot;
	}
	
	public void setScoreCards(List<OptionScoreCard> scoreCards) {
		this.scoreCards = scoreCards;
	}

	@Override
	public String toString() {
		return "VoteResult [scoreCards=" + scoreCards + "]";
	}

	public List<VoteRound> getRounds() {
		return rounds;
	}

	public void setRounds(List<VoteRound> rounds) {
		this.rounds = rounds;
	}
	
	

}
