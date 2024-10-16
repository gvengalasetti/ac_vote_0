package edu.austincollege.acvote.unit.vote;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.austincollege.acvote.ballot.option.VoteOption;
import edu.austincollege.acvote.vote.OptionScoreCard;




public class OptionScoreCardTests {

	OptionScoreCard optA;
	OptionScoreCard optB;
	OptionScoreCard optC;
	OptionScoreCard optD;
	

	@BeforeEach
	public void setUp() {
		optA = new OptionScoreCard(new VoteOption("ip","Pizza"),4);
		optA.setVotes(new int[]{3,1,1,0});
		
		optB = new OptionScoreCard(new VoteOption("js","Sushi"),4);
		optB.setVotes(new int[]{2,2,0,1});
		
		
		optC = new OptionScoreCard(new VoteOption("mt","Tacos"),4);
		optC.setVotes(new int[]{2,3,0,0});
		
		
		optD = new OptionScoreCard(new VoteOption("th","Thai"),4);
		optD.setVotes(new int[]{1,1,0,3});
		
	}
	
	@Test
	public void test_compare_more_first() {
		assertTrue(optA.compareTo(optB)>0);
		assertFalse(optB.compareTo(optA)>0);
	}
	
	
	@Test
	public void test_compare_same_first() {
		assertTrue(optC.compareTo(optB)>0);
	}
	
	@Test
	public void test_sort() {
		List<OptionScoreCard> cards = new ArrayList<>();
		cards.add(optC);
		cards.add(optB);
		cards.add(optD);
		cards.add(optA);

		
		Collections.sort(cards);
		for (OptionScoreCard osc : cards)
			System.out.println(osc);
		
		assertEquals(optD,cards.get(0));
	}

}
