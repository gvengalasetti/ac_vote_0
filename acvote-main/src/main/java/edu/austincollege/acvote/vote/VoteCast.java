package edu.austincollege.acvote.vote;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An instance of this class represents a voter's ranked choices for a specific ballot. We
 * keep the ballot id (bid) and the voting token (anonymously represents the voter) as well as
 * an order list of ballot optionid's by order of preference;
 * 
 * @author mahiggs
 *
 */


public class VoteCast {

	private Integer bid;
	private String token;
	private List<String> votes;
	
	public VoteCast() {
	}
	
	public VoteCast(Integer bid, String tok, String ...oids) {
		this(bid,tok,Arrays.asList(oids));
	}
	

	public VoteCast(Integer bid, String tok, List<String> rankedOptions) {
		this.bid = bid;
		this.token = tok;
		this.votes = rankedOptions;
	}

	public Integer getBid() {
		return bid;
	}

	public String getToken() {
		return token;
	}

	public List<String> getVotes() {
		return votes;
	}

	public void setBid(Integer bid) {
		this.bid = bid;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setVotes(List<String> votes) {
		this.votes = votes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(bid, token);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VoteCast other = (VoteCast) obj;
		return Objects.equals(bid, other.bid) && Objects.equals(token, other.token);
	}

	@Override
	public String toString() {
		return "VoteCast [bid=" + bid + ", token=" + token + ", votes=" + votes + "]";
	}
	
	
}
