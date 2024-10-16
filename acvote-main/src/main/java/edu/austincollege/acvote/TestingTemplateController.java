package edu.austincollege.acvote;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**A simple controller class that tests the actemplate1.html
 * to make sure that it works. 
 * IMPORTANT: This document is just for testing purposes and is temporary.
 * will be removed after actemplate1.html is tested fully
 * @author alwinbhogal
 *
 */


@Controller
public class TestingTemplateController {

	@GetMapping(value="/test/template1")
	@ResponseBody
	public ModelAndView acTemplate1() {
		ModelAndView mav = new ModelAndView("actemplates/actemplate1test");
		return mav;
	}

	@GetMapping(value="/test/template2")
	@ResponseBody
	public ModelAndView acTemplate2() {
		ModelAndView mav = new ModelAndView("actemplates/actemplate2test");
		return mav;
	}

	@GetMapping(value="/test/template3")
	@ResponseBody
	public ModelAndView acTemplate3() {
		ModelAndView mav = new ModelAndView("actemplates/actemplate3test");
		return mav;
	}
	
	@GetMapping(value="/test/template4")
	@ResponseBody
	public ModelAndView acTemplate4() {
		ModelAndView mav = new ModelAndView("actemplates/actemplate4test");
		return mav;
	}

}
