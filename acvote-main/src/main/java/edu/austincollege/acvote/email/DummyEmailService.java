package edu.austincollege.acvote.email;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.dao.BallotDao;
import edu.austincollege.acvote.faculty.Faculty;
import edu.austincollege.acvote.faculty.dao.FacultyDAO;
import edu.austincollege.acvote.vote.VoteToken;
import edu.austincollege.acvote.vote.dao.VoteTokenDao;

@Service
public class DummyEmailService {
	
	@Autowired
	BallotDao ballotDao;
	
	@Autowired
	FacultyDAO facultyDao;
	
	@Autowired
	VoteTokenDao vtDao;
	
	//This is who the email will say the message is from
	private String From = "acvoteadmin@austincollege.edu";
	
	//This is who the email is sent to
	private String To;
	
	private static Logger log = LoggerFactory.getLogger(DummyEmailService.class);
	
	/**
	 * Method sends message to the console containing the to, from and the message from the parameters
	 * @param email
	 * @param body
	 */
	public void sendEmailTo(String email, String body)
	{
		To = email;
		log.debug("\n-----\n- -From: " + From + "\n- -To: " + To + "\n\n- -" + body + "\n-----");
	}
	
	/**
	 * Method uses every token given and creates a message to send to the console containing a link 
	 * to the voting page for the ballot
	 * @param tokens
	 */
	public void sendVoteEmailToList(List<VoteToken> tokens)
	{
		//loop though all tokens
		for(int i = 0; i < tokens.size(); i++)
		{
			//find the faculty that matched the tokens acid
			Faculty fac = new Faculty();
			try {
				fac = facultyDao.findFacultyByID(tokens.get(i).getAcId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//send the faculty's and link to voting to the console
			sendEmailTo(fac.getEmail(), "http://localhost:8080/acvote/voter/vote?bid="+tokens.get(i).getBid()+"&token="+tokens.get(i).getToken());
			
		}
	}
	
	/**
	 * Method uses every token given and creates a message to send to the console containing a link 
	 * to the voting page for the ballot
	 * @param tokens
	 */
	public void sendVoteEmailToList(List<VoteToken> tokens, String body)
	{
		//loop though all tokens
		for(int i = 0; i < tokens.size(); i++)
		{
			//find the faculty that matched the tokens acid
			Faculty fac = new Faculty();
			try {
				fac = facultyDao.findFacultyByID(tokens.get(i).getAcId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//send the faculty's and link to voting to the console
			sendEmailTo(fac.getEmail(), body + "\n\nhttp://localhost:8080/acvote/voter/vote?bid="+tokens.get(i).getBid()+"&token="+tokens.get(i).getToken());
			
		}
	}
}
