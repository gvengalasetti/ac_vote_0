package edu.austincollege.acvote.ballot;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import edu.austincollege.acvote.ballot.option.VoteOption;
import edu.austincollege.acvote.email.DummyEmailService;
import edu.austincollege.acvote.email.EmailService;
import edu.austincollege.acvote.faculty.Faculty;
import edu.austincollege.acvote.faculty.FacultyService;
import edu.austincollege.acvote.lut.LutDao;
import edu.austincollege.acvote.template.BallotTemplate;
import edu.austincollege.acvote.template.TemplateService;
import edu.austincollege.acvote.vote.VoteResult;
import edu.austincollege.acvote.vote.VoteService;
import edu.austincollege.acvote.vote.VoteToken;
import edu.austincollege.acvote.vote.Votes;

/**
 * <h1>Controller for Ballot Subsystem</h1>
 * 
 * <p>
 * Contains end-points for all pages and operations pertaining to ballots.
 * Maintains dependencies on BallotService, TemplateService, FacultyService,
 * VoteService, EmailService, and LutDao helper objects to fetch necessary data.
 * </p>
 * 
 * <p>
 * There exists 3 main "pages" for this subsystem:
 * <ul>
 * <li>the list page lists the all ballots</li>
 * <li>the details page shows the details for a single ballot</li>
 * <li>the results page shows the vote results for a specified ballot</li>
 * </p>
 * 
 * <p>
 * Accessible only by <b>ROLE_ADMIN</b> users
 * </p>
 *
 */
@Controller
@Secured({"ROLE_ADMIN","ROLE_EDITOR"})
public class BallotController {

	private static Logger log = LoggerFactory.getLogger(BallotController.class);

	@Autowired
	BallotService ballotService;

	@Autowired
	TemplateService templateService;

	@Autowired
	FacultyService facultyService;

	@Autowired
	LutDao lutDao;

	@Autowired
	VoteService voteService;

	@Autowired
	DummyEmailService dummyEmailService;

//	@Autowired
//	VoteTokenService voteTokenService;

	@Autowired
	Votes votes;

	@Autowired
	EmailService emailService;
	
	
	/**
	 * Ajax calls that wish to reload the page with a flash message can
	 * post to this endpoint with message, which eventually redirects to 
	 * a get method to list all ballots....with flash message.
	 * 
	 * @param msg
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="/ballots/flash", method=RequestMethod.POST)
	public RedirectView flashBallots(	
				@RequestParam String msg, 
				final RedirectAttributes redirectAttributes) {

		log.debug("flashing [{}]",msg);
		
		redirectAttributes.addFlashAttribute("message",msg);

		return new RedirectView("/ballots",true);
	}
	
	/**
	 * Ajax calls that wish to reload the details page with a flash message
	 * can post to this endpoint.  We redirect to the page with message.
	 * 
	 * @param bid
	 * @param msg
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="/ballot/flash", method=RequestMethod.POST)
	public RedirectView flashBallotDetail(	
				@RequestParam Integer bid,
				@RequestParam String msg, 
				final RedirectAttributes redirectAttributes) {

		log.debug("flashing [{}] with bid={}",msg,bid);
		
		redirectAttributes.addAttribute("bid",bid);
		redirectAttributes.addFlashAttribute("message",msg);

		return new RedirectView("/ballot",true);
	}
	

	/**
	 * Presents all ballots in table form as described in ballotListView.html.
	 * Collects all ballots by requesting them from services, and inserts them into
	 * the html document.
	 * 
	 * @return HTML document with table view of all ballots
	 */
	@GetMapping(value = "/ballots")
	@ResponseBody
	public ModelAndView viewBallots() {

		log.debug("controller listing all ballots");
		
		ModelAndView mav = new ModelAndView("ballot/ballotListView");

		// retrieving ballots
		List<Ballot> ballots = ballotService.getBallots();
		List<BallotTemplate> templates = templateService.listTemplates();
		List<Faculty> fac = new ArrayList<Faculty>();
		//int tokenSize = voteService.getAllTokensForBallot(0)

		// injecting objects into html document
		mav.addObject("votes", votes);
		mav.addObject("ballots", ballots);
		mav.addObject("templates", templates);
		mav.addObject("votingFaculty", fac);
		//mav.addObject(null, templates)

		return mav;
	}

