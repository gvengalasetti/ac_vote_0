package edu.austincollege.acvote.unit.ballot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.dao.BallotDao;
import edu.austincollege.acvote.ballot.dao.JdbcBallotDao;
import edu.austincollege.acvote.ballot.option.VoteOption;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("acvote.config")
class JdbcBallotDaoTests {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Mock
	JdbcTemplate mockJdbcTemplate;

	BallotDao dao;

	Ballot b1;
	Ballot b2;
	Ballot b3;

	@BeforeEach
	void setUp() throws Exception {
		JdbcBallotDao jdao = new JdbcBallotDao();
		jdao.setJdbcTemplate(jdbcTemplate);
		dao = jdao;

		List<VoteOption> options1 = new ArrayList<>();
		options1.add(new VoteOption("5854836", "Cathy Richardson"));
		options1.add(new VoteOption("4138778", "Ed Richardson"));

		List<VoteOption> options2 = new ArrayList<>();
		options2.add(new VoteOption("5993138", "David Schones"));

		List<VoteOption> options3 = new ArrayList<>();
		options3.add(new VoteOption("ip", "Pizza"));
		options3.add(new VoteOption("js", "Sushi"));
		options3.add(new VoteOption("mt", "Tacos"));
		options3.add(new VoteOption("th", "Thai"));

		b1 = new Ballot(1, "Coolest CS Professor",
				"Please drag and drop these candidates to the right column to vote for them. Ranking does matter, so make sure they are in your desired order.",
				"This is a description for ballot 1!", true, options1, "IRV", 1, LocalDateTime.of(2023, 6, 8, 16, 0, 0),
				LocalDateTime.of(2023, 7, 8, 16, 0, 0), "CW", 103);
		b2 = new Ballot(2, "These are definitely some CS professors",
				"Please drag and drop these candidates to the right column to vote for them. Ranking does matter, so make sure they are in your desired order. This is for ballot 2",
				"This description describes ballot 2.", true, options2, "IRV", 3,
				LocalDateTime.of(2023, 10, 1, 0, 0, 0), LocalDateTime.of(2023, 11, 1, 0, 0, 0), "CW", 27);
		b3 = new Ballot(3, "a title", "some instructions", "a description", true, options3, "IRV", 5,
				LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 12, 31, 23, 59, 59), "HU", 85);
	}

	@Test
	void creationTest() {
		assertNotNull(dao);
		assertNotNull(b1);
		assertNotNull(b2);
		assertNotNull(b3);
	}

	/*
	 * listAll Tests
	 */

	@Test
	void listAllTestNormal() throws Exception {
		assertFalse(dao.listAll().isEmpty());
	}

	/**
	 * In this test we ensure the creation of a ballot persists in the database. We
	 * create and then fetch it back. The new ballot should exist and all of the
	 * attribute field values should be correct as specified.
	 */
	@Test
	void createBallotTestNormal() throws Exception {

		List<Ballot> ballots = dao.listAll();
		int prevSize = ballots.size();

		for (Ballot b : ballots) {
			System.err.println(b);
		}

		// let's make a
		Ballot nb = dao.createBallot("new title", "new instr", "new desc", true, null, "IRV", 3,
				LocalDateTime.of(2001, 10, 10, 7, 0, 0), LocalDateTime.of(2001, 10, 10, 19, 0, 0), "CW", 103);

		// now we should have 1 more in the database
		assertEquals(prevSize + 1, dao.listAll().size());

		// and the new one should have all the attribute values we expect
		assertEquals(5, nb.getId());
		assertEquals("new title", nb.getTitle());
		assertEquals("new desc", nb.getDescription());
		assertEquals("new instr", nb.getInstructions());
		assertTrue(nb.isFacultyBased());
		// TODO assert candidates list is empty
		assertEquals("IRV", nb.getTypeOfVote());
		assertEquals(3, nb.getOutcomes());
		assertEquals(LocalDateTime.of(2001, 10, 10, 7, 0, 0), nb.getStartTime());
		assertEquals(LocalDateTime.of(2001, 10, 10, 19, 0, 0), nb.getEndTime());
		assertEquals("CW", nb.getVoters());
		assertEquals(103, nb.getTotalVotesExpected());

	}
	
	@Test
	void createBallotTestException() throws Exception {
		
		List<Ballot> ballots = dao.listAll();
		int prevSize = ballots.size();
		int endSize;
		Ballot nb = null;
		
		try {
			nb = dao.createBallot(null, null, null, false, null, null, null, null, null, null, 0);
			fail("Exception should be thrown");
		} catch (Exception e) {
			
			assertEquals(prevSize, dao.listAll().size());
			assertNull(nb);
		}
	}
	
	@Test
	void createBallotTestExceptionAgain() {
		JdbcBallotDao jdao = new JdbcBallotDao();
		jdao.setJdbcTemplate(mockJdbcTemplate);
		dao = jdao;
		
		Mockito.when(mockJdbcTemplate.update(anyString())).thenReturn(0);
		
		Ballot nb = null;
		
		try {
			nb = dao.createBallot(null, null, null, false, null, null, null, null, null, null, 0);
			fail("Exception expected but not thrown");
		}
		catch (Exception e) {
			
			assertNull(nb);
		}
	}

	/*
	 * deleteBallot Tests
	 */

	@Test
	void deleteBallotTestNormal() throws Exception {
		int prevSize = dao.listAll().size();

		try {
			dao.deleteBallot(1);
			assertNotEquals(prevSize, dao.listAll().size());
		} catch (Exception e) {
			fail("ID invalid");
		}
	}

	@Test
	void deleteBallotTestInvalidID() throws Exception {
		int prevSize = dao.listAll().size();

		try {
			dao.deleteBallot(100);
			fail("Exception not thrown like expected");
		} catch (Exception e) {
			assertEquals(prevSize, dao.listAll().size());
		}
	}

	/*
	 * updateBallot Tests
	 */

	@Test
	void updateBallotTestNormal() throws Exception {
		int prevSize = dao.listAll().size();

		try {
			dao.updateBallot(1, "Coolest CS Professor",
					"Please drag and drop these candidates to the right column to vote for them. Ranking does matter, so make sure they are in your desired order.",
					"This is a description for ballot 1!", true, null, "IRV", 1, LocalDateTime.of(2023, 6, 8, 16, 0, 0),
					LocalDateTime.of(2023, 7, 8, 16, 0, 0), "CW", 103);
			;
			Ballot bal = dao.getBallot(1);
			bal.setOptions(null);
			assertTrue(bal.equals(b1));
			assertEquals(prevSize, dao.listAll().size());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Invalid ID");
		}
	}

	@Test
	void updateBallotTestInvalidID() throws Exception {
		int prevSize = dao.listAll().size();

		try {
			dao.updateBallot(-1, "a title", "some instructions", "a description", true, null, "IRV", 5,
					LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 12, 31, 23, 59, 59), "HU", 85);
			fail("Exception not thrown like expected");
		} catch (Exception e) {
			assertEquals(prevSize, dao.listAll().size());
		}
	}

	/*
	 * getBallot Tests
	 */

	@Test
	void getBallotTestNormal() throws Exception {
		Ballot returned = dao.getBallot(1);
		assertNotNull(returned);
		returned.setOptions(null);
		assertTrue(b1.equals(returned));
	}

	@Test
	void getBallotTestInvalidID() {
		Ballot returned = null;

		try {
			returned = dao.getBallot(15);
			fail("Exception not thrown like excepted");
		} catch (Exception e) {
			assertNull(returned);
		}
	}
	
	@Test
	void getBallotTestIDLessThan0() {
		Ballot returned = null;
		
		try {
			returned = dao.getBallot(-1);
			fail("Exception not thrown like expected");
		}
		catch (Exception e) {
			assertNull(returned);
		}
	}

}
