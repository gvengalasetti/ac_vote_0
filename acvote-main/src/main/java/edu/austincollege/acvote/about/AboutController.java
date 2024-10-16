package edu.austincollege.acvote.about;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AboutController {

	
	@GetMapping("/about")
	@ResponseBody
	public ModelAndView index() {
		
		
		ModelAndView mav = new ModelAndView("about/aboutNav");
		
		return mav;
		
	}
	
	
	/*
	 * Lame example of a person's description using the template
	 * 
	 * make sure you update the mapping, and the method name
	 * 
	 * Also add your mapping to the aboutNav template as a new 
	 * list item matching the format of the others
	 */
    @GetMapping(value="/about/example")
    @ResponseBody
    public ModelAndView aboutExample() {
    	
    	ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		
		mav.addObject("name", "EXAMPLE PROGRAMMER");
		mav.addObject("description", "I am example, a junior in college. I like computers and html. This is not finished yet!");
		mav.addObject("photoName", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Default_pfp.svg/2048px-Default_pfp.svg.png");
		mav.addObject("altName", "Example Image");

		return mav;
    }
	// connection checking 
	@GetMapping(value="/about/kleahy")
	@ResponseBody
	public ModelAndView aboutKieran() {
		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		
		mav.addObject("name", "Kieran Leahy");
		mav.addObject("description", "I am Kieran, a junior at austin college. I am majoring in math and computer science and i worked on this project with my fellow students in cs 380 'Software Enginearing' as our class project ");
		mav.addObject("photoName", "../images/developerImages/devProfileKieran.jpeg");
		mav.addObject("altName", "Kieran Leahy Image");
		return mav;
	}
    
	@GetMapping(value="/about/tward")
	@ResponseBody
	public ModelAndView aboutTate() {

		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		
		mav.addObject("name", "Tobias Ward");
		mav.addObject("description", "Hi, I'm Tate. I am a junior at austin college. I am majoring in math and computer science with a minor in physics. Check out my <a href=\"https://github.com/MoonMoon2\" target=\"_blank\">github</a>! ");
		mav.addObject("photoName", "../images/developerImages/TobiasImage.jpeg");
		mav.addObject("altName", "Tobias Ward Image");
		return mav;
	}
    
	@GetMapping(value="/about/jortega")
	@ResponseBody
	public ModelAndView aboutJenn() {

		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		
		mav.addObject("name", "Jennifer Ortega");
		mav.addObject("description", "Hello! I'm Jenn. I am a senior at Austin College. I am double majoring in art and computer science. I made a story card!");
		mav.addObject("photoName", "../images/developerImages/toasterguy.jpg");
		mav.addObject("altName", "A lil guy");
		return mav;
	}
	
	@GetMapping(value="/about/jvankirk")
	@ResponseBody
	public ModelAndView aboutJaidyn() {
		
		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		
		mav.addObject("name","Jaidyn Vankirk");
		mav.addObject("description", "Hi!!! My name is Jaidyn! I am a junior at Austin College. I am majoring in computer science. The Aussies Spring Show is next week. It's really cool and about space!!!");
		mav.addObject("photoName", "../images/developerImages/Jaidyn'sPhoto.jpeg");
		mav.addObject("altName", "Jaidyn's Photo");
		return mav;
	}
    
	/**This is a method that describes the user Alwin :)
	 * @return mav -> the object that holds onto the template
	 */
	@GetMapping(value="/about/abhogal")
	@ResponseBody
	public ModelAndView aboutAlwin() {
		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		mav.addObject("name","Alwin Bhogal");
		mav.addObject("description", "Hello Everyone! My name is Alwin Bhogal! I am a junior here at Austin College. I am double majoring in Computer Science and Biochemistry. Right now I am waiting for the new Zelda game to come out because why would I study for my finals. I hope I can play instead of studying :).");
		mav.addObject("photoName", "../images/developerImages/abhogal.png");
		mav.addObject("altName", "Alwin Icon");
		return mav;
	}
	
	/**
	 * A brief description of ya boi Rosey Berg
	 */
	@GetMapping(value="/about/arosenberg")
	@ResponseBody
	public ModelAndView aboutAlan() {
		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		mav.addObject("name", "Alan Rosenberg");
		mav.addObject("description", "Waddup y'all, I'm Alan Rosenberg. I am a junior majoring in Computer Science, and the 2001 animated feature film <a href=\"https://en.wikipedia.org/wiki/Atlantis:_The_Lost_Empire\" target=\"_blank\">\"Atlantis\"</a> is the undisputed GOAT.");
		mav.addObject("photoName", "../images/developerImages/Rosey.jpeg");
		mav.addObject("altName", "Alan Rosenberg Image");
		return mav;
	}
	
	@GetMapping(value="/about/bhill")
	@ResponseBody
	public ModelAndView aboutBrandon() {
		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		mav.addObject("name", "Brandon Hill");
		mav.addObject("description", "Hey i'm Brandon. I am a Junior majoring in Computer Science with a minor in Mathmatics. I also play on the baseball team here at Austin College.");
		mav.addObject("photoName", "../images/developerImages/Brandon's Photo.jpeg");
		mav.addObject("altName", "Brandon's Photo");
		return mav;
	}
	
	@GetMapping(value="/about/dwilliams")
	@ResponseBody
	public ModelAndView aboutDylan() {
		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		mav.addObject("name", "Dylan Williams");
		mav.addObject("description", "Hi, I am Dylan Williams.  I am a senior majoring in Computer Science and Mathematics.  You might find me playing drums around campus from time to time.");
		mav.addObject("photoName", "../images/developerImages/DylanPhoto.jpg");
		mav.addObject("altName", "Dylan Williams Image");
		
		return mav;
	}
	

	@GetMapping(value="/about/fellis")
	@ResponseBody
	public ModelAndView aboutFoster() {
		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		mav.addObject("name", "Foster Ellis");
		mav.addObject("description", "hello, i am a junior in computer science");
		mav.addObject("photoName", "../images/developerImages/donkey.JPG");
		mav.addObject("altName", "foster Image");
		return mav;
	}

	@GetMapping(value="/about/jmeloni")
	@ResponseBody
	public ModelAndView aboutJacob() {
		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		mav.addObject("name", "Jacob Meloni");
		mav.addObject("description", "Hi, I'm Jacob, or Jake, I am a senior at Austin College, majoring in computer science with a minor in history. I am typically spending my free time playing Pool or gaming with friends.");
		mav.addObject("photoName", "../images/developerImages/JakeImage.jpeg");
		mav.addObject("altName", "Jacob Meloni Image");
		return mav;
	}
	
	@GetMapping(value="/about/epineda")
	@ResponseBody
	public ModelAndView aboutEnrique() {
		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		mav.addObject("name","Enrique Pineda");
		mav.addObject("description","Hello, I am Enrique Pineda. I am a senior majoring in Engineering Physics and minoring in mathematics. I love creating small robots and playing video games with friends.");
		mav.addObject("photoName", "../images/developerImages/EnriqueImage.jpeg");
		mav.addObject("altName", "Enrique Pineda Image");
		return mav;
	}
	
	 @GetMapping(value="/about/kaskew")
	 @ResponseBody
	 public ModelAndView aboutKate() {
	    	
		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
			
	    mav.addObject("name", "Kate Askew");
		mav.addObject("description", "I am Kate Askew! I am a senior double majoring in Math and Computer Science. I love to play water polo with my teammates!");
		mav.addObject("photoName", "../images/developerImages/kaskewIMG.jpeg");			
		mav.addObject("altName", "Example Kate Image");

		return mav;
	}

	@GetMapping(value="/about/mhaberle")
	@ResponseBody
	public ModelAndView aboutMiller() {
		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		mav.addObject("name","Miller Haberle");
		mav.addObject("description","Hello, I am Miller Haberle. I am a Computer Science major.");
		mav.addObject("photoName", "../images/developerImages/miller.jpeg");
		mav.addObject("altName", "Miller Haberle Image");
		return mav;
	}
	
	@GetMapping(value="/about/lluker")
	@ResponseBody
	public ModelAndView aboutLindy() {
		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		mav.addObject("name", "Lindy Luker");
		mav.addObject("description", "My name is Lindy, and I am a senior at Austin College. I am majoring in Physics and minoring in Computer Science. I love to draw and paint in my free time.");
		mav.addObject("photoName", "../images/developerImages/LindyImage.jpeg");
		mav.addObject("altName", "Lindy Image");
		return mav;
	}
	/*
	 * 
	 */
	@GetMapping(value="/about/mbose")
	@ResponseBody
	public ModelAndView aboutMichael() {
		ModelAndView mav= new ModelAndView("about/aboutProgrammer");
		mav.addObject("name","Michael Bose");
		mav.addObject("description","Hello I am Michael Bose. I am a junior majoring in Engineering Physics and Computer Science. ");
		mav.addObject("photoName","../images/developerImages/mbose.jpg");
		mav.addObject("altname","Bose Image");
		return mav;
	}
	
	@GetMapping("/about/mhiggs")
	@ResponseBody
	public ModelAndView aboutAction() {

		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		mav.addObject("name", "Michael Higgs");
		mav.addObject("description", "Computer Science Prof");
		mav.addObject("photoName", "../images/developerImages/mbose.jpg");
		mav.addObject("altName", "Higgs Image");

		return mav;
	}
	
	@GetMapping(value="/about/asandoval")
	@ResponseBody
	public ModelAndView aboutAlex() {
		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		mav.addObject("name","Alex Sandoval");
		mav.addObject("description", "Hello, I'm Alex. The things that make me unique are that I'm a Computer Science major and that I like coding.");
		mav.addObject("photoName", "../images/developerImages/asandoval.jpg");
		mav.addObject("altName", "Alex's image");
		return mav;
	}
	
	@GetMapping(value="/about/czapata")
	@ResponseBody
	public ModelAndView aboutCynthia() {
		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		mav.addObject("name","Cynthia Zapata");
		mav.addObject("description", "Hi! My name is Cynthia Zapata! I am a senior double majoring in Computer Science and East Asian Studies. Also, I am minoring in Art.");
		mav.addObject("photoName", "../images/developerImages/czapata.png");
		mav.addObject("altName", "Cynthia image");
		return mav;
	}

	@GetMapping(value="/about/ajimdaniels")
	@ResponseBody
	public ModelAndView aboutAmir() {
		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		mav.addObject("name","Amir Jim-Daniels");
		mav.addObject("description", "Hello, I'm Amir Jim-Daniels. I am a senior majoring in computer science and minoring in publc health.");
		mav.addObject("photoName", "../images/developerImages/amirImage.jpg");
		mav.addObject("altName", "Amir image");
		return mav;
	}
	
	@GetMapping(value="/about/gvengalasetti")
    @ResponseBody
    public ModelAndView aboutGuna() {
    	
    	ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		
		mav.addObject("name", "Guna Vengalasetti");
		mav.addObject("description", "Hi, I am Guna Vengalasetti. I am a senior in college. I'm a computer science major and art minor. I like to build and paint skateboards.");
		mav.addObject("photoName", "../images/developerImages/gunaVengalasetti.jpg");
		mav.addObject("altName", "Guna V. Image");

		return mav;
    }
	//about me for Curran Lee
	@GetMapping(value="/about/clee")
	@ResponseBody
	public ModelAndView aboutCurran() {
		ModelAndView mav = new ModelAndView("about/aboutProgrammer");
		mav.addObject("name","Curran Lee");
		mav.addObject("description", "Hello, my name is Curran Lee! I am a senior here. I am majoring in Computer Science and minoring in Philosophy. I am an IEM and Headphone nerd.");
		mav.addObject("photoName", "../images/developerImages/clee.jpg");
		mav.addObject("altName", "Curran Icon");
		return mav;
	}
	


}

