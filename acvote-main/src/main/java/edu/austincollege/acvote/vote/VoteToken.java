package edu.austincollege.acvote.vote;

import java.time.LocalDateTime;
import java.util.Objects;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * An instance of this class encapsulate a voting token that allows a voting faculty
 * member to vote on a ballot.  The token field therein is a MD5 hash using the ballot
 * id, faculty member id, a date time and a random number.  Thus, it would be near impossible
 * for someone to guess and use a token to vote multiple times (ie, manipulate a vote).
 * 
 * <p>
 * <code>
 * VoteToken vt = VoteToken.newToken(1,"123456"); 
 * </code>
 * </p>
 * @author mahiggs
 *
 */
public class VoteToken {

	/**
	 * Primary way to create a voting token for a ballot for a voting faculty.
	 * 
	 * @param bid
	 * @param idStr
	 * @return
	 */
	public static VoteToken newToken(Integer bid, String idStr) {
		return newToken(bid,idStr, LocalDateTime.now());
	}
	
	
	/**
	 * Use this method for creating voting tokens using a specific utc time while
	 * testing.
	 * 
	 * @param bid
	 * @param idStr
	 * @param utc
	 * @return
	 */
	public static VoteToken newToken(Integer bid, String idStr, LocalDateTime utc) {
		
		double rand = Math.random();
		String seed = String.format("%d.%s.%s.%f", bid,idStr,utc.toString(),rand);
		String tok = DigestUtils.md5Hex(seed).toUpperCase();
		return new VoteToken(bid,idStr,tok);
	    
	}
	
	
	private Integer bid;   // id of an existing ballot
	private String acId;   // id of a voting faculty member
	private String token;  // token to use with voting URL and votes cast
	
	private VoteToken() {
	}
	
	private VoteToken(Integer bid, String idStr, String tok ) {
		this.bid = bid;
		this.acId = idStr;
		this.token = tok;
	}


	public Integer getBid() {
		return bid;
	}


	public String getAcId() {
		return acId;
	}


	public String getToken() {
		return token;
	}


	public void setBid(Integer bid) {
		this.bid = bid;
	}


	public void setAcId(String acId) {
		this.acId = acId;
	}


	public void setToken(String token) {
		this.token = token;
	}


	@Override
	public int hashCode() {
		return Objects.hash(acId, bid, token);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VoteToken other = (VoteToken) obj;
		return Objects.equals(acId, other.acId) && Objects.equals(bid, other.bid) && Objects.equals(token, other.token);
	}


	@Override
	public String toString() {
		return "VoteToken [bid=" + bid + ", acId=" + acId + ", token=" + token + "]";
	}
	
	

}
