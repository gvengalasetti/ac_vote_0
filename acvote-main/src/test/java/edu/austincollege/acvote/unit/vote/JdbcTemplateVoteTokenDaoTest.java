package edu.austincollege.acvote.unit.vote;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import edu.austincollege.acvote.vote.dao.JdbcTemplateVoteTokenDao;
import edu.austincollege.acvote.vote.VoteToken;
import edu.austincollege.acvote.vote.dao.VoteTokenDao;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("acvote.config")
public class JdbcTemplateVoteTokenDaoTest {
	
	private Logger log = LoggerFactory.getLogger(JdbcTemplateVoteTokenDao.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	VoteTokenDao dao;
	List<VoteToken> emptyList;
	VoteToken t1;
	VoteToken t2;
	VoteToken t3;
	
	@BeforeEach
	void setUp() throws Exception {
		JdbcTemplateVoteTokenDao jdao = new JdbcTemplateVoteTokenDao();
		jdao.setJdbcTemplate(jdbcTemplate);
		dao = jdao;
		emptyList = new ArrayList<>();
		t1 =  VoteToken.newToken(1, "5854836");
		t2 =  VoteToken.newToken(1, "4138778");
		t3 =  VoteToken.newToken(2, "5488165");
		
		dao.addToken(t1.getBid(), t1);
		dao.addToken(t2.getBid(), t2);
		dao.addToken(t3.getBid(), t3);
	}
	
	@Test
	void creationTest() {
		assertNotNull(dao);
		assertNotNull(emptyList);
		assertNotNull(t1);
		assertNotNull(t2);
		assertNotNull(t3);
	}
	
	/**
	 * Test getting all tokens for ballot
	 * @throws Exception
	 */
	@Test
	void tokensForBallotTest() throws Exception
	{
		List<VoteToken> list = dao.tokensForBallot(1); 
		
		log.debug("the list contains: ");
		for(int i = 0; i < list.size(); i++)
		{
			log.debug(list.get(i).getAcId());
		}
		log.debug("the list is size: " + list.size());
		
		assertNotNull(list);
		assertEquals(2, list.size());
	}
	
	/**
	 * Test looking for a token that doesnt exist
	 * @throws Exception
	 */
	@Test
	void tokenIsAbsentTest() throws Exception
	{
		boolean isAbsent = dao.tokenIsAbsent(2, "4138778");
		
		assertTrue(isAbsent);
	}
	
	/**
	 * Test looking for token after its removed
	 * @throws Exception
	 */
	@Test
	void tokenIsAbsentAfterRemovingTest() throws Exception
	{
		List<VoteToken> list = dao.tokensForBallot(1); 
		
		String tokenTwo = list.get(1).getToken();
		
		//log.debug(tokenTwo);
		
		boolean removed = dao.removeToken(1, tokenTwo);
		
		boolean isAbsent = dao.tokenIsAbsent(1, tokenTwo);
		
		assertTrue(removed);
		assertTrue(isAbsent);
	}
	
	/**
	 * Test removing a token
	 * @throws Exception
	 */
	@Test
	void removeTokenTest() throws Exception
	{
		List<VoteToken> list = dao.tokensForBallot(1); 
		
		String tokenTwo = list.get(1).getToken();
		
		dao.removeToken(1, tokenTwo);
		
		list = dao.tokensForBallot(1); 
		
		log.debug("the list contains: ");
		for(int i = 0; i < list.size(); i++)
		{
			log.debug(list.get(i).getAcId());
		}
		log.debug("the list is size: " + list.size());
		
		assertEquals(1, list.size());
	}
	
	/**
	 * Test Removing all tokens from a ballot
	 * @throws Exception
	 */
	@Test
	void clearTokensForBallot() throws Exception
	{
		dao.clearTokensForBallot(1);
		
		List<VoteToken> list = dao.tokensForBallot(1); 
		
		assertTrue(list.isEmpty());
	}
	
	/**
	 * Test adding a new token
	 * @throws Exception
	 */
	@Test
	void addTokenTest() throws Exception
	{
		VoteToken newToken = VoteToken.newToken(2, "7095095");
		
		dao.addToken(2, newToken);
		
		List<VoteToken> list = dao.tokensForBallot(2);
		
		assertEquals(2, list.size());
	}
	
	/**
	 * Test test adding multiple tokens
	 * @throws Exception
	 */
	@Test
	void addAllTokensTest() throws Exception
	{
		VoteToken tokenOne = VoteToken.newToken(2, "7095095");
		VoteToken tokenTwo = VoteToken.newToken(2, "2323136");
		
		List<VoteToken> tokenList = new ArrayList<VoteToken>();
		tokenList.add(tokenOne);
		tokenList.add(tokenTwo);
		
		
		dao.addAllTokens(2, tokenList);
		
		List<VoteToken> list = dao.tokensForBallot(2);
		
		assertEquals(3, list.size());
	}
	
	

}
