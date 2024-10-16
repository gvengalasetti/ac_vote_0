package edu.austincollege.acvote.vote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.option.VoteOption;


/**
 * A variety of vote counters will extends this abstract class.   The class provides a 
 * processing framework for vote counters.  We expect various "kinds" of vote counters will
 * finalize and establish results in various ways.
 * <p>
 * When asked for the voteResults,  all vote counters will initialize, count and finalize the 
 * vote in ways appropriate to the kind of vote.
 * 
 * @author mahiggs
 *
 */
public abstract class VoteCounter {
	
	private static Logger log = LoggerFactory.getLogger(IrvCounter.class);

	public static final String VOTE_TYPE_IRV = "IRV";
	public static final String VOTE_TYPE_IRV2 = "IRV2";
	public static final String VOTE_TYPE_QOTH = "QOTH";
	public static final String[] VALID_VOTE_TYPES = { VOTE_TYPE_IRV, VOTE_TYPE_IRV2, VOTE_TYPE_QOTH };
	
	
	
	/**
	 * Use this method to create the appropriate type of counter according to the ballot.  The
	 * ballot has a field that indicates the kind of counting we should do.
	 * 
	 * @param b handle
	 * @return
	 * @throws InvalidVoteTypeException
	 */
	public static VoteCounter newCounterFor(Ballot b) throws InvalidVoteTypeException {
		
		VoteCounter cntr = null;
		
		switch (b.getTypeOfVote()) {
			
			case VOTE_TYPE_IRV:
				cntr = new IrvCounter(b);
				break;
				
			default:
				throw new InvalidVoteTypeException(b.getTypeOfVote());
		
		}
		
		return cntr;
		
	}
	
	
	
	/**
	 * A list of voting options excluded from counting.  The option is not "enabled" and 
	 * should be ignored by the vote counter.
	 */
	List<VoteOption> excludedOptions = Collections.emptyList();

	/**
	 * A list of valid, enabled options that is the basis for the vote counting.  For each
	 * valid option, we establish a "score card" to keep state of the count for that option.
	 */
	List<VoteOption> validOptions = Collections.emptyList();
	
	/**
	 * A quick access map to provide the score card for an option using its option id as the key.
	 */
	HashMap<String, OptionScoreCard> cardsMap = new HashMap<>();
	
	
	/**
	 * A handle on the ballot for which the current counter is working.
	 */
	Ballot ballot; 
	

	
	/**
	 * As the primary constructor, we remember the ballot and partition the list of all ballot 
	 * options to those excluded (disabled) and those that are valid.
	 * 
	 * @param b the ballot for which we are counting.
	 */
	public VoteCounter(Ballot b) {

		this.ballot = b;
		
		List<VoteOption> allOptions = b.getOptions();
		
		this.excludedOptions = allOptions.stream().filter(vo -> !vo.isEnabled()).collect(Collectors.toList());
		this.validOptions = allOptions.stream().filter(vo -> vo.isEnabled()).collect(Collectors.toList());
		
		this.initializeVote();
	}
	
	
	
	/**
	 * Initialize the vote count by creating a score card for each of the valid
	 * options on the ballot.   After this method is run, there exists an initial
	 * score card for each valid option ready to start counting the votes.    
	 * 
	 * @param votesCast
	 */
	public void initializeVote( ) {
		
		log.info("initializing vote count for ballot {}",ballot.getId());
		log.info("... {} valid options.",validOptions.size());
		
		cardsMap.clear();
		
		/*
		 * Create a score card for each of the valid options and register them in our 
		 * hashmap look up table for quick access in the algorithm. 
		 */
		
		
			
		for (VoteOption vo : validOptions) {
			OptionScoreCard oc = new OptionScoreCard(vo, validOptions.size());
			cardsMap.put(oc.getOid(), oc);
		}
		
	}
	
	
	/**
	 * Given the votes cast for the current ballot we do the initial counting.
	 * <p>
	 * This method initializes our ancillary data structures and performs
	 * the initial vote.  Note, we only tally the votes and do not analyze
	 * the results to see if we have a clear winner.   The score cards are
	 * sorted by voter's support (ie, the first score card has the most first
	 * choice votes) while the last score card has the least.
	 * <p>
	 * Another algorithm -- <code>finalizeVote</code> -- determines what to do with these score cards.
	 * </p>
	 * @param votesCast
	 */
	public List<OptionScoreCard> countVote( List<VoteCast> votesCast  ) {
		
		/*
		 * creates a hashmap of score cards for each of the valid options.
		 */
		this.initializeVote();  
		
		log.info("counting votes from {} ballots cast.",votesCast.size());
		
		/*
		 * For each vote cast, we register the vote and 
		 * count the votes.   As we iterate over the ballots votes we 
		 * track the preference index (i).   i=0 is the ballots first preference/option/vote.
		 * i=1 is the second preferred vote. We quickly retrieve the option score card and 
		 * tally the number of votes for each preference (first place, second place, etc).
		 * We also assign the current voter ballot to the first place (most preferred) 
		 * option card. 
		 * 
		 */
		for (VoteCast vb : votesCast) {
			int i=0;
			for (String vid : vb.getVotes()) {
				if (excludedOptions.contains(vid)) continue;  // ignore excluded votes
				OptionScoreCard c = cardsMap.get(vid);   // find score card for the voter's option
				c.votes[i] = c.votes[i]+1;			// remember number of first place votes, second place votes, etc...
				if (i == 0) c.ballots.add(vb);    	// allocate first choice votes to candidate
				i++;
			}
		}
		
		/*
		 * Now sort our score cards according to voter support (least to most preferred). 
		 */
		List<OptionScoreCard> clist = new ArrayList<>(cardsMap.values());
		Collections.sort(clist);
		
		return clist;
	}
	
	
	/**
	 * depends on the concrete subclass... we might simply return the first
	 * winning option,  or perhaps we need to perform run-off voting, or some
	 * other computation before returning the results.
	 * 
	 * @param cards
	 * @return
	 */
	public abstract VoteResult finalizeVote(List<OptionScoreCard> cards);

	
	/**
	 * As the primary entry point, we initialize our data structures, perform
	 * the initial count, then finalize the results.   Concrete subclasses must
	 * decide how to finalize the results. 
	 * 
	 * @param votesCast
	 * @return
	 */
	public VoteResult voteResults( List<VoteCast> votesCast ) {
		log.info("deriving results for ballot {}",this.ballot.getId());
		
		this.initializeVote();
		List<OptionScoreCard> cards = this.countVote(votesCast);
		return finalizeVote(cards);
		
	}



	public List<VoteOption> getExcludedOptions() {
		return excludedOptions;
	}



	public List<VoteOption> getValidOptions() {
		return validOptions;
	}



	public HashMap<String, OptionScoreCard> getCardsMap() {
		return cardsMap;
	}



	public Ballot getBallot() {
		return ballot;
	}

}
