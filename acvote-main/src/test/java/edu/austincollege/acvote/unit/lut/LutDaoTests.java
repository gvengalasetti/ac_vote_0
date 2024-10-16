package edu.austincollege.acvote.unit.lut;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import edu.austincollege.acvote.lut.LutDao;
import edu.austincollege.acvote.lut.LutItem;


//needed this annotation to roll back test
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("acvote.config")
public class LutDaoTests {
	
	private static Logger log = LoggerFactory.getLogger(LutDaoTests.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	LutDao dao;
	
	
	/**
	 * Runs before each test, this method creates and configures our
	 * text subject with an appropriate jdbcTemplate that uses our 
	 * configured database.
	 */
	@BeforeEach
	public void setupSubject() {
		dao = new LutDao();
		dao.setJdbcTemplate(jdbcTemplate);
	}
	
	
	@Test
	public void testListAllDivisions() throws Exception {
		List<LutItem> pats = dao.divisions();
		
		System.out.println(pats);		
		
	}

	@Test
	public void testListRanks() throws Exception {
		List<LutItem> pats = dao.ranks();
		
		System.out.println(pats);		
		
	}
	

	@Test
	public void testListVoting() throws Exception {
		List<LutItem> pats = dao.votingStatus();
		System.out.println(pats);		
		
	}

	@Test
	public void testListTenure() throws Exception {
		List<LutItem> pats = dao.tenureStatus();
		System.out.println(pats);		
		
	}

	

	
}
