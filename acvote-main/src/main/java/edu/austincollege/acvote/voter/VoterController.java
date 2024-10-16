package edu.austincollege.acvote.voter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.BallotController;
import edu.austincollege.acvote.ballot.BallotService;
import edu.austincollege.acvote.ballot.option.VoteOption;
import edu.austincollege.acvote.vote.InvalidTokenException;
import edu.austincollege.acvote.vote.VoteService;
import edu.austincollege.acvote.vote.dao.VoteCastDao;
import edu.austincollege.acvote.vote.dao.VoteTokenDao;

@Controller
@Secured({"ROLE_ADMIN","ROLE_VOTER"})
public class VoterController {
	
	private static Logger log = LoggerFactory.getLogger(VoterController.class);
	
	@Autowired
	BallotService ballotService;
	
	@Autowired
	VoteService voteService;
	
	@Autowired
	VoteTokenDao vtDao;

	/**
	 * Directs user to voting page
	 * 
	 * @return
	 */
	@GetMapping("/voter")
	@ResponseBody
	public ModelAndView voterHome() {
		
		ModelAndView mav = new ModelAndView("redirect:/voter/vote");
		return mav;
	}

	/**
	 * If the user is not allowed, this end-point presents an access denied page
	 * 
	 * @return
	 */
	@GetMapping("/voter/deny")
	@ResponseBody
	public ModelAndView deny(@RequestParam String errMsg) {		
		log.info("errMsg{}", errMsg);
		ModelAndView mav = new ModelAndView("voter/deny");
		mav.addObject("errMsg", errMsg);

		return mav;

	}
	
	/**
	 * If the user is allowed, this end-point submits the page to allow them
	 * to cast their vote.
	 * 
	 * @return
	 * @throws InvalidTokenException 
	 */
	@GetMapping("/voter/vote")
	@ResponseBody
	public ModelAndView vote(@RequestParam Integer bid, @RequestParam String token) throws InvalidTokenException {
		
		log.info("controller displaying ballot #{}, token {}", bid, token);
		
		/*
		 * First we check that the ballot still exists.
		 */
		log.debug("checking ballot{}", bid);
		Ballot b=null ;
		b= ballotService.getBallotById(bid);
		if (b == null) {
			ModelAndView mav = new ModelAndView("redirect:/voter/deny");
			mav.addObject("errMsg", "Ballot doesnt exist.");
			return mav;
		}
		

		/*
		 * If exists, we continue by checking that the polls are still open and now() is not too late.
		 */
		log.debug("checking endtime {}", b.getEndTime());
		if(b.getEndTime().isBefore(LocalDateTime.now())){
			ModelAndView mav = new ModelAndView("redirect:/voter/deny");
			mav.addObject("errMsg", "Ballot has expired, votes can no longer be made.");
			return mav;
		}
		
		/*
		 * Finally, we continue to check that voter has not already used her token.
		 */
		log.debug("checking token {}", token);
		try {
			if(vtDao.tokenIsAbsent(bid, token)) {//checks to see if token is valid
				log.info("missing token");
				ModelAndView mav = new ModelAndView("redirect:/voter/deny");
				mav.addObject("errMsg", "Token is used, cannot vote on Ballot again.");
				return mav;

			}
		} catch (Exception e) {
			e.printStackTrace();
			ModelAndView mav = new ModelAndView("redirect:/voter/deny");
			mav.addObject("errMsg", "Problem with checking token, contact mhiggs.");
			return mav;
		}
		
		/*
		 * If all checks have passed, then we allow them access to submit their vote.
		 */
		log.debug("All checks have been passed.");
		ModelAndView mav = new ModelAndView("/voter/submitVote");
		
		//getting and randomizing ballot options
		List<VoteOption> options = b.getOptions();
		Collections.shuffle(options);
		
		//injecting data into html document
		mav.addObject("ballot", b);
		mav.addObject("token", token);
		mav.addObject("options", options);
		
		return mav;
	}
	
	/**
	 * End-point called when casting a vote from vote page.
	 * Calls ajax to submit vote to database
	 * 
	 * @param bid
	 * @param token
	 * @param options
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/voter/ajax/submitBallot")
	@ResponseBody
	public String castVote(@RequestParam Integer bid, @RequestParam String token, @RequestParam String[] options) throws Exception {
		
		log.debug("submitting vote");
		
		for(String s : options) {
			log.debug(s);
		}
		
		try {
			
			voteService.castVote(bid, token, Arrays.asList(options));
			return "successfully submitted vote";
			
		} catch (Exception e) {
			
			return "could not submit vote";
		}
	}
	
	/**
	 * Directs user to voted page
	 * 
	 * @return
	 */
	@GetMapping("/voter/voted")
	@ResponseBody
	public ModelAndView voted() {
		
		log.info("congratulating voter");
		
		ModelAndView mav = new ModelAndView("voter/voted");
		
		return mav;
	}
}
