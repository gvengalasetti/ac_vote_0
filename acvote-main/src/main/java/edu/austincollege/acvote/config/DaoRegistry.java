package edu.austincollege.acvote.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.austincollege.acvote.ballot.dao.BallotDao;
import edu.austincollege.acvote.ballot.dao.BallotOptionDao;
import edu.austincollege.acvote.ballot.dao.JdbcBallotDao;
import edu.austincollege.acvote.ballot.dao.JdbcBallotOptionDao;
import edu.austincollege.acvote.faculty.dao.FacultyDAO;
import edu.austincollege.acvote.faculty.dao.JdbcTemplateFacultyDao;
import edu.austincollege.acvote.lut.LutDao;
import edu.austincollege.acvote.properties.dao.JdbcPropertiesDao;
import edu.austincollege.acvote.properties.dao.PropertiesDao;
import edu.austincollege.acvote.template.dao.JdbcTemplateDao;
import edu.austincollege.acvote.template.dao.TemplateDao;
import edu.austincollege.acvote.users.dao.JdbcUserDao;
import edu.austincollege.acvote.users.dao.UserDao;
import edu.austincollege.acvote.vote.dao.DummyVoteCastDao;
import edu.austincollege.acvote.vote.dao.JdbcTemplateVoteCastDao;
import edu.austincollege.acvote.vote.dao.VoteCastDao;

@Configuration
public class DaoRegistry {
	
	@Bean
	public VoteCastDao vcDao() {
		return new JdbcTemplateVoteCastDao();
	}
	
	
	/**
	 * When autowiring, the variable in your service needs to named
	 * the same as the method below....facultyDao.
	 * 
	 * @return
	 */	
	@Bean
	public FacultyDAO facultyDao() {
		return new JdbcTemplateFacultyDao();
	}
	
	/**
	 * When autowiring, the variable in your service needs to named
	 * the same as the method below....ballotDao.
	 * 
	 * @return
	 */
	@Bean
	public BallotDao ballotDao() {
		return new JdbcBallotDao();
	}
	
//	/**
//	 * When autowiring, the variable in your service needs to named
//	 * the same as the method below....lutDao.
//	 * 
//	 * @return
//	 */
//	@Bean
//	public LutDao lutDao() {
//		return new LutDao();
//	}
	
	/**
	 * An optionDao variable will be autowired by executing the following
	 * factory method.
	 * 
	 * @return
	 */
	@Bean
	public BallotOptionDao optionDao() {
		return new JdbcBallotOptionDao();
	}
	
	/**
	 * When autowiring, the variable in your service needs to named
	 * the same as the method below....templateDao.
	 * 
	 * @return
	 */
	@Bean
	public TemplateDao templateDao() {
		return new JdbcTemplateDao();
	}
	
	@Bean
	public UserDao userDao() {
		return new JdbcUserDao();
	}
	
	@Bean
	public PropertiesDao pDao() {
		return new JdbcPropertiesDao();
	}
	
}
