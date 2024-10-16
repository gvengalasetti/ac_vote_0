package edu.austincollege.acvote.home;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.austincollege.acvote.AppConstants;
import jakarta.annotation.security.RolesAllowed;

/**
 * Home Controller
 * <p>
 * An instance of this class defines the top-level endpoints of our app.  
 * 
 * @author mahiggs
 *
 */
@Controller
public class HomeController {

	private static Logger log = LoggerFactory.getLogger(HomeController.class);


	@Secured("ROLE_ADMIN")
	@GetMapping("/home")
	@ResponseBody
	public ModelAndView handleHome() {

		log.debug("home");
		ModelAndView mav = new ModelAndView("home/adminHome");;
		return mav;

	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/")
	@ResponseBody
	public ModelAndView handleIncomplete() {

		log.debug("home");
		ModelAndView mav = new ModelAndView("redirect:/home");;
		return mav;

	}
	
	@GetMapping("/login")
	@PostMapping("/login")
	@ResponseBody
	public ModelAndView handlelogin(@RequestParam(required = false) String error) {

		log.debug("login");
		ModelAndView mav = new ModelAndView("login");
		
		if (error != null)
			mav.addObject("error",true);
		
		return mav;

	}
	
	
	@GetMapping("/logout-ok")
	@ResponseBody
	public ModelAndView handleLogoutOk() {

		log.info("logout-ok");
		
		ModelAndView mav = new ModelAndView("logout");

		return mav;

	}

	
}
