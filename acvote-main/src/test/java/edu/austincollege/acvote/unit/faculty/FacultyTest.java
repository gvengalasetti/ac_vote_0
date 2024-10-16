package edu.austincollege.acvote.unit.faculty;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.austincollege.acvote.faculty.Faculty;

class FacultyTest {
	// Full Constructor Faculty Test handle
	Faculty tFaculty = new Faculty("1234","lastName","firstName","CS","Div","Rank","generic@gmail.com","T",true, true);;
	// Empty Faculty test
	Faculty tEmptyFaculty = new Faculty();
	
	@Test
	void testDefaultConstructor() {
		assertNotNull(tEmptyFaculty);
	}
	
	@Test
	void testRegConstructor() {
		assertNotNull(tFaculty);
		assertEquals("Rank",tFaculty.getRank());
	}
	
	@Test
	void testGetters() {
		assertEquals("1234",tFaculty.getAcId());
		assertEquals("lastName",tFaculty.getLastName());
		assertEquals("firstName",tFaculty.getFirstName());
		assertEquals("CS",tFaculty.getDept());
		assertEquals("Div",tFaculty.getDiv());
		assertEquals("Rank",tFaculty.getRank());
		assertEquals("generic@gmail.com",tFaculty.getEmail());
		assertEquals("T",tFaculty.getTenure());
		assertEquals(true,tFaculty.isVoting());
	}
	
	@Test
	void testSetters() {
		tFaculty.setAcId("456");
		tFaculty.setLastName("Doe");
		tFaculty.setFirstName("John");
		tFaculty.setDept("Math");
		tFaculty.setDiv("Science");
		tFaculty.setRank("High");
		tFaculty.setEmail("newEmail@gmail.com");
		tFaculty.setTenure("T");
		tFaculty.setVoting(false);
		
		// Test new set values
		assertEquals("456",tFaculty.getAcId());
		assertEquals("Doe",tFaculty.getLastName());
		assertEquals("John",tFaculty.getFirstName());
		assertEquals("Math",tFaculty.getDept());
		assertEquals("Science",tFaculty.getDiv());
		assertEquals("High",tFaculty.getRank());
		assertEquals("newEmail@gmail.com",tFaculty.getEmail());
		assertEquals("T",tFaculty.getTenure());
		assertEquals(false,tFaculty.isVoting());
	}
}
