package edu.austincollege.acvote.faculty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import edu.austincollege.acvote.lut.LutDao;
import edu.austincollege.acvote.utils.Props;

@Controller
@Secured("ROLE_ADMIN")
public class FacultyController {

	private static Logger log = LoggerFactory.getLogger(FacultyController.class);

	@Autowired
	FacultyService facultyService;

	@Autowired
	LutDao lutDao;

	
	/**
	 * Ajax calls that wish to reload the page with a flash message can
	 * post to this endpoint with message, which eventually redirects to 
	 * a get method to list all faculty....with flash message.
	 * 
	 * @param msg
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="/faculty/list/flash", method=RequestMethod.POST)
	public RedirectView flashFacultyList(	
				@RequestParam String msg, 
				final RedirectAttributes redirectAttributes) {

		log.debug("flashing [{}]",msg);
		
		redirectAttributes.addFlashAttribute("message",msg);

		return new RedirectView("/faculty/list",true);
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
	@RequestMapping(value="/faculty/details/flash", method=RequestMethod.POST)
	public RedirectView flashBallotDetail(	
				@RequestParam String acId,
				@RequestParam String msg, 
				final RedirectAttributes redirectAttributes) {

		log.debug("flashing [{}] with acId={}",msg,acId);
		
		redirectAttributes.addAttribute("acId",acId);
		redirectAttributes.addFlashAttribute("message",msg);

		return new RedirectView("/faculty/details",true);
	}
	
	/**
	 * Presents facultyListView.html populated with all faculty in database
	 * 
	 * @return html document with necessary data
	 */
	@GetMapping(value = "/faculty/list")
	@ResponseBody
	public ModelAndView viewAllFaculty() {

		log.info("controller viewing all faculty");

		ModelAndView mav = new ModelAndView("faculty/facultyListView");

		// adding all faculty
		mav.addObject("faculty", facultyService.getAllFaculty());
		mav.addObject("mridate", facultyService.mostRecentImportDate());
		return mav;
	}

	/**
	 * Presents selectedFaculty.html populated with faculty attributes in database
	 * 
	 * @param acId of selected faculty
	 * @return html document with necessary data
	 */
	@GetMapping(value = "/faculty/details")
	@ResponseBody
	public ModelAndView viewFaculty(@RequestParam String acId, final RedirectAttributes redirectAttributes) {

		log.info("controller viewing faculty #{}", acId);
		
		Faculty f = facultyService.findFaculty(acId);
		
		if(f != null) {
			ModelAndView mav = new ModelAndView("faculty/selectedFaculty");

			/*
			 * adding divisons, ranks, tenures, and voting statuses from db as well as
			 * selected faculty
			 */
			mav.addObject("divisions", lutDao.divisions());
			mav.addObject("ranks", lutDao.ranks());
			mav.addObject("tenureStatus", lutDao.tenureStatus());
			mav.addObject("votingStatus", lutDao.votingStatus());
			mav.addObject("faculty", facultyService.findFaculty(acId));
			
			return mav;
		}

		redirectAttributes.addFlashAttribute("message",String.format("invalid faculty id: '%s'",acId));
		
		return new ModelAndView("redirect:/faculty/list");

	}

	/**
	 * Ajax endpoint requesting delete of the faculty identified by the acId
	 * 
	 * @param acId of faculty to be deleted
	 * @return string message indicating success or failure
	 */
	@DeleteMapping("/faculty/ajax/delete")
	@ResponseBody
	public String deleteFaculty(@RequestParam String acId) {

		log.info("controller deleting faculty #{}", acId);

		// calling service to delete specified faculty
		if (facultyService.deleteFaculty(acId)) {
			return String.format("deleted faculty #%s", acId);
		} else {
			return String.format("failed to delete faculty #%s", acId);
		}

	}

	/**
	 * Ajax endpoint requesting edit of the faculty identified by the acId
	 * 
	 * @param acId
	 * @param lastName
	 * @param firstName
	 * @param dept
	 * @param div
	 * @param rank
	 * @param email
	 * @param tenure
	 * @param voting
	 * @param active
	 * @return
	 */
	@PutMapping("/faculty/ajax/editfaculty")
	@ResponseBody
	public String editFaculty(@RequestParam String acId, @RequestParam String lastName, @RequestParam String firstName,
			@RequestParam String dept, @RequestParam String div, @RequestParam String rank, @RequestParam String email,
			@RequestParam String tenure, @RequestParam boolean voting, @RequestParam boolean active) {

		log.info("controller editing faculty #{}", acId);

		try {

			facultyService.updateFaculty(acId, lastName, firstName, dept, div, rank, email, tenure, voting, active);

		} catch (Exception e) {
			return String.format("failed to edit faculty #%s", acId);
		}

		return String.format("successfully edited faculty #%s", acId);
	}

	@PostMapping("/faculty/ajax/create")
	@ResponseBody
	public String createFaculty(@RequestParam String acId) throws  DuplicateFacultyException, IDLengthException{
		
		log.info("controller creating new faculty #{}", acId);


        facultyService.addFaculty(acId, "", "", "", "", "", "", "", false, false);

        return acId;
	
		
	}

	/**
	 * End point for importing faculty from a CSV file form. Note in order for this
	 * to work, the form submitting the POST request must also include the _csrf
	 * (cross site request forgery) token automatically generated and inject by
	 * spring.
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping(value = "/faculty/import")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		log.info("handling file import with {}", file.getOriginalFilename());

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:/faculty/list";
		}

		try {
			int num = facultyService.importFacultyFromStream(file.getInputStream());

			log.info("imported {} faculty from {} ", num, file.getOriginalFilename());

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "redirect:/faculty/list";
	}

}
