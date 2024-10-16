package edu.austincollege.acvote.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HTTPErrorController implements ErrorController {
	
	@GetMapping("/error")
	@ResponseBody
	public ModelAndView handleError(HttpServletRequest request) {
	
		ModelAndView mav = new ModelAndView("/error");
		
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		
		if (status != null) {
	        Integer statusCode = Integer.valueOf(status.toString());
	    
	        if(statusCode == HttpStatus.NOT_FOUND.value()) {
	        	
	        	mav.addObject("errMsg", "The page you are looking for does not exist, or has moved to a different place on the website. (Error 404)");
	        	
	        }
	        else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	        	mav.addObject("errMsg", "An Internal Server Error has occured! We are sorry for any inconvenicence(Error 500)");
	        }
	        
	    }
		
		
		return mav;
		
		
	}
}
