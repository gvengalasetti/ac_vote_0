package edu.austincollege.acvote.unit.ballot;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.option.VoteOption;
import edu.austincollege.acvote.faculty.Faculty;

class VoteOptionTest {
	
	VoteOption higgs;
	VoteOption pizza;

	@BeforeEach
	void setUp() throws Exception {
		
		higgs = new VoteOption("29102", "Higgs", true);
		pizza = new VoteOption("ip", "Pizza", false);
	}

	@Test
	void creationTest() {
		assertNotNull(higgs);
		assertNotNull(pizza);
	}
	
	@Test
	void getterTests() {
		assertNotNull(higgs.getoptionID());
		assertNotNull(higgs.getTitle());
		assertTrue(higgs.isEnabled());
		
		assertNotNull(pizza.getoptionID());
		assertNotNull(pizza.getTitle());
		assertFalse(pizza.isEnabled());
	}
	
	@Test
	void setterTests() {
		higgs.setoptionID("123456");
		assertTrue(higgs.getoptionID().equals("123456"));
		
		higgs.setTitle("Michael Higgs");
		assertTrue(higgs.getTitle().equals("Michael Higgs"));
		
		higgs.setEnabled(false);
		assertFalse(higgs.isEnabled());
		
		pizza.setoptionID("654321");
		assertTrue(pizza.getoptionID().equals("654321"));
		
		pizza.setTitle("Chicago-Style Pizza");
		assertTrue(pizza.getTitle().equals("Chicago-Style Pizza"));
		
		pizza.setEnabled(true);
		assertTrue(pizza.isEnabled());
	}
	
	@Test
	void equalsTest() {
		assertTrue(higgs.equals(higgs));
		assertTrue(pizza.equals(pizza));
	}
	
	@Test
	void equalsTestNull() {
		assertFalse(higgs.equals(null));
		assertFalse(pizza.equals(null));
	}
	
	@Test
	void equalsTestDifferentClass() {
		assertFalse(higgs.equals(new Faculty()));
		assertFalse(pizza.equals(new Ballot()));
	}
	
	@Test
	void equalsTestLast() {
		assertFalse(higgs.equals(pizza));
		
		higgs = pizza;
		
		assertTrue(higgs.equals(pizza));
	}
	
	@Test
	void hashCodeTest() {
		assertNotNull(higgs.hashCode());
		assertNotNull(pizza.hashCode());
	}

}
