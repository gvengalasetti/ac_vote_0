package edu.austincollege.acvote.unit.ballot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import edu.austincollege.acvote.ballot.dao.BallotOptionDao;
import edu.austincollege.acvote.ballot.dao.InvalidBallotIdException;
import edu.austincollege.acvote.ballot.dao.InvalidOptionIdException;
import edu.austincollege.acvote.ballot.dao.JdbcBallotDao;
import edu.austincollege.acvote.ballot.dao.JdbcBallotOptionDao;
import edu.austincollege.acvote.ballot.option.VoteOption;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("acvote.config")
class JdbcBallotOptionDaoTests {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Mock
	JdbcTemplate mockJdbcTemplate;
	
	BallotOptionDao dao;

	
	@BeforeEach
	void setUp() throws Exception {
		
		/* 
		 * first we create our subject and inject the jdbcTemplate helper object
		 */
		JdbcBallotOptionDao jdao = new JdbcBallotOptionDao();
		jdao.setJdbcTemplate(jdbcTemplate);
		dao = jdao;
		
	}

	
	/* ----------------------------------------------------------------------------------------------------------
	 * Make sure we can check for valid ballot ids.  Our schema.sql should be populated with a few ballots...at
	 * least 1.
	 * ----------------------------------------------------------------------------------------------------------
	 */
	@Test
	public void testValidBallotId_whenIdIsValid() throws Exception {
		assertTrue(dao.validBallotId(1));
	}
	
	/* ----------------------------------------------------------------------------------------------------------
	 * Make sure we can check for INVALID ballot ids.  Our schema.sql should be populated with a few ballots...at
	 * least 1.  Any negative id should be invalid.
	 * ----------------------------------------------------------------------------------------------------------
	 */
	@Test
	public void testValidBallotId_whenIdIsNOTValid() throws Exception {
		assertFalse(dao.validBallotId(-1));
	}
	
	
	

	/* ----------------------------------------------------------------------------------------------------------
	 * DAO should find the option when the ballot and the option are persisted in our database.
	 * 
	 * BRITTLE TEST WARNING: this test is dependent on the data in our schema.sql database.
	 * 
	 * @throws Exception
	 * ----------------------------------------------------------------------------------------------------------
	 */	
	@Test
	public void testOption_whenOptionIdIsValid() throws Exception {
		
		VoteOption opt = dao.option(1, "5854836");   // according to schema.sql we should have an option on ballot 1
		assertNotNull(opt);
		
		// make sure all attributes have the expected values
		assertEquals("5854836", opt.getoptionID());
		assertEquals("Cathy Richardson",opt.getTitle());
		assertTrue(opt.isEnabled());
		
	}

	
	/* ----------------------------------------------------------------------------------------------------------
	 * DAO should throw an exception if the option id is invalid.
	 * 
	 * @throws Exception
	 * ----------------------------------------------------------------------------------------------------------
	 */
	@Test
	public void testOption_whenOptionIdIsNOTValid() throws Exception {
		
		Assertions.assertThrows(InvalidOptionIdException.class, () -> {
			
			VoteOption opt = dao.option(1, "XXYYYZZ");   // according to schema.sql we should have an option on ballot 1
			
		}, "Invalid option id exception expected.");
		
		
	}

	/* ----------------------------------------------------------------------------------------------------------
	 * DAO should throw an exception if the ballot id is invalid.
	 * 
	 * @throws Exception
	 * ----------------------------------------------------------------------------------------------------------
	 */
	@Test
	public void testOption_whenBallotIdIsNotValid() throws Exception {
		
		Assertions.assertThrows(InvalidBallotIdException.class, () -> {
			
			VoteOption opt = dao.option(-1, "XXYYYZZ");   // according to schema.sql we should have an option on ballot 1
			
		}, "Invalid ballot id exception expected.");
		

		
	}
	
