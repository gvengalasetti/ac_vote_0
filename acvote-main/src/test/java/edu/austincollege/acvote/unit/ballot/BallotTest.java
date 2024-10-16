package edu.austincollege.acvote.unit.ballot;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.option.VoteOption;

class BallotTest {
	
	Ballot b;
	VoteOption higgs;
	VoteOption block;

	@BeforeEach
	void setUp() throws Exception {
		b = new Ballot();
		
		higgs = new VoteOption("29102", "Higgs", true);
		block = new VoteOption("29103", "Block", true);
			
	}

	@Test
	void creationTest() {
		assertNotNull(b);
	}
	
	@Test
	void getterTests() {
		assertNotNull(b.getId());
		assertNull(b.getTitle());
		assertNull(b.getInstructions());
		assertNull(b.getDescription());
		assertTrue(b.getOptions().isEmpty());
		assertNull(b.getTypeOfVote());
		assertNull(b.getOutcomes());
		assertNull(b.getStartTime());
		assertNull(b.getEndTime());
		assertNull(b.getVoters());
		assertEquals(0, b.getTotalVotesExpected());
		assertFalse(b.isFacultyBased());
	}
	
	@Test
	void setterTests() {
		b.setId(20);
		assertEquals(20, b.getId());
		
		b.setTitle("Spare");
		assertTrue(b.getTitle().equals("Spare"));
		
		b.setInstructions("I got a spare at the bowling alley");
		assertTrue(b.getInstructions().equals("I got a spare at the bowling alley"));
		
		b.setDescription("A spare is when you fuck up, but fix it in time");
		assertTrue(b.getDescription().equals("A spare is when you fuck up, but fix it in time"));
		
		b.setOptions(new ArrayList<VoteOption>());
		assertTrue(b.getOptions().isEmpty());
		
		b.setTypeOfVote("IRV");
		assertTrue(b.getTypeOfVote().equals("IRV"));
		
		b.setOutcomes(1);
		assertEquals(1, b.getOutcomes());
		
		LocalDateTime now = LocalDateTime.now();
		b.setStartTime(now);
		assertEquals(now, b.getStartTime());
		
		LocalDateTime tenDays = LocalDateTime.now().plusDays(10);
		b.setEndTime(tenDays);
		assertEquals(tenDays, b.getEndTime());
		
		b.setTotalVotesExpected(50);
		assertEquals(50, b.getTotalVotesExpected());
		
		b.setFacultyBased(true);
		assertTrue(b.isFacultyBased());
	}
	
	/*
	 * addCandidate Tests
	 */
	
	@Test
	void addCandidateTest() {
		
		b.addCandidates(higgs);
		b.addCandidates(block);
		
		assertEquals(2, b.getOptions().size());
	}
	
	@Test
	void addCandidateTest2() {
		
		List<VoteOption> options = new ArrayList<>();
		options.add(higgs);
		options.add(block);
		
		b.addCandidates(options);
	}
	
	@Test
	void addCandidateTestEmptyList() {
		b.addCandidates();
		assertEquals(0, b.getOptions().size());
	}
	
	/*
	 * removeCandidate Tests
	 */
	
	@Test
	void removeCandidateTest() {
		b.addCandidates(higgs);
		b.addCandidates(block);
		
		assertTrue(b.removeCandidates(higgs));
		assertEquals(1, b.getOptions().size());
		assertTrue(b.removeCandidates(block));
		assertEquals(0, b.getOptions().size());
	}
	
	@Test
	void removeCandidateTestEmptyList() {
		b.addCandidates(higgs);
		b.addCandidates(block);
		
		assertTrue(b.removeCandidates(higgs));
		assertEquals(1, b.getOptions().size());
		
		assertTrue(b.removeCandidates(block));
		assertEquals(0, b.getOptions().size());
		
		assertFalse(b.removeCandidates(higgs));
		assertFalse(b.removeCandidates(block));
		
		
	}
	
	/*
	 * equals Tests
	 */
	
	@Test
	void equalsTestNormal() {
		assertTrue(b.equals(b));
	}
	
	@Test
	void equalsTestNull() {
		assertFalse(b.equals(null));
	}
	
	@Test
	void equalsTestDifferentClass() {
		assertFalse(b.equals(5));
	}
	
	@Test
	void equalsTestDifferentBallots() {
		assertFalse(b.equals(new Ballot(1, null, null, null, true, null, null, 0, null, null, null, 0)));
	}
	
	/*
	 * hasCode Tests
	 */
	
	@Test
	void hashCodeTest() {
		int hash = b.hashCode();
		assertNotNull(hash);
	}
	
	/*
	 * toString Tests
	 */
	
	@Test
	void toStringTest() {
		String s = b.toString();
		assertNotNull(s);
	}
}
