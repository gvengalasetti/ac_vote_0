package edu.austincollege.acvote.unit.users;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import edu.austincollege.acvote.users.AcUser;
import edu.austincollege.acvote.users.DuplicateUIDException;
import edu.austincollege.acvote.users.dao.JdbcUserDao;
import edu.austincollege.acvote.users.dao.UserDao;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("acvote.config")
class JdbcUserDaoTests {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	UserDao dao;
	
	List<AcUser> users;
	List<AcUser> admins;
	AcUser u1;
	AcUser u2;
	AcUser u3;
	AcUser u4;
	AcUser u5;
	AcUser u6;
	AcUser u7;
	AcUser u8;

	@BeforeEach
	void setUp() throws Exception {
		JdbcUserDao jdao = new JdbcUserDao();
		jdao.setJdbcTemplate(jdbcTemplate);
		dao = jdao;
		
		/*
insert into AcUser (uid, role) values ('arosenberg20', 'ADMIN');
insert into AcUser (uid, role) values ('gvengalasetti19', 'ADMIN');
insert into AcUser (uid, role) values ('bhill20', 'ADMIN');
insert into AcUser (uid, role) values ('kleahy20', 'ADMIN');
insert into AcUser (uid, role) values ('mhiggs', 'VOTER');
insert into AcUser (uid, role) values ('ablock', 'VIEWER');
insert into AcUser (uid, role) values ('jedge', 'EDITOR');
		*/
		
		u1 = new AcUser("arosenberg20", "ADMIN");
		u2 = new AcUser("gvengalasetti19", "ADMIN");
		u3 = new AcUser("kleahy20", "ADMIN");
		u4 = new AcUser("bhill20", "ADMIN");
		u5 = new AcUser("mhiggs", "VOTER");
		u6 = new AcUser("ablock", "VIEWER");
		u7 = new AcUser("jedge", "EDITOR");
		u8 = new AcUser("jmealy", "EDITOR");
		
		users = new ArrayList<>();
		users.add(u1);
		users.add(u2);
		users.add(u3);
		users.add(u4);
		users.add(u5);
		users.add(u6);
		users.add(u7);
		
		admins = new ArrayList<>();
		admins.add(u1);
		admins.add(u2);
		admins.add(u3);
		admins.add(u4);
		admins.add(u5);
	}

	/*
	 * Testing listAll
	 */
	
	/**
	 * Normal test case
	 * @throws Exception 
	 */
	@Test
	void listAllTestNormal() throws Exception {
		
		List<AcUser> results = dao.listAll();
		
		assertNotNull(results);
		
		for(int i = 0 ; i < results.size() ; i++) {
			assertTrue(users.contains(results.get(i)));
		}
	}
	
	/*
	 * Testing parameterized listAll
	 */
	
	/**
	 * Normal test case
	 * 
	 * @throws Exception
	 */
	@Test
	void listAllRoleTestNormal() throws Exception {
		
		List<AcUser> results = dao.listAll("ADMIN");
		
		assertNotNull(results);
		
		for(int i = 0 ; i < results.size() ; i++) {
			assertTrue(admins.contains(results.get(i)));
		}
	}
	
	/**
	 * Test case in which role does not exist in db
	 * 
	 * @throws Exception 
	 */
	@Test
	void listAllRoleTestStupid() throws Exception {
		
		List<AcUser> results = dao.listAll("SMART VOTERS");
		
		assertNotNull(results);
		assertTrue(results.isEmpty());
	}
	
	/*
	 * Testing userById
	 */
	
	/**
	 * Normal test case
	 * 
	 * @throws Exception
	 */
	@Test
	void userByIdTestNormal() throws Exception {
		
		AcUser u = dao.userById("mhiggs");
		
		assertNotNull(u);
		assertEquals(u, u5);
	}
	
	/**
	 * Test case in which uid is bullshit
	 */
	@Test
	void userByIdTestInvalidUid() {
		
		AcUser u = null;
		
		try {
			u = dao.userById("BAGEL");
			fail("Exception not thrown like expected");
		}
		catch (Exception e) {
			
			assertNull(u);
			assertNotEquals(u, u1);
			assertNotEquals(u, u2);
			assertNotEquals(u, u3);
			assertNotEquals(u, u4);
			assertNotEquals(u, u5);
			assertNotEquals(u, u6);
			assertNotEquals(u, u7);
			assertNotEquals(u, u8);
		}
	}
	
	/**
	 * Test case in which uid is empty
	 */
	@Test
	void userByIdTestEmptyUid() {
		
		AcUser u = null;
		
		try {
			u = dao.userById("  ");
			fail("Exception not thrown like expected");
		}
		catch (Exception e) {
			
			assertNull(u);
			assertNotEquals(u, u1);
			assertNotEquals(u, u2);
			assertNotEquals(u, u3);
			assertNotEquals(u, u4);
			assertNotEquals(u, u5);
			assertNotEquals(u, u6);
			assertNotEquals(u, u7);
		}
	}
	
	/*
	 * Testing create
	 */
	
	/**
	 * Normal test case
	 * 
	 * @throws Exception
	 */
	@Test
	void createTestNormal() {
		
		try {
			
			dao.create("jmealy", "EDITOR");
		}
		catch (Exception e) {
			
			fail("Exception not expected");
		}
	}
	
	/**
	 * Test case in which uid is null
	 */
	@Test
	void createTestNullUid() {
		
		try {
			dao.create(null, "EDITOR");
			fail("Exception not thrown like expected");
		}
		catch (Exception e) {
			
			assertTrue(e.getMessage().equals("unable to create user null: null uid"));
		}
	}
	
	@Test
	void createTestDuplicateUid() {
		
		try {
			dao.create("arosenberg20", "VIEWER");
			fail("Exception not thrown like expected");
		}
		catch (DuplicateUIDException e) {
			
			assertTrue(e.getMessage().equals("unable to create user arosenberg20: duplicate uid"));
		}
		catch (Exception e) {
			
			fail("wrong type of exception thrown");
		}
	}
	
	/*
	 * Testing delete
	 */
	
	@Test
	void deleteTestNormal() throws Exception {
		
		try {
			dao.delete("arosenberg20");
		}
		catch (Exception e) {
			fail("Exception unexpected");
		}
	}
	
	@Test
	void deleteTestInvalidUid() throws Exception {
		
		try {
			dao.delete("emusk");
			fail("Exception not thrown like expected");
		}
		catch (Exception e) {
			
			assertEquals("unable to delete user emusk",e.getMessage());
		}
	}

}
