package edu.austincollege.acvote.unit.vote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.austincollege.acvote.ballot.Ballot;
import edu.austincollege.acvote.ballot.option.VoteOption;
import edu.austincollege.acvote.vote.InvalidVoteTypeException;
import edu.austincollege.acvote.vote.IrvCounter;
import edu.austincollege.acvote.vote.OptionScoreCard;
import edu.austincollege.acvote.vote.VoteCast;
import edu.austincollege.acvote.vote.VoteCounter;
import edu.austincollege.acvote.vote.VoteResult;

public class VoteCountTests {

	class TestSubject extends VoteCounter {

		public TestSubject(Ballot b) {
			super(b);
		}

		@Override
		public VoteResult finalizeVote(List<OptionScoreCard> cards) {
			// do nothing stubb... only testing the other methods here.
			return null;
		}

	}

	TestSubject vc = null;

	ArrayList<VoteOption> lst = new ArrayList<>();
	ArrayList<VoteCast> lst3 = new ArrayList<>();

	Ballot b;

	@BeforeEach
	public void testSetup() {

		lst.add(new VoteOption("ip", "Pizza", true));
		lst.add(new VoteOption("js", "Sushi", true));
		lst.add(new VoteOption("mt", "Tacos", true));
		lst.add(new VoteOption("th", "Thai", true));

		lst3.add(new VoteCast(3, "t01", "ip", "js", "mt", "th"));
		lst3.add(new VoteCast(3, "t02", "js", "ip", "th", "mt"));
		lst3.add(new VoteCast(3, "t03", "ip", "mt", "th", "js"));
		lst3.add(new VoteCast(3, "t04", "mt", "th", "ip", "js"));

		// faculty based
		b = new Ballot(3, "Favorite Foods", "Rank all of the foods listed.", "Vote for your favorite food.", false,
				lst, "IRV", 1, LocalDateTime.of(2025, 12, 31, 12, 0), LocalDateTime.of(2025, 12, 31, 15, 0), "SC", 5);

		vc = new TestSubject(b);
	}

	@Test
	public void test_constructor() {

		assertEquals(b, vc.getBallot());

		assertTrue(vc.getExcludedOptions().isEmpty());
		assertEquals(4, vc.getValidOptions().size());

		vc.initializeVote();

	}

	@Test
	public void test_initializer() {

		vc.initializeVote();

		HashMap<String, OptionScoreCard> cards = vc.getCardsMap();

		assertEquals(4, cards.size()); // should have score cards for all 4 options
		for (VoteOption vo : lst) {
			OptionScoreCard c = cards.get(vo.getoptionID());
			assertEquals(vo, c.getOption());
			assertTrue(c.getBallots().isEmpty());
			assertEquals(4, c.getVotes().length);
		}

	}

	@Test
	public void test_count() {
		// vc.initializeVote(); not needed if countVote does it paranoidly
		List<OptionScoreCard> cards = vc.countVote(lst3);

		for (OptionScoreCard sc : cards) {
			System.out.println(sc);

			// since 3 votes, 2 preferred pizza and 1 sushi
			if (sc.getOid().equals("ip"))
				assertEquals(2, sc.getBallots().size());
			if (sc.getOid().equals("js"))
				assertEquals(1, sc.getBallots().size());
		}
	}

	@Test
	public void test_finalize_when_irv() {
		IrvCounter irv = new IrvCounter(b);
		List<OptionScoreCard> cards = irv.countVote(lst3);

		VoteResult vres = irv.finalizeVote(cards);

		System.out.println(vres);

	}

	@Test
	public void test_createCounter_whenIrv() throws Exception {

		b.setTypeOfVote(VoteCounter.VOTE_TYPE_IRV);

		VoteCounter ctr = VoteCounter.newCounterFor(b); // should be IRV

		assertTrue(ctr instanceof IrvCounter);

	}

	@Test
	public void test_createCounter_whenNOTIrv() throws Exception {

		b.setTypeOfVote(VoteCounter.VOTE_TYPE_QOTH);

		Assertions.assertThrows(InvalidVoteTypeException.class, () -> {

			VoteCounter ctr = VoteCounter.newCounterFor(b); // should be IRV

		}, "should throw invalid vote type exception");

	}

}