	/**
	 * Presents details of single ballot as described in selectedBallot.html.
	 * Collects ballot and all its details from BallotService, and inserts them into
	 * the html document.
	 * 
	 * @param bid ballot id
	 * @return HTML document with details of a ballot
	 */
	@GetMapping(value = "/ballot")
	@ResponseBody
	public ModelAndView viewBallot(@RequestParam Integer bid) {

		log.info("controller showing details for ballot={}", bid);

		// retrieving html document
		ModelAndView mav = new ModelAndView("ballot/selectedBallot");

		// retrieving ballot from service
		Ballot bal = ballotService.getBallotById(bid);

		// injecting data into html document
		mav.addObject("ballot", bal);
		mav.addObject("options", bal.getOptions());
		mav.addObject("divisions", lutDao.divisions());
		mav.addObject("ranks", lutDao.ranks());
		mav.addObject("tenures", lutDao.tenureStatus());
		mav.addObject("votingGroup", bal.getVoters());
		mav.addObject("allFaculty", facultyService.getAllFaculty());

		return mav;

	}

	/**
	 * Presents results of single ballot as described in ballotResults.html page.
	 * Collects ballot and all its results from services and inserts them into the
	 * html document.
	 * 
	 * @param bid ballot id
	 * @return HTML document with results of a ballot
	 */
	@GetMapping(value = "/ballot/{bid}/results")
	@ResponseBody
	public ModelAndView viewBallotResults(@PathVariable(value = "bid") Integer bid) {

		log.info("controller showing results for ballot={}", bid);

		// retrieving html document
		ModelAndView mav = new ModelAndView("ballot/ballotResults");

		// retrieving data from services
		Ballot bal = ballotService.getBallotById(bid);
		VoteResult vres = ballotService.resultsForBallot(bal);

		// injecting data into html document
		mav.addObject("ballot", bal);
		mav.addObject("votes", votes);
		mav.addObject("closed", bal.getEndTime().compareTo(LocalDateTime.now()) < 0);
		mav.addObject("result", vres);

		return mav;

	}

