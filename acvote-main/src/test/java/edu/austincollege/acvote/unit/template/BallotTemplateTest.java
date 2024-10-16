package edu.austincollege.acvote.unit.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.austincollege.acvote.template.BallotTemplate;
import edu.austincollege.acvote.ballot.Ballot;

public class BallotTemplateTest {
	BallotTemplate template;
	
	@BeforeEach
	void setUp() throws Exception {
		template = new BallotTemplate();
	}
	
	@Test
	void creationTest() {
		assertNotNull(template);
		template = new BallotTemplate(1, "tempT","balT","inst","desc","irv",1,true);
	}
	
	@Test
	void toStringTest() {
		String result = template.toString();
		
		assertNotNull(result);
	}
	
	@Test
	void getterTests() {
		assertNotNull(template.getId());
		assertNull(template.getTemplateTitle());
		assertNull(template.getBallotTitle());
		assertNull(template.getInstructions());
		assertNull(template.getDescription());
		assertEquals(0, template.getOutcomes());
		assertNull(template.getTypeOfVote());
		assertFalse(template.isBasis());
	}
	
	@Test
	void setterTests() {
		template.setId(1);
		template.setTemplateTitle("temp title");
		template.setBallotTitle("bal title");
		template.setInstructions("instructions");
		template.setDescription("description");
		template.setOutcomes(1);
		template.setTypeOfVote("irv");
		template.setBasis(true);
		
		assertEquals(1, template.getId());
		assertEquals("temp title", template.getTemplateTitle());
		assertEquals("bal title", template.getBallotTitle());
		assertEquals("instructions", template.getInstructions());
		assertEquals("description", template.getDescription());
		assertEquals(1, template.getOutcomes());
		assertEquals("irv", template.getTypeOfVote());
		assertTrue(template.isBasis());
	}
	
	@Test
	void hashTest() {
		assertNotNull(template.hashCode());
	}
	
	@Test
	void compareTest() {
		BallotTemplate newTemp = new BallotTemplate();
		Ballot ballot = new Ballot();
		
		assertTrue(template.equals(template));
		assertTrue(template.equals(newTemp));
		assertFalse(template.equals(ballot));
		assertFalse(template.equals(null));
	}
	
	@Test
	void compareIrvTest() {
		BallotTemplate newTemp = new BallotTemplate();
		
		template.setTypeOfVote("irv2");
		newTemp.setTypeOfVote("irv");
		
		assertFalse(template.equals(newTemp));
	}
	
	@Test
	void compareTemplateTitleTest() {
		BallotTemplate newTemp = new BallotTemplate();
		template.setTemplateTitle("temp title");
		newTemp.setTemplateTitle("title temp");
		
		assertFalse(template.equals(newTemp));
	}
	
	@Test
	void compareOutcomesTest() {
		BallotTemplate newTemp = new BallotTemplate();
		template.setOutcomes(1);
		newTemp.setOutcomes(100);
	
		
		assertFalse(template.equals(newTemp));
	}
	
	@Test
	void compareInstructionsTest() {
		BallotTemplate newTemp = new BallotTemplate();
		template.setInstructions("instructions");
		newTemp.setInstructions("how to vote");
		
		assertFalse(template.equals(newTemp));
	}
	
	@Test
	void compareIdTest() {
		BallotTemplate newTemp = new BallotTemplate();
		template.setId(23);
		newTemp.setId(19);
		
		assertFalse(template.equals(newTemp));
	}
		
	@Test
	void compareDescriptionTest() {
		BallotTemplate newTemp = new BallotTemplate();
		template.setDescription("description");
		newTemp.setDescription("desc");
		
		assertFalse(template.equals(newTemp));
	}
	
	@Test
	void compareBasisTest() {
		BallotTemplate newTemp = new BallotTemplate();
		template.setBasis(true);
		newTemp.setBasis(false);
		
		assertFalse(template.equals(newTemp));
	}
	
	@Test
	void compareBallotTitleTest() {
		BallotTemplate newTemp = new BallotTemplate();
		template.setBallotTitle("bal title");
		newTemp.setBallotTitle("title");
		
		assertFalse(template.equals(newTemp));
	}
	}
	

