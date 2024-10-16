package edu.austincollege.acvote.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.austincollege.acvote.faculty.FacultyIO;
import edu.austincollege.acvote.vote.Votes;

@Configuration
public class HelperRegistry {
	
	@Bean
	public FacultyIO facultyIO() {
		return new FacultyIO();
	}

	
	
	
	@Bean
	public Votes votes() {
		return new Votes();
	}
	
}
