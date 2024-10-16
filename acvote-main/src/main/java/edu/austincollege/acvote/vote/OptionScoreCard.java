package edu.austincollege.acvote.vote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.austincollege.acvote.ballot.option.VoteOption;

/**
 * When counting ballots we keep a scorecard for each of the options.   The score
 * card remembers the associated option.
 *  
 * <p>
 * NOTE: this is a transient object used only when counting votes.
 * </p>
 * 
 * @author mahiggs
 *
 */
public class OptionScoreCard implements Comparable<OptionScoreCard> {

	
	VoteOption option;   // handle on the option for which we are counting
	
	int[] votes;    // initial distribution of supporting votes .... round 1
	
	List<VoteCast> ballots;  // current list of ballots for this option; may vary by round
	
	
	/**
	 * Primary constructor.  Creates a score card instance from/for a given voting option, 
	 * given the total number of options we the card can track the initial distribution of 
	 * votes.
	 * 
	 * @param n number of voting options to track.
	 */
	public OptionScoreCard(VoteOption op, int n) {
		votes = new int[n];
		this.option = op;
		ballots = new ArrayList<VoteCast>();
	}


	public OptionScoreCard(OptionScoreCard other) {
		votes = Arrays.copyOf(other.votes, other.votes.length);
		this.option = other.option;
		ballots = new ArrayList<VoteCast>(other.ballots);
	}
	

    /**
     * When comparing to other option score cards we look at the vote distribution
     * and identify the first non-equal value.  For example, if the optionA and 
     * optionB have 10 first choice votes and 5 second choice, we consider the 
     * third choice votes.   If optionA has 7 and optionB has 3 then we consider
     * optionA the "greater" of the two options.
     */
	@Override
	public int compareTo(OptionScoreCard other) {
		
		if (this.ballots.size() != other.ballots.size())
			return this.ballots.size() - other.ballots.size();

		for (int i=0; i<this.votes.length; i++)
	        if (this.votes[i] != other.votes[i])
	                return this.votes[i] - other.votes[i];

		return 0;
	}

	
	
	
	
	public String toString(){
		String lbl = option.getTitle();
		
		if (lbl.length()>25) lbl = lbl.substring(0,25);
		
		StringBuffer buf = new StringBuffer();
		buf.append('[');
		buf.append(this.ballots.size());
		buf.append(']');
		
		for (int i=0; i<votes.length; i++) {
			buf.append(' ');
			buf.append(String.format("%2d", votes[i]));
			buf.append(' ');
		}
		buf.append(String.format(" %25s (%10s) ",lbl, this.getOid()));
		return buf.toString();
	}
	
	
	/**
	 * Returns the total number of votes for the current option.
	 * 
	 * @return
	 */
	public int totalVotes() {
		int sum = 0;
		
		for (int i=0; i<votes.length; i++) {
			sum += votes[i];
		}
		
		return sum;
	}



	public String getOid() {
		return this.option.getoptionID();
	}



	public VoteOption getOption() {
		return option;
	}



	public int[] getVotes() {
		return votes;
	}



	public List<VoteCast> getBallots() {
		return ballots;
	}



	public void setOption(VoteOption option) {
		this.option = option;
	}



	public void setVotes(int[] votes) {
		this.votes = votes;
	}



	public void setBallots(List<VoteCast> ballots) {
		this.ballots = ballots;
	}
		

}
