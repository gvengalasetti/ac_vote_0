package edu.austincollege.acvote.vote.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import edu.austincollege.acvote.ballot.dao.InvalidBallotIdException;
import edu.austincollege.acvote.vote.VoteCast;

/**
 * @author mahiggs
 *
 */
public class DummyVoteCastDao implements VoteCastDao {

	public static HashMap<Integer,List<VoteCast>> store = new HashMap<>();
	
	{
		List<VoteCast> lst = new ArrayList<>();
		lst.add(new VoteCast(1,"b51bb819f0adf8b057935a19fe858e80", "5854836","4138778"));
		lst.add(new VoteCast(1,"c4405aab152f1842a9b8f4099efb5774", "5854836","4138778"));
		lst.add(new VoteCast(1,"4d719e69a994d5039bf9ce18ef34e844", "4138778","5854836"));
		lst.add(new VoteCast(1,"8b6990c4a1cb6901fbfcba6a67c29894", "5854836","4138778"));
		store.put(1, lst);

		List<VoteCast> lst3 = new ArrayList<>();
		lst3.add(new VoteCast(3,"t01", "ip","js","mt","th"));
		lst3.add(new VoteCast(3,"t02", "js","ip","th","mt"));
		lst3.add(new VoteCast(3,"t03", "ip","mt","th","js"));
		lst3.add(new VoteCast(3,"t04", "mt","th","ip","js"));
		lst3.add(new VoteCast(3,"t05", "ip","mt" ));
		lst3.add(new VoteCast(3,"t06", "mt","ip","js","th"));
		

		store.put(1, lst);
		store.put(2, new ArrayList<VoteCast>());
		store.put(3, lst3);
		
	}
	

	public DummyVoteCastDao() {
	}

	@Override
	public List<VoteCast> votesForBallot(Integer bid) throws Exception {
		if (!store.keySet().contains(bid)) throw new InvalidBallotIdException(String.format("%d is not a valid ballot id",bid));
		List<VoteCast> src = store.get(bid);
		List<VoteCast> dst = new ArrayList<VoteCast>();
		dst.addAll(src);
		return dst;
	}

	@Override
	public void castVote(VoteCast vote) throws Exception {
		
		assert store.keySet().contains(vote.getBid());
		
		String tok = vote.getToken();
		List<VoteCast> votes = store.get(vote.getBid());

		for (VoteCast vc : votes) {
			if (vc.getToken().equals(vote.getToken())) 
				throw new DuplicateVotingTokenException(vote+" has duplicate token");
		}
		
		votes.add(vote);

	}

	@Override

	public int voteCountForBallot(Integer bid) throws Exception {
		
		return store.get(bid).size();
	}
	@Override
	public void clearVotesForBallot(Integer bid) throws Exception {
		//if (!store.keySet().contains(bid)) throw new InvalidBallotIdException(String.format("%d is not a valid ballot id",bid));
		store.remove(bid);
	}

}
