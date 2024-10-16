package edu.austincollege.acvote.unit.ballot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.dao.BallotDao;
import edu.austincollege.acvote.ballot.dao.DummyBallotDAO;

class DummyBallotDAOTest {

	BallotDao dummy;
	List<Ballot> emptyList;
	Ballot sample1;
	Ballot sample2;
	Ballot sample3;

	@BeforeEach
	void setUp() throws Exception {
		dummy = new DummyBallotDAO();
		emptyList = new ArrayList<>();
		sample1 = new Ballot(1, "Coolest CS Professor", "Please drag and drop these candidates around!",
				"This is a description for ballot 1!", true, null, "IRV", 2, LocalDateTime.of(2023, 6, 8, 16, 0, 0),
				LocalDateTime.of(2023, 7, 8, 16, 0, 0), "CW", 124);
		sample2 = new Ballot(15, "Coolest CS Professor", "Please drag and drop these candidates around!",
				"This is a description for ballot 1!", true, null, "IRV", 2, LocalDateTime.now().plusDays(10),
				LocalDateTime.now().plusDays(20), "CW", 124);
		sample3 = new Ballot(16, "Which ones are CS Professors?", "Please drag and drop these candidates around!",
				"This is a description for ballot 2!", true, null, "IRV", 2, LocalDateTime.now().plusDays(50),
				LocalDateTime.now().plusDays(60), "CW", 7);
	}

	/*
	 * listAll Tests
	 */

	@Test
	void listAllTestNormal() throws Exception {
		assertFalse(dummy.listAll().isEmpty());
	}

	@Test
	void listAllTestEmpty() throws Exception {
		((DummyBallotDAO) dummy).setBallots(emptyList);
		assertTrue(dummy.listAll().isEmpty());
	}

	/*
	 * createBallot Tests
	 */

	@Test
	void createBallotTest() throws Exception {
		int prevSize = dummy.listAll().size();

		dummy.createBallot("Which ones are CS Professors?", "Please drag and drop these candidates around!",
				"This is a description for ballot 2!", true, null, "IRV", 2, LocalDateTime.of(2023, 6, 8, 16, 0, 0),
				LocalDateTime.of(2023, 7, 8, 16, 0, 0), "CW", 7);

		assertNotEquals(prevSize, dummy.listAll().size());
	}

	/*
	 * deleteBallot Tests
	 */

	@Test
	void deleteBallotTestNormal() throws Exception {
		int prevSize = dummy.listAll().size();
		try {
			dummy.deleteBallot(1);
			assertNotEquals(prevSize, dummy.listAll().size());
		} catch (Exception e) {
			fail("ID invalid");
		}
	}

	@Test
	void deleteBallotTestInvalidID() throws Exception {
		int prevSize = dummy.listAll().size();
		try {
			dummy.deleteBallot(100);
			fail("Exception not thrown like expected");
		} catch (Exception e) {
			assertEquals(prevSize, dummy.listAll().size());
		}
	}
	
	@Test
	void deleteBalloTestNullID() throws Exception {
		
	}

	/*
	 * updateBallot Tests
	 */

	@Test
	void updateBallotTestNormal() throws Exception {
		int prevSize = dummy.listAll().size();
		try {
			dummy.updateBallot(1, "Coolest CS Professor", "Please drag and drop these candidates around!",
					"This is a description for ballot 1!", true, null, "IRV", 2, LocalDateTime.now().plusDays(10),
					LocalDateTime.now().plusDays(20), "CW", 124);
			assertEquals(prevSize, dummy.listAll().size());
		} catch (Exception e) {
			fail("Invalid ID");
		}
	}

	@Test
	void updateBallotTestInvalidID() throws Exception {
		int prevSize = dummy.listAll().size();
		try {
			dummy.updateBallot(16, "Which ones are CS Professors?", "Please drag and drop these candidates around!",
					"This is a description for ballot 2!", true, null, "IRV", 2, LocalDateTime.now().plusDays(50),
					LocalDateTime.now().plusDays(60), "CW", 7);
			fail("Exception not thrown like expected");
		} catch (Exception e) {
			assertEquals(prevSize, dummy.listAll().size());
		}
	}

	/*
	 * getBallot Tests
	 */

	@Test
	void getBallotTestNormal() throws Exception {
		Ballot returned = dummy.getBallot(1);
		assertNotNull(returned);
		returned.setOptions(null);
		assertTrue(sample1.equals(returned));
	}

	@Test
	void getBallotTestInvalidID() {
		Ballot returned = null;

		try {
			returned = dummy.getBallot(15);
			fail("Exception not thrown like expected");
		} catch (Exception e) {
			assertNull(returned);
		}

	}

}
