package edu.austincollege.acvote.unit.template;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.austincollege.acvote.template.BallotTemplate;
import edu.austincollege.acvote.template.dao.DummyTemplateDao;
import edu.austincollege.acvote.template.dao.TemplateDao;

class DummyTemplateDaoTests {
	
	TemplateDao dao;
	BallotTemplate t1;
	BallotTemplate t2;
	BallotTemplate t3;

	@BeforeEach
	void setUp() throws Exception {
		dao = new DummyTemplateDao();
		t1 = new BallotTemplate(1, "Sample Ballot Template", "Best CS Prof", "sumbit your ballot, or else",
				"who's the goat?", "IRV", 2, true);
		t2 = new BallotTemplate(4, "Sample Ballot Template", "Best CS Prof", "sumbit your ballot, or else",
				"who's the goat?", "IRV", 2, true);
		t3 = new BallotTemplate(15, "One more Ballot Template", "Insert Title Here",
				"idk just vote on something", "italian beef", "IRV", 1, false);
	}
	
	/*
	 * listAll Tests
	 */

	@Test
	void listAllTestNormal() throws Exception {
		assertFalse(dao.listAll().isEmpty());
	}
	
	@Test
	void listAllTestEmpty() throws Exception {
		((DummyTemplateDao) dao).setTemplates(new ArrayList<>());
		assertTrue(dao.listAll().isEmpty());
	}
	
	/*
	 * create Tests
	 */
	
	@Test
	void createTestNormal() throws Exception {
		
		assertTrue(t2.equals(dao.create("Sample Ballot Template", "Best CS Prof", "sumbit your ballot, or else",
				"who's the goat?", "IRV", 2, true)));
		assertNotEquals(3, dao.listAll().size());
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
	
	@Test
	void getTestInvalidID() {
		BallotTemplate returned = null;
		
		try {
			returned = dao.get(15);
			fail("Exception not thrown like expected");
		}
		catch (Exception e) {
			assertNull(returned);
		}
	}
	
	/*
	 * edit Tests
	 */
	
	@Test
	void editTestNormal() throws Exception {
		
			BallotTemplate returned = dao.edit(1, "One more Ballot Template", "Insert Title Here",
				"idk just vote on something", "italian beef", "IRV", 1, false);
			assertEquals(3, dao.listAll().size());
			assertFalse(t1.equals(returned));
		
	}
	
	@Test
	void editTestInvalidID() throws Exception {
		BallotTemplate returned = null;
		
		try {
			returned = dao.edit(15, "One more Ballot Template", "Insert Title Here",
				"idk just vote on something", "italian beef", "IRV", 1, false);
			fail("Exception not thrown like expected");
		}
		catch (Exception e) {
			assertEquals(3, dao.listAll().size());
			assertNull(returned);
		}
	}
	
	/*
	 * delete Tests
	 */
	
	@Test
	void deleteTestNormal() throws Exception {
		

			assertTrue(dao.delete(1));
			assertNotEquals(3, dao.listAll().size());
		
		
	}
	
	@Test
	void deleteTestInvalidID() throws Exception {
		
		try {
			assertFalse(dao.delete(15));
			fail("Exception not thrown like expected");
		}
		catch (Exception e) {
			assertEquals(3, dao.listAll().size());
		}
	}

}
