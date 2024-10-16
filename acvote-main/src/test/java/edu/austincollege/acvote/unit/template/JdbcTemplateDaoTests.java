package edu.austincollege.acvote.unit.template;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import edu.austincollege.acvote.template.dao.JdbcTemplateDao;
import edu.austincollege.acvote.template.BallotTemplate;
import edu.austincollege.acvote.template.dao.TemplateDao;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("acvote.config")
class JdbcTemplateDaoTests {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	TemplateDao dao;
	JdbcTemplateDao jdao;
	BallotTemplate t1;
	BallotTemplate t2;
	BallotTemplate t3;

	@BeforeEach
	void setUp() throws Exception {
		jdao = new JdbcTemplateDao();
		jdao.setJdbcTemplate(jdbcTemplate);
		dao = jdao;
		t1 = new BallotTemplate(1, "CC", "Curriculum Committee", "Please rank the following candidates by transfering to your ballot and ordering them as needed according your preference. Click SUBMIT to cast your ballot.", 
				"<b>Committee Charge: </b>3-year term.  The Executive Committee of the Faculty advises the full Faculty on any matters of organization and represents that body, as appropriate, before other campus constituencies",
				"irv", 1, true);
		t2 = new BallotTemplate(2, "JC", "Johnson Center Committee", "Please rank the following candidates by transfering to your ballot and ordering them as needed according your preference. Click SUBMIT to cast your ballot.", "",
				"irv", 1, true);
		t3 = new BallotTemplate(1, "new title", "newer title", "instruction", "description", "IRV", 1, true);
	}

	/*
	 * listAll Tests
	 */
	
	@Test
	void listAllTestNormal() throws Exception {
		assertFalse(dao.listAll().isEmpty());
	}
	
	/*
	 * create Tests
	 */
	
	@Test
	void createTestNormal() throws Exception {
		
		List<BallotTemplate> templates = dao.listAll();
		int prevSize = templates.size();
		
		for(BallotTemplate bt : templates) {
			System.err.println(bt);
		}
		
		BallotTemplate nt = dao.create("template title", "ballot title", "instr", "desc", "IRV", 1, false);
		
		assertEquals(prevSize+1, dao.listAll().size());
		
		assertEquals(8, nt.getId());
		assertEquals("template title", nt.getTemplateTitle());
		assertEquals("ballot title", nt.getBallotTitle());
		assertEquals("instr", nt.getInstructions());
		assertEquals("desc", nt.getDescription());
		assertEquals("IRV", nt.getTypeOfVote());
		assertEquals(1, nt.getOutcomes());
		assertFalse(nt.isBasis());
	}
	
	/**
	 * test exception when creating template
	 * @throws Exception
	 */
	@Test
	void createTestException() throws Exception {
		
		List<BallotTemplate> templates = dao.listAll();
		
		for(BallotTemplate bt : templates) {
			System.err.println(bt);
		}
		
		
		
		BallotTemplate nt = dao.create(null, null, null, null, null, -1000000000, false);
		
		assertNull(nt);
	}
	
	/*
	 * get Tests
	 */
	
	@Test
	void getTestNormal() throws Exception {
		BallotTemplate returned = dao.get(1);
		assertNotNull(returned);
		assertTrue(t1.equals(returned));
	}
	
	/**
	 * test get id out of range
	 */
	@Test
	void getTestInvalidID() {
		BallotTemplate returned = null;
		//returned = dao.get(10);
		
			assertNull(returned);
		
			// Use assertThrows to test if the expected exception is thrown
		    Exception exception = assertThrows(Exception.class, () -> dao.get(10));

		    // Verify the exception message or other details, if necessary
		    assertEquals("unable to get template #10", exception.getMessage());
	}
	
	/**
	 * test get negative id
	 */
	@Test
	void getTestNegativeInvalidID() {
		BallotTemplate returned = null;
		//returned = dao.get(10);
		
			assertNull(returned);
		
			// Use assertThrows to test if the expected exception is thrown
		    Exception exception = assertThrows(Exception.class, () -> dao.get(-1));

		    // Verify the exception message or other details, if necessary
		    assertEquals("invalid template id", exception.getMessage());
	}
	
	/*
	 * edit Tests
	 */
	
	@Test
	void editTestNormal() throws Exception {
		List<BallotTemplate> templates = dao.listAll();
		int prevSize = templates.size();
		BallotTemplate returned = null;
		
		
			returned = dao.edit(1, "new title", "newer title", "instruction", "description", "IRV", 1, true);
			
			for(BallotTemplate bt : templates) {
				assertFalse(bt.equals(returned));
			}
			assertTrue(t3.equals(returned));
			assertEquals(prevSize, dao.listAll().size());
		
	}
	
	/**
	 * test edit with id out of range
	 * @throws Exception
	 */
	@Test
	void editTestInvalidID() throws Exception {
		List<BallotTemplate> templates = dao.listAll();
		int prevSize = templates.size();
		BallotTemplate returned = null;
		
		try {
			returned = dao.edit(10, "new title", "newer title", "instruction", "description", "IRV", 1, true);
			fail("Exception not thrown like expected");
		}
		catch(Exception e) {
			for(BallotTemplate bt : templates) {
				assertFalse(bt.equals(returned));
			}
			assertNull(returned);
			assertEquals(prevSize, dao.listAll().size());
		}
	}
	
	/*
	 * delete Tests
	 */
	
	@Test
	void deleteTestNormal() throws Exception {
		List<BallotTemplate> templates = dao.listAll();
		int prevSize = templates.size();
		
		
			assertTrue(dao.delete(1));
			assertEquals(prevSize-1, dao.listAll().size());
	}
	
	/**
	 * test delete with id out of range
	 * @throws Exception
	 */
	@Test
	void deleteTestInvalidID() throws Exception {
		List<BallotTemplate> templates = dao.listAll();
		int prevSize = templates.size();
		
		try {
			dao.delete(10);
			fail("Exception not thrown like expected");
		}
		catch(Exception e) {
			assertEquals(prevSize, dao.listAll().size());
		}
	}
	
	/**
	 * test adding double quotes
	 */
	@Test
	void doubleQuoteTest()
	{
		String before = "'hello'";
		
		String after = jdao.dq(before);
		
		assertEquals("''hello''", after);
	}
	
	/**
	 * test undoing double quotes
	 */
	@Test
	void undoDoubleQuoteTest()
	{
		String before = "''hello again''";
		
		String after = jdao.undoDq(before);
		
		assertEquals("'hello again'", after);
	}

}