	/**
	 * End-point called when removing a specific option from the specified ballot
	 * 
	 * @param bid ballot id
	 * @param oid option id
	 * @return html response entity indicating success
	 */
	@DeleteMapping(value = "/ballot/ajax/deleteOption")
	@ResponseBody
	public ResponseEntity<String> handleDeleteOptionFromBallot(@RequestParam Integer bid, @RequestParam String oid) {

		log.info("deleting option  {} from {}");

		// deleting option
		try {
		ballotService.deleteOptionFromBallot(bid, oid);
		} catch (Exception e) {
			
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok("option deleted");

	}

	/**
	 * End-point called when disabling specific option from the specified ballot
	 * 
	 * @param bid   ballot id
	 * @param oid   option id
	 * @param state true or false
	 * @return html response entity indicating success
	 */
	@PutMapping(value = "/ballot/ajax/enableOption")
	@ResponseBody
	public ResponseEntity<String> handleEnableOption(@RequestParam Integer bid, @RequestParam String oid,
			@RequestParam String state) {

		log.info("setting option {} in ballot {} enabled to {}", bid, oid, state);

		// toggling option
		boolean success = ballotService.toggleOption(bid, oid, "TRUE".equals(state.toUpperCase()));

		if(success)
			return ResponseEntity.ok("option toggled");
		else
			return ResponseEntity.badRequest().build();
	}

	/**
	 * End-point called when removing all options from the current ballot
	 * 
	 * @param bid ballot id
	 * @return html response entity indicating success
	 */
	@DeleteMapping(value = "/ballot/ajax/deleteAllOptions")
	@ResponseBody
	public ResponseEntity<String> handleDeleteAllOptions(@RequestParam Integer bid) {

		log.info("deleting all options");

		// clearing options
		boolean success = ballotService.clearOptionsFromBallot(bid);

		if(success)
			return ResponseEntity.ok("cleared options");
		else
			return ResponseEntity.badRequest().build();
	}

	/**
	 * Creates new ballot and redirects user to edit page to add details to new
	 * ballot
	 * 
	 * @return message confirming creation of ballot
	 */
	@PostMapping(value = "/ballot/ajax/createBallot")
	@ResponseBody
	public String createBallot() {

		log.info("controller creating new ballot");

		LocalDateTime currTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
		int id = ballotService.addBallot("Enter Title", "Enter Instructions", "Enter Description", true, null, "IRV", 0,
				currTime, currTime.plusDays(10), "CW", 1);

		log.debug(String.valueOf(id));
		return String.valueOf(id);

	}

	/**
	 * Creates new ballot from the selected template and redirects user to edit page
	 * of the ballot
	 * 
	 * @param tid template id
	 * @return message confirming creation of ballot
	 */
	@PostMapping(value = "/ballot/ajax/createBallotFromTemplate")
	@ResponseBody
	public String createBallotFromTemplate(@RequestParam Integer tid) {

		log.info("controller creating ballot from template");

		// get the template with the matching id
		BallotTemplate temp = templateService.getTemplate(tid);

		// create a new ballot using the current date and fields from the template
		LocalDateTime currTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
		int id = ballotService.addBallot(temp.getBallotTitle(), temp.getInstructions(), temp.getDescription(),
				temp.isBasis(), null, temp.getTypeOfVote(), temp.getOutcomes(), currTime, currTime.plusDays(10), "CW", 1);

		log.debug("The id of the created ballot is: " + String.valueOf(id));
		return String.valueOf(id);

	}

	/**
	 * End-point for deletion of a ballot. Deletes and redirects to /ballots
	 * 
	 * @param bid id of ballot to be deleted
	 * @return confirmation of deletion
	 */
	@DeleteMapping(value = "/ballot/ajax/deleteBallot")
	@ResponseBody
	public String deleteBallot(@RequestParam String bid) {

		log.info("controller deleting ballot={}", bid);

		int id = Integer.parseInt(bid);

		// deleting ballot
		boolean success = ballotService.deleteBallot(id);

		if (success)
			return String.format("ballot %s has been deleted", bid);
		else
			return String.format("ballot %s has NOT been deleted", bid);
	}


	/**
	 * End-point called when adding a new custom option to the specified ballot
	 * 
	 * @param bid   ballot id
	 * @param oid   option id
	 * @param title option title
	 * @return html response entity indicating success
	 */
	@PostMapping(value = "/ballot/ajax/addOption")
	@ResponseBody
	public ResponseEntity<String> handleAddCustomOption(@RequestParam Integer bid, @RequestParam String oid,
			@RequestParam String title) {

		log.info("adding custom option to ballot {}:", bid);

		try {
			/*
			 * Prepare the new list of options, in this case a list of one
			 */
			ArrayList<VoteOption> newVoteOptions = new ArrayList<>();
			VoteOption vo = new VoteOption(oid, title, true);
			newVoteOptions.add(vo);

			ballotService.addBallotOptions(bid, newVoteOptions);
		} catch (Exception e) {

//			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok("custom options");
	}


	/**
	 * End-point called when adding a new faculty option to the specified ballot
	 * 
	 * @param bid          ballot id
	 * @param newOptionIDs id(s) of faculty
	 * @return
	 */
	@PostMapping(value = "/ballot/ajax/addCandidates")
	@ResponseBody
	public ResponseEntity<String> handleAddFacultyOptions(@RequestParam Integer bid,
			@RequestParam(value = "newOptionIDs[]") String[] newOptionIDs) {

		log.info("adding candidates to ballot {}:", bid);

		for (String s : newOptionIDs) {
			log.debug("... {}", s);
		}

		try {

			Ballot ballot = ballotService.getBallotById(bid);

			// find and make list of faculty with IDs using DAO (will be using set list for
			// now)
			List<Faculty> faculty = facultyService.findFacultyMembers(newOptionIDs);

			for (Faculty f : faculty) {
				log.debug("... {} {}", f.getAcId(), f.getLastName());
			}

			// convert array of IDs and Names to voteOptions
			ArrayList<VoteOption> newVoteOptions = ballotService.convertFacultytoVoteOptions(faculty);

			// remove any overlapping options
			for (int i = 0; i < ballot.getOptions().size(); i++) {
				for (int j = 0; j < newVoteOptions.size(); j++) {
					if (ballot.getOptions().get(i).getoptionID().equals(newVoteOptions.get(j).getoptionID())) {
						log.debug("Removing the duplicate option id: " + newVoteOptions.get(j).getoptionID() + " - "
								+ newVoteOptions.remove(newVoteOptions.get(j)));
					}
				}
			}

			// if after removing over laps the list still has options then add them
			if (newVoteOptions.size() > 0) {
				ballotService.addBallotOptions(bid, newVoteOptions);
			}
		} catch (Exception e) {

			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok("faculty options");
	}


/**
 * End-point called when editing the title of the ballot
 * 
 * @param bid         ballot id
 * @param ballotTitle ballot title
 * @return message indicating success
 */
	@PutMapping(value = "/ballot/ajax/setBallotTitle")
	@ResponseBody
	public String setBallotTitle(@RequestParam String bid, @RequestParam String ballotTitle) {

		log.info("controller editing title of ballot={}: {}", bid, ballotTitle);

		int id = Integer.parseInt(bid);

		// editing title
		Ballot ballot = ballotService.setBallotTitle(id, ballotTitle);

		return String.format("title updated");
	}


	/**
	 * End-point called when editing the description of the ballot
	 * 
	 * @param bid               ballot id
	 * @param ballotDescription ballot description
	 * @return message indicating success
	 */
	@PutMapping(value = "/ballot/ajax/setBallotDescription")
	@ResponseBody
	public String setBallotDescription(@RequestParam String bid, @RequestParam String ballotDescription) {

		log.info("controller editing description of ballot={}: {}", bid, ballotDescription);

		int id = Integer.parseInt(bid);

		// editing description
		Ballot ballot = ballotService.setBallotDescription(id, ballotDescription);

		return String
				.format("description updated");
	}


	/**
	 * End-point called when editing the instructions of the ballot
	 * 
	 * @param bid                ballot id
	 * @param ballotInstructions ballot instructions
	 * @return message indicating success
	 */
	@PutMapping(value = "/ballot/ajax/setBallotInstructions")
	@ResponseBody
	public String setBallotInstructions(@RequestParam String bid, @RequestParam String ballotInstructions) {

		log.info("controller editing instructions of ballot={}: {}", bid, ballotInstructions);

		int id = Integer.parseInt(bid);

		// editing instructions
		Ballot ballot = ballotService.setBallotInstructions(id, ballotInstructions);

		return String
				.format("instructions updated");
	}


	/**
	 * End-point called when editing the vote type of the ballot
	 * 
	 * @param bid              ballot id
	 * @param ballotTypeOfVote ballot vote type
	 * @return message indicating success
	 */
	@PutMapping(value = "/ballot/ajax/setBallotTypeOfVote")
	@ResponseBody
	public String setBallotTypeOfVote(@RequestParam String bid, @RequestParam String ballotTypeOfVote) {

		log.info("controller editing vote type of ballot={}: {}", bid, ballotTypeOfVote);

		int id = Integer.parseInt(bid);

		// editing vote type
		Ballot ballot = ballotService.setBallotTypeOfVote(id, ballotTypeOfVote);

		return String
				.format("vote type updated");
	}

	/**
	 * End-point called when editing the basis of the ballot
	 * 
	 * @param bid   ballot id
	 * @param basis ballot basis
	 * @return message indicating success
	 */
	@PutMapping(value = "/ballot/ajax/setBasis")
	@ResponseBody
	public String setBasis(@RequestParam String bid, @RequestParam String basis) {

		log.info("controller editing basis of ballot={}: {}", bid, basis);

		int id = Integer.parseInt(bid);
		boolean bas = "true".equals(basis);

		Ballot ballot;

		try {

			// editing basis
			ballot = ballotService.setBasis(id, bas);
			return String.format("basis updated");

		} catch (Exception e) {

			e.printStackTrace();
			return String.format("unable to change the basis for ballot %d", id);
		}

	}

	/**
	 * End-point called when editing the outcomes of the ballot
	 * 
	 * @param bid            ballot id
	 * @param ballotOutcomes ballot outcomes
	 * @return
	 */
	@PutMapping(value = "/ballot/ajax/setBallotOutcomes")
	@ResponseBody
	public String setBallotOutcomes(@RequestParam String bid, @RequestParam Integer ballotOutcomes) {

		log.info("controller editing outcomes of ballot={}: {}", bid, ballotOutcomes);

		int id = Integer.parseInt(bid);

		// editing outcomes
		Ballot ballot = ballotService.setBallotOutcomes(id, ballotOutcomes);

		return String.format("number of outcomes updated to " + ballot.getOutcomes());
	}


	/**
	 * End-point called when editing the start time of the ballot
	 * 
	 * @param bid             ballot id
	 * @param ballotStartTime ballot start time
	 * @return message indicating success
	 */
	@PutMapping(value = "/ballot/ajax/setBallotStartTime")
	@ResponseBody
	public String setBallotStartTime(@RequestParam String bid, @RequestParam LocalDateTime ballotStartTime) {

		log.info("controller editing start time of ballot={}: {}", bid, ballotStartTime);

		int id = Integer.parseInt(bid);

		// editing start time
		Ballot ballot = ballotService.setBallotStartTime(id, ballotStartTime);

		return String.format("Vote start time updated to " + ballot.getStartTime());
	}


	/**
	 * End-point called when editing the end time of the ballot
	 * 
	 * @param bid           ballot id
	 * @param ballotEndTime ballot end time
	 * @return message indicating success
	 */
	@PutMapping(value = "/ballot/ajax/setBallotEndTime")
	@ResponseBody
	public String setBallotEndTime(@RequestParam String bid, @RequestParam LocalDateTime ballotEndTime) {

		log.info("controller editing end time of ballot={}: {}", bid, ballotEndTime);

		int id = Integer.parseInt(bid);

		// editing end time
		Ballot ballot = ballotService.setBallotEndTime(id, ballotEndTime);

		return String.format("Vote end time updated to " + ballot.getEndTime());
	}

	/**
	 * End-point called when editing the voting group of a ballot
	 * 
	 * @param bid   ballot id
	 * @param group ballot voting group
	 * @return message indicating success
	 */
	@PutMapping(value = "/ballot/ajax/setVotingGroup")
	@ResponseBody
	public String setVotingGroup(@RequestParam String bid, @RequestParam String group) {

		log.info("controller editing voting group of ballot={}: {}", bid, group);

		int id = Integer.parseInt(bid);

		// editing voting group
		Ballot ballot = ballotService.setBallotVotingGroup(id, group);

		return String.format("Voting group updated to " + ballot.getVoters());
	}


	@PutMapping(value = "/ballot/ajax/setOptionEnabled")
	@ResponseBody
	public String toggleOption(@RequestParam Integer bid, @RequestParam String oid, @RequestParam String enabled) {

		log.info("controller toggling option #{} to {}", oid, enabled);

		boolean success = ballotService.toggleOption(bid, oid, Boolean.valueOf(enabled));

		if (success)
			return String.format("toggled option #%s to %b", oid, Boolean.valueOf(enabled));
		else
			return "option toggle failed";

	}


	/**
	 * Method will start vote by creating vote tokens for all the voters, setting
	 * the start time to now, and emailing all the voters
	 * 
	 * End-point called when starting vote
	 * 
	 * @param bid ballot id
	 * @return message indicating success
	 */
	@PostMapping(value = "/ballot/ajax/startVote")
	@ResponseBody
	public String startVote(@RequestParam Integer bid) {

		Ballot bal = ballotService.getBallotById(bid);

		// List will hold all the tokens created from starting the vote
		List<VoteToken> tokens = new ArrayList<VoteToken>();

		// method will create all the tokens
		tokens = voteService.startRestartVote(bal);

		// send all the tokens an email containing the link to the vote
		dummyEmailService.sendVoteEmailToList(tokens);

		// change expected votes to the amount of voters that got tokens and set the
		// start time to now
		ballotService.setVotesExpected(bal, tokens.size());
		ballotService.setBallotStartTime(bal.getId(), LocalDateTime.now());
		
		//send test email to website
		try {
			emailService.sendVoteTemplateEmailToList("ACVote", bal, tokens);
		} catch (Exception e) {
			e.printStackTrace();
			return "failed to start vote";
		}		
			return "Starting Vote";
		}
	/**
	 * End-point called when reminding voters to vote. Sends all voters another
	 * email.
	 * 
	 * @param bid  ballot id
	 * @param body
	 * @return message indicating success
	 */
	@PostMapping(value = "/ballot/ajax/remindVoters")
	@ResponseBody
	public String remindVoters(@RequestParam Integer bid, @RequestParam(value = "facIds[]") String[] facIds, @RequestParam String body) 
	{
		//get all available tokens for ballot
		List<VoteToken> tokens = voteService.getAllTokensForBallot(bid);
		
		
		
		//list of selected faculty
		List<Faculty> fac = facultyService.findFacultyMembers(facIds);
		
		Ballot bal = ballotService.getBallotById(bid);
		
		//list will hold all selected tokens
		List<VoteToken> newTokens = voteService.convertFacultyToTokens(tokens, fac);
		
		//send all the tokens an email containing the link to the vote
		dummyEmailService.sendVoteEmailToList(newTokens);
				
		//send tokens reminder with body as the message
		try {
			emailService.sendVoteTemplateEmailWithTextToList(body, "ACVote Reminder", bal, newTokens);
		} catch (Exception e) {
			e.printStackTrace();
			return "failed to remind voters";
		}
		
		return "Sending Reminders";
	}
		
	/**
	 * Method sends all tokens for the ballot an email with the body as the message and the link included
	 * 
	 */
	@GetMapping(value = "/ballot/ajax/tokenToFaculty")
	@ResponseBody
	public List<Faculty> tokenToFaculty(@RequestParam Integer bid) 
	{
		//get all available tokens for ballot
		List<VoteToken> tokens = voteService.getAllTokensForBallot(bid);
		
		List<Faculty> fac = new ArrayList<Faculty>();
		
		fac = voteService.convertTokensToFaculty(tokens);
		
		return fac;
	}
		
		
	

}