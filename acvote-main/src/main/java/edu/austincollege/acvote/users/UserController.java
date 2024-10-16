package edu.austincollege.acvote.users;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class UserController {
	
	private static Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;

	
	/**
	 * Ajax calls that wish to reload the page with a flash message can
	 * post to this end point with message, which eventually redirects to 
	 * a get method to list all users....with flash message.
	 * 
	 * @param msg
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="/users/flash", method=RequestMethod.POST)
	public RedirectView flashUsers(	
				@RequestParam String msg, 
				final RedirectAttributes redirectAttributes) {

		log.debug("flashing [{}]",msg);
		
		redirectAttributes.addFlashAttribute("message",msg);

		return new RedirectView("/users",true);
	}
	
	/**
	 * End-point for displaying all users
	 * 
	 * @return userListView.html with all users injected
	 */
	@GetMapping(value = "/users")
	@ResponseBody
	public ModelAndView viewUsers() {

		ModelAndView mav = new ModelAndView("/users/userListView");
		List<AcUser> users = userService.listAllUsers();

		mav.addObject("users", users);
		return mav;
	}

	/**
	 * End-point for creating users in database
	 * 
	 * @return message indicating success of failure
	 * @throws Exception 
	 */
	@PostMapping(value = "/users/ajax/createUser")
	@ResponseBody
	public String createUser(@RequestParam String uid, @RequestParam String role) throws Exception {
		
		log.info("controller creating {} user {}", role, uid);
		
		try {
			userService.addUser(uid, role);
			
			return "success";
		}
		catch (NullUIDException e) {
			return "null uid";
		}
		catch (DuplicateUIDException e) {
			return "duplicate uid";
		}
		catch(Exception e) {
			throw e;
		}

	}
	
	/**
	 * End-point for deleting users from database
	 * Deletes via ajax
	 * 
	 * @param uid
	 * @return message indicating success or failure
	 */
	@DeleteMapping(value = "/users/ajax/deleteUser")
	@ResponseBody
	public String deleteUser(@RequestParam String uid) {
		
		log.info("deleting user uid = {}", uid);
		
		if(userService.deleteUser(uid))
			return String.format("user %s has been deleted", uid);
		else
			return String.format("could not delete user %s", uid);
	}
}
