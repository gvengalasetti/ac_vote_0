package edu.austincollege.acvote.unit.vote;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import edu.austincollege.acvote.vote.VoteCast;
import edu.austincollege.acvote.vote.VoteToken;
import edu.austincollege.acvote.vote.dao.JdbcTemplateVoteCastDao;
import edu.austincollege.acvote.vote.dao.VoteCastDao;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("acvote.config")
public class JdbcTemplateVoteCastDaoTest {

private Logger log = LoggerFactory.getLogger(JdbcTemplateVoteCastDao.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	VoteCastDao dao;
	List<VoteCast> emptyList;
	VoteToken t1;
	VoteToken t2;
	VoteToken t3;
	
	VoteCast c1;
	VoteCast c2;
	VoteCast c3;
	
	@BeforeEach
	void setUp() throws Exception {
		JdbcTemplateVoteCastDao jdao = new JdbcTemplateVoteCastDao();
		jdao.setJdbcTemplate(jdbcTemplate);
		dao = jdao;
		emptyList = new ArrayList<>();
		t1 =  VoteToken.newToken(3, "5854836");
		t2 =  VoteToken.newToken(3, "4138778");
		t3 =  VoteToken.newToken(3, "5488165");
		
		c1 = new VoteCast();
		c2 = new VoteCast();
		c3 = new VoteCast();
		
		ArrayList<String> abc = new ArrayList<String>();
		abc.add("mt");
		abc.add("th");
		abc.add("ip");
		
		ArrayList<String> cba = new ArrayList<String>();
		cba.add("js");
		cba.add("mt");
		cba.add("th");
		
		ArrayList<String> bac = new ArrayList<String>();
		bac.add("ip");
		bac.add("js");
		bac.add("mt");
		
		
		c1.setBid(3);
		c1.setToken(t1.getToken());
		c1.setVotes(abc);
		
		c2.setBid(3);
		c2.setToken(t2.getToken());
		c2.setVotes(cba);
		
		c3.setBid(3);
		c3.setToken(t3.getToken());
		c3.setVotes(bac);
	}
	
	@Test
	void VotesForBallotTest() throws Exception
	{
		List<VoteCast> results = dao.votesForBallot(3);
		
		assertEquals(6, results.size());
		
	}
	
	@Test
	void VotesForEmptyBallotTest() throws Exception
	{
		List<VoteCast> results = dao.votesForBallot(0);
		
		assertEquals(0, results.size());
		
	}
	
	
	@Test
	void CastVoteTest() throws Exception
	{
		dao.castVote(c1);
		
		List<VoteCast> results = dao.votesForBallot(3);
		
		assertEquals(7, results.size());
	}
	
	@Test
	void CastVoteMultipleTest() throws Exception
	{
		dao.castVote(c1);
		dao.castVote(c2);
		dao.castVote(c3);
		
		List<VoteCast> results = dao.votesForBallot(3);
		
		assertEquals(9, results.size());
	}
	
}
