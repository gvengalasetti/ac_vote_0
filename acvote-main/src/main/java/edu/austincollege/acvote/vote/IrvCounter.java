package edu.austincollege.acvote.vote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

import edu.austincollege.acvote.ballot.Ballot;

public class IrvCounter extends VoteCounter {

	private static Logger log = LoggerFactory.getLogger(IrvCounter.class);

	public IrvCounter(Ballot b) {
		super(b);
	}

	
	/**
	 * Creates a deepish-copy of the score card
	 * @param clist
	 * @return
	 */
	private ArrayList<OptionScoreCard> deepishCopy(List<OptionScoreCard> clist ) {
		ArrayList<OptionScoreCard> copy = new ArrayList<>();
		for (OptionScoreCard card : clist) 
			copy.add(new OptionScoreCard(card));
		return copy;
	}
	
	
	@Override
	public VoteResult finalizeVote(List<OptionScoreCard> clist) {
		
		log.info("finalizing vote for ballot {}",ballot.getId());
		
		
		VoteResult vres = new VoteResult(this.ballot);
		
		int rndCnt = 1;
		
		
		VoteRound round = new VoteRound(rndCnt, deepishCopy(clist));
		
		/*
		 * keep eliminating and redistributing ballots from losers until
		 * we get to the expected number of outcomes or a majority is realized
		 * 
		 */
		while ( clist.size() > this.ballot.getOutcomes()) {

			/*
			 * The loser is the first/smallest scorecard.
			 */
			OptionScoreCard loser = clist.get(0);
			clist.remove(0);
			round.setLoser(loser.getOption());

			/*
			 * A scorecard might have several cast ballots currently supporting it.   Once
			 * eliminated we redistribute these ballots according to voter preference...
			 */
			log.info("eliminating {} option; {} ballots to redistribute ",loser.getOid(), loser.ballots.size());
			round.addNote(String.format("eliminating option %s (%s)",loser.option.getTitle(), loser.option.getoptionID()));			
			for (VoteCast vb : loser.ballots) {
							
				boolean redis = false;
				
				/*
				 * For each of the cast votes, we look through the vote options in order
				 * of voters preference.   Earlier votes are for already eliminated
				 * options.  The first non-eliminated option for which there is a vote
				 * will be transfered to that option.
				 * 
				 * Here we are processing in order of voter preference.
				 */
				for (String vid : vb.getVotes()) {    // vb is a ballot cast for eliminated option
					
					if (this.excludedOptions.contains(vid)) continue;   // always ignore excluded options
					
					if (loser.getOid().equals(vid)) continue;   // cannot redistribute to same scorecard
					
					// note: this last check was redundant because only loser options are redistributed and the
					// option is no longer in our current viable options list (clist).  
					
					OptionScoreCard nxtOptionCard = this.cardsMap.get(vid);  // get a handle on the potential next opt card 
					
					/*
					 * Option score cards that have been eliminated (and therefore cannot receive redistribution)
					 * do not remain in our clist.  Only viable candidates still alive remain.  if the next preferred
					 * option remains in our list, then redistribute to that option score card.
					 */
					if (clist.contains(nxtOptionCard)) {   // oc is the score card for the next voter's preference
						/*
						 * viable option score card remain in our candidate/clist
						 */
						log.info("...redistributing from {} to {}",loser.getOid(),nxtOptionCard.getOption().getTitle());
						round.addNote(String.format("...redistributing %s ballot to %s",vb.getToken(), nxtOptionCard.getOid()));			

						redis = true;
						nxtOptionCard.getBallots().add(vb);
						break;    // no need to continue...we redistributed this ballot to a viable option
						
					} else {
						/*
						 * if the option score card is absent, then it must have been eliminated
						 * in prior rounds.   So we ignore/skip.
						 */
						log.debug("...skipping {} as target. ",vid);
					}
					
				}
				
				
				/*
				 * If we exhaust our possible redistribution options and our flag is still false, this 
				 * voter/token is no longer part of the counting game.
				 */
				if (!redis) {
					log.info("......voter {} is now mute.", vb.getToken());
				}
			}
			
			/*
			 * Make sure the remaining score card options are ordered by voting support in order to 
			 * keep the winners at the end of the list and the losers at the front of our list. 
			 */
			Collections.sort(clist);
			
			vres.addRound(round);  // round information to results   

			
			/*
			 * Now prepare a new run-off round....
			 */
			rndCnt++;
			
			round = new VoteRound(rndCnt, deepishCopy(clist));
				
			}  

		vres.setScoreCards(clist);
		
		return vres;
	}

}
