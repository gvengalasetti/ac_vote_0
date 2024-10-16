package edu.austincollege.acvote.vote.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import edu.austincollege.acvote.db.JdbcTemplateAbstractDao;
import edu.austincollege.acvote.vote.VoteCast;
import edu.austincollege.acvote.vote.VoteToken;
import edu.austincollege.acvote.vote.dao.JdbcTemplateVoteTokenDao;
import edu.austincollege.acvote.vote.dao.JdbcTemplateVoteTokenDao.VoteTokenMapper;

@Component("JdbcTemplateVoteCastDao")
public class JdbcTemplateVoteCastDao extends JdbcTemplateAbstractDao implements VoteCastDao {
	private Logger log = LoggerFactory.getLogger(JdbcTemplateVoteTokenDao.class);
	
	/**
	 * Guides JdbcTemplate how to map the result set to our VoteCast object class.
	 * Column name must match those in our db schema.
	 * 
	 * @author brandonhill
	 *
	 */
	private class VoteCastMapper implements RowMapper<VoteCast> {

		@Override
		public VoteCast mapRow(ResultSet rs, int rowNum) throws SQLException {
			//create the list of strings that will hold the oids in the order of their ranking
			List<String> votes = new ArrayList<String>();
			
			//create the vote cast that will only hold a single real oid while the rest will be null
			VoteCast vt = new VoteCast();
			vt.setBid(rs.getInt("bid"));
			vt.setToken(rs.getString("token"));
			for(int i = 0; i < rs.getInt("rank"); i++) //fill the list of strings with null
			{
				votes.add(null);
			}
			votes.set((rs.getInt("rank")-1), rs.getString("oid")); //change the oid at the index of the rank
			vt.setVotes(votes);
			

			return vt;
		}

	}
	
	/**
	 * method will take the bid and find all vote cast that match.
	 * Then iterates through all of the vote cast and combines them by matching tokens.
	 */
	@Override
	public List<VoteCast> votesForBallot(Integer bid) throws Exception {	
		//get all the vote cast with matching bid
		String sql = String.format("SELECT * FROM vote_cast WHERE bid = %d", bid);
		List<VoteCast> results = getJdbcTemplate().query(sql, new VoteCastMapper());
		List<VoteCast> voteCasts = new ArrayList<VoteCast>();
		
		//make sure the list has at least one vote cast
		while(results.size() > 0)
		{
			//remove the first vote cast and fill its oids with null
			VoteCast newVoteCast = results.remove(0);
			for(int i = 0; i < results.size(); i++)
			{
				newVoteCast.getVotes().add(null);
			}
			//create a list of vote cast that after iterating through, will get removed from the total list
			List<VoteCast> toBeRemoved = new ArrayList<VoteCast>();
			
			//iterate through the whole list and if there is a matching token then set the oid to the index of its rank
			for(int i = 0; i < results.size(); i++)
			{
				if(newVoteCast.getToken().equals(results.get(i).getToken()))
				{
					toBeRemoved.add(results.get(i));
					//newVoteCast.getVotes().addAll(results.get(i).getVotes());
					for(int j = 0; j < results.get(i).getVotes().size(); j++)
					{
						if(results.get(i).getVotes().get(j) != null)
						{
							newVoteCast.getVotes().set(j, results.get(i).getVotes().get(j));
						}
					}
				}
			}
			
			//remove any nulls that didnt get filled
			while(newVoteCast.getVotes().remove(null)) {}
			//add the new vote cast to the list
			voteCasts.add(newVoteCast);
			//remove all matching vote cast
			results.removeAll(toBeRemoved);
		}
		
		//return the list of all combined vote cast
		return voteCasts;
	}

	/**
	 * Method will take a vote cast and add multiple entries to represent the different oids
	 */
	@Override
	public void castVote(VoteCast vote) throws Exception {
		int bid = vote.getBid();
		String tokenString = vote.getToken();
		
		log.debug("adding vote cast to ballot: " + bid);
		
		//get the list of oids and their indexes are the ranking
		List<String> voteRanking = vote.getVotes();
		//log.debug("size of voteRanking: " + voteRanking.size());
		log.debug(voteRanking.toString());
		
		//loop will iterate through list of oids and insert them using the index as ranking starting at 1
		for(int i = 0; i < voteRanking.size(); i++)
		{
			//log.debug("current oid: " + voteRanking.get(i));
			String oid = voteRanking.get(i);
			String sql = String.format("INSERT INTO vote_cast (bid, oid, token, rank) VALUES (%d, '%s', '%s', %d)", bid, oid, tokenString, i+1);
		
			KeyHolder k = new GeneratedKeyHolder();
		
			int num = getJdbcTemplate().update(connection -> {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				return ps;
			}, k);
		
			if(num != 1) {
				String msg = String.format("unable to add vote cast fragment");
				log.error(msg);
				throw new Exception(msg);
			}
		}

	}
/**
 * calls method votesForBallot and returns size of list created
 */
	@Override
	public int voteCountForBallot(Integer bid) throws Exception {

		
		
		
		return this.votesForBallot(bid).size();
	}

	@Override
	public void clearVotesForBallot(Integer bid) throws Exception {
		int rc = getJdbcTemplate().update(String.format("DELETE FROM vote_cast WHERE bid = %d", bid));
		log.debug("...got {}", rc);
		if(rc < 1) {
			String msg = String.format("unable to remove vote_cast or is already empty for bid [%d]", bid);
			log.error(msg);
			//throw new Exception(msg);
		}
		else
		{
			log.debug("sucessfully cleared vote_cast for bid #{}",bid);
		}
		
		
	}

}