	@Test
	public void testOption_whenMultipleResults() throws Exception {
		
		JdbcBallotOptionDao jdao = new JdbcBallotOptionDao();
		jdao.setJdbcTemplate(mockJdbcTemplate);
		dao = jdao;
		
		List<VoteOption> options = new ArrayList<>();
		options.add(new VoteOption());
		options.add(new VoteOption());
		
		Mockito.when(mockJdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(options);
		Mockito.when(mockJdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(1);
		
		Assertions.assertThrows(InvalidOptionIdException.class, () -> {
			
			VoteOption option = dao.option(1, "XXYYYZZ");
			
		}, "Invalid ballot id exception expected.");
	}
	
	/* ----------------------------------------------------------------------------------------------------------
	 * Make sure we can retrieve all options for a given ballot.
	 * 
	 * BRITTLE TEST WARNING: This test depends on our schema.sql having only 2 ballots.  It will fail when we 
	 * add more data to schema.sql.  
	 * ----------------------------------------------------------------------------------------------------------
	 */
	@Test
	public void testAllOptions_whenValidBallot() throws Exception {
		
		List<VoteOption> options = dao.allOptionsOnBallot(1);
		
		assertEquals(2,options.size());   // currently there should be 2 options for ballot 1
				
	}
	
	/* ----------------------------------------------------------------------------------------------------------
	 * Make sure an exception is thrown when the ballot id is invalid or not found.
	 * 
	 * BRITTLE TEST WARNING: This test depends on our schema.sql having only 2 ballots.  It will fail when we 
	 * add more data to schema.sql.  
	 * ----------------------------------------------------------------------------------------------------------
	 */
	@Test
	public void testAllOptions_whenInvalidBallotId() throws Exception {
		

		Assertions.assertThrows(InvalidBallotIdException.class, () -> {
			
			List<VoteOption> options = dao.allOptionsOnBallot(-1);
			
		}, "Invalid ballot id exception expected.");
		

		
						
	}	
	
	/* ----------------------------------------------------------------------------------------------------------
	 * Make sure we can create a new option for a ballot. 
	 * ----------------------------------------------------------------------------------------------------------
	 */
	@Test
	public void testCreate_whenOptionIdIsUnique() throws Exception {

		assertEquals(2,dao.allOptionsOnBallot(1).size());   // should be 2 options to being with
		
		VoteOption opt = dao.createOption(1, "123456", "new", true);   // now create another
		
		List<VoteOption> options = dao.allOptionsOnBallot(1);  // should be 3
		
		assertEquals(3,options.size());
		assertEquals("123456",opt.getoptionID());
		assertEquals("new",opt.getTitle());
		assertTrue(opt.isEnabled());
				
	}
	
	/* ----------------------------------------------------------------------------------------------------------
	 * Make sure we can create a new option for a ballot. 
	 * ----------------------------------------------------------------------------------------------------------
	 */
	@Test
	public void testCreate_whenOptionIdIsDuplicated() throws Exception {

		int oldnum = dao.allOptionsOnBallot(1).size();
		assertTrue(oldnum>1);
		
		Assertions.assertThrows(InvalidOptionIdException.class, () -> {
		
		VoteOption opt = dao.createOption(1, "5854836", "new", true);   // now create another but with a duplicate ID
		
		}, "expected a invalid option id exception");
		
		List<VoteOption> options = dao.allOptionsOnBallot(1);  // should be 3
		
		assertEquals(oldnum,options.size());  // assert nothing added

	}
	
	@Test
	public void testCreate_whenOptionIdIsNotUnique() throws Exception {
		
		JdbcBallotOptionDao jdao = new JdbcBallotOptionDao();
		jdao.setJdbcTemplate(mockJdbcTemplate);
		dao = jdao;
		
		Mockito.when(mockJdbcTemplate.update(anyString())).thenReturn(0);
		Mockito.when(mockJdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(1);
		
		int oldnum = dao.allOptionsOnBallot(1).size();
		assertTrue(oldnum == 0);
		
		Assertions.assertThrows(Exception.class, () -> {
			
			VoteOption opt = dao.createOption(1, "5854836", "new", true);
			
		}, "expected an invalid option id exception");
		
		List<VoteOption> options = dao.allOptionsOnBallot(1);
		
		assertEquals(oldnum, options.size());
	}
	
	
	/**
	 * Ensure we can add option to ballot from a java object.
	 * @throws Exception
	 */
	@Test
	public void testAddOption_whenOptionIsUnique() throws Exception {
		
		assertEquals(2,dao.allOptionsOnBallot(1).size());   // should be 2 options to being with
		
		VoteOption opt = new VoteOption("123456", "new", true); 
		dao.addOptionOnBallot(1, opt);
		
		List<VoteOption> options = dao.allOptionsOnBallot(1);  
		
		assertEquals(3,options.size());
		assertEquals("123456",opt.getoptionID());
		assertEquals("new",opt.getTitle());
		assertTrue(opt.isEnabled());
		
	}
	
	/* ----------------------------------------------------------------------------------------------------------
	 * Make sure we can create a new option for a ballot. 
	 * ----------------------------------------------------------------------------------------------------------
	 */
	@Test
	public void testAddOption_whenOptionIdIsDuplicated() throws Exception {

		int oldnum = dao.allOptionsOnBallot(1).size();
		assertTrue(oldnum>1);
		
		Assertions.assertThrows(InvalidOptionIdException.class, () -> {
			
			
		
		VoteOption opt = new VoteOption("5854836", "new", true);   // now create another but with a duplicate ID
		
		dao.addOptionOnBallot(1, opt);
		
		}, "expected a invalid option id exception");
		
		List<VoteOption> options = dao.allOptionsOnBallot(1); 
		
		assertEquals(oldnum,options.size());  // assert nothing added

	}
	
	
	/**
	 * Ensure we can clear all objects on an existing ballot
	 * @throws Exception
	 */
	@Test
	public void testClearOptions () throws Exception {
		
		assertEquals(2,dao.allOptionsOnBallot(1).size());   // should be 2 options to being with
		
		dao.clearOptionsOnBallot(1);
		
		List<VoteOption> options = dao.allOptionsOnBallot(1);  
		
		assertEquals(0,options.size());
		
	}
	
	@Test
	public void testClearOptionsFailure() {
		
		JdbcBallotOptionDao jdao = new JdbcBallotOptionDao();
		jdao.setJdbcTemplate(mockJdbcTemplate);
		dao = jdao;
		
		Mockito.when(mockJdbcTemplate.update(anyString())).thenReturn(0);
		Mockito.when(mockJdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(1);
		
		boolean success = false;
		
		try {
			success = dao.clearOptionsOnBallot(1);
			fail("Exception not thrown like excpeted");
			
		}
		catch (Exception e) {
			
			assertFalse(success);
		}
	}
	
	/**
	 * Ensure we can add option to ballot from a java object.
	 * @throws Exception
	 */
	@Test
	public void testDeleteOption_whenValidOptionId () throws Exception {
		
		List<VoteOption> options = dao.allOptionsOnBallot(1);
		assertEquals(2,options.size());   // should be 2 options to being with

		VoteOption vo1 = options.get(0);
		VoteOption vo2 = options.get(1);
		
		String oid = vo1.getoptionID();
		
		assertTrue(options.contains(vo1));
		assertTrue(options.contains(vo2));
		
		dao.deleteOptionOnBallot(1, oid);
				
		List<VoteOption> options2 = dao.allOptionsOnBallot(1);  
		assertEquals(options.size()-1, options2.size());
	
		for (VoteOption vo : options2) {
			if (vo.getoptionID().equals(oid)) Assertions.fail("failed to delete option ");
		}
	}
	
	
	/**
	 * Ensure exception is thrown when we try to delete a missing/invalid option
	 * @throws Exception
	 */
	@Test
	public void testDeleteOption_whenInvalidOptionId () throws Exception {
		
		List<VoteOption> options = dao.allOptionsOnBallot(1);
		assertEquals(2,options.size());   // should be 2 options to being with

		VoteOption vo1 = options.get(0);
		VoteOption vo2 = options.get(1);
		
		String oid = vo1.getoptionID();
		
		assertTrue(options.contains(vo1));
		
		Assertions.assertThrows(InvalidOptionIdException.class, () -> {
			dao.deleteOptionOnBallot(1, "XYZZY");  // bad option id should throw exeption
		}, "expected a invalid option id exception");
		

	}
	
	/*
	 * updateOptionOnBallot Tests
	 */
	
	@Test
	void updateBallotTestNormal() throws Exception {
		List<VoteOption> options = dao.allOptionsOnBallot(1);
		assertEquals(2, options.size());
		
		try {
			assertTrue(dao.updateOptionOnBallot(1, "5854836", false));
			assertEquals(2, options.size());
		} 
		catch (Exception e) {
			e.printStackTrace();
			Assertions.fail("Exception thrown when unexpected");
		}
	}
	
	@Test
	void updateBallotTestInvalidBid() throws Exception {
		List<VoteOption> options = dao.allOptionsOnBallot(1);
		assertEquals(2, options.size());
		
		VoteOption v1 = options.get(0);
		
		try {
			dao.updateOptionOnBallot(5, "5854836", false);
			Assertions.fail("Exception not thrown like expected");
		}
		catch(Exception e) {
			assertTrue(v1.equals(dao.option(1, "5854836")));
			assertEquals(2, dao.allOptionsOnBallot(1).size());
		}
	}
	
	@Test
	void updateBallotTestInvalidOid() throws Exception {
		List<VoteOption> options = dao.allOptionsOnBallot(1);
		assertEquals(2, options.size());
		
		VoteOption v1 = options.get(0);
		
		try {
			dao.updateOptionOnBallot(1, "dog", false);
			Assertions.fail("Exception not thrown like expected");
		}
		catch(Exception e) {
			assertTrue(v1.equals(dao.option(1, "5854836")));
			assertEquals(2, dao.allOptionsOnBallot(1).size());
		}
	}

}
