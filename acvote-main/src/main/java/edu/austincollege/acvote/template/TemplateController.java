package edu.austincollege.acvote.template;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.BallotService;
import edu.austincollege.acvote.lut.LutDao;


/**
 * 
 * Separate controller for using template service and template operations.  A ballot 
 * template is a starter kit for creating a ballot.   We allow the admin user to create
 * and manage these templates to make the process of frequent recurrent voting easy.  If
 * a template exists, the values in the template are used to quickly populate a new ballot
 * created from the template, leaving the admin user only a few fields to modify.
 *
 *
 */
@Controller
public class TemplateController {
	
	private static Logger log = LoggerFactory.getLogger(TemplateController.class);
	
	@Autowired
	TemplateService templateService;
	
	@Autowired
	BallotService ballotService;
	
	@Autowired
	LutDao lutDao;
	
	
	/**
	 * Ajax calls that wish to reload the page with a flash message can
	 * post to this endpoint with message, which eventually redirects to 
	 * a get method to list all ballots....with flash message.
	 * 
	 * @param msg
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="/templates/flash", method=RequestMethod.POST)
	public RedirectView flashTemplates(	
				@RequestParam String msg, 
				final RedirectAttributes redirectAttributes) {

		log.debug("flashing [{}]",msg);
		
		redirectAttributes.addFlashAttribute("message",msg);

		return new RedirectView("/templates",true);
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
	@RequestMapping(value="/template/flash", method=RequestMethod.POST)
	public RedirectView flashTemplateDetail(	
				@RequestParam Integer tid,
				@RequestParam String msg, 
				final RedirectAttributes redirectAttributes) {

		log.debug("flashing [{}] with tid={}",msg,tid);
		
		redirectAttributes.addAttribute("tid",tid);
		redirectAttributes.addFlashAttribute("message",msg);

		return new RedirectView("/template",true);
	}
	
	
	/**
	 * Endpoint for viewing all ballot templates
	 * 
	 * @return html document with all templates
	 */
	@GetMapping(value = "/templates")
	@ResponseBody
	public ModelAndView viewTemplates() {
		
		log.info("controller listing templates");

		ModelAndView mav = new ModelAndView("template/templateListView");
		
		//getting templates from service
		List<BallotTemplate> templates = templateService.listTemplates();
		mav.addObject("templates", templates);
		
		return mav;
	}
	
	/**
	 * End point for viewing details of a single template
	 * 
	 * @param template id
	 * @return html document with template details
	 */
	@GetMapping(value = "/template")
	@ResponseBody
	public ModelAndView viewDetails(@RequestParam Integer tid) {
		
		log.info("controller viewing template {}", tid);
		
		ModelAndView mav = new ModelAndView("template/selectedTemplate");
		
		mav.addObject("template", templateService.getTemplate(tid));
		
		return mav;
	}
	/**
	 * Method to create template using the values from the ballot with the matching id
	 * @param bid
	 * @return
	 */
	
	@PostMapping(value = "/template/ajax/createTemplateFromBallot")
	@ResponseBody
	public String createTemplateFromBallot(@RequestParam String bid)
	{
		int id = Integer.parseInt(bid);
		Ballot ballot = ballotService.getBallotById(id);
		
		BallotTemplate newTemp = templateService.createTemplate("Template of ballot: " + ballot.getId(), ballot.getTitle(), ballot.getInstructions(), ballot.getDescription(), ballot.getTypeOfVote(), ballot.getOutcomes(), ballot.isFacultyBased());
		
		if(newTemp != null)
		{
			log.debug("Template created with id: " + newTemp.getId());
			return "Template created with id: " + newTemp.getId();
		}
		else
		{
			return "Template could not be created";
		}
		
	}
	
	/**
	 * method to delete template with matching id
	 * @param tid
	 * @return
	 */
	@DeleteMapping(value = "/template/ajax/deleteTemplate")
	@ResponseBody
	public String deleteTemplate(@RequestParam Integer tid)
	{
		BallotTemplate temp = templateService.getTemplate(tid);
		
		boolean deleted = templateService.deleteTemplate(tid);
		
		if(deleted)
		{
			log.debug("Deleted template with id: " + temp.getId());
			return "Deleted template with id: " + temp.getId();
		}
		else
		{
			log.debug("Could not delete template with id: " + temp.getId());
			return "Could not delete template with id: " + temp.getId();
		}
		

	}
	
	@PutMapping(value = "/template/ajax/editTemplate")
	@ResponseBody
	public String editTemplate(@RequestParam Integer tid,
			@RequestParam String tTitle,
			@RequestParam String bTitle,
			@RequestParam String description,
			@RequestParam String instructions,
			@RequestParam String voteType,
			@RequestParam String outcomes,
			@RequestParam String basis) {
		
		log.info("controller editing template #{}", tid);
		
		int newOutcomes = Integer.parseInt(outcomes);
		boolean b = basis.equals("true");
		
		BallotTemplate t = templateService.editTemplate(tid, tTitle, bTitle, instructions, description, voteType, newOutcomes, b);
		
		if(t == null)
			return "unable to update template";
		
		else
			return "updated template";
	}

	@PostMapping(value = "/template/ajax/createTemplate")
	@ResponseBody
	public String createTemplate()
	{
		log.info("controller creating new template");

		BallotTemplate t = templateService.createTemplate("Enter Template Title", "Enter Ballot Title", "Enter Instructions", "Enter Description", "IRV", 1, true);
		int tid=t.getId();
		log.debug(String.valueOf(tid));
		return String.valueOf(tid);
		

}
}